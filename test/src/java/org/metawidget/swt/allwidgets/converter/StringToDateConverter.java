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

package org.metawidget.swt.allwidgets.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;
import org.metawidget.iface.MetawidgetException;

/**
 * @author Richard Kennard
 */

public class StringToDateConverter
	extends Converter
{
	//
	// Private members
	//

	private DateFormat	mFormat;

	//
	// Constructor
	//

	public StringToDateConverter( String pattern )
	{
		super( String.class, Date.class );

		mFormat = new SimpleDateFormat( pattern );
		mFormat.setLenient( false );
	}

	//
	// Public methods
	//


	@Override
	public Object convert( Object toConvert )
	{
		if ( toConvert == null || "".equals( toConvert ))
		{
			return null;
		}

		synchronized ( mFormat )
		{
			try
			{
				return mFormat.parse( (String) toConvert );
			}
			catch( ParseException e )
			{
				throw MetawidgetException.newException( "Could not parse '" + toConvert + "'", e );
			}
		}
	}
}
