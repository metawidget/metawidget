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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasName;

/**
 * Stub for GWT environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress widget
 * creation entirely or to create child widgets with a different name. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class Stub
	extends FlowPanel
	implements HasName {

	//
	// Private members
	//

	private String				mName;

	private Map<String, String>	mAttributes;

	//
	// Constructors
	//

	public Stub() {

		// Default constructor
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

	public String getName() {

		return mName;
	}

	public void setName( String name ) {

		mName = name;
	}

	public void setAttribute( String name, String value ) {

		if ( mAttributes == null ) {
			mAttributes = new HashMap<String, String>();
		}

		mAttributes.put( name, value );
	}

	public Map<String, String> getAttributes() {

		return mAttributes;
	}
}
