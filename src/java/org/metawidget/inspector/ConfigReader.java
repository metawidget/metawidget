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
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.StringUtils;
import org.metawidget.util.LogUtils.Log;
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

public class ConfigReader
	implements ResourceResolver
{
	//
	//
	// Package-level statics
	//
	//

	final static Log				LOG	= LogUtils.getLog( ConfigReader.class );

	//
	//
	// Private members
	//
	//

	private final SAXParserFactory	mFactory;

	//
	//
	// Constructor
	//
	//

	public ConfigReader()
	{
		mFactory = SAXParserFactory.newInstance();
		mFactory.setNamespaceAware( true );
	}

	//
	//
	// Public methods
	//
	//

	/**
	 * Sets whether the ConfigReader should validate the input against the
	 * <code>inspector-config-1.0.xsd</code> schema.
	 * <p>
	 * False by default.
	 */

	public void setValidating( boolean validating )
	{
		if ( !validating )
		{
			mFactory.setSchema( null );
			return;
		}

		// (J2SE1.4 and Android don't support java.xml.validation)

		if ( !ClassUtils.classExists( "javax.xml.validation.SchemaFactory" ) )
			return;

		SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		InputStream in = openResource( "org/metawidget/inspector/inspector-config-1.0.xsd" );

		try
		{
			mFactory.setSchema( factory.newSchema( new StreamSource( in ) ) );
		}
		catch ( SAXException e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	//
	// Public methods
	//
	//

	/**
	 * Read configuration from an application resource.
	 */

	public Inspector read( String resource )
	{
		if ( resource == null )
			throw InspectorException.newException( "No resource specified" );

		return read( openResource( resource ));
	}

	/**
	 * Read configuration from an input stream.
	 */

	public Inspector read( InputStream stream )
	{
		if ( stream == null )
			throw InspectorException.newException( "No input stream specified" );

		try
		{
			ConfigHandler configHandler = new ConfigHandler();
			mFactory.newSAXParser().parse( stream, configHandler );

			return configHandler.getInspector();
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
		ClassLoader loaderContext = Thread.currentThread().getContextClassLoader();

		if ( loaderContext != null )
		{
			InputStream stream = loaderContext.getResourceAsStream( resource );

			if ( stream != null )
				return stream;
		}

		InputStream stream = ConfigReader.class.getResourceAsStream( resource );

		if ( stream != null )
			return stream;

		throw InspectorException.newException( "Unable to locate " + resource );
	}

	//
	//
	// Protected methods
	//
	//

	@SuppressWarnings( "unchecked" )
	protected <T> T convertFromString( String input, Class<T> toReturn )
	{
		if ( String.class.isAssignableFrom( toReturn ) )
			return (T) input;

		if ( Class.class.isAssignableFrom( toReturn ) )
		{
			try
			{
				return (T) Class.forName( input );
			}
			catch( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		if ( Pattern.class.isAssignableFrom( toReturn ) )
			return (T) Pattern.compile( input );

		// (use new Integer, not Integer.valueOf, so that we're 1.4 compatible)

		if ( int.class.isAssignableFrom( toReturn ) )
			return (T) new Integer( input );

		// (use new Boolean, not Boolean.valueOf, so that we're 1.4 compatible)

		if ( boolean.class.isAssignableFrom( toReturn ) )
			return (T) new Boolean( input );

		// InputStreams
		//
		// Note: this means we can set, say, AbstractXmlInspector's <inputStream>
		// directly in the inspector-config.xml, rather than using <file>. This makes
		// it tempting to do away with <file> altogether, but we keep it because it
		// has an important role in 'lazy' evaluation. For example, HibernateInspectorConfig
		// calls .setFile in its constructor. Without this, it would have to call
		// .setInputStreams, so it would need a ResourceResolver passing in its
		// constructor, which all gets very icky

		if ( InputStream.class.isAssignableFrom( toReturn ) )
			return (T) openResource( input );

		throw InspectorException.newException( "Don't know how to convert '" + input + "' to " + toReturn );
	}

	//
	//
	// Inner classes
	//
	//

	protected class ConfigHandler
		extends DefaultHandler
	{
		//
		//
		// Private statics
		//
		//

		private final static String	JAVA_NAMESPACE_PREFIX	= "java:";

		//
		//
		// Private members
		//
		//

		/**
		 * Whether to expect an Object or a Property next.
		 */

		private boolean				mExpectObject;

		private Stack<Object>		mStackConstructing		= CollectionUtils.newStack();

		// (use StringBuffer for J2SE 1.4 compatibility)

		private StringBuffer		mBufferValue;

		//
		//
		// Public methods
		//
		//

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes )
			throws SAXException
		{
			// Start of everything?
			//
			// Note: we rely on our schema-validating parser to enforce the correct nesting of
			// elements and/or prescence of attributes, so we don't need to re-check that here

			if ( "inspector-config".equals( localName ) )
			{
				mExpectObject = true;
				return;
			}

			try
			{
				// Construct object

				if ( mExpectObject )
				{
					// Strings in Collections/Arrays

					if ( "string".equalsIgnoreCase( localName ) )
					{
						mStackConstructing.push( String.class );
						startRecording();
						return;
					}

					// Classes in Collections/Arrays

					if ( "class".equalsIgnoreCase( localName ) )
					{
						mStackConstructing.push( Class.class );
						startRecording();
						return;
					}

					// Inspectors

					if ( !uri.startsWith( JAVA_NAMESPACE_PREFIX ) )
						throw new SAXException( "Namespace must begin with " + JAVA_NAMESPACE_PREFIX );

					String packagePrefix = uri.substring( JAVA_NAMESPACE_PREFIX.length() ) + StringUtils.SEPARATOR_DOT;
					String toConstruct = packagePrefix + StringUtils.uppercaseFirstLetter( localName );
					Class<?> classToConstruct = ClassUtils.niceForName( toConstruct );

					if ( classToConstruct == null )
					{
						if ( !mStackConstructing.isEmpty() && mStackConstructing.peek() instanceof Constructor )
							throw InspectorException.newException( "No such class " + toConstruct + ". Did you forget a 'config' attribute?" );

						throw InspectorException.newException( "No such class" + toConstruct );
					}

					String configClass = attributes.getValue( "config" );

					// ResourceResolver-based constructor

					Constructor<?> constructor;

					if ( configClass == null )
					{
						try
						{
							constructor = classToConstruct.getConstructor( ResourceResolver.class );
						}
						catch ( NoSuchMethodException e1 )
						{
							// Config-less constructor

							try
							{
								constructor = classToConstruct.getConstructor();
							}
							catch ( NoSuchMethodException e2 )
							{
								throw InspectorException.newException( toConstruct + " requires a 'config' attribute" );
							}
						}

						mStackConstructing.push( constructor );
						mExpectObject = true;
					}

					// Config and ResourceResolver-based constructor

					else
					{
						Class<?> config = Class.forName( packagePrefix + configClass );

						try
						{
							constructor = classToConstruct.getConstructor( config, ResourceResolver.class );
						}
						catch ( NoSuchMethodException e1 )
						{
							// Config-based constructor

							try
							{
								constructor = classToConstruct.getConstructor( config );
							}
							catch ( NoSuchMethodException e2 )
							{
								throw InspectorException.newException( toConstruct + " does not support a 'config' attribute" );
							}
						}

						mStackConstructing.push( constructor );
						mStackConstructing.push( config.newInstance() );
						mExpectObject = false;
					}

					return;
				}

				// Inspect property

				Object toInit = mStackConstructing.peek();
				String methodName = "set" + StringUtils.uppercaseFirstLetter( localName );
				Class<?> classToInit = toInit.getClass();

				for ( Method method : classToInit.getMethods() )
				{
					if ( methodName.equals( method.getName() ) )
					{
						mStackConstructing.push( method );

						Class<?>[] parameters = method.getParameterTypes();

						if ( parameters.length == 0 )
							throw new SAXException( method + " is not a setter method - has no parameters" );

						if ( parameters.length > 1 )
							throw new SAXException( method + " is not a setter method - has multiple parameters" );

						Class<?> parameter = parameters[0];

						// Determine property type

						if ( List.class.isAssignableFrom( parameter ) )
						{
							mStackConstructing.push( CollectionUtils.newArrayList() );
						}
						else if ( parameter.isArray() )
						{
							mStackConstructing.push( Array.newInstance( parameter.getComponentType(), 0 ) );
						}
						else
						{
							mStackConstructing.push( parameter );
							startRecording();
						}

						mExpectObject = true;
						return;
					}
				}

				throw new SAXException( "No such method on " + classToInit + "." + methodName );
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
			// End of everything?

			if ( "inspector-config".equals( localName ) )
				return;

			try
			{
				Object popped = mStackConstructing.pop();

				// Popped off a Constructor? Construct it

				if ( popped instanceof Constructor )
				{
					Constructor<?> constructor = (Constructor<?>) popped;
					Class<?>[] types = constructor.getParameterTypes();

					if ( types.length == 0 )
						popped = constructor.newInstance();

					else if ( types.length == 1 && types[0].equals( ResourceResolver.class ) )
						popped = constructor.newInstance( ConfigReader.this );

					else
						throw InspectorException.newException( "Don't know how to invoke " + constructor );
				}

				// Popped off a Class? Instantiate it

				else if ( popped instanceof Class )
				{
					popped = convertFromString( endRecording(), (Class<?>) popped );
				}

				// Peek at what's next

				mExpectObject = true;

				if ( mStackConstructing.isEmpty() )
					throw InspectorException.newException( "Premature end of stack" );

				Object peeked = mStackConstructing.peek();

				// Peeked Collection

				if ( peeked instanceof Collection )
				{
					@SuppressWarnings( "unchecked" )
					Collection<Object> collectionPeeked = (Collection<Object>) peeked;
					collectionPeeked.add( popped );
				}

				// Peeked Array

				else if ( peeked.getClass().isArray() )
				{
					@SuppressWarnings( "unchecked" )
					Object[] arrayPeeked = (Object[]) mStackConstructing.pop();
					mStackConstructing.push( ArrayUtils.add( arrayPeeked, popped ) );
				}

				// Peeked Setter

				else if ( peeked instanceof Method )
				{
					Method methodSetter = (Method) mStackConstructing.pop();
					methodSetter.invoke( mStackConstructing.peek(), popped );
					mExpectObject = false;
				}

				// Peeked Constructor for a xxxConfig

				else if ( peeked instanceof Constructor )
				{
					Constructor<?> constructor = (Constructor<?>) mStackConstructing.pop();

					if ( constructor.getParameterTypes().length == 2 )
						popped = constructor.newInstance( popped, ConfigReader.this );
					else
						popped = constructor.newInstance( popped );

					// Peek again, because we need to put the Constructed object somewhere

					if ( mStackConstructing.isEmpty() )
					{
						mStackConstructing.push( popped );
					}
					else
					{
						peeked = mStackConstructing.peek();

						if ( peeked instanceof Collection )
						{
							@SuppressWarnings( "unchecked" )
							Collection<Object> collectionPeeked = (Collection<Object>) peeked;
							collectionPeeked.add( popped );
						}
						else if ( peeked.getClass().isArray() )
						{
							@SuppressWarnings( "unchecked" )
							Object[] arrayPeeked = (Object[]) mStackConstructing.pop();
							mStackConstructing.push( ArrayUtils.add( arrayPeeked, popped ) );
						}
						else
						{
							throw InspectorException.newException( "Don't know how to combine a " + popped.getClass() + " with a " + peeked.getClass() );
						}
					}
				}
			}
			catch ( Exception e )
			{
				// Prevent InvocationTargetException 'masking' the error

				if ( e instanceof InvocationTargetException )
					e = (Exception) ((InvocationTargetException) e).getTargetException();

				LOG.error( "Unable to parse config", e );
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

		public Inspector getInspector()
		{
			if ( mStackConstructing.isEmpty() )
				throw InspectorException.newException( "No Inspector declared" );

			if ( mStackConstructing.size() > 1 )
				throw InspectorException.newException( "More than one Inspector declared" );

			return (Inspector) mStackConstructing.pop();
		}
	}
}
