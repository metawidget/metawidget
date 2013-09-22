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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Models a Contact in the Address Book
 * <p>
 * So that it can easily be reused across different examples, this class only uses annotations from
 * <code>org.metawidget.inspector.annotation.*</code>. In the real world, apps should prefer to use
 * something like <code>javax.persistence.Column(nullable = false)</code> or
 * <code>org.hibernate.validator.NotNull</code> rather than <code>UiRequired</code>: Metawidget will
 * inspect your <em>existing</em> annotations as much as possible.
 * <p>
 * Implements Serializable because some Web containers require session-level values to be
 * Serializable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class Contact
	implements Comparable<Contact>, Serializable {

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

	//
	// Constructor
	//

	public Contact() {

		this( null, null, null );
	}

	public Contact( String title, String firstname, String surname ) {

		mTitle = title;
		mFirstname = firstname;
		mSurname = surname;
		mAddress.setOwner( this );
	}

	protected Contact( Contact contact ) {

		this( contact.mTitle, contact.mFirstname, contact.mSurname );

		mId = contact.mId;
		mGender = contact.mGender;
		mAddress.setStreet( contact.mAddress.getStreet() );
		mAddress.setCity( contact.mAddress.getCity() );
		mAddress.setState( contact.mAddress.getState() );
		mAddress.setPostcode( contact.mAddress.getPostcode() );

		if ( contact.mCommunications != null ) {

			// Don't use CollectionUtils.newHashSet(), GWT can't compile it

			mCommunications = new HashSet<Communication>();

			for ( Communication communication : contact.mCommunications ) {

				mCommunications.add( new Communication( communication ) );
			}
		}

		mNotes = contact.mNotes;
	}

	//
	// Public methods
	//

	/**
	 * Gets the Contact's id.
	 * <p>
	 * Note: this method is annotated <code>UiHidden</code>. Metawidget is designed to use
	 * <em>existing</em> annotations as much as possible. Real apps would generally use something
	 * like <code>javax.persistence.Id</code> here in preference to <code>UiHidden</code>.
	 */

	@UiHidden
	public long getId() {

		return mId;
	}

	public void setId( long id ) {

		mId = id;
	}

	/**
	 * Get the Contact's title.
	 * <p>
	 * Note: this method is annotated <code>UiRequired</code>. Metawidget is designed to use
	 * <em>existing</em> annotations as much as possible. Real apps would generally use something
	 * like <code>javax.persistence.Column(nullable = false)</code> or
	 * <code>org.hibernate.validator.NotNull</code> here in preference to <code>UiRequired</code>.
	 */

	@UiRequired
	public String getTitle() {

		return mTitle;
	}

	public void setTitle( String title ) {

		mTitle = title;
	}

	/**
	 * Get's the Contact's firstname.
	 * <p>
	 * Note: this method is annotated <code>UiRequired</code>. Metawidget is designed to use
	 * <em>existing</em> annotations as much as possible. Real apps would generally use something
	 * like <code>javax.persistence.Column(nullable = false)</code> or
	 * <code>org.hibernate.validator.NotNull</code> here in preference to <code>UiRequired</code>.
	 */

	@UiComesAfter( "title" )
	@UiRequired
	public String getFirstname() {

		return mFirstname;
	}

	public void setFirstname( String firstname ) {

		mFirstname = firstname;
	}

	/**
	 * Get's the Contact's surname.
	 * <p>
	 * Note: this method is annotated <code>UiRequired</code>. Metawidget is designed to use
	 * <em>existing</em> annotations as much as possible. Real apps would generally use something
	 * like <code>javax.persistence.Column(nullable = false)</code> or
	 * <code>org.hibernate.validator.NotNull</code> here in preference to <code>UiRequired</code>.
	 * <p>
	 * Note: this method is annotated <code>UiAttribute</code>. Metawidget is designed to use
	 * <em>existing</em> annotations as much as possible. Real apps would generally use something
	 * like <code>org.hibernate.validator.Length</code> here in preference to
	 * <code>UiAttribute</code>.
	 */

	@UiComesAfter( "firstname" )
	@UiRequired
	@UiAttribute( name = MAXIMUM_LENGTH, value = "50" )
	public String getSurname() {

		return mSurname;
	}

	public void setSurname( String surname ) {

		mSurname = surname;
	}

	@UiHidden
	public String getFullname() {

		StringBuilder builder = new StringBuilder();

		if ( mTitle != null ) {
			builder.append( mTitle );
		}

		if ( mFirstname != null ) {
			if ( builder.length() > 0 ) {
				builder.append( ' ' );
			}

			builder.append( mFirstname );
		}

		if ( mSurname != null ) {
			if ( builder.length() > 0 ) {
				builder.append( ' ' );
			}

			builder.append( mSurname );
		}

		return builder.toString();
	}

	@UiComesAfter( { "dateOfBirth", "surname", "company" } )
	public Gender getGender() {

		return mGender;
	}

	public void setGender( Gender gender ) {

		mGender = gender;
	}

	@UiComesAfter( "gender" )
	@UiSection( "Contact Details" )
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

	public boolean addCommunication( Communication communication ) {

		if ( communication.getType() == null || "".equals( communication.getType() ) ) {
			throw new RuntimeException( "Communication type is required" );
		}

		if ( communication.getValue() == null || "".equals( communication.getValue() ) ) {
			throw new RuntimeException( "Communication value is required" );
		}

		if ( mCommunications == null ) {
			// (don't use CollectionUtils.newHashSet() here, to avoid
			// dragging CollectionUtils in under GWT)

			mCommunications = new HashSet<Communication>();
		}

		return mCommunications.add( communication );
	}

	public boolean removeCommunication( Communication communication ) {

		if ( mCommunications == null ) {
			return false;
		}

		return mCommunications.remove( communication );
	}

	public boolean removeCommunication( long id ) {

		if ( mCommunications == null ) {
			return false;
		}

		for ( Iterator<Communication> i = mCommunications.iterator(); i.hasNext(); ) {
			if ( i.next().getId() != id ) {
				continue;
			}

			i.remove();
			return true;
		}

		return false;
	}

	/**
	 * Gets the notes.
	 */

	@UiComesAfter
	@UiSection( "Other" )
	@UiLarge
	public String getNotes() {

		return mNotes;
	}

	public void setNotes( String notes ) {

		mNotes = notes;
	}

	public int compareTo( Contact that ) {

		return ObjectUtils.nullSafeCompareTo( getFirstname(), that.getFirstname() );
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		Contact contactThat = (Contact) that;

		if ( mId == 0 ) {
			if ( contactThat.mId != 0 ) {
				return false;
			}

			return super.equals( contactThat );
		}

		return mId == contactThat.mId;
	}

	@Override
	public int hashCode() {

		if ( mId == 0 ) {
			return super.hashCode();
		}

		int hashCode = 1;
		hashCode = 31 * hashCode + (int) ( mId ^ ( mId >>> 32 ) );

		return hashCode;
	}

	@Override
	public String toString() {

		return getFullname();
	}
}
