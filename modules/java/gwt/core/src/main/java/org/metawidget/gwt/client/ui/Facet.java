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

package org.metawidget.gwt.client.ui;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Facet for GWT environments.
 * <p>
 * Facets differ from Stubs in that Stubs override widget creation, whereas Facets are 'decorations'
 * (such as button bars) to be recognized and arranged at the discretion of the Layout.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class Facet
	extends SimplePanel
	implements HasName {

	//
	// Private members
	//

	private String	mName;

	//
	// Public methods
	//

	public String getName() {

		return mName;
	}

	public void setName( String name ) {

		mName = name;
	}
}
