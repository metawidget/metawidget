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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.metawidget.iface.Immutable;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.XmlUtils.CachingContentHandler;
import org.metawidget.util.simple.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

// TODO: proper version in MANIFEST.MF?

/**
 * Helper class for reading <code>metadata.xml</code> files and configuring Metawidgets.
 * <p>
 * In spirit, <code>metadata.xml</code> is a general-purpose mechanism for configuring JavaBeans
 * based on XML files. In practice, there are some Metawidget-specific features such as:
 * <p>
 * <ul>
 * <li>support for reusing immutable objects (as defined by <code>isImmutable</code>)</li>
 * <li>caching XML input based on resource name (uses <code>XmlUtils.CachingContextHandler</code>)</li>
 * <li>resolving resources from specialized locations, such as under <code>WEB-INF</code> using
 * <code>ServletContext.getResource</code> (<code>ConfigReader</code> implements
 * <code>ResourceResolver</code>)</li>
 * </ul>
 * <p>
 * This class is not just static methods, because ConfigReaders need to be able to be subclassed
 * (eg. <code>ServletConfigReader</code>)
 * <h3>Important</h3>
 * <p>
 * <code>ConfigReader</code>'s support for reusing immutable objects (eg. <code>JpaInspector</code>)
 * that use config objects (eg. <code>JpaInspectorConfig</code>) is dependant on the config object
 * overriding <code>equals</code> and <code>hashCode</code>. <strong>Failure to override these
 * methods may result in your object not being reused, or being reused inappropriately</strong>.
 *
 * @author Richard Kennard
 */

public class ConfigReader
	implements ResourceResolver, Immutable {

	//
	// Package private statics
	//

	/**
	 * Dummy config to cache by if immutable has no Config.
	 */

	private static final String										IMMUTABLE_NO_CONFIG			= "no-config";

	/* package private */static final Log							LOG							= LogUtils.getLog( ConfigReader.class );

	/* package private */static final String						JAVA_NAMESPACE_PREFIX		= "java:";

	//
	// Protected members
	//

	protected final SAXParserFactory								mFactory;

	//
	// Private members
	//

	/**
	 * Cache of resource content based on resource name
	 */

	private final Map<String, CachingContentHandler>				mResourceCache				= CollectionUtils.newHashMap();

	/**
	 * Cache of objects that are immutable, indexed by a unique location (ie. the resource name) and
	 * element number. This is a broad-grained cache that can prune off large portions of the tree.
	 * For example, it can cache a <code>CompositeInspector</code> at the top-level, including all
	 * child <code>Inspector</code>s and their various <code>xxxConfig</code>s.
	 */

	/* package private */final Map<String, Map<Integer, Object>>	mImmutableByLocationCache	= CollectionUtils.newHashMap();

	/**
	 * Cache of objects that are immutable, indexed by their Class (and within that their Config).
	 * This is a more fine-grained cache than mImmutableByLocationCache, but is more widely
	 * applicable. For example, it can cache the same <code>Inspector</code> between different XMLs
	 * from different <code>InputStream</code>s, and the same <code>PropertyStyle</code> across
	 * multiple different <code>Inspector</code>s.
	 */

	/* package private */final Map<Class<?>, Map<Object, Object>>	mImmutableByClassCache		= CollectionUtils.newHashMap();

	/**
	 * Cache of objects that are immutable, indexed by their id. This is a less automatic cache than
	 * either mImmutableByLocationCache or mImmutableByClassCache because the developer has to
	 * specify an id explicitly. But it leads to cleaner metawidget.xml files because developers
	 * need only specify, say, a PropertyStyle with nested Config options once.
	 */

	// TODO: mImmutableByIdCache

	/* package private */final Map<Class<?>, Map<Object, Object>>	mImmutableByIdCache			= CollectionUtils.newHashMap();

	/**
	 * Patterns do not cache well, because <code>java.util.regex.Pattern</code> does not override
	 * <code>equals</code> or <code>hashCode</code>. Therefore we cache them manually to return the
	 * same instance.
	 */

	/* package private */final Map<String, Pattern>					mPatternCache				= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public ConfigReader() {

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
	public <T> T configure( String resource, Class<T> toConfigure, String... names ) {

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
	 * This version further caches any immutable objects, in the same way as
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

	public Object configure( String resource, Object toConfigure, String... names ) {

		ConfigHandler configHandler = new ConfigHandler( toConfigure, names );

		// Establish cache

		String locationKey = resource + StringUtils.SEPARATOR_FORWARD_SLASH;

		if ( toConfigure instanceof Class<?> ) {
			locationKey += ( (Class<?>) toConfigure ).getName();
		} else if ( toConfigure != null ) {
			locationKey += toConfigure.getClass().getName();
		}

		locationKey += ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false );

		Map<Integer, Object> immutableByLocationCache = mImmutableByLocationCache.get( locationKey );

		if ( immutableByLocationCache == null ) {
			immutableByLocationCache = CollectionUtils.newHashMap();
		}

		configHandler.setImmutableForThisLocationCache( immutableByLocationCache );

		try {
			// Replay the existing cache...

			CachingContentHandler cachingContentHandler = mResourceCache.get( locationKey );

			if ( cachingContentHandler != null ) {
				cachingContentHandler.replay( configHandler );
			}

			// ...or cache a new one

			else {
				LOG.debug( "Reading resource from {0}", locationKey );
				cachingContentHandler = new CachingContentHandler( configHandler );
				configHandler.setCachingContentHandler( cachingContentHandler );
				mFactory.newSAXParser().parse( openResource( resource ), cachingContentHandler );

				// Only cache if successful

				mResourceCache.put( locationKey, cachingContentHandler );
				mImmutableByLocationCache.put( locationKey, immutableByLocationCache );
			}

			return configHandler.getConfigured();
		} catch ( Exception e ) {
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
	public <T> T configure( InputStream stream, Class<T> toConfigure, String... names ) {

		return (T) configure( stream, (Object) toConfigure, names );
	}

	/**
	 * Read configuration from an input stream.
	 * <p>
	 * This version of <code>configure</code> caches any immutable objects (as determined by
	 * <code>isImmutable</code>) and reuses them for subsequent calls. This helps ensure there is
	 * only ever one instance of a, say, <code>Inspector</code> or <code>WidgetBuilder</code>.
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

	public Object configure( InputStream stream, Object toConfigure, String... names ) {

		if ( stream == null ) {
			throw MetawidgetException.newException( "No input stream specified" );
		}

		try {
			ConfigHandler configHandler = new ConfigHandler( toConfigure, names );
			mFactory.newSAXParser().parse( stream, configHandler );

			return configHandler.getConfigured();
		} catch ( Exception e ) {
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

	public InputStream openResource( String resource ) {

		return new SimpleResourceResolver().openResource( resource );
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

	protected boolean isNative( String name ) {

		if ( "null".equals( name ) ) {
			return true;
		}

		if ( "string".equals( name ) ) {
			return true;
		}

		if ( "class".equals( name ) ) {
			return true;
		}

		if ( "pattern".equals( name ) ) {
			return true;
		}

		if ( "format".equals( name ) ) {
			return true;
		}

		if ( "int".equals( name ) ) {
			return true;
		}

		if ( "boolean".equals( name ) ) {
			return true;
		}

		if ( "resource".equals( name ) ) {
			return true;
		}

		if ( "url".equals( name ) ) {
			return true;
		}

		if ( "file".equals( name ) ) {
			return true;
		}

		if ( "bundle".equals( name ) ) {
			return true;
		}

		if ( "constant".equals( name ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Certain XML tags are supported 'natively' by the reader.
	 * <p>
	 * Certain tags are indicative of a broader type, but it would be too onerous to specify them
	 * differently for their exact type (ie. <code>array</code> or <code>enum</code>). Therefore
	 * they are lazily resolved at time of method call.
	 */

	protected boolean isLazyResolvingNative( String name ) {

		if ( "enum".equals( name ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Create the given native type based on the recorded text (as returned by
	 * <code>SAX.endRecording</code>)
	 *
	 * @param namespace
	 *            the Class of the object under construction
	 */

	protected Object createNative( String name, Class<?> namespace, String recordedText )
		throws Exception {

		if ( "null".equals( name ) ) {
			return null;
		}

		if ( "string".equals( name ) ) {
			return recordedText;
		}

		if ( "class".equals( name ) ) {
			if ( "".equals( recordedText ) ) {
				return null;
			}

			return Class.forName( recordedText );
		}

		if ( "pattern".equals( name ) ) {
			Pattern pattern = mPatternCache.get( recordedText );

			if ( pattern == null ) {
				pattern = Pattern.compile( recordedText );
				mPatternCache.put( recordedText, pattern );
			}

			return pattern;
		}

		if ( "format".equals( name ) ) {
			return new MessageFormat( recordedText );
		}

		// (use new Integer, not Integer.valueOf, so that we're 1.4 compatible)

		if ( "int".equals( name ) ) {
			return new Integer( recordedText );
		}

		// (use new Boolean, not Boolean.valueOf, so that we're 1.4 compatible)

		if ( "boolean".equals( name ) ) {
			return new Boolean( recordedText );
		}

		if ( "bundle".equals( name ) ) {
			return ResourceBundle.getBundle( recordedText );
		}

		if ( "enum".equals( name ) ) {
			return recordedText;
		}

		if ( "constant".equals( name ) ) {
			// External constant

			int lastIndexOf = recordedText.lastIndexOf( '.' );

			if ( lastIndexOf != -1 ) {
				return Class.forName( recordedText.substring( 0, lastIndexOf ) ).getDeclaredField( recordedText.substring( lastIndexOf + 1 ) ).get( null );
			}

			// Relative to current namespace

			return namespace.getDeclaredField( recordedText ).get( null );
		}

		// These native types can never be equal to each other. This will cause a problem if they
		// are used by, say, a PropertyStyle because that PropertyStyle can never be shared

		if ( "resource".equals( name ) ) {
			return openResource( recordedText );
		}

		if ( "url".equals( name ) ) {
			return new URL( recordedText ).openStream();
		}

		if ( "file".equals( name ) ) {
			return new FileInputStream( recordedText );
		}

		throw MetawidgetException.newException( "Don't know how to convert '" + recordedText + "' to a " + name );
	}

	/**
	 * Certain XML tags are supported 'natively' as collections by the reader.
	 */

	protected Object createNativeCollection( String name ) {

		if ( "array".equals( name ) ) {
			return new Object[0];
		}

		if ( "list".equals( name ) ) {
			return CollectionUtils.newArrayList();
		}

		if ( "set".equals( name ) ) {
			return CollectionUtils.newHashSet();
		}

		return null;
	}

	/**
	 * Create a native that is 'lazily resolved' based on the method it is being applied to. Most
	 * natives are explicitly typed (ie. boolean, int etc.) but it is too onerous to do that for
	 * everything (ie. we support array instead of string-array, int-array etc.)
	 *
	 * @param nativeValue
	 *            never null
	 * @param toResolveTo
	 * @return the resolved native, or null if no resolution was possible
	 */

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	protected Object createLazyResolvingNative( Object nativeValue, Class<?> toResolveTo ) {

		// Arrays (ie. convert Object[] into String[])

		if ( toResolveTo.isArray() && nativeValue.getClass().isArray() ) {
			Object[] array = (Object[]) nativeValue;
			Object[] compatibleArray = (Object[]) Array.newInstance( toResolveTo.getComponentType(), array.length );

			try {
				System.arraycopy( array, 0, compatibleArray, 0, array.length );
				return compatibleArray;
			} catch ( ArrayStoreException e ) {
				// Could not be converted, is not compatible

				return null;
			}
		}

		// Enums

		else if ( toResolveTo.isEnum() && nativeValue instanceof String ) {
			try {
				Object enumValue = Enum.valueOf( (Class<? extends Enum>) toResolveTo, (String) nativeValue );
				return enumValue;
			} catch ( IllegalArgumentException e ) {
				// Could not be converted, is not compatible

				return null;
			}
		}

		// Could not be converted

		return null;
	}

	/**
	 * Lookup a class based on the URI namespace and the local name of the XML tag.
	 *
	 * @param uri
	 *            the URI namespace, to be used as the package name
	 * @param localName
	 *            the name of the tag, to be used as the class name
	 */

	protected Class<?> lookupClass( String uri, String localName )
		throws SAXException {

		if ( !uri.startsWith( JAVA_NAMESPACE_PREFIX ) ) {
			throw new SAXException( "Namespace '" + uri + "' of element <" + localName + "> must start with " + JAVA_NAMESPACE_PREFIX );
		}

		String packagePrefix = uri.substring( JAVA_NAMESPACE_PREFIX.length() );

		// Try regular class

		String uppercasedLocalName = StringUtils.uppercaseFirstLetter( localName );
		String classToConstruct = packagePrefix + StringUtils.SEPARATOR_DOT_CHAR + uppercasedLocalName;
		ClassLoader classLoader = getClass().getClassLoader();
		Class<?> clazz = ClassUtils.niceForName( classToConstruct, classLoader );

		if ( clazz == null ) {

			// Try inner class

			String innerClassToConstruct = packagePrefix + '$' + uppercasedLocalName;
			clazz = ClassUtils.niceForName( innerClassToConstruct, classLoader );

			if ( clazz == null ) {
				throw MetawidgetException.newException( "No such class " + classToConstruct + " or supported tag <" + localName + ">" );
			}
		}

		// Return it

		return clazz;
	}

	/**
	 * Certain classes are both immutable. We only ever need one instance of such classes for an
	 * entire application.
	 */

	protected boolean isImmutable( Class<?> clazz ) {

		return ( Immutable.class.isAssignableFrom( clazz ) );
	}

	//
	// Inner classes
	//

	private class ConfigHandler
		extends DefaultHandler {

		//
		// Private statics
		//

		/**
		 * Possible 'encountered' states.
		 * <p>
		 * Note: not using enum, for JDK 1.4 compatibility.
		 */

		private static final int		ENCOUNTERED_METHOD						= 0;

		private static final int		ENCOUNTERED_NATIVE_TYPE					= 1;

		private static final int		ENCOUNTERED_NATIVE_COLLECTION_TYPE		= 2;

		private static final int		ENCOUNTERED_CONFIGURED_TYPE				= 3;

		private static final int		ENCOUNTERED_JAVA_OBJECT					= 4;

		private static final int		ENCOUNTERED_ALREADY_CACHED_IMMUTABLE	= 5;

		private static final int		ENCOUNTERED_WRONG_TYPE					= 6;

		private static final int		ENCOUNTERED_WRONG_NAME					= 7;

		/**
		 * Possible 'expecting' states.
		 * <p>
		 * Note: not using enum, for JDK 1.4 compatibility.
		 */

		private static final int		EXPECTING_ROOT							= 0;

		private static final int		EXPECTING_TO_CONFIGURE					= 1;

		private static final int		EXPECTING_OBJECT						= 2;

		private static final int		EXPECTING_METHOD						= 3;

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
		 * Number of elements encountered so far. Used as a simple way to get a unique 'row/column'
		 * location into the XML tree.
		 */

		private int						mLocationIndex;

		/**
		 * Map of objects that are immutable for this XML document. Keyed by location index.
		 */

		private Map<Integer, Object>	mImmutableForThisLocationCache;

		/**
		 * Track our depth in the XML tree.
		 */

		private int						mDepth;

		/**
		 * Depth after which to skip type processing, so as to ignore chunks of the XML tree.
		 */

		private int						mIgnoreTypeAfterDepth					= -1;

		/**
		 * Depth after which to skip name processing, so as to ignore chunks of the XML tree.
		 */

		private int						mIgnoreNameAfterDepth					= -1;

		/**
		 * Depth after which to skip immutable caching, so as to ignore chunks of the XML tree.
		 */

		private int						mIgnoreImmutableAfterDepth				= -1;

		/**
		 * Stack of Objects constructed so far.
		 */

		private Stack<Object>			mConstructing							= CollectionUtils.newStack();

		/**
		 * Next expected state in the XML tree.
		 */

		private int						mExpecting								= EXPECTING_ROOT;

		/**
		 * Stack of encountered states in the XML tree.
		 */

		private Stack<Integer>			mEncountered							= CollectionUtils.newStack();

		// (use StringBuffer for J2SE 1.4 compatibility)

		private StringBuffer			mBufferValue;

		private CachingContentHandler	mCachingContentHandler;

		//
		// Constructor
		//

		public ConfigHandler( Object toConfigure, String... names ) {

			mToConfigure = toConfigure;
			mNames = names;
		}

		//
		// Public methods
		//

		public void setImmutableForThisLocationCache( Map<Integer, Object> immutableForThisLocationCache ) {

			mImmutableForThisLocationCache = immutableForThisLocationCache;
		}

		public void setCachingContentHandler( CachingContentHandler cachingContentHandler ) {

			mCachingContentHandler = cachingContentHandler;
		}

		public Object getConfigured() {

			if ( mConstructing.isEmpty() ) {
				throw MetawidgetException.newException( "No match for " + mToConfigure + " within config" );
			}

			if ( mConstructing.size() > 1 ) {
				throw MetawidgetException.newException( "Config still processing" );
			}

			return mConstructing.peek();
		}

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes )
			throws SAXException {

			mDepth++;

			if ( mIgnoreTypeAfterDepth != -1 && mDepth > mIgnoreTypeAfterDepth ) {
				return;
			}

			if ( mIgnoreNameAfterDepth != -1 && mDepth > mIgnoreNameAfterDepth ) {
				return;
			}

			if ( Character.isUpperCase( localName.charAt( 0 ) ) ) {
				throw MetawidgetException.newException( "XML node '" + localName + "' should start with a lowercase letter" );
			}

			try {
				// Note: we rely on our schema-validating parser to enforce the correct
				// nesting of elements and/or prescence of attributes, so we don't need to
				// re-check that here

				switch ( mExpecting ) {
					case EXPECTING_ROOT:
						if ( mToConfigure == null ) {
							mExpecting = EXPECTING_OBJECT;
						} else {
							mExpecting = EXPECTING_TO_CONFIGURE;
						}
						break;

					case EXPECTING_TO_CONFIGURE: {
						// Initial elements must be at depth == 2

						if ( mDepth != 2 ) {
							return;
						}

						Class<?> toConfigureClass = lookupClass( uri, localName );

						// Match by Class...

						if ( mToConfigure instanceof Class<?> ) {
							if ( !( (Class<?>) mToConfigure ).isAssignableFrom( toConfigureClass ) ) {
								mEncountered.push( ENCOUNTERED_WRONG_TYPE );
								mIgnoreTypeAfterDepth = 2;

								// Pause caching (if any)

								if ( mCachingContentHandler != null ) {
									mCachingContentHandler.pause( false );
								}

								return;
							}

							if ( !mConstructing.isEmpty() ) {
								throw MetawidgetException.newException( "Already configured a " + mConstructing.peek().getClass() + ", ambiguous match with " + toConfigureClass );
							}

							handleNonNativeObject( uri, localName, attributes );
						}

						// ...or instance of Object

						else {
							if ( !toConfigureClass.isAssignableFrom( mToConfigure.getClass() ) ) {
								mEncountered.push( ENCOUNTERED_WRONG_TYPE );
								mIgnoreTypeAfterDepth = 2;

								// Pause caching (if any)

								if ( mCachingContentHandler != null ) {
									mCachingContentHandler.pause( false );
								}

								return;
							}

							if ( !mConstructing.isEmpty() ) {
								throw MetawidgetException.newException( "Already configured a " + mConstructing.peek().getClass() + ", ambiguous match with " + toConfigureClass );
							}

							mConstructing.push( mToConfigure );
							mEncountered.push( ENCOUNTERED_JAVA_OBJECT );
						}

						mExpecting = EXPECTING_METHOD;
						break;
					}

					case EXPECTING_OBJECT: {
						if ( mCachingContentHandler == null || !mCachingContentHandler.isPaused() ) {
							mLocationIndex++;
						}

						// Native types

						if ( isNative( localName ) || isLazyResolvingNative( localName ) ) {
							mEncountered.push( ENCOUNTERED_NATIVE_TYPE );
							startRecording();

							mExpecting = EXPECTING_METHOD;
							return;
						}

						// Native collection types

						Object collection = createNativeCollection( localName );

						if ( collection != null ) {
							mConstructing.push( collection );
							mEncountered.push( ENCOUNTERED_NATIVE_COLLECTION_TYPE );

							mExpecting = EXPECTING_OBJECT;
							return;
						}

						// JDK 1.4 hack

						if ( isJdk14Hack( uri, localName ) ) {
							mEncountered.push( ENCOUNTERED_WRONG_TYPE );
							mExpecting = EXPECTING_OBJECT;
							return;
						}

						handleNonNativeObject( uri, localName, attributes );

						mExpecting = EXPECTING_METHOD;
						break;
					}

					case EXPECTING_METHOD: {
						// Screen names

						if ( mNames != null ) {
							// Initial elements are at depth == 2

							int nameIndex = mDepth - 3;

							if ( nameIndex < mNames.length ) {
								String expectingName = mNames[nameIndex];

								// Skip wrong names

								if ( !localName.equals( expectingName ) ) {
									mEncountered.push( ENCOUNTERED_WRONG_NAME );
									mIgnoreNameAfterDepth = mDepth;

									// Pause caching (if any)

									if ( mCachingContentHandler != null ) {
										mCachingContentHandler.pause( false );
									}

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
			} catch ( RuntimeException e ) {
				throw e;
			} catch ( Exception e ) {
				throw new SAXException( e );
			}
		}

		public void startRecording() {

			mBufferValue = new StringBuffer();
		}

		@Override
		public void characters( char[] characters, int start, int length ) {

			if ( mBufferValue == null ) {
				return;
			}

			mBufferValue.append( characters, start, length );
		}

		public String endRecording() {

			String value = mBufferValue.toString();
			mBufferValue = null;

			return value;
		}

		@Override
		public void endElement( String uri, String localName, String name )
			throws SAXException {

			mDepth--;

			if ( mIgnoreTypeAfterDepth != -1 ) {
				if ( mDepth >= mIgnoreTypeAfterDepth ) {
					return;
				}

				mIgnoreTypeAfterDepth = -1;

				// Unpause caching (if any)

				if ( mCachingContentHandler != null ) {
					mCachingContentHandler.unpause( false );
				}
			}

			if ( mIgnoreNameAfterDepth != -1 ) {
				if ( mDepth >= mIgnoreNameAfterDepth ) {
					return;
				}

				mIgnoreNameAfterDepth = -1;

				// Unpause caching (if any)

				if ( mCachingContentHandler != null ) {
					mCachingContentHandler.unpause( false );
				}
			}

			// All done?

			if ( mDepth == 0 ) {
				return;
			}

			// Inside the tree somewhere, but of a different toConfigure?

			if ( mConstructing.isEmpty() ) {
				return;
			}

			// Configure based on what was encountered

			try {
				int encountered = mEncountered.pop().intValue();

				switch ( encountered ) {
					case ENCOUNTERED_NATIVE_TYPE: {
						// Pop/push to peek at namespace

						Object methodParameters = mConstructing.pop();
						Object constructing = mConstructing.peek();
						mConstructing.push( methodParameters );

						// Create native

						addToConstructing( createNative( localName, constructing.getClass(), endRecording() ) );

						mExpecting = EXPECTING_OBJECT;
						return;
					}

					case ENCOUNTERED_NATIVE_COLLECTION_TYPE: {
						Object nativeCollectionType = mConstructing.pop();

						@SuppressWarnings( "unchecked" )
						Collection<Object> parameters = (Collection<Object>) mConstructing.peek();
						parameters.add( nativeCollectionType );

						mExpecting = EXPECTING_OBJECT;
						return;
					}

					case ENCOUNTERED_CONFIGURED_TYPE:
					case ENCOUNTERED_JAVA_OBJECT:
					case ENCOUNTERED_ALREADY_CACHED_IMMUTABLE: {
						Object object = mConstructing.pop();

						if ( encountered == ENCOUNTERED_CONFIGURED_TYPE ) {
							Class<?> classToConstruct = lookupClass( uri, localName );
							Object configuredObject = null;

							// Immutable by class (and config)? Don't re-instantiate

							if ( isImmutable( classToConstruct ) ) {
								configuredObject = getImmutableByClass( classToConstruct, object );
							}

							if ( configuredObject == null ) {
								try {
									Constructor<?> constructor = classToConstruct.getConstructor( object.getClass() );
									configuredObject = constructor.newInstance( object );
								} catch ( NoSuchMethodException e ) {
									String likelyConfig = getLikelyConfig( classToConstruct );

									if ( "".equals( likelyConfig ) ) {
										throw MetawidgetException.newException( classToConstruct + " does not have a constructor that takes a " + object.getClass() + ", as specified by your config attribute. It only has a config-less constructor" );
									} else if ( likelyConfig != null ) {
										throw MetawidgetException.newException( classToConstruct + " does not have a constructor that takes a " + object.getClass() + ", as specified by your config attribute. Did you mean config=\"" + likelyConfig + "\"?" );
									}

									throw MetawidgetException.newException( classToConstruct + " does not have a constructor that takes a " + object.getClass() + ", as specified by your config attribute" );
								}

								// Immutable? Cache it going forward

								if ( isImmutable( classToConstruct ) ) {
									LOG.debug( "\tInstantiated immutable {0} (config hashCode {1})", classToConstruct, object.hashCode() );
									putImmutableByClass( configuredObject, object );
								}
							} else if ( isImmutable( classToConstruct ) ) {
								// Unpause caching (if any)

								if ( mCachingContentHandler != null && mDepth < mIgnoreImmutableAfterDepth ) {
									mCachingContentHandler.unpause( true );
									mIgnoreImmutableAfterDepth = -1;

									// If the configuredObject was cached by class, it may have come
									// from a different 'location' (either a different resource, or
									// a different mLocationIndex within this same resource) so we
									// still need to cache it at this new location

									putImmutableByLocation( configuredObject );
								}
							}

							// Use the configured object (not its config) as the 'object' from now
							// on

							object = configuredObject;
						}

						// Back at root? Expect another TO_CONFIGURE

						if ( mDepth == 1 ) {
							mConstructing.push( object );
							mExpecting = EXPECTING_TO_CONFIGURE;
							return;
						}

						addToConstructing( object );

						mExpecting = EXPECTING_OBJECT;
						return;
					}

					case ENCOUNTERED_METHOD: {
						@SuppressWarnings( "unchecked" )
						List<Object> parameters = (List<Object>) mConstructing.pop();
						Object constructing = mConstructing.peek();

						Class<?> constructingClass = constructing.getClass();
						String methodName = "set" + StringUtils.uppercaseFirstLetter( localName );

						try {
							Method method = classGetMethod( constructingClass, methodName, parameters );
							method.invoke( constructing, parameters.toArray() );
						} catch ( NoSuchMethodException e ) {
							// Hint for config-based constructors

							for ( Constructor<?> constructor : constructingClass.getConstructors() ) {
								Class<?>[] parameterTypes = constructor.getParameterTypes();

								if ( parameterTypes.length != 1 ) {
									continue;
								}

								String parameterClassName = ClassUtils.getSimpleName( parameterTypes[0].getClass() );

								if ( parameterClassName.endsWith( "Config" ) ) {
									throw MetawidgetException.newException( "No such method " + methodName + " on " + constructingClass + ". Did you forget config=\"" + parameterClassName + "\"?" );
								}
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
			} catch ( RuntimeException e ) {
				throw e;
			} catch ( Exception e ) {
				// Prevent InvocationTargetException 'masking' the error

				if ( e instanceof InvocationTargetException ) {
					Throwable t = ( (InvocationTargetException) e ).getTargetException();

					// getTargetException may return a StackOverflowError

					if ( !( t instanceof Exception ) ) {
						throw new RuntimeException( t );
					}

					e = (Exception) t;
				}

				throw new SAXException( e );
			}
		}

		@Override
		public void warning( SAXParseException exception ) {

			LOG.warn( exception.getMessage() );
		}

		@Override
		public void error( SAXParseException exception ) {

			throw MetawidgetException.newException( exception );
		}

		//
		// Private methods
		//

		private void handleNonNativeObject( String uri, String localName, Attributes attributes )
			throws Exception {

			Class<?> classToConstruct = lookupClass( uri, localName );

			// Already cached (by location)?
			//
			// Note: if it is already cached by location, any child nodes will have been 'paused
			// away' by CachingContentHandler, so we don't have to worry about checking the config
			// attribute

			Object object = null;

			if ( isImmutable( classToConstruct ) ) {
				object = getImmutableByLocation();
			}

			// Configured types

			if ( object == null ) {
				String configClassName = attributes.getValue( "config" );

				if ( configClassName != null ) {
					String configToConstruct;

					if ( configClassName.indexOf( '.' ) == -1 ) {
						configToConstruct = classToConstruct.getPackage().getName() + '.' + configClassName;
					} else {
						configToConstruct = configClassName;
					}

					Class<?> configClass = ClassUtils.niceForName( configToConstruct, getClass().getClassLoader() );
					if ( configClass == null ) {
						throw MetawidgetException.newException( "No such configuration class " + configToConstruct );
					}

					Object config = configClass.newInstance();

					if ( config instanceof NeedsResourceResolver ) {
						( (NeedsResourceResolver) config ).setResourceResolver( ConfigReader.this );
					}

					mConstructing.push( config );
					mEncountered.push( ENCOUNTERED_CONFIGURED_TYPE );

					// Pause caching (if any)

					if ( mIgnoreImmutableAfterDepth == -1 && mCachingContentHandler != null && isImmutable( classToConstruct ) ) {
						mCachingContentHandler.pause( true );
						mIgnoreImmutableAfterDepth = mDepth;
					}

					mExpecting = EXPECTING_METHOD;
					return;
				}
			}

			// Already cached (without config)?

			if ( object == null && isImmutable( classToConstruct ) ) {
				object = getImmutableByClass( classToConstruct, IMMUTABLE_NO_CONFIG );
			}

			// Java objects

			if ( object == null ) {
				try {
					Constructor<?> defaultConstructor = classToConstruct.getConstructor();
					object = defaultConstructor.newInstance();
				} catch ( NoSuchMethodException e ) {
					String likelyConfig = getLikelyConfig( classToConstruct );

					if ( likelyConfig != null ) {
						throw MetawidgetException.newException( classToConstruct + " does not have a default constructor. Did you mean config=\"" + likelyConfig + "\"?" );
					}

					throw MetawidgetException.newException( classToConstruct + " does not have a default constructor" );
				}

				// Immutable by class (with no config)? Cache for next time

				if ( isImmutable( classToConstruct ) ) {
					LOG.debug( "\tInstantiated immutable {0} (no config)", classToConstruct );
					putImmutableByClass( object, null );
				}
			}

			mConstructing.push( object );
			mEncountered.push( ENCOUNTERED_JAVA_OBJECT );
		}

		private void addToConstructing( Object toAdd ) {

			Object parameters = mConstructing.peek();

			// Collections

			if ( parameters instanceof Collection<?> ) {
				@SuppressWarnings( "unchecked" )
				Collection<Object> collection = (Collection<Object>) parameters;
				collection.add( toAdd );
				return;
			}

			// Arrays

			if ( parameters.getClass().isArray() ) {
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

		private Object getImmutableByLocation() {

			// No cache (ie. XML coming from a nameless InputStream)?

			if ( mImmutableForThisLocationCache == null ) {
				return null;
			}

			return mImmutableForThisLocationCache.get( mLocationIndex );
		}

		private void putImmutableByLocation( Object immutable ) {

			// No cache by (ie. XML coming from a nameless InputStream)?

			if ( mImmutableForThisLocationCache == null ) {
				return;
			}

			mImmutableForThisLocationCache.put( mLocationIndex, immutable );
		}

		private Object getImmutableByClass( Class<?> clazz, Object config ) {

			Map<Object, Object> configs = mImmutableByClassCache.get( clazz );

			if ( configs == null ) {
				return null;
			}

			Object configToLookup = config;

			if ( configToLookup == null ) {
				configToLookup = IMMUTABLE_NO_CONFIG;
			}

			// Config must have implemented its .hashCode() and .equals() properly for this to work!

			return configs.get( configToLookup );
		}

		private void putImmutableByClass( Object immutable, Object config ) {

			Class<?> clazz = immutable.getClass();
			Map<Object, Object> configs = mImmutableByClassCache.get( clazz );

			if ( configs == null ) {
				configs = CollectionUtils.newHashMap();
				mImmutableByClassCache.put( clazz, configs );
			}

			Object configToStoreUnder = config;

			if ( configToStoreUnder == null ) {
				configToStoreUnder = IMMUTABLE_NO_CONFIG;
			} else {
				// Sanity check

				try {
					Class<?> configClass = configToStoreUnder.getClass();

					// Hard error

					// equals

					Class<?> equalsDeclaringClass = configClass.getMethod( "equals", Object.class ).getDeclaringClass();

					if ( Object.class.equals( equalsDeclaringClass ) ) {
						throw MetawidgetException.newException( configClass + " does not override .equals(), so cannot cache reliably" );
					}

					// hashCode
					//
					// Note: tempting to check for System.identityHashCode( configClass ) ==
					// configClass.hashCode() here, but that
					// could actually be true occasionally, causing hard-to-find bugs in production!

					Class<?> hashCodeDeclaringClass = configClass.getMethod( "hashCode" ).getDeclaringClass();

					if ( Object.class.equals( hashCodeDeclaringClass ) ) {
						throw MetawidgetException.newException( configClass + " does not override .hashCode(), so cannot cache reliably" );
					}

					// Soft warning (System.identityHashCode( configClass ) ==
					// configClass.hashCode() may be true occasionally, even if properly overridden)

					if ( System.identityHashCode( configToStoreUnder ) == configToStoreUnder.hashCode() ) {
						LOG.warn( "{0} overrides .hashCode(), but it returns the same as System.identityHashCode, so cannot be cached reliably", configClass );
					}

					if ( !equalsDeclaringClass.equals( hashCodeDeclaringClass ) ) {
						throw MetawidgetException.newException( equalsDeclaringClass + " implements .equals(), but .hashCode() is implemented by " + hashCodeDeclaringClass + ", so cannot cache reliably" );
					}

					if ( !configClass.equals( equalsDeclaringClass ) ) {
						// Soft warning
						//
						// Note: only show this if the configClass appears to have its own 'state'.
						// Base this assumption on whether it declares any methods. We don't want to
						// use .getDeclaredFields because that requires a security manager
						// check of checkMemberAccess(Member.DECLARED), whereas we may only have
						// checkMemberAccess(Member.PUBLIC) permission
						//
						// This check may seem overkill, but given that we are encouraging people to
						// extend their xxxConfigs from BaseObjectInspectorConfig and
						// BaseXmlInspectorConfig, it is actually the most likely scenario

						outer: for ( Method declaredMethod : configClass.getMethods() ) {
							if ( configClass.equals( declaredMethod.getDeclaringClass() ) ) {

								// (permit overloaded methods and co-variant return types)

								for ( Method equalsDeclaredMethod : equalsDeclaringClass.getMethods() ) {

									if ( equalsDeclaredMethod.getName().equals( declaredMethod.getName() ) ) {
										break outer;
									}
								}

								LOG.warn( "{0} does not override .equals() (only its super{1} does), so may not be cached reliably", configClass, equalsDeclaringClass );
								break;
							}
						}

						// Note: not necessary to do !configClass.equals( hashCodeDeclaringClass ),
						// as will already have thrown an Exception from
						// !equalsDeclaringClass.equals( hashCodeDeclaringClass ) if that's the case
					}
				} catch ( Exception e ) {
					throw MetawidgetException.newException( e );
				}
			}

			configs.put( configToStoreUnder, immutable );

			// Unpause caching (if any)

			if ( mCachingContentHandler != null && mDepth < mIgnoreImmutableAfterDepth ) {
				mCachingContentHandler.unpause( true );
				mIgnoreImmutableAfterDepth = -1;

				if ( config != null ) {
					putImmutableByLocation( immutable );
				}
			}
		}

		/**
		 * Hack to support Java 5 versus JDK 1.4 differences.
		 * <p>
		 * We want Metawidget to be useful 'out of the box' (ie. without configuring) on Java 5.
		 * This means we need to include <code>MetawidgetAnnotationInspector</code> in the default
		 * config. But this <em>also</em> means we will fail with an
		 * <code>UnsupportedClassVersionError</code> on JDK 1.4, and we want to run 'out of the box'
		 * on JDK 1.4.
		 * <p>
		 * Therefore we allow ourselves this hack: we do not fail if we encounter an
		 * <code>UnsupportedClassVersionError</code> when trying to instantiate a
		 * <code>MetawidgetAnnotationInspector</code>.
		 */

		protected boolean isJdk14Hack( String uri, String localName ) {

			if ( !( JAVA_NAMESPACE_PREFIX + "org.metawidget.inspector.annotation" ).equals( uri ) ) {
				return false;
			}

			if ( !"metawidgetAnnotationInspector".equals( localName ) ) {
				return false;
			}

			try {
				ClassUtils.niceForName( "org.metawidget.inspector.annotation.MetawidgetAnnotationInspector", getClass().getClassLoader() );
				return false;
			} catch ( UnsupportedClassVersionError e ) {
				LOG.debug( "\tNot instantiating org.metawidget.inspector.annotation.MetawidgetAnnotationInspector - wrong Java version" );
				return true;
			}
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
			throws NoSuchMethodException {

			int numberOfParameterTypes = args.size();
			Method likelyMatch = null;

			// For each method...

			methods: for ( Method method : clazz.getMethods() ) {
				// ...with a matching name...

				if ( !method.getName().equals( name ) ) {
					continue;
				}

				likelyMatch = method;

				// ...and compatible parameters...

				Class<?>[] methodParameterTypes = method.getParameterTypes();

				if ( methodParameterTypes.length != numberOfParameterTypes ) {
					continue;
				}

				// Array/enum compatibility handling mangles the args, so take a copy

				List<Object> compatibleArgs = CollectionUtils.newArrayList( args );

				// ...test each parameter for compatibility...

				for ( int loop = 0; loop < numberOfParameterTypes; loop++ ) {
					Object arg = compatibleArgs.get( loop );
					Class<?> parameterType = methodParameterTypes[loop];

					// ...primitives...

					if ( parameterType.isPrimitive() ) {
						parameterType = ClassUtils.getWrapperClass( parameterType );
					} else if ( arg == null ) {
						continue;
					}

					if ( !parameterType.isInstance( arg ) ) {
						// ...lazy resolvers...

						Object resolvedValue = createLazyResolvingNative( arg, parameterType );

						if ( resolvedValue == null ) {
							continue methods;
						}

						compatibleArgs.remove( loop );
						compatibleArgs.add( loop, resolvedValue );
					}
				}

				args.clear();
				args.addAll( compatibleArgs );

				// ...return it. Note we make no attempt to find the 'closest match'

				return method;
			}

			// No such method

			if ( likelyMatch != null ) {
				throw new NoSuchMethodException( methodToString( clazz, name, args ) + ". Did you mean " + methodToString( likelyMatch ) + "?" );
			}

			throw new NoSuchMethodException( methodToString( clazz, name, args ) );
		}

		/**
		 * @return null if ambiguous match, empty String for default constructor, otherwise name of
		 *         single parameter
		 */

		private String getLikelyConfig( Class<?> clazz ) {

			Constructor<?>[] constructors = clazz.getConstructors();

			if ( constructors.length != 1 ) {
				return null;
			}

			if ( constructors[0].getParameterTypes().length == 0 ) {
				return "";
			}

			if ( constructors[0].getParameterTypes().length > 1 ) {
				return null;
			}

			Class<?> likelyConfigClass = constructors[0].getParameterTypes()[0];

			if ( likelyConfigClass.getPackage().equals( clazz.getPackage() ) ) {
				return ClassUtils.getSimpleName( likelyConfigClass );
			}

			return likelyConfigClass.getName();
		}

		private String methodToString( Method method ) {

			StringBuffer buffer = new StringBuffer();

			for ( Class<?> parameterType : method.getParameterTypes() ) {
				if ( buffer.length() > 0 ) {
					buffer.append( ", " );
				}

				if ( parameterType.isArray() ) {
					buffer.append( ClassUtils.getSimpleName( parameterType.getComponentType() ) );
					buffer.append( "[]" );
				} else {
					buffer.append( ClassUtils.getSimpleName( parameterType ) );
				}
			}

			buffer.insert( 0, "(" );
			buffer.insert( 0, method.getName() );
			buffer.append( ")" );

			return buffer.toString();
		}

		private String methodToString( Class<?> clazz, String methodName, List<Object> args ) {

			StringBuffer buffer = new StringBuffer();

			for ( Object obj : args ) {
				if ( buffer.length() > 0 ) {
					buffer.append( ", " );
				}

				if ( obj == null ) {
					buffer.append( "null" );
				} else {
					buffer.append( ClassUtils.getSimpleName( obj.getClass() ) );
				}
			}

			buffer.insert( 0, "(" );
			buffer.insert( 0, methodName );
			buffer.insert( 0, '.' );
			buffer.insert( 0, clazz );
			buffer.append( ")" );

			return buffer.toString();
		}
	}
}
