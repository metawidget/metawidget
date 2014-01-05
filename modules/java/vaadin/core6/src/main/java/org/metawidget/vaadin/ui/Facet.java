// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui;

import com.vaadin.ui.Panel;

/**
 * Facet for Vaadin environments.
 *
 * @author Loghman Barari
 */

public class Facet
	extends Panel {

	//
	// Constructor
	//

	public Facet() {

		addStyleName( "light" );
		((com.vaadin.ui.Layout) getContent()).setMargin( false );
	}
}
