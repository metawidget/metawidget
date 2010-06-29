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

package org.metawidget.example.swt.addressbook.converter;

import org.eclipse.core.databinding.conversion.Converter;
import org.metawidget.example.shared.addressbook.model.Gender;

/**
 * @author Richard Kennard
 */

public class GenderToStringConverter
	extends Converter {

	//
	// Constructor
	//

	public GenderToStringConverter() {

		super( Gender.class, String.class );
	}

	//
	// Public methods
	//

	@Override
	public Object convert( Object toConvert ) {

		return ( (Gender) toConvert ).name();
	}
}
