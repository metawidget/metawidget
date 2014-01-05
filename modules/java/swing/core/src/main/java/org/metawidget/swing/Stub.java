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

package org.metawidget.swing;

import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.metawidget.util.CollectionUtils;

/**
 * Stub for Swing environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress widget
 * creation entirely or to create child widgets with a different name. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 * <p>
 * We define separate Stub widgets, as opposed to simply a <code>SwingMetawidget.addStub</code>
 * method, as this is more amenable to visual UI builders.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

// Note: Stub extends JComponent, not JPanel, because in general it should not be opaque
//
public class Stub
	extends JComponent {

	//
	// Private members
	//

	private Map<String, String>	mAttributes;

	//
	// Constructors
	//

	public Stub() {

		// Default to BoxLayout, so that the controls fill the Stub width-ways. This
		// is important for things like JTabbedPaneLayout

		setLayout( new BoxLayout( this, BoxLayout.PAGE_AXIS ) );
	}

	/**
	 * Convenience constructor.
	 * <p>
	 * Useful for creating stubs that will otherwise be empty, such as
	 * <code>metawidget.add( new Stub( "foo" ))</code>
	 */

	public Stub( String name ) {

		setName( name );
	}

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
