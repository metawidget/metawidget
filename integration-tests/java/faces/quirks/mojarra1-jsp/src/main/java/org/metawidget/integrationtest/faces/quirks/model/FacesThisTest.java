// Metawidget (licensed under LGPL)
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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

		this.mChild = child;
	}

	@UiHidden
	public String getIdentity() {

		return this.mIdentity;
	}

	public void setIdentity( String identity ) {

		this.mIdentity = identity;
	}
}
