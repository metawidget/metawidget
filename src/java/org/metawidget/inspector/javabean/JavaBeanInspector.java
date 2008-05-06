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

package org.metawidget.inspector.javabean;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.javabean.JavaBeanInspectionResultConstants.*;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.metawidget.inspector.impl.AbstractPojoInspector;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspector to look for properties defined by the JavaBean convention.
 * <p>
 * The properties are returned alphabetically as a default ordering. Most clients will want to
 * refine this by using, say, <code>UiComesAfter</code> and MetawidgetAnnotationInspector.
 *
 * @author Richard Kennard
 */

public class JavaBeanInspector
	extends AbstractPojoInspector
{
	//
	//
	// Private members
	//
	//

	private String[]	mExcludeProperties;

	private Class<?>[]	mExcludeReturnTypes;

	private boolean		mSorted;

	//
	//
	// Constructor
	//
	//

	public JavaBeanInspector()
	{
		this( new JavaBeanInspectorConfig() );
	}

	public JavaBeanInspector( JavaBeanInspectorConfig config )
	{
		super( config );

		// Defensive copy

		String[] excludeProperties = config.getExcludeProperties();
		mExcludeProperties = new String[excludeProperties.length];
		System.arraycopy( excludeProperties, 0, mExcludeProperties, 0, excludeProperties.length );

		Class<?>[] excludeReturnTypes = config.getExcludeReturnTypes();
		mExcludeReturnTypes = new Class<?>[excludeReturnTypes.length];
		System.arraycopy( excludeReturnTypes, 0, mExcludeReturnTypes, 0, excludeReturnTypes.length );

		mSorted = config.isSorted();
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected void inspect( Class<?> clazz, Object toInspect, Element toAddTo )
		throws Exception
	{
		Document document = toAddTo.getOwnerDocument();
		Map<String, Map<String, String>> sortedElements = CollectionUtils.newTreeMap();

		for ( Property property : getProperties( clazz ).values() )
		{
			String name = property.getName();

			// Ignore JavaBean-convention properties

			if ( ArrayUtils.contains( mExcludeProperties, name ) )
				continue;

			// Ignore some return types

			Class<?> returnType = property.getPropertyClass();

			if ( ArrayUtils.contains( mExcludeReturnTypes, returnType ) )
				continue;

			Map<String, String> attributes = inspect( property, toInspect );

			// If sorting, add it to our TreeMap...

			if ( mSorted )
			{
				sortedElements.put( name, attributes );
				continue;
			}

			// ...otherwise, add immediately

			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, name );
			XmlUtils.setMapAsAttributes( element, attributes );
			toAddTo.appendChild( element );
		}

		// Sort by name

		if ( !mSorted )
			return;

		// Compile document

		for ( Map.Entry<String, Map<String, String>> entry : sortedElements.entrySet() )
		{
			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, entry.getKey() );

			XmlUtils.setMapAsAttributes( element, entry.getValue() );

			toAddTo.appendChild( element );
		}
	}

	@Override
	protected Map<String, String> inspect( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// ...type...

		Class<?> propertyClass = property.getPropertyClass();

		// ...(may be polymorphic)...

		Class<?> actualClass = null;

		if ( !Modifier.isFinal( propertyClass.getModifiers() ) )
		{
			if ( property.isReadable() )
			{
				try
				{
					Object actual = property.read( toInspect );

					if ( actual != null )
						actualClass = ClassUtils.getUnproxiedClass( actual.getClass(), mPatternProxy );
				}
				catch ( Throwable t )
				{
					// By definition, a 'getter' method should not affect the state
					// of the object. However, sometimes a getter's implementation
					// may fail if an object is not in a certain state (eg. JSF's
					// DataModel.getRowData) - in which case fall back to property
				}
			}
		}

		if ( actualClass != null && !propertyClass.equals( actualClass ) )
		{
			attributes.put( DECLARED_CLASS, propertyClass.getName() );
			attributes.put( TYPE, actualClass.getName() );
		}
		else
		{
			attributes.put( TYPE, propertyClass.getName() );
		}

		// ...(no-setter/no-getter)...

		if ( !property.isWritable() )
		{
			attributes.put( NO_SETTER, TRUE );
			attributes.put( READ_ONLY, TRUE );
		}

		if ( !property.isReadable() )
			attributes.put( NO_GETTER, TRUE );

		return attributes;
	}
}
