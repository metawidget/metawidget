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

package org.metawidget.test.swing.allwidgets.converter.beansbinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdesktop.beansbinding.Converter;
import org.metawidget.iface.MetawidgetException;

/**
 * @author Richard Kennard
 */

public class DateConverter
	extends Converter<Date,String>
{
	//
	// Private members
	//

	private DateFormat	mFormat;

	//
	// Constructor
	//

	public DateConverter( String pattern )
	{
		mFormat = new SimpleDateFormat( pattern );
		mFormat.setLenient( false );
	}

	//
	// Public methods
	//

	@Override
	public String convertForward( Date value )
	{
		if ( value == null )
			return "";

		synchronized ( mFormat )
		{
			return mFormat.format( value );
		}
	}

	@Override
	public Date convertReverse( String value )
	{
		if ( value == null || "".equals( value ))
			return null;

		synchronized ( mFormat )
		{
			try
			{
				return mFormat.parse( value );
			}
			catch( ParseException e )
			{
				throw MetawidgetException.newException( "Could not parse '" + value + "'", e );
			}
		}
	}
}
