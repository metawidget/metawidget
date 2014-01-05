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

import org.eclipse.core.databinding.conversion.Converter;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NestedWidgetsToStringConverter
	extends Converter {

	//
	// Constructor
	//

	public NestedWidgetsToStringConverter() {

		super( NestedWidgets.class, String.class );
	}

	//
	// Public methods
	//

	public Object convert( Object toConvert ) {

		return StringUtils.quietValueOf( toConvert );
	}
}
