// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.test.model.annotatedaddressbook;

import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Simulation of Address Book.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
