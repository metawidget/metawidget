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

import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Models a Communication of a Contact.
 * <p>
 * Implements Serializable because Web containers require session-level values to be Serializable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class Communication
	implements Comparable<Communication>, Serializable {

	//
	// Private members
	//

	private long				mId;

	private String				mType;

	private String				mValue;

	//
	// Constructor
	//

	public Communication() {

		// Default constructor
	}

	public Communication( String type, String value ) {

		mType = type;
		mValue = value;
	}

	public Communication( Communication communication ) {

		this( communication.mType, communication.mValue );

		mId = communication.mId;
	}

	//
	// Public methods
	//

	/**
	 * Gets the Communication's id.
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

	public String getType() {

		return mType;
	}

	public void setType( String type ) {

		mType = type;
	}

	public String getValue() {

		return mValue;
	}

	public void setValue( String value ) {

		mValue = value;
	}

	public int compareTo( Communication that ) {

		// Compare types

		int compareTypes = ObjectUtils.nullSafeCompareTo( getType(), that.getType() );

		if ( compareTypes != 0 ) {
			return compareTypes;
		}

		// If types are the same, compare ids

		return (int) ( getId() - that.getId() );
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		Communication communicationThat = (Communication) that;

		if ( mId == 0 ) {
			if ( communicationThat.mId != 0 ) {
				return false;
			}

			return super.equals( communicationThat );
		}

		return mId == communicationThat.mId;
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

		StringBuilder toReturn = new StringBuilder();

		if ( mType != null && !"".equals( mType ) ) {
			toReturn.append( mType );
			toReturn.append( StringUtils.SEPARATOR_COLON_CHAR );
		}

		if ( mValue != null ) {
			if ( toReturn.length() != 0 ) {
				toReturn.append( ' ' );
			}

			toReturn.append( mValue );
		}

		return toReturn.toString();
	}
}
