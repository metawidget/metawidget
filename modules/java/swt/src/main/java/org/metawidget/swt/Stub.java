// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.swt;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.metawidget.util.CollectionUtils;

/**
 * Stub for Swing environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress widget
 * creation entirely or to create child widgets with a different name. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 * <p>
 * We define separate Stub widgets, as opposed to simply a <code>SwtMetawidget.addStub</code>
 * method, as this is more amenable to visual UI builders.
 *
 * @author Stefan Ackermann
 */

public class Stub
	extends Composite {

	//
	// Constructor
	//
	
	public Stub( Composite parent, int style ) {

		super( parent, style );
	}

	//
	// Private members
	//

	private Map<String, String>	mAttributes;

	//
	// Public methods
	//

	public void setAttribute( String name, String value ) {

		if ( mAttributes == null ) {
			mAttributes = CollectionUtils.newHashMap();
		}

		mAttributes.put( name, value );
	}

	public Map<String, String> getAttributes() {

		return mAttributes;
	}
}
