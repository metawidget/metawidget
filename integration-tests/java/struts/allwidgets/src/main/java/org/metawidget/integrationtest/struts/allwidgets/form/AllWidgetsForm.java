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

package org.metawidget.integrationtest.struts.allwidgets.form;

import org.apache.struts.action.ActionForm;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;
import org.metawidget.integrationtest.shared.allwidgets.proxy.AllWidgetsProxy.AllWidgets_$$_javassist_1;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AllWidgetsForm
	extends ActionForm {

	//
	// Private members
	//

	private AllWidgets			mAllWidgets			= new AllWidgets_$$_javassist_1();

	//
	// Public methods
	//

	public AllWidgets getAllWidgets() {

		return mAllWidgets;
	}
}
