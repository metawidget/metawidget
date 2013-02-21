// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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
