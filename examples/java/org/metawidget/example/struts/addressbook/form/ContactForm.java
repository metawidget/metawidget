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

package org.metawidget.example.struts.addressbook.form;

import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.metawidget.example.shared.addressbook.model.Address;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.struts.UiStrutsLookup;

/**
 * @author Richard Kennard
 */

public abstract class ContactForm
	extends ActionForm
{
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

	private boolean				mReadOnly;

	//
	// Public methods
	//

	@UiHidden
	public long getId()
	{
		return mId;
	}

	public void setId( long id )
	{
		mId = id;
	}

	/**
	 * Get the Person's title.
	 */

	@UiAttribute( name = "required", value = "true" )
	@UiStrutsLookup( name = "contacts", property = "allTitles" )
	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle( String title )
	{
		mTitle = title;
	}

	@UiComesAfter( "title" )
	@UiAttribute( name = "required", value = "true" )
	public String getFirstname()
	{
		return mFirstname;
	}

	public void setFirstname( String firstname )
	{
		mFirstname = firstname;
	}

	@UiComesAfter( "firstname" )
	@UiAttribute( name = "required", value = "true" )
	public String getSurname()
	{
		return mSurname;
	}

	public void setSurname( String surname )
	{
		mSurname = surname;
	}

	/**
	 * Get the Person's title.
	 */

	@UiComesAfter( { "dateOfBirthAsString", "surname", "company" } )
	public Gender getGender()
	{
		return mGender;
	}

	public void setGender( Gender gender )
	{
		mGender = gender;
	}

	@UiComesAfter( "gender" )
	@UiSection( "Contact Details" )
	public Address getAddress()
	{
		return mAddress;
	}

	public void setAddress( Address address )
	{
		mAddress = address;
	}

	@UiComesAfter( "address" )
	public Set<Communication> getCommunications()
	{
		return mCommunications;
	}

	public void setCommunications( Set<Communication> communications )
	{
		mCommunications = communications;
	}

	@UiComesAfter
	@UiSection( "Other" )
	@UiLarge
	public String getNotes()
	{
		return mNotes;
	}

	public void setNotes( String notes )
	{
		mNotes = notes;
	}

	@UiHidden
	public boolean isReadOnly()
	{
		return mReadOnly;
	}

	public void setReadOnly( boolean readOnly )
	{
		mReadOnly = readOnly;
	}
}
