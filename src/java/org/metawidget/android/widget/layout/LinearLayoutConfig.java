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

package org.metawidget.android.widget.layout;

/**
 * Configures a LinearLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class LinearLayoutConfig {

	//
	// Private members
	//

	private int	mLabelStyle;

	//
	// Public methods
	//

	public int getLabelStyle() {

		return mLabelStyle;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public LinearLayoutConfig setLabelStyle( int labelStyle ) {

		mLabelStyle = labelStyle;
		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( that == null ) {
			return false;
		}

		if ( getClass() != that.getClass() ) {
			return false;
		}

		if ( mLabelStyle != ( (LinearLayoutConfig) that ).mLabelStyle ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		return mLabelStyle;
	}
}
