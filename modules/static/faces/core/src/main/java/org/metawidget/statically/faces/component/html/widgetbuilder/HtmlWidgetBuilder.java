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

package org.metawidget.statically.faces.component.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticStub;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.faces.component.html.HtmlInputText;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class HtmlWidgetBuilder
	implements WidgetBuilder<StaticWidget, StaticMetawidget> {

	//
	// Public methods
	//

	public StaticWidget buildWidget( String elementName, Map<String, String> attributes, StaticMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new StaticStub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new StaticStub();
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, fail gracefully with a text box

		if ( type == null ) {
			return new HtmlInputText();
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new HtmlInputText();
		}

		if ( clazz != null ) {
			// booleans

			if ( boolean.class.equals( clazz ) ) {
				return new HtmlInputText();
			}

			// Primitives

			if ( clazz.isPrimitive() ) {
				return new HtmlInputText();
			}

			// String

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return new HtmlInputText();
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					return new HtmlInputText();
				}

				return new HtmlInputText();
			}

			// Character

			if ( Character.class.equals( clazz ) ) {
				return new HtmlInputText();
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return new HtmlInputText();
			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return new HtmlInputText();
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new StaticStub();
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new HtmlInputText();
		}

		// Not simple

		return null;
	}
}
