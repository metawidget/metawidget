// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.NeedsResourceResolver;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.actionstyle.ActionStyle;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.layout.iface.Layout;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.XmlUtils.CachingContentHandler;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Helper class for reading <code>metadata.xml</code> files and configuring Metawidgets.
 * <p>
 * In spirit, <code>metadata.xml</code> is a general-purpose mechanism for configuring JavaBeans
 * based on XML files. In practice, there are some Metawidget-specific features such as:
 * <p>
 * <ul>
 * <li>support for reusing immutable, threadsafe objects (as defined by
 * <code>isImmutableThreadsafe</code>)</li>
 * <li>caching XML input based on resource name (uses <code>XmlUtils.CachingContextHandler</code>)</li>
 * </ul>
 * <p>
 * This class is not just static methods, because ConfigReaders need to be able to be subclassed
 * (eg. <code>ServletConfigReader</code>)
 * <h2>Important</h2> <code>ConfigReader</code>'s support for reusing immutable objects (eg.
 * <code>JpaInspector</code>) that use config objects (eg. <code>JpaInspectorConfig</code>) is
 * dependant on the config object overriding <code>equals</code> and <code>hashCode</code>.
 * <strong>Failure to override these methods may result in your object not being reused, or being
 * reused inappropriately</strong>.
 *
 * @author Richard Kennard
 */

public class ConfigReader
	implements ResourceResolver
{
	//
	// Private statics
	//

	/**
	 * Dummy config to cache by if immutable threadsafe has no Config.
	 */

	private final static String								IMMUTABLE_THREADSAFE_NO_CONFIG		= "no-config";

	//
	// Package private statics
	//

	/* package private */final static Log					LOG									= LogUtils.getLog( ConfigReader.class );

	//
	// Protected members
	//

	protected SAXParserFactory								mFactory;

	//
	// Package private members
	//

	/**
	 * Cache of resource content based on resource name
	 */

	/* package private */Map<String, CachingContentHandler>	RESOURCE_CACHE						= CollectionUtils.newHashMap();

	/**
	 * Cache of objects that are both immutable and threadsafe, indexed by a unique key (ie. the
	 * resource name) and element number. This is a fast, broadbrush cache that can prune off large
	 * portions of the tree. For example, it can cache a <code>CompositeInspector</code> at the
	 * top-level, including all child <code>Inspector</code>s and their various
	 * <code>xxxConfig</code>s.
	 */

	/* package private */Map<String, Map<Integer, Object>>	IMMUTABLE_THREADSAFE_BY_KEY_CACHE	= CollectionUtils.newHashMap();

	/**
	 * Cache of objects that are both immutable and threadsafe, indexed by their Class (and within
	 * that their Config). This is a slower cache than IMMUTABLE_THREADSAFE_OBJECTS_BY_KEY_CACHE,
	 * but is more widely applicable. For example, it can cache the same <code>PropertyStyle</code>
	 * across multiple different <code>Inspector</code>s.
	 */

	/* package private */Map<Class<?>, Map<Object, Object>>	IMMUTABLE_THREADSAFE_BY_CLASS_CACHE	= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public ConfigReader()
	{
		mFactory = SAXParserFactory.newInstance();
		mFactory.setNamespaceAware( true );
	}

	//
	// Public methods
	//

	/**
	 * Read configuration from an application resource.
	 * <p>
	 * This is a convenience method for <code>configure( String, Object )</code> that casts the
	 * returned Object to an instance of the given <code>toConfigure</code> class.
	 *
	 * @param resource
	 *            resource name that will be looked up using openResource
	 * @param toConfigure
	 *            class to instantiate. Can be a superclass of the one actually in the resource
	 * @param names
	 *            path to a property within the object. If specified, siblings to this path will be
	 *            ignored. This allows ConfigReader to be used to initialise only a specific part of
	 *            an object
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T configure( String resource, Class<T> toConfigure, String... names )
	{
		return (T) configure( resource, (Object) toConfigure, names );
	}

	/**
	 * Read configuration from an application resource.
	 * <p>
	 * This version of <code>configure</code> uses <code>openResource</code> to open the specified
	 * resource. It assumes the resource name is a unique key, so subsequent calls do not need to
	 * re-open the resource, or re-parse it, making this verison of <code>configure</code> much
	 * faster than <code>configure( InputStream, Object )</code>.
	 * <p>
	 * This version further caches any immutable and threadsafe objects, in the same way as
	 * <code>configure( InputStream, Object )</code> (see the JavaDoc for that method).
	 *
	 * @param resource
	 *            resource name that will be looked up using openResource
	 * @param toConfigure
	 *            object to configure. Can be a subclass of the one actually in the resource
	 * @param names
	 *            path to a property within the object. If specified, siblings to this path will be
	 *            ignored. This allows ConfigReader to be used to initialise only a specific part of
	 *            an object
	 */

	public Object configure( String resource, Object toConfigure, String... names )
	{
		ConfigHandler configHandler = new ConfigHandler( toConfigure, names );
		configHandler.setImmutableThreadsafeKey( resource );

		try
		{
			// Replay the existing cache...

			CachingContentHandler cachingContentHandler = RESOURCE_CACHE.get( resource );

			if ( cachingContentHandler != null )
			{
				cachingContentHandler.replay( configHandler );
			}

			// ...or cache a new one

			else
			{
				LOG.debug( "Reading resource from " + resource );
				cachingContentHandler = new CachingContentHandler( configHandler );
				mFactory.newSAXParser().parse( openResource( resource ), cachingContentHandler );
				RESOURCE_CACHE.put( resource, cachingContentHandler );
			}

			return configHandler.getConfigured();
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	/**
	 * Read configuration from an input stream.
	 * <p>
	 * This is a convenience method for <code>configure( InputStream, Object )</code> that casts the
	 * returned Object to an instance of the given <code>toConfigure</code> class.
	 *
	 * @param stream
	 *            XML input as a stream
	 * @param toConfigure
	 *            class to instantiate. Can be a superclass of the one actually in the resource
	 * @param names
	 *            path to a property within the object. If specified, siblings to this path will be
	 *            ignored. This allows ConfigReader to be used to initialise only a specific part of
	 *            an object
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T configure( InputStream stream, Class<T> toConfigure, String... names )
	{
		return (T) configure( stream, (Object) toConfigure, names );
	}

	/**
	 * Read configuration from an input stream.
	 * <p>
	 * This version of <code>configure</code> caches any immutable and threadsafe objects (as
	 * determined by <code>isImmutableThreadsafe</code>) and reuses them for subsequent calls. This
	 * helps ensure there is only ever one instance of a, say, <code>Inspector</code> or
	 * <code>WidgetBuilder</code>.
	 * <p>
	 * If the Object to configure is a <code>Class</code>, this method will create and return an
	 * instance of that class based on the configuration file. For example, if the configuration
	 * file is...
	 * <p>
	 * <code>
	 * &lt;metawidget&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;myInspector config="myConfig"&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;someConfigParameter/&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;/myInspector&gt;<br/>
	 * &lt;/metawidget&gt;
	 * </code>
	 * <p>
	 * ...then the code...
	 * <p>
	 * <code>
	 * Inspector myInspector = myConfigReader.configure( stream, Inspector.class );
	 * </code>
	 * <p>
	 * ...will create a <code>MyInspector</code> configured with <code>someConfigParameter</code>.
	 * <p>
	 * Conversely, if the Object to configure is already an instance, this method will configure the
	 * instance. For example if the configuration file is...
	 * <p>
	 * <code>
	 * &lt;metawidget&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;swingMetawidget&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;opaque&gt;&lt;boolean&gt;true&lt;/boolean&gt;&lt;/opaque&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;/swingMetawidget&gt;<br/>
	 * &lt;/metawidget&gt;
	 * </code>
	 * <p>
	 * ...then the code...
	 * <p>
	 * <code>
	 * JPanel panel = new JPanel();
	 * myConfigReader.configure( stream, panel );
	 * </code>
	 * <p>
	 * ...will call <code>setOpaque</code> on the given <code>JPanel</code>.
	 *
	 * @param stream
	 *            XML input as a stream
	 * @param toConfigure
	 *            object to configure. Can be a subclass of the one actually in the resource
	 * @param names
	 *            path to a property within the object. If specified, siblings to this path will be
	 *            ignored. This allows ConfigReader to be used to initialise only a specific part of
	 *            an object
	 */

	public Object configure( InputStream stream, Object toConfigure, String... names )
	{
		if ( stream == null )
			throw MetawidgetException.newException( "No input stream specified" );

		try
		{
			ConfigHandler configHandler = new ConfigHandler( toConfigure, names );
			mFactory.newSAXParser().parse( stream, configHandler );

			return configHandler.getConfigured();
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	/**
	 * Locate the given resource by trying, in order:
	 * <p>
	 * <ul>
	 * <li>the current thread's context classloader, if any
	 * <li>the classloader that loaded ClassUtils
	 * </ul>
	 */

	public InputStream openResource( String resource )
	{
		try
		{
			return ClassUtils.openResource( resource );
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	//
	// Protected methods
	//

	/**
	 * Certain XML tags are supported 'natively' by the reader.
	 * <p>
	 * Deciding (ie. <code>isNative</code>) and creating (ie. <code>createNative</code>) are
	 * separated into two phases. The former is called to decide whether to
	 * <code>SAX.startRecording</code>. The latter is called after <code>SAX.endRecording</code>.
	 */

	protected boolean isNative( String name )
	{
		if ( "null".equals( name ) )
			return true;

		if ( "string".equals( name ) )
			return true;

		if ( "class".equals( name ) )
			return true;

		if ( "pattern".equals( name ) )
			return true;

		if ( "int".equals( name ) )
			return true;

		if ( "boolean".equals( name ) )
			return true;

		if ( "resource".equals( name ) )
			return true;

		if ( "url".equals( name ) )
			return true;

		if ( "file".equals( name ) )
			return true;

		if ( "bundle".equals( name ) )
			return true;

		return false;
	}

	/**
	 * Create the given native type based on the recorded text (as returned by
	 * <code>SAX.endRecording</code>)
	 */

	protected Object createNative( String name, String recordedText )
		throws Exception
	{
		if ( "null".equals( name ) )
			return null;

		if ( "string".equals( name ) )
			return recordedText;

		if ( "class".equals( name ) )
		{
			if ( "".equals( recordedText ) )
				return null;

			return Class.forName( recordedText );
		}

		if ( "pattern".equals( name ) )
			return Pattern.compile( recordedText );

		// (use new Integer, not Integer.valueOf, so that we're 1.4 compatible)

		if ( "int".equals( name ) )
			return new Integer( recordedText );

		// (use new Boolean, not Boolean.valueOf, so that we're 1.4 compatible)

		if ( "boolean".equals( name ) )
			return new Boolean( recordedText );

		if ( "resource".equals( name ) )
			return openResource( recordedText );

		if ( "url".equals( name ) )
			return new URL( recordedText ).openStream();

		if ( "file".equals( name ) )
			return new FileInputStream( recordedText );

		if ( "bundle".equals( name ) )
			return ResourceBundle.getBundle( recordedText );

		throw MetawidgetException.newException( "Don't know how to convert '" + recordedText + "' to a " + name );
	}

	/**
	 * Certain XML tags are supported 'natively' as collections by the reader.
	 */

	protected Object createNativeCollection( String name )
	{
		if ( "array".equals( name ) )
			return new Object[0];

		if ( "list".equals( name ) )
			return CollectionUtils.newArrayList();

		if ( "set".equals( name ) )
			return CollectionUtils.newHashSet();

		return null;
	}

	/**
	 * Certain XML tags are supported 'natively' as arrays by the reader.
	 */

	protected Object[] createNativeArray( String name )
	{
		if ( "array".equals( name ) )
			return new String[0];

		return null;
	}

	/**
	 * Certain classes are both immutable and threadsafe. We only ever need one instance of such
	 * classes for an entire application.
	 */

	protected boolean isImmutableThreadsafe( Class<?> clazz )
	{
		if ( Inspector.class.isAssignableFrom( clazz ) )
			return true;

		if ( PropertyStyle.class.isAssignableFrom( clazz ) )
			return true;

		if ( ActionStyle.class.isAssignableFrom( clazz ) )
			return true;

		if ( WidgetBuilder.class.isAssignableFrom( clazz ) )
			return true;

		if ( WidgetProcessor.class.isAssignableFrom( clazz ) )
			return true;

		if ( Layout.class.isAssignableFrom( clazz ) )
			return true;

		return false;
	}

	//
	// Inner classes
	//

	private class ConfigHandler
		extends DefaultHandler
	{
		//
		// Private statics
		//

		private final static String		JAVA_NAMESPACE_PREFIX							= "java:";

		/**
		 * Possible 'encountered' states.
		 * <p>
		 * Note: not using enum, for JDK 1.4 compatibility.
		 */

		private final static int		ENCOUNTERED_METHOD								= 0;

		private final static int		ENCOUNTERED_NATIVE_TYPE							= 1;

		private final static int		ENCOUNTERED_NATIVE_COLLECTION_TYPE				= 2;

		private final static int		ENCOUNTERED_CONFIGURED_TYPE						= 3;

		private final static int		ENCOUNTERED_JAVA_OBJECT							= 4;

		private final static int		ENCOUNTERED_ALREADY_CACHED_IMMUTABLE_THREADSAFE	= 5;

		private final static int		ENCOUNTERED_WRONG_TYPE							= 6;

		private final static int		ENCOUNTERED_WRONG_NAME							= 7;

		/**
		 * Possible 'expecting' states.
		 * <p>
		 * Note: not using enum, for JDK 1.4 compatibility.
		 */

		private final static int		EXPECTING_ROOT									= 0;

		private final static int		EXPECTING_TO_CONFIGURE							= 1;

		private final static int		EXPECTING_OBJECT								= 2;

		private final static int		EXPECTING_METHOD								= 3;

		//
		// Private members
		//

		/**
		 * Object to configure.
		 */

		private Object					mToConfigure;

		/**
		 * Path within object to configure (if specified, siblings to the path will be ignored).
		 */

		private String[]				mNames;

		/**
		 * Key under which to cache IMMUTABLE_THREADSAFE_OBJECTS.
		 */

		private String					mImmutableThreadsafeKey;

		/**
		 * Number of elements encountered so far. Used as a simple way to get a unique 'row/column'
		 * reference into the XML tree.
		 */

		private int						mElement;

		/**
		 * Map of objects that are immutable and threadsafe for this XML document. Keyed by element
		 * number.
		 */

		private Map<Integer, Object>	mImmutableThreadsafeByKey;

		/**
		 * Track our depth in the XML tree.
		 */

		private int						mDepth;

		/**
		 * Depth after which to skip type processing, so as to ignore chunks of the XML tree.
		 */

		private int						mIgnoreTypeAfterDepth							= -1;

		/**
		 * Depth after which to skip name processing, so as to ignore chunks of the XML tree.
		 */

		private int						mIgnoreNameAfterDepth							= -1;

		/**
		 * Element number where this element starts.
		 */

		private int						mStoreAsElement									= -1;

		/**
		 * Depth after which to ignore 'immutable threadsafe caching, so that we only consider the
		 * 'top-level' of an object that itself contains immutable and threadsafe objects.
		 */

		private int						mStoreImmutableThreadsafeByKeyAtDepth			= -1;

		/**
		 * Stack of Objects constructed so far.
		 */

		private Stack<Object>			mConstructing									= CollectionUtils.newStack();

		/**
		 * Next expected state in the XML tree.
		 */

		private int						mExpecting										= EXPECTING_ROOT;

		/**
		 * Stack of encountered states in the XML tree.
		 */

		private Stack<Integer>			mEncountered									= CollectionUtils.newStack();

		// (use StringBuffer for J2SE 1.4 compatibility)

		private StringBuffer			mBufferValue;

		//
		// Constructor
		//

		public ConfigHandler( Object toConfigure, String... names )
		{
			mToConfigure = toConfigure;
			mNames = names;
		}

		//
		// Public methods
		//

		public void setImmutableThreadsafeKey( String immutableThreadsafeKey )
		{
			mImmutableThreadsafeKey = immutableThreadsafeKey;
		}

		public Object getConfigured()
		{
			if ( mConstructing.isEmpty() )
				throw MetawidgetException.newException( "No match for " + mToConfigure + " within config" );

			if ( mConstructing.size() > 1 )
				throw MetawidgetException.newException( "Config still processing" );

			return mConstructing.peek();
		}

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes )
			throws SAXException
		{
			mElement++;
			mDepth++;

			if ( mIgnoreTypeAfterDepth != -1 && mDepth > mIgnoreTypeAfterDepth )
				return;

			if ( mIgnoreNameAfterDepth != -1 && mDepth > mIgnoreNameAfterDepth )
				return;

			if ( Character.isUpperCase( localName.charAt( 0 ) ) )
				throw MetawidgetException.newException( "XML node '" + localName + "' should start with a lowercase letter" );

			try
			{
				// Note: we rely on our schema-validating parser to enforce the correct
				// nesting of elements and/or prescence of attributes, so we don't need to
				// re-check that here

				switch ( mExpecting )
				{
					case EXPECTING_ROOT:
						if ( mToConfigure == null )
							mExpecting = EXPECTING_OBJECT;
						else
							mExpecting = EXPECTING_TO_CONFIGURE;
						break;

					case EXPECTING_TO_CONFIGURE:
					{
						// Initial elements must be at depth == 2

						if ( mDepth != 2 )
							return;

						Class<?> toConfigureClass = classForName( uri, localName );

						// Match by Class...

						if ( mToConfigure instanceof Class )
						{
							if ( !( (Class<?>) mToConfigure ).isAssignableFrom( toConfigureClass ) )
							{
								mEncountered.push( ENCOUNTERED_WRONG_TYPE );
								mIgnoreTypeAfterDepth = 2;
								return;
							}

							if ( !mConstructing.isEmpty() )
								throw MetawidgetException.newException( "Already configured a " + mConstructing.peek().getClass() + ", ambiguous match with " + toConfigureClass );

							handleNonNativeObject( uri, localName, attributes );
						}

						// ...or instance of Object

						else
						{
							if ( !toConfigureClass.isAssignableFrom( mToConfigure.getClass() ) )
							{
								mEncountered.push( ENCOUNTERED_WRONG_TYPE );
								mIgnoreTypeAfterDepth = 2;
								return;
							}

							if ( !mConstructing.isEmpty() )
								throw MetawidgetException.newException( "Already configured a " + mConstructing.peek().getClass() + ", ambiguous match with " + toConfigureClass );

							mConstructing.push( mToConfigure );
							mEncountered.push( ENCOUNTERED_JAVA_OBJECT );
						}

						mExpecting = EXPECTING_METHOD;
						break;
					}

					case EXPECTING_OBJECT:
					{
						// Native types

						if ( isNative( localName ) )
						{
							mEncountered.push( ENCOUNTERED_NATIVE_TYPE );
							startRecording();

							mExpecting = EXPECTING_METHOD;
							return;
						}

						// Native collection types

						Object collection = createNativeCollection( localName );

						if ( collection != null )
						{
							mConstructing.push( collection );
							mEncountered.push( ENCOUNTERED_NATIVE_COLLECTION_TYPE );

							mExpecting = EXPECTING_OBJECT;
							return;
						}

						handleNonNativeObject( uri, localName, attributes );

						mExpecting = EXPECTING_METHOD;
						break;
					}

					case EXPECTING_METHOD:
					{
						// Screen names

						if ( mNames != null )
						{
							// Initial elements are at depth == 2

							int nameIndex = mDepth - 3;

							if ( nameIndex < mNames.length )
							{
								String expectingName = mNames[nameIndex];

								// Skip wrong names

								if ( !localName.equals( expectingName ) )
								{
									mEncountered.push( ENCOUNTERED_WRONG_NAME );
									mIgnoreNameAfterDepth = mDepth;
									return;
								}
							}
						}

						// Process method

						mConstructing.push( new ArrayList<Object>() );
						mEncountered.push( ENCOUNTERED_METHOD );

						mExpecting = EXPECTING_OBJECT;
						break;
					}
				}
			}
			catch ( RuntimeException e )
			{
				throw e;
			}
			catch ( Exception e )
			{
				throw new SAXException( e );
			}
		}

		public void startRecording()
		{
			mBufferValue = new StringBuffer();
		}

		@Override
		public void characters( char[] characters, int start, int length )
		{
			if ( mBufferValue == null )
				return;

			mBufferValue.append( characters, start, length );
		}

		public String endRecording()
		{
			String value = mBufferValue.toString();
			mBufferValue = null;

			return value;
		}

		@Override
		public void endElement( String uri, String localName, String name )
			throws SAXException
		{
			mDepth--;

			if ( mIgnoreTypeAfterDepth != -1 )
			{
				if ( mDepth >= mIgnoreTypeAfterDepth )
					return;

				mIgnoreTypeAfterDepth = -1;
			}

			if ( mIgnoreNameAfterDepth != -1 )
			{
				if ( mDepth >= mIgnoreNameAfterDepth )
					return;

				mIgnoreNameAfterDepth = -1;
			}

			// All done?

			if ( mDepth == 0 )
				return;

			// Inside the tree somewhere, but of a different toConfigure?

			if ( mConstructing.isEmpty() )
				return;

			// Configure based on what was encountered

			try
			{
				int encountered = mEncountered.pop().intValue();

				switch ( encountered )
				{
					case ENCOUNTERED_NATIVE_TYPE:
					{
						addToConstructing( createNative( localName, endRecording() ) );

						mExpecting = EXPECTING_OBJECT;
						return;
					}

					case ENCOUNTERED_NATIVE_COLLECTION_TYPE:
					{
						Object nativeCollectionType = mConstructing.pop();

						@SuppressWarnings( "unchecked" )
						Collection<Object> parameters = (Collection<Object>) mConstructing.peek();
						parameters.add( nativeCollectionType );

						mExpecting = EXPECTING_OBJECT;
						return;
					}

					case ENCOUNTERED_CONFIGURED_TYPE:
					case ENCOUNTERED_JAVA_OBJECT:
					case ENCOUNTERED_ALREADY_CACHED_IMMUTABLE_THREADSAFE:
					{
						Object object = mConstructing.pop();

						if ( encountered == ENCOUNTERED_CONFIGURED_TYPE )
						{
							Class<?> classToConstruct = classForName( uri, localName );
							Object configuredObject = null;

							// Immutable and Threadsafe by class (and config)? Don't re-instantiate

							if ( isImmutableThreadsafe( classToConstruct ) )
								configuredObject = getImmutableThreadsafeByClass( classToConstruct, object );

							if ( configuredObject == null )
							{
								try
								{
									Constructor<?> constructor = classToConstruct.getConstructor( object.getClass() );
									configuredObject = constructor.newInstance( object );
								}
								catch ( NoSuchMethodException e )
								{
									// Hint for config-based constructors

									throw MetawidgetException.newException( classToConstruct + " does not have a constructor that takes a " + object.getClass() + ", as specified by the config attribute" );
								}
							}

							// Immutable and threadsafe? Cache it going forward

							if ( isImmutableThreadsafe( classToConstruct ) )
							{
								putImmutableThreadsafeByClass( configuredObject, object );

								if ( mDepth == ( mStoreImmutableThreadsafeByKeyAtDepth - 1 ) )
									putImmutableThreadsafeByKey( configuredObject );
							}

							// Use the configured object (not its config) as the 'object' from now
							// on

							object = configuredObject;
						}

						else if ( encountered != ENCOUNTERED_ALREADY_CACHED_IMMUTABLE_THREADSAFE && isImmutableThreadsafe( object.getClass() ) )
						{
							putImmutableThreadsafeByClass( object, null );

							if ( mDepth == ( mStoreImmutableThreadsafeByKeyAtDepth - 1 ) )
								putImmutableThreadsafeByKey( object );
						}

						// Back at root? Expect another TO_CONFIGURE

						if ( mDepth == 1 )
						{
							mConstructing.push( object );
							mExpecting = EXPECTING_TO_CONFIGURE;
							return;
						}

						addToConstructing( object );

						mExpecting = EXPECTING_OBJECT;
						return;
					}

					case ENCOUNTERED_METHOD:
					{
						@SuppressWarnings( "unchecked" )
						List<Object> parameters = (List<Object>) mConstructing.pop();
						Object constructing = mConstructing.peek();

						Class<?> constructingClass = constructing.getClass();
						String methodName = "set" + StringUtils.uppercaseFirstLetter( localName );

						try
						{
							Method method = classGetMethod( constructingClass, methodName, parameters );
							method.invoke( constructing, parameters.toArray() );
						}
						catch ( NoSuchMethodException e )
						{
							// Hint for config-based constructors

							for ( Constructor<?> constructor : constructingClass.getConstructors() )
							{
								Class<?>[] parameterTypes = constructor.getParameterTypes();

								if ( parameterTypes.length != 1 )
									continue;

								String parameterClassName = ClassUtils.getSimpleName( parameterTypes[0].getClass() );

								if ( parameterClassName.endsWith( "Config" ) )
									throw MetawidgetException.newException( "No such method " + methodName + " on " + constructingClass + ". Did you forget config=\"" + parameterClassName + "\"?" );
							}

							throw e;
						}

						mExpecting = EXPECTING_METHOD;
						return;
					}

					case ENCOUNTERED_WRONG_TYPE:
						return;

					case ENCOUNTERED_WRONG_NAME:
						return;
				}
			}
			catch ( RuntimeException e )
			{
				throw e;
			}
			catch ( Exception e )
			{
				// Prevent InvocationTargetException 'masking' the error

				if ( e instanceof InvocationTargetException )
				{
					Throwable t = ( (InvocationTargetException) e ).getTargetException();

					// getTargetException may return a StackOverflowError

					if ( !( t instanceof Exception ) )
						throw new RuntimeException( t );

					e = (Exception) t;
				}

				throw new SAXException( e );
			}
		}

		@Override
		public void warning( SAXParseException exception )
		{
			LOG.warn( exception.getMessage() );
		}

		@Override
		public void error( SAXParseException exception )
		{
			throw MetawidgetException.newException( exception );
		}

		//
		// Private methods
		//

		private void handleNonNativeObject( String uri, String localName, Attributes attributes )
			throws Exception
		{
			Class<?> classToConstruct = classForName( uri, localName );

			// Immutable and Threadsafe by key? Don't re-parse

			if ( mStoreAsElement == -1 && isImmutableThreadsafe( classToConstruct ) )
			{
				Object immutableThreadsafeByKey = getImmutableThreadsafeByKey( classToConstruct );

				if ( immutableThreadsafeByKey != null )
				{
					mConstructing.push( immutableThreadsafeByKey );
					mEncountered.push( ENCOUNTERED_ALREADY_CACHED_IMMUTABLE_THREADSAFE );
					mIgnoreTypeAfterDepth = mDepth;

					return;
				}

				mStoreAsElement = mElement;
				mStoreImmutableThreadsafeByKeyAtDepth = mDepth;
			}

			// Configured types

			String configClassName = attributes.getValue( "config" );

			if ( configClassName != null )
			{
				String configToConstruct;

				if ( configClassName.indexOf( '.' ) == -1 )
					configToConstruct = classToConstruct.getPackage().getName() + '.' + configClassName;
				else
					configToConstruct = configClassName;

				Class<?> configClass = ClassUtils.niceForName( configToConstruct );
				if ( configClass == null )
					throw MetawidgetException.newException( "No such configuration class " + configToConstruct );

				Object config = configClass.newInstance();

				if ( config instanceof NeedsResourceResolver )
					( (NeedsResourceResolver) config ).setResourceResolver( ConfigReader.this );

				mConstructing.push( config );
				mEncountered.push( ENCOUNTERED_CONFIGURED_TYPE );

				mExpecting = EXPECTING_METHOD;
				return;
			}

			Object object = null;

			// Immutable and Threadsafe by class (with no config)? Don't re-instantiate

			if ( isImmutableThreadsafe( classToConstruct ) )
				object = getImmutableThreadsafeByClass( classToConstruct, IMMUTABLE_THREADSAFE_NO_CONFIG );

			// Java objects

			if ( object == null )
			{
				try
				{
					Constructor<?> defaultConstructor = classToConstruct.getConstructor();
					object = defaultConstructor.newInstance();
				}
				catch ( NoSuchMethodException e )
				{
					// Hint for config-based constructors

					Constructor<?>[] constructors = classToConstruct.getConstructors();

					if ( constructors.length == 1 && constructors[0].getParameterTypes().length == 1 )
						throw MetawidgetException.newException( classToConstruct + " does not have a default constructor. Did you mean config=\"" + ClassUtils.getSimpleName( constructors[0].getParameterTypes()[0] ) + "\"?" );

					throw MetawidgetException.newException( classToConstruct + " does not have a default constructor" );
				}
			}

			// Immutable and Threadsafe by class (with no config)? Cache for next time

			if ( isImmutableThreadsafe( classToConstruct ) )
				putImmutableThreadsafeByClass( classToConstruct, null );

			mConstructing.push( object );
			mEncountered.push( ENCOUNTERED_JAVA_OBJECT );
		}

		private void addToConstructing( Object toAdd )
		{
			Object parameters = mConstructing.peek();

			// Collections

			if ( parameters instanceof Collection )
			{
				@SuppressWarnings( "unchecked" )
				Collection<Object> collection = (Collection<Object>) parameters;
				collection.add( toAdd );
				return;
			}

			// Arrays

			if ( parameters.getClass().isArray() )
			{
				Object[] array = (Object[]) mConstructing.pop();
				Object[] newArray = new Object[array.length + 1];
				System.arraycopy( array, 0, newArray, 0, array.length );

				newArray[array.length] = toAdd;
				mConstructing.push( newArray );
				return;
			}

			// Unknown

			throw MetawidgetException.newException( "Don't know how to add to a " + parameters.getClass() );
		}

		private Object getImmutableThreadsafeByKey( Class<?> clazz )
		{
			// No key to cache by (ie. XML coming from a nameless InputStream)?

			if ( mImmutableThreadsafeKey == null )
				return null;

			// Look up existing

			if ( mImmutableThreadsafeByKey == null )
			{
				mImmutableThreadsafeByKey = IMMUTABLE_THREADSAFE_BY_KEY_CACHE.get( mImmutableThreadsafeKey );

				if ( mImmutableThreadsafeByKey == null )
					return null;
			}

			return mImmutableThreadsafeByKey.get( mElement );
		}

		private void putImmutableThreadsafeByKey( Object immutableThreadsafe )
		{
			// No key to cache by (ie. XML coming from a nameless InputStream)?

			if ( mImmutableThreadsafeKey == null )
				return;

			// Look up existing

			if ( mImmutableThreadsafeByKey == null )
			{
				mImmutableThreadsafeByKey = IMMUTABLE_THREADSAFE_BY_KEY_CACHE.get( mImmutableThreadsafeKey );

				if ( mImmutableThreadsafeByKey == null )
				{
					mImmutableThreadsafeByKey = CollectionUtils.newHashMap();
					IMMUTABLE_THREADSAFE_BY_KEY_CACHE.put( mImmutableThreadsafeKey, mImmutableThreadsafeByKey );
				}
			}

			mImmutableThreadsafeByKey.put( mStoreAsElement, immutableThreadsafe );
			mStoreAsElement = -1;
			LOG.debug( "Instantiated immutable threadsafe " + immutableThreadsafe.getClass() );
		}

		private Object getImmutableThreadsafeByClass( Class<?> clazz, Object config )
		{
			Map<Object, Object> configs = IMMUTABLE_THREADSAFE_BY_CLASS_CACHE.get( clazz );

			if ( configs == null )
				return null;

			Object configToLookup = config;

			if ( configToLookup == null )
				configToLookup = IMMUTABLE_THREADSAFE_NO_CONFIG;

			// Config must have implemented its .hashCode() and .equals() properly for this to work!

			return configs.get( configToLookup );
		}

		private void putImmutableThreadsafeByClass( Object immutableThreadsafe, Object config )
		{
			Class<?> clazz = immutableThreadsafe.getClass();
			Map<Object, Object> configs = IMMUTABLE_THREADSAFE_BY_CLASS_CACHE.get( clazz );

			if ( configs == null )
			{
				configs = CollectionUtils.newHashMap();
				IMMUTABLE_THREADSAFE_BY_CLASS_CACHE.put( clazz, configs );
			}

			Object configToStoreUnder = config;

			if ( configToStoreUnder == null )
			{
				configToStoreUnder = IMMUTABLE_THREADSAFE_NO_CONFIG;
			}
			else
			{
				// Sanity check

				try
				{
					Class<?> configClass = configToStoreUnder.getClass();

					// Hard warning

					// equals

					Class<?> equalsDeclaringClass = configClass.getMethod( "equals", Object.class ).getDeclaringClass();

					if ( Object.class.equals( equalsDeclaringClass ) )
						throw MetawidgetException.newException( configClass + " does not override .equals(), so cannot cache reliably" );

					// hashCode

					Class<?> hashCodeDeclaringClass = configClass.getMethod( "hashCode" ).getDeclaringClass();

					if ( Object.class.equals( hashCodeDeclaringClass ) )
						throw MetawidgetException.newException( configClass + " does not override .hashCode(), so cannot cache reliably" );

					if ( !equalsDeclaringClass.equals( hashCodeDeclaringClass ) )
						throw MetawidgetException.newException( equalsDeclaringClass + " implements .equals(), but .hashCode() is implemented by " + hashCodeDeclaringClass + ", so cannot cache reliably" );

					if ( !configClass.equals( equalsDeclaringClass ) )
					{
						// Soft warning
						//
						// Note: only show this if the configClass appears to have its own 'state'.
						// Base this assumption on whether it declares any methods. We don't want to
						// use .getDeclaredFields because that requires a security manager
						// check of checkMemberAccess(Member.DECLARED), whereas we may only have
						// checkMemberAccess(Member.PUBLIC) permission

						for ( Method declaredMethod : configClass.getMethods() )
						{
							if ( configClass.equals( declaredMethod.getDeclaringClass() ) )
							{
								LOG.warn( configClass + " does not override .equals() (only its super" + equalsDeclaringClass + " does), so may not be cached reliably" );
								break;
							}
						}

						// Note: not necessary to do !configClass.equals( hashCodeDeclaringClass ),
						// as will already have thrown an Exception from
						// !equalsDeclaringClass.equals( hashCodeDeclaringClass ) if that's the case
					}
				}
				catch ( Exception e )
				{
					throw MetawidgetException.newException( e );
				}
			}

			configs.put( configToStoreUnder, immutableThreadsafe );
			LOG.debug( "Instantiated immutable threadsafe " + immutableThreadsafe.getClass() );
		}

		/**
		 * Resolves a class based on the URI namespace and the local name of the XML tag.
		 */

		private Class<?> classForName( String uri, String localName )
			throws SAXException
		{
			if ( !uri.startsWith( JAVA_NAMESPACE_PREFIX ) )
				throw new SAXException( "Namespace '" + uri + "' of element <" + localName + "> must start with " + JAVA_NAMESPACE_PREFIX );

			String packagePrefix = uri.substring( JAVA_NAMESPACE_PREFIX.length() ) + StringUtils.SEPARATOR_DOT_CHAR;
			String toConstruct = packagePrefix + StringUtils.uppercaseFirstLetter( localName );
			Class<?> clazz = ClassUtils.niceForName( toConstruct );

			if ( clazz == null )
				throw MetawidgetException.newException( "No such class " + toConstruct + " or supported tag <" + localName + ">" );

			return clazz;
		}

		/**
		 * Finds a method with the specified parameter types.
		 * <p>
		 * Like <code>Class.getMethod</code>, but works based on <code>isInstance</code> rather than
		 * an exact match of parameter types. This is essentially a crude and partial implementation
		 * of http://java.sun.com/docs/books/jls/second_edition/html/expressions.doc.html#20448. In
		 * particular, no attempt at 'closest matching' is implemented.
		 */

		private Method classGetMethod( Class<?> clazz, String name, List<Object> args )
			throws NoSuchMethodException
		{
			int numberOfParameterTypes = args.size();

			// For each method...

			methods: for ( Method method : clazz.getMethods() )
			{
				// ...with a matching name...

				if ( !method.getName().equals( name ) )
					continue;

				// ...and compatible parameters...

				Class<?>[] methodParameterTypes = method.getParameterTypes();

				if ( methodParameterTypes.length != numberOfParameterTypes )
					continue;

				for ( int loop = 0; loop < numberOfParameterTypes; loop++ )
				{
					Class<?> parameterType = methodParameterTypes[loop];

					if ( parameterType.isPrimitive() )
						parameterType = ClassUtils.getWrapperClass( parameterType );

					Object arg = args.get( loop );

					if ( arg == null )
						continue;

					if ( !parameterType.isInstance( arg ) )
					{
						if ( !parameterType.isArray() || !arg.getClass().isArray() )
							continue methods;

						Object[] array = (Object[]) arg;
						Object[] compatibleArray = (Object[]) Array.newInstance( parameterType.getComponentType(), array.length );

						try
						{
							System.arraycopy( array, 0, compatibleArray, 0, array.length );
							args.remove( loop );
							args.add( loop, compatibleArray );
						}
						catch ( ArrayStoreException e )
						{
							// Not compatible

							continue;
						}
					}
				}

				// ...return it. Note we make no attempt to find the 'closest match'

				return method;
			}

			// No such method

			StringBuffer buffer = new StringBuffer();

			for ( Object obj : args )
			{
				if ( buffer.length() > 0 )
					buffer.append( ", " );

				if ( obj == null )
					buffer.append( "null" );
				else
					buffer.append( obj.getClass() );
			}

			buffer.insert( 0, "( " );
			buffer.insert( 0, name );
			buffer.insert( 0, '.' );
			buffer.insert( 0, clazz );
			buffer.append( " )" );

			throw new NoSuchMethodException( buffer.toString() );
		}
	}
}
