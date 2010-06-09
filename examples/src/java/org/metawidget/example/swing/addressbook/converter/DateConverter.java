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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.jdesktop.beansbinding.Converter;

/**
 * @author Richard Kennard
 */

public class DateConverter
	extends Converter<Date, String>
{
	//
	// Private statics
	//

	private final static DateFormat	FORMAT	= DateFormat.getDateInstance( DateFormat.SHORT );

	static
	{
		FORMAT.setLenient( false );
	}

	//
	// Public methods
	//

	@Override
	public String convertForward( Date date )
	{
		if ( date == null )
		{
			return "";
		}

		synchronized ( FORMAT )
		{
			return FORMAT.format( date );
		}
	}

	@Override
	public Date convertReverse( String date )
	{
		if ( date == null || "".equals( date ))
		{
			return null;
		}

		try
		{
			synchronized ( FORMAT )
			{
				return FORMAT.parse( date );
			}
		}
		catch( ParseException e )
		{
			throw new RuntimeException( e );
		}
	}
}
