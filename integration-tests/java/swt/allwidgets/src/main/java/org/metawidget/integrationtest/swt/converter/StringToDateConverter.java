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

package org.metawidget.integrationtest.swt.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;
import org.metawidget.iface.MetawidgetException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StringToDateConverter
	extends Converter {

	//
	// Private members
	//

	private DateFormat	mFormat;

	//
	// Constructor
	//

	public StringToDateConverter( String pattern ) {

		super( String.class, Date.class );

		mFormat = new SimpleDateFormat( pattern );
		mFormat.setLenient( false );
	}

	//
	// Public methods
	//

	public Object convert( Object toConvert ) {

		if ( toConvert == null || "".equals( toConvert ) ) {
			return null;
		}

		synchronized ( mFormat ) {
			try {
				return mFormat.parse( (String) toConvert );
			} catch ( ParseException e ) {
				throw MetawidgetException.newException( "Could not parse '" + toConvert + "'", e );
			}
		}
	}
}
