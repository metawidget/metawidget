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

package org.metawidget.integrationtest.faces.allwidgets.converter;

import java.util.Date;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

/**
 * Converts Dates to a String representation.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@FacesConverter( forClass = Date.class )
public class DateConverter
	extends DateTimeConverter {

	//
	// Constructor
	//

	public DateConverter() {

		setPattern( "E MMM dd HH:mm:ss z yyyy" );
	}
}
