// Metawidget (licensed under LGPL)
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

package org.metawidget.vaadin.ui.widgetprocessor.binding.simple;

/**
 * Built-in Converter to convert Objects to Strings.
 *
 * @author Richard Kennard
 */

public class ToStringConverter
	implements Converter<Object, String> {

	//
	// Public methods
	//

	public String convert( Object value, Class<? extends String> actualType ) {

		if ( value == null ) {
			return "";
		}

		// Convert Enums to their name(), not their .toString(). We rely on
		// org.metawidget.vaadin.ui.widgetbuilder.LookupLabel to fix this up for presentation

		if ( value instanceof Enum ) {
			return ( (Enum<?>) value ).name();
		}

		return value.toString();
	}
}
