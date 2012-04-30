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

package org.metawidget.vaadin.widgetprocessor.binding.simple;

/**
 * Converts a <code>String</code> into a <code>Character</code>. Vaadin seems to support most
 * primitive types by default, but not this one.
 *
 * @author Loghman Barari
 */

public class CharacterConverter
	extends BaseConverter<Character> {

	//
	// Public methods
	//

	public Character convertFromString( String value, Class<Character> expectedType ) {

		if ( value != null && value.length() > 0 ) {
			return value.charAt( 0 );
		}

		throw new RuntimeException( value );
	}
}
