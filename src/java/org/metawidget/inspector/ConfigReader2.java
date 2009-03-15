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
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ArrayUtils;
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

public class ConfigReader2<C>
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

	public C configure( String resource, C initial )
	{
		return configure( openResource( resource ), initial );
	}

	/**
	 * Read configuration from an input stream.
	 */

	public C configure( InputStream stream, C initial )
	{
		if ( stream == null )
			throw InspectorException.newException( "No input stream specified" );

		try
		{
			ConfigHandler configHandler = new ConfigHandler();
			configHandler.pushInitial( initial );
			mFactory.newSAXParser().parse( stream, configHandler );

			return configHandler.popFinal();
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
	 * Convert the given String to an Object of the given Class.
	 * <p>
	 * Subclasses can override this method to hook in custom resource resolution.
	 */

	@SuppressWarnings( "unchecked" )
	protected <T> T convertFromString( String input, Class<T> toReturn )
	{
		if ( String.class.isAssignableFrom( toReturn ) )
			return (T) input;

		if ( Class.class.isAssignableFrom( toReturn ))
		{
			if ( "".equals( input ) )
				return null;

			try
			{
				return (T) Class.forName( input );
			}
			catch ( Exception e )
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
		// Note: this means we can set, say, BaseXmlInspector's <inputStream>
		// directly in the inspector-config.xml, rather than using <file>. This makes
		// it tempting to do away with <file> altogether, but we keep it because it
		// has an important role in 'lazy' evaluation. For example, HibernateInspectorConfig
		// calls .setFile in its constructor. Without this, it would have to call
		// .setInputStreams, so it would need a ResourceResolver passing in its
		// constructor, which all gets very icky

		// TODO: support URLs?

		if ( InputStream.class.isAssignableFrom( toReturn ) )
			return (T) openResource( input );

		if ( ResourceBundle.class.isAssignableFrom( toReturn ))
			return (T) ResourceBundle.getBundle( input );

		throw InspectorException.newException( "Don't know how to convert '" + input + "' to " + toReturn );
	}

	//
	// Inner classes
	//

	protected class ConfigHandler
		extends DefaultHandler
	{
		//
		// Private statics
		//

		private final static String	JAVA_NAMESPACE_PREFIX	= "java:";

		private final static int	EXPECT_INITIAL			= 0;

		private final static int	EXPECT_OBJECT			= 1;

		private final static int	EXPECT_PROPERTY			= 2;

		//
		// Private members
		//

		/**
		 * What the expect next.
		 */

		private int					mExpect					= EXPECT_INITIAL;

		private int					mElementDepth;

		private Stack<Object>		mStackConstructing		= CollectionUtils.newStack();

		private Stack<Object>		mStackConstructed		= CollectionUtils.newStack();

		// (use StringBuffer for J2SE 1.4 compatibility)

		private StringBuffer		mBufferValue;

		//
		// Public methods
		//

		public void pushInitial( C toPush )
		{
			if ( mElementDepth != 0 || !mStackConstructing.isEmpty() )
				throw InspectorException.newException( "Already reading configuration" );

			mStackConstructing.push( toPush );
		}

		@SuppressWarnings( "unchecked" )
		public C popFinal()
		{
			if ( mElementDepth != 0 )
				throw InspectorException.newException( "Still reading configuration" );

			if ( mStackConstructing.isEmpty() )
				throw InspectorException.newException( "Nothing to return" );

			if ( mStackConstructing.size() > 1 )
				throw InspectorException.newException( "More than one object to return" );

			return (C) mStackConstructing.pop();
		}

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes )
			throws SAXException
		{
			mElementDepth++;

			try
			{
				switch ( mExpect )
				{
					// Start of everything?

					case EXPECT_INITIAL:
					{
						if ( "metawidget".equals( localName ) )
							return;

						// Initial elements must be at root level

						if ( mElementDepth > 2 )
							return;

						// Java objects
						//
						// (namespace may be 'java:org.foo' or 'urn:java:org.foo'

						int indexOf = uri.indexOf( JAVA_NAMESPACE_PREFIX );

						if ( indexOf == -1 )
							throw new SAXException( "Namespace must contain " + JAVA_NAMESPACE_PREFIX );

						String packagePrefix = uri.substring( indexOf + JAVA_NAMESPACE_PREFIX.length() ) + StringUtils.SEPARATOR_DOT_CHAR;
						String toInitialize = packagePrefix + StringUtils.uppercaseFirstLetter( localName );
						Class<?> initialClass = ClassUtils.niceForName( toInitialize );

						if ( initialClass.isAssignableFrom( mStackConstructing.peek().getClass() ) )
							mExpect = EXPECT_PROPERTY;

						break;
					}

						// Construct object
						//
						// Note: we rely on our schema-validating parser to enforce the correct
						// nesting of elements and/or prescence of attributes, so we don't need to
						// re-check that here

					case EXPECT_OBJECT:
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

						// Java objects
						//
						// (namespace may be 'java:org.foo' or 'urn:java:org.foo'

						int indexOf = uri.indexOf( JAVA_NAMESPACE_PREFIX );

						if ( indexOf == -1 )
							throw new SAXException( "Namespace must contain " + JAVA_NAMESPACE_PREFIX );

						String packagePrefix = uri.substring( indexOf + JAVA_NAMESPACE_PREFIX.length() ) + StringUtils.SEPARATOR_DOT_CHAR;
						String toConstruct = packagePrefix + StringUtils.uppercaseFirstLetter( localName );
						Class<?> classToConstruct = ClassUtils.niceForName( toConstruct );

						if ( classToConstruct == null )
						{
							if ( !mStackConstructing.isEmpty() && mStackConstructing.peek() instanceof Constructor )
								throw InspectorException.newException( "No such class " + toConstruct + ". Did you forget a 'config' attribute?" );

							throw InspectorException.newException( "No such class " + toConstruct );
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
						}

						// Config and ResourceResolver-based constructor

						else
						{
							Class<?> config;

							if ( configClass.indexOf( '.' ) == -1 )
								config = Class.forName( packagePrefix + configClass );
							else
								config = Class.forName( configClass );

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
							mExpect = EXPECT_PROPERTY;
						}
						break;
					}

						// Inspect property

					case EXPECT_PROPERTY:
					{
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

								mExpect = EXPECT_OBJECT;
								return;
							}
						}

						throw new SAXException( "No such method on " + classToInit + "." + methodName );
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
			mElementDepth--;

			// End of everything?

			if ( "metawidget".equals( localName ) || mElementDepth == 1 )
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
						popped = constructor.newInstance( ConfigReader2.this );

					else
						throw InspectorException.newException( "Don't know how to invoke " + constructor );
				}

				// Popped off a Class? Find something that matches it

				else if ( popped instanceof Class )
				{
					if ( !mStackConstructed.isEmpty() )
					{
						Object constructed = mStackConstructed.pop();

						if ( !((Class<?>) popped).isAssignableFrom( constructed.getClass() ))
							throw InspectorException.newException( "Cannot assign a " + constructed.getClass() + " to a " + popped );

						popped = constructed;
					}
					else
					{
						popped = convertFromString( endRecording(), (Class<?>) popped );
					}
				}

				// Peek at what's next

				mExpect = EXPECT_OBJECT;

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
					Object[] arrayPeeked = (Object[]) mStackConstructing.pop();
					mStackConstructing.push( ArrayUtils.add( arrayPeeked, popped ) );
				}

				// Peeked Setter

				else if ( peeked instanceof Method )
				{
					Method methodSetter = (Method) mStackConstructing.pop();
					methodSetter.invoke( mStackConstructing.peek(), popped );
					mExpect = EXPECT_PROPERTY;
				}

				// Peeked Constructor for an xxxConfig

				/*else if ( peeked instanceof Constructor )
				{
					Constructor<?> constructor = (Constructor<?>) mStackConstructing.pop();

					if ( constructor.getParameterTypes().length == 2 )
						popped = constructor.newInstance( popped, ConfigReader2.this );
					else
						popped = constructor.newInstance( popped );

					// Peek again, because we need to put the Constructed object somewhere

					peeked = mStackConstructing.peek();

					if ( peeked instanceof Collection )
					{
						@SuppressWarnings( "unchecked" )
						Collection<Object> collectionPeeked = (Collection<Object>) peeked;
						collectionPeeked.add( popped );
					}
					else if ( peeked.getClass().isArray() )
					{
						Object[] arrayPeeked = (Object[]) mStackConstructing.pop();
						mStackConstructing.push( ArrayUtils.add( arrayPeeked, popped ) );
					}
					else
					{
						throw InspectorException.newException( "Don't know how to combine a " + popped.getClass() + " with a " + peeked.getClass() );
					}
				}*/
				else
				{
					mStackConstructed.push( popped );
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
	}
}
