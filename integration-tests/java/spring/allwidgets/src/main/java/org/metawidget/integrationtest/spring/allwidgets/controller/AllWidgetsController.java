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

package org.metawidget.integrationtest.spring.allwidgets.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.integrationtest.spring.allwidgets.editor.DateEditor;
import org.metawidget.integrationtest.spring.allwidgets.editor.NestedWidgetsEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AllWidgetsController
	extends SimpleFormController {

	//
	// Protected methods
	//

	@Override
	protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder )
		throws Exception {

		super.initBinder( request, binder );

		binder.registerCustomEditor( Date.class, new DateEditor() );
		binder.registerCustomEditor( NestedWidgets.class, new NestedWidgetsEditor() );
	}
}
