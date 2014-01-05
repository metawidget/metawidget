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

package org.metawidget.integrationtest.android.allwidgets.converter;

import org.metawidget.android.widget.widgetprocessor.binding.simple.Converter;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.simple.StringUtils;

import android.view.View;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NestedWidgetsConverter
	implements Converter<NestedWidgets> {

	//
	// Public methods
	//

	public Object convertForView( View widget, NestedWidgets value ) {

		return StringUtils.quietValueOf( value );
	}

	public NestedWidgets convertFromView( View widget, Object value, Class<?> intoClass ) {

		String[] values = ArrayUtils.fromString( (String) value );

		if ( values.length == 0 ) {
			return null;
		}

		NestedWidgets nestedWidgets = new NestedWidgets();
		nestedWidgets.setNestedTextbox1( values[0] );

		if ( values.length > 1 ) {
			nestedWidgets.setNestedTextbox2( values[1] );
		}

		return nestedWidgets;
	}
}
