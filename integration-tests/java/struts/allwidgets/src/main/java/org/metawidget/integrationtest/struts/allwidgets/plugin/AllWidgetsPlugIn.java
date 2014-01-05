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

package org.metawidget.integrationtest.struts.allwidgets.plugin;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.integrationtest.struts.allwidgets.converter.NestedWidgetsConverter;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AllWidgetsPlugIn
	implements PlugIn {

	//
	// Public methods
	//

	public void init( ActionServlet servlet, ModuleConfig config ) {

		DateConverter converterDate = new DateConverter();
		converterDate.setPattern( "E MMM dd HH:mm:ss z yyyy" );
		ConvertUtils.register( converterDate, Date.class );

		ConvertUtils.register( new NestedWidgetsConverter(), NestedWidgets.class );
	}

	public void destroy() {

		// Do nothing
	}
}