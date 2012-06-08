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

package org.metawidget.test.model.annotatedaddressbook;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Set;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Simluation of Address Book.
 *
 * @author Richard Kennard
 */

public abstract class Contact
	implements Comparable<Contact> {

	//
	// Private members
	//

	private long				mId;

	private String				mTitle;

	private String				mFirstname;

	private String				mSurname;

	private Gender				mGender;

	private Address				mAddress;

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
		mAddress = new Address();
		mAddress.setOwner( this );
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
	 * Note: this getter is multiple annotated with <code>UiFacesLookup</code>,
	 * <code>UiSpringLookup</code> and <code>UiJspLookup</code>. Normally you only need one or the
	 * other, but we use both because we use this same code in both Faces, Spring and JSP examples.
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
