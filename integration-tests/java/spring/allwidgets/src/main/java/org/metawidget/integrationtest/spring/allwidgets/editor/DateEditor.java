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

package org.metawidget.integrationtest.spring.allwidgets.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DateEditor
	extends PropertyEditorSupport {

	//
	// Private statics
	//

	private static final DateFormat	FORMAT	= new SimpleDateFormat( "E MMM dd HH:mm:ss z yyyy" );

	static {
		FORMAT.setLenient( false );
	}

	//
	// Public methods
	//

	@Override
	public String getAsText() {

		Object value = getValue();

		if ( value == null ) {
			return "";
		}

		synchronized ( FORMAT ) {
			return FORMAT.format( value );
		}
	}

	@Override
	public void setAsText( String text ) {

		if ( text == null || "".equals( text ) ) {
			setValue( null );
			return;
		}

		try {
			synchronized ( FORMAT ) {
				setValue( FORMAT.parse( text ) );
			}
		} catch ( ParseException e ) {
			throw new IllegalArgumentException( e );
		}
	}
}