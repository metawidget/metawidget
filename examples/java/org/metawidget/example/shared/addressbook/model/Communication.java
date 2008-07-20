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
import org.metawidget.inspector.faces.UiFacesLookup;
import org.metawidget.inspector.jsp.UiJspLookup;

/**
 * Models a Communication of a Contact.
 * <p>
 * Implements Serializable because Web containers require session-level values to be Serializable.
 *
 * @author Richard Kennard
 */

public class Communication
	implements Comparable<Communication>, Serializable
{
	//
	//
	// Private statics
	//
	//

	private final static long	serialVersionUID	= 5357117362912411289L;

	//
	//
	// Private members
	//
	//

	private long				mId;

	private String				mType;

	private String				mValue;

	//
	//
	// Constructor
	//
	//

	public Communication()
	{
		// Default constructor
	}

	public Communication( String type, String value )
	{
		mType = type;
		mValue = value;
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

	@UiFacesLookup( "#{communications.allAsSelectItems}" )
	@UiJspLookup( "${communications.all}" )
	public String getType()
	{
		return mType;
	}

	public void setType( String type )
	{
		mType = type;
	}

	@UiComesAfter( "Type" )
	public String getValue()
	{
		return mValue;
	}

	public void setValue( String value )
	{
		mValue = value;
	}

	public int compareTo( Communication that )
	{
		if ( that == null )
			return -1;

		// Compare types

		String thisType = getType();
		String thatType = that.getType();

		if ( thisType == null )
		{
			if ( thatType != null )
				return -1;

			return 0;
		}
		else if ( thatType == null )
			return 1;

		int compareTo = thisType.compareTo( thatType );

		if ( compareTo != 0 )
			return compareTo;

		// If types are the same, compare ids

		return (int) ( getId() - that.getId() );
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof Communication ) )
			return false;

		Communication communicationThat = (Communication) that;

		if ( mId == 0 )
		{
			if ( communicationThat.mId != 0 )
				return false;

			return super.equals( communicationThat );
		}

		return mId == communicationThat.mId;
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
		String toReturn = mValue;

		if ( toReturn == null )
			return "";

		if ( mType != null )
			toReturn = mType + ": " + toReturn;

		return toReturn;
	}
}
