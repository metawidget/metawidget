// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.statically.jsp.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget buildWidget( String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return null;
		}

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new StaticXmlStub();
		}

		// Masked (return a couple of nested Stubs, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			StaticXmlStub staticStub = new StaticXmlStub();
			staticStub.getChildren().add( new StaticXmlStub() );
			return staticStub;
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new StaticXmlStub();
		}

		// Spring Lookup?

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels == null ) {
				return new CoreOut();
			}

			// Special support for read-only lookups with labels

			List<String> labels = CollectionUtils.fromString( lookupLabels );

			if ( labels.isEmpty() ) {
				return new CoreOut();
			}

			return new CoreOut();
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				return new CoreOut();
			}

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) ) {
				return new CoreOut();
			}

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) ) {
				return new CoreOut();
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				return new CoreOut();
			}

			// Collections will be supported by JspWidgetBuilder

			if ( Collection.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
				return null;
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new CoreOut();
		}

		// Nested Metawidget

		return null;
	}
}
