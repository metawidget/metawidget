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

package org.metawidget.test.swing.allwidgets.converter.beanutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.metawidget.MetawidgetException;

/**
 * @author Richard Kennard
 */

public class DateConverter
	implements Converter
{
	//
	//
	// Private members
	//
	//

	private DateFormat	mFormat;

	//
	//
	// Constructor
	//
	//

	public DateConverter( String pattern )
	{
		mFormat = new SimpleDateFormat( pattern );
		mFormat.setLenient( false );
	}

	//
	//
	// Public methods
	//
	//

	@SuppressWarnings( "unchecked" )
	public Object convert( Class clazz, Object value )
	{
		// To String

		if ( clazz.equals( String.class ))
		{
			if ( value == null )
				return "";

			synchronized ( mFormat )
			{
				return mFormat.format( (Date) value );
			}
		}

		// To Date

		if ( clazz.equals( Date.class ))
		{
			if ( value == null || "".equals( value ))
				return null;

			synchronized ( mFormat )
			{
				try
				{
					return mFormat.parse( (String) value );
				}
				catch( ParseException e )
				{
					throw MetawidgetException.newException( "Could not parse '" + value + "'", e );
				}
			}
		}

		throw new UnsupportedOperationException( "Don't know how to convert '" + value + "' to a " + clazz );
	}
}
