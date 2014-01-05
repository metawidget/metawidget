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

package org.metawidget.test.model.annotatedaddressbook;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLookup;

/**
 * Simulation of Address Book.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class Address {

	//
	// Private members
	//

	private Contact				mOwner;

	private String				mStreet;

	private String				mCity;

	private String				mState;

	private String				mPostcode;

	//
	// Public methods
	//

	@UiHidden
	public Contact getOwner() {

		return mOwner;
	}

	public void setOwner( Contact owner ) {

		mOwner = owner;
	}

	@UiComesAfter( "owner" )
	public String getStreet() {

		return mStreet;
	}

	public void setStreet( String street ) {

		mStreet = street;
	}

	@UiComesAfter( "street" )
	public String getCity() {

		return mCity;
	}

	public void setCity( String city ) {

		mCity = city;
	}

	@UiComesAfter( "city" )
	@UiLookup( { "Anytown", "Cyberton", "Lostville", "Whereverton" } )
	public String getState() {

		return mState;
	}

	public void setState( String state ) {

		mState = state;
	}

	@UiComesAfter( "state" )
	public String getPostcode() {

		return mPostcode;
	}

	public void setPostcode( String postcode ) {

		mPostcode = postcode;
	}
}
