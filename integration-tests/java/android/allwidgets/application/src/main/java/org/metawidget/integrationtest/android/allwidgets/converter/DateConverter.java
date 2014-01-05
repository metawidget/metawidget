// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
