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

package org.metawidget.integrationtest.faces.quirks.model;

import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;

/**
 * Models an entity that tests some Faces-specific 'this'.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FacesThisTest {

	//
	// Private members
	//

	private String			mIdentity;

	private FacesThisTest	mChild;

	//
	// Constructor
	//

	public FacesThisTest() {

		this( false );
	}

	/**
	 * @param noMore
	 */

	public FacesThisTest( boolean noMore ) {

		if ( noMore ) {
			return;
		}
		
		mIdentity = "FacesThisTest #1";
		mChild = new FacesThisTest( true );
		mChild.mIdentity = "FacesThisTest #2";
	}

	//
	// Public methods
	//

	/**
	 * The injection of 'this' into the EL context needs to work for both normal scenarios and
	 * 'direct to child' scenarios (ie. the Metawidget is pointed at #{_thisTest.child.me}). In the
	 * latter, '_this' has to refer to the parent, not the child.
	 */

	@UiLabel( "#{_this.identity}" )
	public String getMe() {

		return null;
	}

	@UiHidden
	public FacesThisTest getChild() {

		return mChild;
	}

	public void setChild( FacesThisTest child ) {

		mChild = child;
	}

	@UiHidden
	public String getIdentity() {

		return mIdentity;
	}

	public void setIdentity( String identity ) {

		mIdentity = identity;
	}
}
