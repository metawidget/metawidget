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

package org.metawidget.integrationtest.android.allwidgets.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.metawidget.android.widget.widgetprocessor.binding.simple.Converter;

import android.view.View;
import android.widget.DatePicker;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DateConverter
	implements Converter<Date> {

	//
	// Private statics
	//

	private static final DateFormat	FORMAT	= DateFormat.getDateInstance( DateFormat.SHORT );

	//
	// Public methods
	//

	public Object convertForView( View widget, Date date ) {

		if ( widget instanceof DatePicker ) {
			return date;
		}

		synchronized ( FORMAT ) {
			return FORMAT.format( date );
		}
	}

	public Date convertFromView( View widget, Object date, Class<?> intoClass ) {

		if ( date instanceof String ) {
			try {
				synchronized ( FORMAT ) {
					return FORMAT.parse( (String) date );
				}
			} catch ( ParseException e ) {
				throw new RuntimeException( e );
			}
		}

		return (Date) date;
	}
}
