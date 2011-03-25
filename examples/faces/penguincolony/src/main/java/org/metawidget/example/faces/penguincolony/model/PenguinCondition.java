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

package org.metawidget.example.faces.penguincolony.model;

import org.metawidget.inspector.annotation.UiComesAfter;

/**
 * @author Richard Kennard
 */

public class PenguinCondition {

	//
	// Private members
	//

	private Condition	mBeak;

	private Condition	mWings;

	private Condition	mFeet;

	//
	// Public methods
	//

	public Condition getBeak() {

		return mBeak;
	}

	public void setBeak( Condition beak ) {

		mBeak = beak;
	}

	@UiComesAfter( "beak" )
	public Condition getWings() {

		return mWings;
	}

	public void setWings( Condition wings ) {

		mWings = wings;
	}

	@UiComesAfter( "wings" )
	public Condition getFeet() {

		return mFeet;
	}

	public void setFeet( Condition feet ) {

		mFeet = feet;
	}
}
