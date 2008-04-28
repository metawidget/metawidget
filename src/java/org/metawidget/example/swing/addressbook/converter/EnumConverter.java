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

public class EnumConverter<T extends Enum<T>>
	extends Converter<T, Object>
{
	//
	//
	// Private members
	//
	//

	private Class<T>	mEnum;

	//
	//
	// Constructor
	//
	//

	public EnumConverter( Class<T> anEnum )
	{
		mEnum = anEnum;
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public Object convertForward( T anEnum )
	{
		return anEnum.name();
	}

	@Override
	public T convertReverse( Object name )
	{
		return Enum.valueOf( mEnum, (String) name );
	}
}
