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

package org.metawidget.example.struts.addressbook.form;

import java.util.Set;

import org.apache.struts.validator.ValidatorForm;
import org.metawidget.example.shared.addressbook.model.Address;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.struts.UiStrutsLookup;

/**
 * @author Richard Kennard
 */

public abstract class ContactForm
	extends ValidatorForm {

	//
	// Private members
	//

	private long				mId;

	private String				mTitle;

	private String				mFirstname;

	private String				mSurname;

	private Gender				mGender;

	private Address				mAddress			= new Address();

	private Set<Communication>	mCommunications;

	private String				mNotes;

	private boolean				mReadOnly;

	//
	// Public methods
	//

	@UiHidden
	public long getId() {

		return mId;
	}

	public void setId( long id ) {

		mId = id;
	}

	/**
	 * Get the Person's title.
	 * <p>
	 * Note: the 'required' metadata comes from validator-rules.xml.
	 */

	@UiStrutsLookup( name = "contacts", property = "allTitles" )
	public String getTitle() {

		return mTitle;
	}

	public void setTitle( String title ) {

		mTitle = title;
	}

	/**
	 * Get the Person's firstname.
	 * <p>
	 * Note: the 'required' metadata comes from validator-rules.xml.
	 */

	@UiComesAfter( "title" )
	public String getFirstname() {

		return mFirstname;
	}

	public void setFirstname( String firstname ) {

		mFirstname = firstname;
	}

	/**
	 * Get the Person's surname.
	 * <p>
	 * Note: the 'required' metadata comes from validator-rules.xml.
	 */

	@UiComesAfter( "firstname" )
	public String getSurname() {

		return mSurname;
	}

	public void setSurname( String surname ) {

		mSurname = surname;
	}

	/**
	 * Get the Person's title.
	 */

	@UiComesAfter( { "dateOfBirthAsString", "surname", "company" } )
	public Gender getGender() {

		return mGender;
	}

	public void setGender( Gender gender ) {

		mGender = gender;
	}

	@UiComesAfter( "gender" )
	@UiSection( "contactDetails" )
	public Address getAddress() {

		return mAddress;
	}

	public void setAddress( Address address ) {

		mAddress = address;
	}

	@UiComesAfter( "address" )
	public Set<Communication> getCommunications() {

		return mCommunications;
	}

	public void setCommunications( Set<Communication> communications ) {

		mCommunications = communications;
	}

	@UiComesAfter
	@UiSection( "other" )
	@UiLarge
	public String getNotes() {

		return mNotes;
	}

	public void setNotes( String notes ) {

		mNotes = notes;
	}

	@UiHidden
	public boolean isReadOnly() {

		return mReadOnly;
	}

	public void setReadOnly( boolean readOnly ) {

		mReadOnly = readOnly;
	}
}
