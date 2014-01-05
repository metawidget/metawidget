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

package org.metawidget.integrationtest.gwt.allwidgets.client.converter;

import java.util.List;

import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.BaseConverter;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.util.simple.StringUtils;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NestedWidgetsConverter
	extends BaseConverter<NestedWidgets> {

	//
	// Public methods
	//

	public NestedWidgets convertFromWidget( Widget widget, Object value, Class<?> type ) {

		List<String> values = GwtUtils.fromString( (String) value, StringUtils.SEPARATOR_COMMA_CHAR );

		if ( values.isEmpty() ) {
			return null;
		}

		NestedWidgets nestedWidgets = new NestedWidgets();
		nestedWidgets.setNestedTextbox1( values.get( 0 ) );

		if ( values.size() > 1 ) {
			nestedWidgets.setNestedTextbox2( values.get( 1 ) );
		}

		return nestedWidgets;
	}
}
