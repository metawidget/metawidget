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

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Helper class for reading inspector-config.xml files and instantiating Inspectors.
 * <p>
 * All Inspectors are configured via a separate &lt;inspector name&gt;Config class passed to their
 * constructor. This ensures they are immutable once instantiated.
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

	final static Log					LOG	= LogUtils.getLog( ConfigReader.class );

	//
	// Protected members
	//

	protected final SAXParserFactory	mFactory;

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

	public void configure( String resource, Object toConfigure )
	{
		configure( openResource( resource ), toConfigure );
	}

	/**
	 * Read configuration from an input stream.
	 */

	public void configure( InputStream stream, Object toConfigure )
	{
		if ( stream == null )
			throw InspectorException.newException( "No input stream specified" );

		try
		{
			ConfigHandler configHandler = new ConfigHandler( toConfigure );
			mFactory.newSAXParser().parse( stream, configHandler );
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

	//
	// Inner classes
	//

	private static enum EncounteredState
	{
		METHOD, NATIVE_TYPE, NATIVE_COLLECTION_TYPE, CONFIGURED_TYPE, JAVA_OBJECT
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

		private final static String		JAVA_NAMESPACE_PREFIX	= "java:";

		//
		// Private members
		//

		/**
		 * Object to configure.
		 */

		private Object					mToConfigure;

		/**
		 * Track our depth in the SAX tree.
		 * <p>
		 * Needed so we can ignore large chunks of the SAX tree that don't match our
		 * <code>mToConfigure</code>.
		 */

		private int						mDepth;

		/**
		 * Stack of Objects constructed so far.
		 */

		private Stack<Object>			mConstructing			= CollectionUtils.newStack();

		/**
		 * Next expected state in the SAX tree.
		 */

		private ExpectingState			mExpecting				= ExpectingState.ROOT;

		/**
		 * Stack of encountered states in the SAX tree.
		 */

		private Stack<EncounteredState>	mEncountered			= CollectionUtils.newStack();

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

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes )
			throws SAXException
		{
			mDepth++;

			try
			{
				switch ( mExpecting )
				{
					case ROOT:
						mExpecting = ExpectingState.TO_CONFIGURE;
						break;

					case TO_CONFIGURE:
					{
						// Initial elements must be at depth == 2

						if ( mDepth != 2 )
							return;

						// See if a match

						Class<?> toConfigureClass = classForName( uri, localName );

						if ( !toConfigureClass.isAssignableFrom( mToConfigure.getClass() ) )
							return;

						if ( !mConstructing.isEmpty() )
							throw InspectorException.newException( "Already configured a " + mConstructing.peek().getClass() + ", ambiguous match with " + toConfigureClass );

						mConstructing.push( mToConfigure );
						mExpecting = ExpectingState.METHOD;
						break;
					}

						// Construct object
						//
						// Note: we rely on our schema-validating parser to enforce the correct
						// nesting of elements and/or prescence of attributes, so we don't need to
						// re-check that here

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

						// Configured types

						Class<?> classToConstruct = classForName( uri, localName );
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

			// All done?

			if ( mDepth == 0 )
				return;

			// Inside the tree somewhere, but of a different toConfigure?

			if ( mConstructing.isEmpty() )
				return;

			// Back at root? Expect another TO_CONFIGURE

			if ( mDepth == 1 )
			{
				mConstructing.pop();
				mExpecting = ExpectingState.TO_CONFIGURE;
				return;
			}

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
					{
						Class<?> classToConstruct = classForName( uri, localName );

						Object config = mConstructing.pop();
						Constructor<?> constructor = classToConstruct.getConstructor( config.getClass() );

						@SuppressWarnings( "unchecked" )
						Collection<Object> parameters = (Collection<Object>) mConstructing.peek();
						parameters.add( constructor.newInstance( config ) );
						mExpecting = ExpectingState.OBJECT;
						return;
					}

					case JAVA_OBJECT:
					{
						Object object = mConstructing.pop();

						@SuppressWarnings( "unchecked" )
						Collection<Object> parameters = (Collection<Object>) mConstructing.peek();
						parameters.add( object );

						mExpecting = ExpectingState.OBJECT;
						return;
					}

					case METHOD:
					{
						// Look up parameter types

						@SuppressWarnings( "unchecked" )
						List<Object> parameters = (List<Object>) mConstructing.pop();
						int length = parameters.size();
						Class<?>[] parameterTypes = new Class<?>[length];
						Object[] args = new Object[length];

						for ( int loop = 0; loop < length; loop++ )
						{
							Object arg = parameters.get( loop );
							args[loop] = arg;
							parameterTypes[loop] = arg.getClass();
						}

						// ...look up method...

						Object constructing = mConstructing.peek();
						Method method = getOverloadedMethod( constructing.getClass(), "set" + StringUtils.uppercaseFirstLetter( localName ), parameterTypes );

						// ...and call it

						method.invoke( constructing, args );

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
		 * Overloaded methods get resolved at <em>compile-time</em>, overridden methods get
		 * resolved at <em>runtime</em>.
		 */

		private Method getOverloadedMethod( Class<?> clazz, String name, Class<?>[] parameterTypes )
		{
			int numberOfParameterTypes = parameterTypes.length;

			methods: for ( Method method : clazz.getMethods() )
			{
				if ( !method.getName().equals( name ) )
					continue;

				Class<?>[] methodParameterTypes = method.getParameterTypes();

				if ( methodParameterTypes.length != numberOfParameterTypes )
					continue;

				for ( int loop = 0; loop < numberOfParameterTypes; loop++ )
				{
					Class<?> parameterType = methodParameterTypes[loop];

					if ( parameterType.isPrimitive() )
						parameterType = ClassUtils.getWrapperClass( parameterType );

					if ( !parameterType.isAssignableFrom( parameterTypes[loop] ) )
						continue methods;
				}

				// TODO: closest match?

				return method;
			}

			return null;
		}
	}
}
