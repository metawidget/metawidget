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

package org.metawidget.example.swing.appframework;

/**
 * @author Richard Kennard
 */

public class Owner {

	//
	// Private members
	//

	private String	mFirstname;

	private String	mSurname;

	//
	// Public methods
	//

	public String getFirstname() {

		return mFirstname;
	}

	public void setFirstname( String firstname ) {

		mFirstname = firstname;
	}

	public String getSurname() {

		return mSurname;
	}

	public void setSurname( String surname ) {

		mSurname = surname;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		if ( mFirstname != null ) {
			builder.append( mFirstname );
		}

		if ( mSurname != null ) {
			if ( builder.length() > 0 ) {
				builder.append( " " );
			}

			builder.append( mSurname );
		}

		if ( builder.length() == 0 ) {
			builder.append( "(no name specified)" );
		}

		return builder.toString();
	}
}
