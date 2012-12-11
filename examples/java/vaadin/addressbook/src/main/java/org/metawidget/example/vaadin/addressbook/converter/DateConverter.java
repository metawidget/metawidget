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

package org.metawidget.example.vaadin.addressbook.converter;

import java.text.DateFormat;
import java.util.Date;

import org.metawidget.vaadin.ui.widgetprocessor.binding.simple.Converter;

public class DateConverter
	implements Converter<Date, String> {

	//
	// Private members
	//

	private DateFormat	mFormat;

	//
	// Constructor
	//

	public DateConverter() {

		mFormat = DateFormat.getDateInstance( DateFormat.SHORT );
	}

	//
	// Public methods
	//

	public String convert( Date value, Class<? extends String> expectedType ) {

		if ( value == null ) {
			return null;
		}

		synchronized ( mFormat ) {
			return mFormat.format( value );
		}
	}
}
