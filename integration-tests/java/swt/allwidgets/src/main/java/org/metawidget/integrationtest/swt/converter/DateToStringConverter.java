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

package org.metawidget.integrationtest.swt.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DateToStringConverter
	extends Converter {

	//
	// Private members
	//

	private DateFormat	mFormat;

	//
	// Constructor
	//

	public DateToStringConverter( String pattern ) {

		super( Date.class, String.class );

		mFormat = new SimpleDateFormat( pattern );
		mFormat.setLenient( false );
	}

	//
	// Public methods
	//

	public Object convert( Object toConvert ) {

		if ( toConvert == null ) {
			return "";
		}

		synchronized ( mFormat ) {
			return mFormat.format( toConvert );
		}
	}
}
