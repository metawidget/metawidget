// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.swt;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Facet for Swt environments.
 * <p>
 * Facets differ from Stubs in that Stubs override widget creation, whereas Facets are 'decorations'
 * (such as button bars) to be recognized and arranged at the discretion of the Layout.
 * <p>
 * We define separate Facet widgets, as opposed to simply a <code>SwtMetawidget.addFacet</code>
 * method, as this is more amenable to visual UI builders.
 *
 * @author Stefan Ackermann
 */

public class Facet
	extends Composite {

	//
	// Constructor
	//

	public Facet( Composite parent, int style ) {

		super( parent, style );

		setLayout( new FillLayout() );
	}
}
