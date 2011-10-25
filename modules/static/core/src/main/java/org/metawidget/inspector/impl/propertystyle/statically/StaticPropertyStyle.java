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

package org.metawidget.inspector.impl.propertystyle.statically;

import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.simple.Pair;

/**
 * @author Richard Kennard
 */

public class StaticPropertyStyle
	extends JavaBeanPropertyStyle {

	//
	// Public methods
	//

	/**
	 * Traverses the given Class heirarchy using properties of the given names.
	 *
	 * @return the declared type (not actual type). May be null
	 */

	@Override
	public Pair<Object, String> traverse( Object toTraverse, String type, boolean onlyToParent, String... names ) {

		// Traverse through names (if any)

		if ( names == null || names.length == 0 ) {

			// If no names, no parent

			if ( onlyToParent ) {
				return new Pair<Object,String>( null, null );
			}

			return new Pair<Object,String>( null, type );
		}

		String traverseDeclaredType = type;

		for ( int loop = 0, length = names.length; loop < length; loop++ ) {

			if ( onlyToParent && loop >= length - 1 ) {
				return new Pair<Object,String>( null, traverseDeclaredType );
			}

			String name = names[loop];
			Property property = getProperties( traverseDeclaredType ).get( name );

			if ( property == null || !property.isReadable() ) {
				return new Pair<Object,String>( null, null );
			}

			traverseDeclaredType = property.getType();
		}

		return new Pair<Object,String>( null, traverseDeclaredType );
	}

	@Override
	public boolean isStopAtNull() {

		return false;
	}
}
