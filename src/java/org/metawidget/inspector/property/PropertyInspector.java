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

package org.metawidget.inspector.property;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.property.PropertyInspectionResultConstants.*;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.metawidget.inspector.impl.AbstractPropertyInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspector to look for names and types of properties.
 * <p>
 * If the actual type of the property's object is a subtype of the declared type, both
 * the actual and the declared type are returned.
 * <p>
 * The properties are returned alphabetically as a default ordering. Most clients will want to
 * refine this by using, say, <code>UiComesAfter</code> and MetawidgetAnnotationInspector.
 *
 * @author Richard Kennard
 */

public class PropertyInspector
	extends AbstractPropertyInspector
{
	//
	//
	// Private members
	//
	//

	private boolean		mSorted;

	//
	//
	// Constructor
	//
	//

	public PropertyInspector()
	{
		this( new PropertyInspectorConfig() );
	}

	public PropertyInspector( PropertyInspectorConfig config )
	{
		super( config );

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

		Class<?> propertyClass = property.getType();

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
