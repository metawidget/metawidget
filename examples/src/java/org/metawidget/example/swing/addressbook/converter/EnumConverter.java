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

package org.metawidget.example.swing.addressbook.converter;

import org.jdesktop.beansbinding.Converter;

/**
 * @author Richard Kennard
 */

public abstract class EnumConverter<T extends Enum<T>>
	extends Converter<T, String> {

	//
	// Private members
	//

	private Class<T>	mEnum;

	//
	// Constructor
	//

	protected EnumConverter( Class<T> anEnum ) {

		mEnum = anEnum;
	}

	//
	// Public methods
	//

	@Override
	public String convertForward( T anEnum ) {

		// The enum will have been converted to its '.name' by Java5Inspector when
		// it creates lookup values and labels. This means we must also convert the
		// enum to its '.name' during binding.
		//
		// The alternative to this is to have the Metawidgets deal with enums directly, but
		// that is less desirable because it ties the Metawidgets to a Java 5 platform

		return anEnum.name();
	}

	@Override
	public T convertReverse( String name ) {

		return Enum.valueOf( mEnum, name );
	}
}
