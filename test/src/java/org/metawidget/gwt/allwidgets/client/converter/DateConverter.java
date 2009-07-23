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

package org.metawidget.gwt.allwidgets.client.converter;

import java.util.Date;

import org.metawidget.gwt.client.propertybinding.simple.BaseConverter;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class DateConverter
	extends BaseConverter<Date>
{
	//
	// Private members
	//

	private DateTimeFormat	mFormat;

	//
	// Constructor
	//

	public DateConverter()
	{
		mFormat = DateTimeFormat.getShortDateFormat();
	}

	//
	// Public methods
	//

	public Date convertFromWidget( Widget widget, Object value, Class<?> type )
	{
		if ( value == null || "".equals( value ))
			return null;

		return mFormat.parse( (String) value );
	}

	@Override
	public Object convertForWidget( Widget widget, Date value )
	{
		if ( value == null )
			return null;

		return mFormat.format( value );
	}
}
