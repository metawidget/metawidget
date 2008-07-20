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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Lob;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.faces.UiFacesLookup;
import org.metawidget.inspector.spring.UiSpringLookup;

/**
 * Models a Contact in the Address Book
 * <p>
 * Implements Serializable because Web containers require session-level
 * values to be Serializable.
 *
 * @author Richard Kennard
 */

public abstract class Contact
	implements Comparable<Contact>, Serializable
{
	//
	//
	// Private members
	//
	//

	private long				mId;

	private String				mTitle;

	private String				mFirstnames;

	private String				mSurname;

	private Gender				mGender;

	private Address				mAddress;

	private Set<Communication>	mCommunications;

	private String				mNotes;

	//
	//
	// Constructor
	//
	//

	public Contact()
	{
		this( null, null, null );
	}

	public Contact( String title, String firstnames, String surname )
	{
		mTitle = title;
		mFirstnames = firstnames;
		mSurname = surname;
		mAddress = new Address();
		mAddress.setOwner( this );
	}

	//
	//
	// Public methods
	//
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

	@Column( nullable = false )
	@UiFacesLookup( "#{contacts.allTitlesAsSelectItems}" )
	@UiSpringLookup( "${contacts.allTitles}" )
	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle( String title )
	{
		mTitle = title;
	}

	@UiComesAfter( "title" )
	@Column( nullable = false )
	public String getFirstnames()
	{
		return mFirstnames;
	}

	public void setFirstnames( String firstname )
	{
		mFirstnames = firstname;
	}

	@UiComesAfter( "firstnames" )
	@Column( nullable = false, length = 50 )
	public String getSurname()
	{
		return mSurname;
	}

	public void setSurname( String surname )
	{
		mSurname = surname;
	}

	@UiHidden
	public String getFullname()
	{
		StringBuilder builder = new StringBuilder();

		if ( mTitle != null )
			builder.append( mTitle );

		if ( mFirstnames != null )
		{
			if ( builder.length() > 0 )
				builder.append( ' ' );

			builder.append( mFirstnames );
		}

		if ( mSurname != null )
		{
			if ( builder.length() > 0 )
				builder.append( ' ' );

			builder.append( mSurname );
		}

		return builder.toString();
	}

	@UiComesAfter( { "dateOfBirth", "surname", "company" } )
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

	public boolean addCommunication( Communication communication )
	{
		if ( communication.getType() == null || "".equals( communication.getType() ))
			throw new RuntimeException( "Communication type is required" );

		if ( communication.getValue() == null || "".equals( communication.getValue() ))
			throw new RuntimeException( "Communication value is required" );

		if ( mCommunications == null )
		{
			// (don't use CollectionUtils.newHashSet() here, to avoid
			//  dragging CollectionUtils in under GWT)

			mCommunications = new HashSet<Communication>();
		}

		return mCommunications.add( communication );
	}

	public boolean removeCommunication( Communication communication )
	{
		if ( mCommunications == null )
			return false;

		return mCommunications.remove( communication );
	}

	public boolean removeCommunication( long id )
	{
		if ( mCommunications == null )
			return false;

		for( Iterator<Communication> i = mCommunications.iterator(); i.hasNext(); )
		{
			if ( i.next().getId() != id )
				continue;

			i.remove();
			return true;
		}

		return false;
	}

	@UiComesAfter
	@UiSection( "Other" )
	@Lob
	public String getNotes()
	{
		return mNotes;
	}

	public void setNotes( String notes )
	{
		mNotes = notes;
	}

	public int compareTo( Contact that )
	{
		if ( that == null )
			return -1;

		String thisFirstnames = getFirstnames();
		String thatFirstnames = that.getFirstnames();

		if ( thisFirstnames == null )
		{
			if ( thatFirstnames != null )
				return -1;

			return 0;
		}

		return thisFirstnames.compareTo( thatFirstnames );
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof Contact ) )
			return false;

		Contact personThat = (Contact) that;

		if ( mId == 0 )
		{
			if ( personThat.mId != 0 )
				return false;

			return super.equals( personThat );
		}

		return mId == personThat.mId;
	}

	@Override
	public int hashCode()
	{
		if ( mId == 0 )
			return super.hashCode();

		return Long.valueOf( mId ).hashCode();
	}

	@Override
	public String toString()
	{
		return getFullname();
	}
}
