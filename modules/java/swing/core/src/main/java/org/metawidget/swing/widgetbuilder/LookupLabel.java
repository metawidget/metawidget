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

package org.metawidget.swing.widgetbuilder;

import java.util.Map;

import javax.swing.JLabel;

/**
 * Label whose values use a lookup.
 * <p>
 * This class is <code>public</code> for binding implementations such as BeansBinding.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LookupLabel
	extends JLabel {

	//
	// Private members
	//

	private Map<String, String>	mLookup;

	//
	// Constructor
	//

	public LookupLabel( Map<String, String> lookup ) {

		if ( lookup == null ) {
			throw new NullPointerException( "lookup" );
		}

		mLookup = lookup;
	}

	//
	// Public methods
	//

	@Override
	public void setText( String text ) {

		String lookup = text;

		if ( lookup != null && mLookup != null ) {
			lookup = mLookup.get( lookup );
		}

		super.setText( lookup );
	}
}
