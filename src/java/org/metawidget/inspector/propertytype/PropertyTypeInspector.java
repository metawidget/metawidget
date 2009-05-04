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

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

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
	protected boolean shouldInspectPropertyAsEntity( Property property )
	{
		return true;
	}

	@Override
	protected Map<String, String> inspectPropertyAsEntity( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = super.inspectPropertyAsEntity( property, toInspect );

		// Actual class
		//
		// Note: we don't do this the other way around (eg. return the actual class as
		// TYPE and have a, say, DECLARED_CLASS attribute) because the type must be
		// consistent between Object and XML-based inspectors. In particular, we don't
		// want to use a proxied class as the 'type'

		String propertyClass = property.getType().getName();
		String actualClass = attributes.get( TYPE );

		if ( !actualClass.equals( propertyClass ) )
		{
			attributes.put( TYPE, propertyClass );
			attributes.put( ACTUAL_CLASS, actualClass );
		}

		return attributes;
	}

	@Override
	protected Map<String, String> inspectPropertyFromParent( Property propertyInParent, Object parentToInspect )
		throws Exception
	{
		// Return the parent property's type. This will be the declared type, not
		// the actual type. This is a special case for when the value of the parent
		// property turns out to be null. For most Inspectors, they should abort at
		// that point, but PropertyTypeInspector needs to still return a type

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, propertyInParent.getType().getName() );

		return attributes;
	}

	@Override
	protected Map<String, String> inspectEntity( Class<?> classToInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Type

		attributes.put( TYPE, classToInspect.getName() );

		// Special support for Booleans, which are tri-state

		if ( Boolean.class.isAssignableFrom( classToInspect ) )
		{
			attributes.put( LOOKUP, "true, false" );
			attributes.put( LOOKUP_LABELS, "Yes, No" );
		}

		return attributes;
	}

	@Override
	protected Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// No setter
		//
		// Note: we do not also attributes.put( READ_ONLY, TRUE ) here. If an attribute
		// has no setter, but IS a complex type, then it should not be considered READ_ONLY
		// as it may be settable by its nested primitives

		if ( !property.isWritable() )
			attributes.put( NO_SETTER, TRUE );

		// No getter

		if ( !property.isReadable() )
			attributes.put( NO_GETTER, TRUE );

		return attributes;
	}
}
