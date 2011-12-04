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
 *
 *
 * @author Loghman Barari
 */

public class CharacterConverter implements Converter<Character> {

	public Character convert(Object newValue) {

		if (newValue instanceof Character) {
			return (Character) newValue;
		}
		else if (newValue != null) {

			String s = newValue.toString();

			if (s.length() > 0) {
				return s.charAt(0);
			}
		}

		return null;
	}

}
