// Metawidget
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

package org.metawidget.example.shared.addressbook.model;

import java.io.Serializable;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLookup;

/**
 * Models the Address of a Contact.
 * <p>
 * Implements Serializable because Web containers require session-level values to be Serializable.
 *
 * @author Richard Kennard
 */

public class Address
	implements Serializable {

	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1l;

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
