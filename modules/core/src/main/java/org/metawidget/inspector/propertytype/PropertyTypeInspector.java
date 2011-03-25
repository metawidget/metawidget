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
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public PropertyTypeInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public PropertyTypeInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected boolean shouldInspectPropertyAsEntity( Property property ) {

		return true;
	}

	@Override
	protected Map<String, String> inspectEntity( Class<?> declaredClass, Class<?> actualClass )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Type

		attributes.put( TYPE, declaredClass.getName() );

		// Actual class
		//
		// Note: we don't do this the other way around (eg. return the actual class as
		// TYPE and have a, say, DECLARED_CLASS attribute) because the type must be
		// consistent between Object and XML-based inspectors. In particular:
		//
		// 1. Entity types must be consistent in order for CompositeInspector to
		// merge correctly
		// 2. Property types don't need to be consistent, but sometimes a widget
		// needs to be created directly from the top-level entity level so the
		// WidgetBuilders will always need to check ACTUAL_CLASS
		// 3. We don't want to use a proxied class as the 'type'

		if ( !actualClass.equals( declaredClass ) ) {
			attributes.put( ACTUAL_CLASS, actualClass.getName() );
		}

		// Special support for Booleans, which are tri-state

		if ( Boolean.class.equals( actualClass ) ) {
			attributes.put( LOOKUP, "true, false" );
			attributes.put( LOOKUP_LABELS, "Yes, No" );
		}

		return attributes;
	}

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// No setter
		//
		// Note: we do not also attributes.put( READ_ONLY, TRUE ) here. If an attribute
		// has no setter, but IS a complex type, then it should not be considered READ_ONLY
		// as it may be settable by its nested primitives

		if ( !property.isWritable() ) {
			attributes.put( NO_SETTER, TRUE );
		}

		// No getter

		if ( !property.isReadable() ) {
			attributes.put( NO_GETTER, TRUE );
		}

		return attributes;
	}
}
