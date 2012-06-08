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

import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Simulation of Address Book.
 *
 * @author Richard Kennard
 */

public class Communication
	implements Comparable<Communication> {

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
			toReturn.append( ':' );
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
