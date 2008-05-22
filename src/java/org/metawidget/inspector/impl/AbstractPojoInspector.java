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

package org.metawidget.inspector.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import org.metawidget.inspector.InspectorException;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience implementation for Inspectors that inspect POJOs.
 * <p>
 * Handles iterating over a POJO for JavaBean-convention properties and public member fields. Also
 * handles unwrapping a POJO wrapped by a proxy library (such as CGLIB or Javassist).
 *
 * @author Richard Kennard
 */

public abstract class AbstractPojoInspector
	extends AbstractInspector
{
	//
	//
	// Private statics
	//
	//

	private final static Map<Class<?>, Map<String, Property>>	PROPERTIES_CACHE	= CollectionUtils.newWeakHashMap();

	//
	//
	// Protected members
	//
	//

	protected Pattern											mPatternProxy;

	//
	//
	// Constructors
	//
	//

	/**
	 * Default constructor. It is intended many AbstractPojoInspector subclasses be functional
	 * without explicit configuration, and this saves subclasses having to define explicit
	 * constructors.
	 */

	protected AbstractPojoInspector()
	{
		this( new AbstractPojoInspectorConfig() );
	}

	protected AbstractPojoInspector( AbstractPojoInspectorConfig config )
	{
		mPatternProxy = config.getProxyPattern();
	}

	//
	//
	// Public methods
	//
	//

	public Document inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		try
		{
			Object childToInspect = null;
			String strChildName = null;
			Map<String, String> mapParentAttributes = null;
			Class<?> clazz = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful annotations...

				Object objParentToInspect = traverse( toInspect, type, true, names );

				if ( objParentToInspect == null )
					return null;

				Class<?> classParent = ClassUtils.getUnproxiedClass( objParentToInspect.getClass(), mPatternProxy );
				strChildName = names[names.length - 1];

				Property propertyInParent = getProperties( classParent ).get( strChildName );

				if ( propertyInParent == null )
					return null;

				clazz = propertyInParent.getPropertyClass();

				if ( propertyInParent.isReadable() )
				{
					childToInspect = propertyInParent.read( objParentToInspect );

					mapParentAttributes = inspect( propertyInParent, toInspect );

					if ( !Modifier.isFinal( clazz.getModifiers() ) && childToInspect != null )
						clazz = ClassUtils.getUnproxiedClass( childToInspect.getClass(), mPatternProxy );
				}
			}

			// ...otherwise, just start at the end point

			if ( clazz == null )
			{
				childToInspect = traverse( toInspect, type, false, names );

				if ( childToInspect == null )
					return null;

				clazz = ClassUtils.getUnproxiedClass( childToInspect.getClass(), mPatternProxy );
			}

			Document document = newDocumentBuilder().newDocument();
			Element elementEntity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect child properties

			if ( childToInspect != null )
				inspect( clazz, childToInspect, elementEntity );

			// Nothing of consequence to return?

			if ( !elementEntity.hasChildNodes() && ( mapParentAttributes == null || mapParentAttributes.isEmpty() ) )
				return null;

			// Start a new DOM Document

			Element elementRoot = document.createElementNS( NAMESPACE, ROOT );
			document.appendChild( elementRoot );
			elementRoot.appendChild( elementEntity );
			elementEntity.setAttribute( TYPE, clazz.getName() );

			// Add any parent attributes

			if ( mapParentAttributes != null )
			{
				XmlUtils.setMapAsAttributes( elementEntity, mapParentAttributes );
				elementEntity.setAttribute( NAME, strChildName );
			}

			// Return the document

			return document;
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	//
	// Protected methods
	//
	//

	protected void inspect( Class<?> clazz, Object toInspect, Element toAddTo )
		throws Exception
	{
		Document document = toAddTo.getOwnerDocument();

		for ( Property property : getProperties( clazz ).values() )
		{
			Map<String, String> attributes = inspect( property, toInspect );

			if ( attributes == null || attributes.isEmpty() )
				continue;

			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, property.getName() );

			XmlUtils.setMapAsAttributes( element, attributes );

			toAddTo.appendChild( element );
		}
	}

	/**
	 * Inspect the given property and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 */

	protected abstract Map<String, String> inspect( Property property, Object toInspect )
		throws Exception;

	protected Map<String, Property> getProperties( Class<?> clazz )
		throws InspectorException
	{
		// Sanity check

		String className = clazz.getName();

		if ( mPatternProxy.matcher( className ).find() )
			throw InspectorException.newException( className + " is proxied" );

		synchronized ( PROPERTIES_CACHE )
		{
			Map<String, Property> properties = PROPERTIES_CACHE.get( clazz );

			if ( properties == null )
			{
				properties = CollectionUtils.newHashMap();

				// Public fields

				for ( Field field : clazz.getFields() )
				{
					// Ignore static public fields (like Groovy's __timestamp)

					if ( Modifier.isStatic( field.getModifiers() ) )
						continue;

					String propertyName = field.getName();
					properties.put( propertyName, new FieldProperty( propertyName, field ) );
				}

				// Getter methods

				for ( Method methodRead : clazz.getMethods() )
				{
					Class<?>[] parameters = methodRead.getParameterTypes();

					if ( parameters.length != 0 )
						continue;

					Class<?> toReturn = methodRead.getReturnType();

					if ( void.class.equals( toReturn ) )
						continue;

					String methodName = methodRead.getName();
					String propertyName = null;

					if ( methodName.startsWith( ClassUtils.JAVABEAN_GET_PREFIX ) )
						propertyName = methodName.substring( ClassUtils.JAVABEAN_GET_PREFIX.length() );
					else if ( methodName.startsWith( ClassUtils.JAVABEAN_IS_PREFIX ) )
						propertyName = methodName.substring( ClassUtils.JAVABEAN_IS_PREFIX.length() );
					else
						continue;

					if ( !StringUtils.isFirstLetterUppercase( propertyName ) )
						continue;

					// Already found (via its field/getter)?

					String lowercasedPropertyName = StringUtils.lowercaseFirstLetter( propertyName );

					Property propertyExisting = properties.get( lowercasedPropertyName );

					if ( propertyExisting != null )
					{
						if ( !( propertyExisting instanceof JavaBeanProperty ) )
							continue;

						// Beware covariant return types: always prefer the
						// subclass

						if ( toReturn.isAssignableFrom( propertyExisting.getPropertyClass() ) )
							continue;
					}

					// Try to find a matching setter

					Method methodWrite = null;

					try
					{
						methodWrite = clazz.getMethod( ClassUtils.JAVABEAN_SET_PREFIX + propertyName, toReturn );
					}
					catch ( NoSuchMethodException e )
					{
						// May not be one
					}

					properties.put( lowercasedPropertyName, new JavaBeanProperty( lowercasedPropertyName, toReturn, methodRead, methodWrite ) );
				}

				// Setter methods (for those without getters)

				for ( Method methodWrite : clazz.getMethods() )
				{
					Class<?>[] parameters = methodWrite.getParameterTypes();

					if ( parameters.length != 1 )
						continue;

					String methodName = methodWrite.getName();

					if ( !methodName.startsWith( ClassUtils.JAVABEAN_SET_PREFIX ) )
						continue;

					String propertyName = methodName.substring( ClassUtils.JAVABEAN_SET_PREFIX.length() );

					if ( !StringUtils.isFirstLetterUppercase( propertyName ) )
						continue;

					// Already found (via its field/getter)?

					String lowercasedPropertyName = StringUtils.lowercaseFirstLetter( propertyName );

					if ( properties.containsKey( lowercasedPropertyName ) )
						continue;

					properties.put( lowercasedPropertyName, new JavaBeanProperty( lowercasedPropertyName, parameters[0], null, methodWrite ) );
				}

				PROPERTIES_CACHE.put( clazz, Collections.unmodifiableMap( properties ) );
			}

			return properties;
		}
	}

	//
	//
	// Private methods
	//
	//

	private Object traverse( Object toTraverse, String type, boolean onlyToParent, String... names )
		throws InspectorException
	{
		// Validate type

		if ( toTraverse == null )
			return null;

		if ( !ClassUtils.getUnproxiedClass( toTraverse.getClass(), mPatternProxy ).getName().equals( type ) )
			return null;

		if ( names == null )
			return toTraverse;

		int length = names.length;

		if ( length == 0 )
			return toTraverse;

		// Traverse names

		if ( onlyToParent )
			length--;

		Object traverse = toTraverse;

		for ( int loop = 0; loop < length; loop++ )
		{
			Property property = getProperties( ClassUtils.getUnproxiedClass( traverse.getClass(), mPatternProxy ) ).get( names[loop] );

			if ( !property.isReadable() )
				return null;

			traverse = property.read( traverse );

			if ( traverse == null )
				return null;
		}

		return traverse;
	}

	//
	//
	// Inner classes
	//
	//

	/**
	 * Unifies JavaBean-convention-based and field-based properties, so that subclasses can treat
	 * them as one and the same.
	 * <p>
	 * Note: this class has the same methods as <code>java.lang.reflect.AnnotatedElement</code>,
	 * but stops short of <code>implements AnnotatedElement</code> in order to maintain J2SE 1.4
	 * compatibility.
	 */

	protected abstract static class Property
	{
		//
		//
		// Private methods
		//
		//

		private String		mName;

		private Class<?>	mClass;

		//
		//
		// Constructor
		//
		//

		public Property( String name, Class<?> clazz )
		{
			mName = name;
			mClass = clazz;
		}

		//
		//
		// Public methods
		//
		//

		public String getName()
		{
			return mName;
		}

		public Class<?> getPropertyClass()
		{
			return mClass;
		}

		public abstract boolean isReadable();

		public abstract Object read( Object obj );

		public abstract boolean isWritable();

		public abstract <T extends Annotation> T getAnnotation( Class<T> annotation );

		public boolean isAnnotationPresent( Class<? extends Annotation> annotation )
		{
			return ( getAnnotation( annotation ) != null );
		}

		public abstract Type getPropertyGenericType();

		@Override
		public String toString()
		{
			return mName;
		}
	}

	/**
	 * Public member field-based property.
	 */

	private static class FieldProperty
		extends Property
	{
		//
		//
		// Private methods
		//
		//

		private Field	mField;

		//
		//
		// Constructor
		//
		//

		public FieldProperty( String name, Field field )
		{
			super( name, field.getType() );

			mField = field;
		}

		//
		//
		// Public methods
		//
		//

		public Field getField()
		{
			return mField;
		}

		@Override
		public boolean isReadable()
		{
			return true;
		}

		@Override
		public Object read( Object obj )
		{
			try
			{
				return mField.get( obj );
			}
			catch ( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		@Override
		public boolean isWritable()
		{
			return true;
		}

		@Override
		public <T extends Annotation> T getAnnotation( Class<T> annotation )
		{
			return mField.getAnnotation( annotation );
		}

		public Annotation[] getAnnotations()
		{
			return mField.getAnnotations();
		}

		public Annotation[] getDeclaredAnnotations()
		{
			return mField.getDeclaredAnnotations();
		}

		@Override
		public Type getPropertyGenericType()
		{
			return mField.getGenericType();
		}
	}

	/**
	 * JavaBean-convention-based property.
	 */

	private static class JavaBeanProperty
		extends Property
	{
		//
		//
		// Private methods
		//
		//

		private Method	mReadMethod;

		private Method	mWriteMethod;

		//
		//
		// Constructor
		//
		//

		public JavaBeanProperty( String name, Class<?> clazz, Method readMethod, Method writeMethod )
		{
			super( name, clazz );

			mReadMethod = readMethod;
			mWriteMethod = writeMethod;
		}

		//
		//
		// Public methods
		//
		//

		public Method getReadMethod()
		{
			return mReadMethod;
		}

		public Method getWriteMethod()
		{
			return mWriteMethod;
		}

		@Override
		public boolean isReadable()
		{
			return ( mReadMethod != null );
		}

		@Override
		public Object read( Object obj )
		{
			try
			{
				return mReadMethod.invoke( obj );
			}
			catch ( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		@Override
		public boolean isWritable()
		{
			return ( mWriteMethod != null );
		}

		@Override
		public <T extends Annotation> T getAnnotation( Class<T> annotationClass )
		{
			if ( mReadMethod != null )
			{
				T annotation = mReadMethod.getAnnotation( annotationClass );

				if ( annotation != null )
					return annotation;
			}

			if ( mWriteMethod != null )
			{
				T annotation = mWriteMethod.getAnnotation( annotationClass );

				if ( annotation != null )
					return annotation;
			}

			return null;
		}

		public Annotation[] getAnnotations()
		{
			if ( mReadMethod == null )
			{
				if ( mWriteMethod == null )
					return null;

				return mWriteMethod.getAnnotations();
			}

			Annotation[] annotationsRead = mReadMethod.getAnnotations();

			if ( mWriteMethod == null )
				return annotationsRead;

			return ArrayUtils.add( annotationsRead, mWriteMethod.getAnnotations() );
		}

		public Annotation[] getDeclaredAnnotations()
		{
			if ( mReadMethod == null )
			{
				if ( mWriteMethod == null )
					return null;

				return mWriteMethod.getDeclaredAnnotations();
			}

			Annotation[] annotationsRead = mReadMethod.getDeclaredAnnotations();

			if ( mWriteMethod == null )
				return annotationsRead;

			return ArrayUtils.add( annotationsRead, mWriteMethod.getDeclaredAnnotations() );
		}

		@Override
		public Type getPropertyGenericType()
		{
			if ( mReadMethod != null )
				return mReadMethod.getGenericReturnType();

			if ( mWriteMethod != null )
				return mWriteMethod.getGenericParameterTypes()[0];

			return null;
		}
	}
}
