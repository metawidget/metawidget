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

package org.metawidget.integrationtest.gwt.allwidgets.client.converter;

import java.util.Date;

import org.metawidget.gwt.client.widgetprocessor.binding.simple.BaseConverter;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DateConverter
	extends BaseConverter<Date> {

	//
	// Private members
	//

	private DateTimeFormat	mFormat;

	//
	// Constructor
	//

	public DateConverter() {

		mFormat = DateTimeFormat.getFormat( PredefinedFormat.DATE_SHORT );
	}

	//
	// Public methods
	//

	public Date convertFromWidget( Widget widget, Object value, Class<?> type ) {

		if ( value == null || "".equals( value ) ) {
			return null;
		}

		return mFormat.parse( (String) value );
	}

	@Override
	public Object convertForWidget( Widget widget, Date value ) {

		if ( value == null ) {
			return null;
		}

		return mFormat.format( value );
	}
}
