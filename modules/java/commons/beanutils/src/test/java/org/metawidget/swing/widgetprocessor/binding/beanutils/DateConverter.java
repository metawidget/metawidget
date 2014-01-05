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

package org.metawidget.swing.widgetprocessor.binding.beanutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.metawidget.iface.MetawidgetException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DateConverter
	implements Converter {

	//
	// Private members
	//

	private DateFormat	mFormat;

	//
	// Constructor
	//

	public DateConverter( String pattern ) {

		mFormat = new SimpleDateFormat( pattern );
		mFormat.setLenient( false );
	}

	//
	// Public methods
	//

	@SuppressWarnings( "rawtypes" )
	public Object convert( Class clazz, Object value ) {

		// To String

		if ( clazz.equals( String.class ) ) {
			if ( value == null ) {
				return "";
			}

			synchronized ( mFormat ) {
				return mFormat.format( (Date) value );
			}
		}

		// To Date

		if ( clazz.equals( Date.class ) ) {
			if ( value == null || "".equals( value ) ) {
				return null;
			}

			synchronized ( mFormat ) {
				try {
					return mFormat.parse( (String) value );
				} catch ( ParseException e ) {
					throw MetawidgetException.newException( "Could not parse '" + value + "'", e );
				}
			}
		}

		throw new UnsupportedOperationException( "Don't know how to convert '" + value + "' to a " + clazz );
	}
}
