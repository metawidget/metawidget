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

package org.metawidget.inspector.propertytype;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.w3c.dom.Element;

/**
 * Inspector to look for types of properties.
 * <p>
 * If the actual type of the property's object is a subtype of the declared type, both the actual
 * and the declared type are returned.
 * <p>
 * The properties are returned in the order defined by their <code>PropertyStyle</code>. For
 * <code>JavaBeanPropertyStyle</code> (the default) this is 'alphabetical by name'. Most clients
 * will want to refine this by using, say, <code>UiComesAfter</code> and
 * MetawidgetAnnotationInspector.
 * 
 * @author Richard Kennard
 */

public class PropertyTypeInspector
	extends BaseObjectInspector
{
	//
	// Constructor
	//

	public PropertyTypeInspector()
	{
		this( new BaseObjectInspectorConfig() );
	}

	public PropertyTypeInspector( BaseObjectInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// ...type...

		Class<?> propertyClass = property.getType();
		attributes.put( TYPE, propertyClass.getName() );

		// ...(may be polymorphic)...

		if ( !Modifier.isFinal( propertyClass.getModifiers() ) )
		{
			if ( property.isReadable() )
			{
				try
				{
					Object actual = property.read( toInspect );

					// Note: we don't do this the other way around (eg. return the actual class as
					// TYPE and have a, say, DECLARED_CLASS attribute) because the type must be
					// consistent between Object and XML-based inspectors. In particular, we don't
					// want to use a proxied class as the 'type'

					if ( actual != null )
						attributes.put( ACTUAL_CLASS, actual.getClass().getName() );
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

		// ...(no-setter/no-getter)...

		if ( !property.isWritable() )
		{
			attributes.put( NO_SETTER, TRUE );

			// Note: we do not also attributes.put( READ_ONLY, TRUE ) here. If an attribute
			// has no setter, but IS a complex type, then it should not be considered READ_ONLY
			// as it may be settable by its nested primitives
		}

		if ( !property.isReadable() )
			attributes.put( NO_GETTER, TRUE );

		return attributes;
	}

	/**
	 * Overridden to return <code>false<code>.
	 * <p>
	 * <code>PropertyTypeInspector</code> always returns an XML document, even if just to convey the
	 * <code>type</code> attribute of the top-level <code>entity</code> element.
	 */

	@Override
	protected boolean isInspectionEmpty( Element elementEntity, Map<String, String> parentAttributes )
	{
		return false;
	}
}
