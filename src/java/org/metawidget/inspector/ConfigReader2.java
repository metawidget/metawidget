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

package org.metawidget.inspector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Helper class for reading <code>metadata.xml</code> files and configuring Metawidgets.
 * <p>
 * In spirit, <code>metadata.xml</code> is a general-purpose mechanism for configuring JavaBeans
 * based on XML files. In practice, there are some Metawidget-specific features such as support for
 * immutable objects.
 * <p>
 * This class is not just a collection of static methods, because ConfigReaders need to be able to
 * be subclassed.
 *
 * @author Richard Kennard
 */

public class ConfigReader2
	implements ResourceResolver
{
	//
	// Package-level statics
	//

	final static Log					LOG								= LogUtils.getLog( ConfigReader.class );

	//
	// Private statics
	//

	private final static int			BUFFER_SIZE						= 1024 * 64;

	//
	// Protected members
	//

	protected final SAXParserFactory	mFactory;

	//
	// Package-level members
	//

	/**
	 * Certain objects are both immutable and threadsafe
	 */

	Map<String, Map<Integer, Object>>	IMMUTABLE_THREADSAFE_OBJECTS	= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public ConfigReader2()
	{
		mFactory = SAXParserFactory.newInstance();
		mFactory.setNamespaceAware( true );
	}

	//
	// Public methods
	//

	/**
	 * Read configuration from an application resource.
	 */

	public Object configure( String resource, Object toConfigure )
	{
		return configure( openResource( resource ), toConfigure );
	}

	/**
	 * Read configuration from an input stream.
	 */

	public Object configure( InputStream stream, Object toConfigure )
	{
		if ( stream == null )
			throw InspectorException.newException( "No input stream specified" );

		try
		{
			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			streamBetween( stream, streamOut );
			byte[] xml = streamOut.toByteArray();

			ConfigHandler configHandler = new ConfigHandler( toConfigure );
			configHandler.setXml( new String( xml ) );
			mFactory.newSAXParser().parse( new ByteArrayInputStream( xml ), configHandler );

			return configHandler.getConfigured();
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	/**
	 * Locate the given resource by trying, in order:
	 * <p>
	 * <ul>
	 * <li>the current thread's context classloader, if any
	 * <li>the classloader that loaded ConfigReader
	 * </ul>
	 */

	public InputStream openResource( String resource )
	{
		if ( resource == null || "".equals( resource.trim() ) )
			throw InspectorException.newException( "No resource specified" );

		// Thread's ClassLoader

		ClassLoader loaderContext = Thread.currentThread().getContextClassLoader();

		if ( loaderContext != null )
		{
			InputStream stream = loaderContext.getResourceAsStream( resource );

			if ( stream != null )
				return stream;
		}

		// ConfigReader's ClassLoader

		InputStream stream = ConfigReader.class.getResourceAsStream( resource );

		if ( stream != null )
			return stream;

		throw InspectorException.newException( "Unable to locate " + resource + " on CLASSPATH" );
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

		if ( "stream".equals( name ) )
			return true;

		if ( "bundle".equals( name ) )
			return true;

		return false;
	}

	/**
	 * Create the given native type based on the recorded text (as returned by
	 * <code>SAX.endRecording</code>)
	 */

	@SuppressWarnings( "unchecked" )
	protected Object createNative( String name, String recordedText )
		throws Exception
	{
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

		// InputStreams
		//
		// Note: this means we can set, say, BaseXmlInspector's <inputStream>
		// directly in the inspector-config.xml, rather than using <file>. This makes
		// it tempting to do away with <file> altogether, but we keep it because it
		// has an important role in 'lazy' evaluation. For example, HibernateInspectorConfig
		// calls .setFile in its constructor. Without this, it would have to call
		// .setInputStreams, so it would need a ResourceResolver passing in its
		// constructor, which all gets very icky

		// TODO: support URLs?

		if ( "stream".equals( name ) )
			return openResource( recordedText );

		if ( "bundle".equals( name ) )
			return ResourceBundle.getBundle( recordedText );

		throw InspectorException.newException( "Don't know how to convert '" + recordedText + "' to a " + name );
	}

	/**
	 * Certain XML tags are supported 'natively' as collections by the reader.
	 */

	protected Collection<Object> createNativeCollection( String name )
	{
		if ( "list".equals( name ) )
			return CollectionUtils.newArrayList();

		if ( "set".equals( name ) )
			return CollectionUtils.newHashSet();

		return null;
	}

	/**
	 * Certain classes are both immutable and threadsafe. We only ever need one instance of such
	 * classes.
	 */

	protected boolean isImmutableThreadsafe( Class<?> clazz )
	{
		if ( Inspector.class.isAssignableFrom( clazz ) )
			return true;

		if ( WidgetBuilder.class.isAssignableFrom( clazz ) )
			return true;

		return false;
	}

	//
	// Private methods
	//

	private void streamBetween( InputStream in, OutputStream out )
		throws IOException
	{
		try
		{
			int iCount;

			// (must create a local buffer for Thread-safety)

			byte[] byteData = new byte[BUFFER_SIZE];

			while ( ( iCount = in.read( byteData, 0, BUFFER_SIZE ) ) != -1 )
			{
				out.write( byteData, 0, iCount );
			}
		}
		finally
		{
			out.close();
			in.close();
		}
	}

	//
	// Inner classes
	//

	private static enum EncounteredState
	{
		METHOD, NATIVE_TYPE, NATIVE_COLLECTION_TYPE, CONFIGURED_TYPE, JAVA_OBJECT, IMMUTABLE_THREADSAFE
	}

	private static enum ExpectingState
	{
		ROOT, TO_CONFIGURE, OBJECT, METHOD
	}

	private class ConfigHandler
		extends DefaultHandler
	{
		//
		// Private statics
		//

		private final static String		JAVA_NAMESPACE_PREFIX		= "java:";

		//
		// Private members
		//

		/**
		 * Object to configure.
		 */

		private Object					mToConfigure;

		/**
		 * XML document. Used purely as a key into IMMUTABLE_THREADSAFE_OBJECTS.
		 */

		private String					mXml;

		/**
		 * Number of elements encountered so far. Used as a simple way to get a unique 'row/column'
		 * reference into the XML tree.
		 */

		private int						mElement;

		/**
		 * Map of objects that are immutable and threadsafe for this XML document. Keyed by element
		 * number.
		 */

		private Map<Integer, Object>	mImmutableThreadsafe;

		/**
		 * Track our depth in the XML tree.
		 */

		private int						mDepth;

		/**
		 * Depth after which to skip processing, so as to ignore chunks of the XML tree.
		 */

		private int						mIgnoreAfterDepth			= -1;

		/**
		 * Element number where this element starts.
		 */

		private int						mStoreAsElement				= -1;

		/**
		 * Depth after which to ignore immutable threadsafe caching, so that we only consider the
		 * 'top-level' of an object that itself contains immutable and threadsafe objects.
		 */

		private int						mImmutableThreadsafeAtDepth	= -1;

		/**
		 * Stack of Objects constructed so far.
		 */

		private Stack<Object>			mConstructing				= CollectionUtils.newStack();

		/**
		 * Next expected state in the XML tree.
		 */

		private ExpectingState			mExpecting					= ExpectingState.ROOT;

		/**
		 * Stack of encountered states in the XML tree.
		 */

		private Stack<EncounteredState>	mEncountered				= CollectionUtils.newStack();

		// (use StringBuffer for J2SE 1.4 compatibility)

		private StringBuffer			mBufferValue;

		//
		// Constructor
		//

		public ConfigHandler( Object toConfigure )
		{
			mToConfigure = toConfigure;
		}

		//
		// Public methods
		//

		public void setXml( String xml )
		{
			mXml = xml;
		}

		public Object getConfigured()
		{
			return mToConfigure;
		}

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes )
			throws SAXException
		{
			mElement++;
			mDepth++;

			if ( mIgnoreAfterDepth != -1 && mDepth > mIgnoreAfterDepth )
				return;

			try
			{
				// Note: we rely on our schema-validating parser to enforce the correct
				// nesting of elements and/or prescence of attributes, so we don't need to
				// re-check that here

				switch ( mExpecting )
				{
					case ROOT:
						if ( mToConfigure == null )
							mExpecting = ExpectingState.OBJECT;
						else
							mExpecting = ExpectingState.TO_CONFIGURE;
						break;

					case TO_CONFIGURE:
					{
						// Initial elements must be at depth == 2

						if ( mDepth != 2 )
							return;

						Class<?> toConfigureClass = classForName( uri, localName );

						// Match by Class...

						if ( mToConfigure instanceof Class )
						{
							if ( !toConfigureClass.isAssignableFrom( (Class<?>) mToConfigure ) )
							{
								mIgnoreAfterDepth = 2;
								return;
							}

							if ( !mConstructing.isEmpty() )
								throw InspectorException.newException( "Already configured a " + mConstructing.peek().getClass() + ", ambiguous match with " + toConfigureClass );

							handleNonNativeObject( uri, localName, attributes );
						}

						// ...or instance of Object

						else
						{
							if ( !toConfigureClass.isAssignableFrom( mToConfigure.getClass() ) )
							{
								mIgnoreAfterDepth = 2;
								return;
							}

							if ( !mConstructing.isEmpty() )
								throw InspectorException.newException( "Already configured a " + mConstructing.peek().getClass() + ", ambiguous match with " + toConfigureClass );

							mConstructing.push( mToConfigure );
						}

						mExpecting = ExpectingState.METHOD;
						break;
					}

					case OBJECT:
					{
						// Native types

						if ( isNative( localName ) )
						{
							mEncountered.push( EncounteredState.NATIVE_TYPE );
							startRecording();

							mExpecting = ExpectingState.METHOD;
							return;
						}

						// Native collection types

						Collection<Object> collection = createNativeCollection( localName );

						if ( collection != null )
						{
							mConstructing.push( collection );
							mEncountered.push( EncounteredState.NATIVE_COLLECTION_TYPE );

							mExpecting = ExpectingState.OBJECT;
							return;
						}

						handleNonNativeObject( uri, localName, attributes );

						mExpecting = ExpectingState.METHOD;
						break;
					}

					case METHOD:
					{
						mConstructing.push( new ArrayList<Object>() );
						mEncountered.push( EncounteredState.METHOD );

						mExpecting = ExpectingState.OBJECT;
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

			if ( mIgnoreAfterDepth != -1 )
			{
				if ( mDepth >= mIgnoreAfterDepth )
					return;

				mIgnoreAfterDepth = -1;
			}

			// All done?

			if ( mDepth == 0 )
				return;

			// Inside the tree somewhere, but of a different toConfigure?

			if ( mConstructing.isEmpty() )
				return;

			// Back at root? Expect another TO_CONFIGURE

			if ( mDepth == 1 )
			{
				mToConfigure = mConstructing.pop();
				mExpecting = ExpectingState.TO_CONFIGURE;
				return;
			}

			// Configure based on what was encountered

			try
			{
				EncounteredState encountered = mEncountered.pop();

				switch ( encountered )
				{
					case NATIVE_TYPE:
					{
						@SuppressWarnings( "unchecked" )
						Collection<Object> parameters = (Collection<Object>) mConstructing.peek();
						parameters.add( createNative( localName, endRecording() ) );
						mExpecting = ExpectingState.OBJECT;
						return;
					}

					case NATIVE_COLLECTION_TYPE:
					{
						@SuppressWarnings( "unchecked" )
						Collection<Object> collection = (Collection<Object>) mConstructing.pop();

						@SuppressWarnings( "unchecked" )
						Collection<Object> parameters = (Collection<Object>) mConstructing.peek();
						parameters.add( collection );

						mExpecting = ExpectingState.OBJECT;
						return;
					}

					case CONFIGURED_TYPE:
					case JAVA_OBJECT:
					case IMMUTABLE_THREADSAFE:
					{
						Object object = mConstructing.pop();

						if ( encountered == EncounteredState.CONFIGURED_TYPE )
						{
							Class<?> classToConstruct = classForName( uri, localName );
							Constructor<?> constructor = classToConstruct.getConstructor( object.getClass() );
							object = constructor.newInstance( object );
						}

						@SuppressWarnings( "unchecked" )
						Collection<Object> parameters = (Collection<Object>) mConstructing.peek();
						parameters.add( object );

						if ( encountered != EncounteredState.IMMUTABLE_THREADSAFE && mDepth == ( mImmutableThreadsafeAtDepth - 1 ) && isImmutableThreadsafe( object.getClass() ) )
							putImmutableThreadsafe( object );

						mExpecting = ExpectingState.OBJECT;
						return;
					}

					case METHOD:
					{
						@SuppressWarnings( "unchecked" )
						List<Object> parameters = (List<Object>) mConstructing.pop();
						Object constructing = mConstructing.peek();
						Method method = classGetMethod( constructing.getClass(), "set" + StringUtils.uppercaseFirstLetter( localName ), parameters );
						method.invoke( constructing, parameters.toArray() );

						mExpecting = ExpectingState.METHOD;
						return;
					}
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
					e = (Exception) ( (InvocationTargetException) e ).getTargetException();

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
			throw InspectorException.newException( exception );
		}

		//
		// Private methods
		//

		private void handleNonNativeObject( String uri, String localName, Attributes attributes )
			throws Exception
		{
			Class<?> classToConstruct = classForName( uri, localName );

			// Immutable and Threadsafe?

			if ( mStoreAsElement == -1 && isImmutableThreadsafe( classToConstruct ) )
			{
				Object immutableThreadsafe = getImmutableThreadsafe( classToConstruct );

				if ( immutableThreadsafe != null )
				{
					mConstructing.push( immutableThreadsafe );
					mEncountered.push( EncounteredState.IMMUTABLE_THREADSAFE );
					mIgnoreAfterDepth = mDepth;

					return;
				}

				mStoreAsElement = mElement;
				mImmutableThreadsafeAtDepth = mDepth;
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
					throw InspectorException.newException( "No such configuration class " + configToConstruct );

				mConstructing.push( configClass.newInstance() );
				mEncountered.push( EncounteredState.CONFIGURED_TYPE );

				mExpecting = ExpectingState.METHOD;
				return;
			}

			// Java objects

			mConstructing.push( classToConstruct.newInstance() );
			mEncountered.push( EncounteredState.JAVA_OBJECT );
		}

		private Object getImmutableThreadsafe( Class<?> clazz )
		{
			if ( mImmutableThreadsafe == null )
			{
				mImmutableThreadsafe = IMMUTABLE_THREADSAFE_OBJECTS.get( mXml );

				if ( mImmutableThreadsafe == null )
					return null;
			}

			return mImmutableThreadsafe.get( mElement );
		}

		private void putImmutableThreadsafe( Object immutableThreadsafe )
		{
			if ( mImmutableThreadsafe == null )
			{
				mImmutableThreadsafe = IMMUTABLE_THREADSAFE_OBJECTS.get( mXml );

				if ( mImmutableThreadsafe == null )
				{
					mImmutableThreadsafe = CollectionUtils.newHashMap();
					IMMUTABLE_THREADSAFE_OBJECTS.put( mXml, mImmutableThreadsafe );
				}
			}

			mImmutableThreadsafe.put( mStoreAsElement, immutableThreadsafe );
			mStoreAsElement = -1;
		}

		/**
		 * Resolves a class based on the URI namespace and the local name of the XML tag.
		 */

		private Class<?> classForName( String uri, String localName )
			throws SAXException
		{
			int indexOf = uri.indexOf( JAVA_NAMESPACE_PREFIX );

			if ( indexOf == -1 )
				throw new SAXException( "Namespace must contain " + JAVA_NAMESPACE_PREFIX );

			String packagePrefix = uri.substring( indexOf + JAVA_NAMESPACE_PREFIX.length() ) + StringUtils.SEPARATOR_DOT_CHAR;
			String toConstruct = packagePrefix + StringUtils.uppercaseFirstLetter( localName );
			Class<?> clazz = ClassUtils.niceForName( toConstruct );

			if ( clazz == null )
				throw InspectorException.newException( "No such class " + toConstruct );

			return clazz;
		}

		/**
		 * Finds a method with the specified parameter types.
		 * <p>
		 * Like <code>Class.getMethod</code>, but works based on <code>isInstance</code> rather
		 * than an exact match of parameter types. This is essentially a crude and partial
		 * implementation of
		 * http://java.sun.com/docs/books/jls/second_edition/html/expressions.doc.html#20448. In
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

					if ( !parameterType.isInstance( args.get( loop ) ) )
						continue methods;
				}

				// ...return it. Note we make no attempt to find the 'closest match'

				return method;
			}

			// No such method

			throw new NoSuchMethodException( clazz + "." + name );
		}
	}
}
