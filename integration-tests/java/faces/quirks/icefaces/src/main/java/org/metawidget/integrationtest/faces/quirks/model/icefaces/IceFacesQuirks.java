// Metawidget (licensed under LGPL)
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

package org.metawidget.integrationtest.faces.quirks.model.icefaces;

import java.util.Date;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiSection;

/**
 * Models an entity that tests some ICEfaces-specific quirks.
 *
 * @author Richard Kennard
 */

public class IceFacesQuirks {

	//
	// Private members
	//

	private Date					mDate;

	private IceFacesEmbeddedQuirks	mEmbeddedQuirksInSection = new IceFacesEmbeddedQuirks();

	private IceFacesEmbeddedQuirks	mEmbeddedQuirks = new IceFacesEmbeddedQuirks();

	//
	// Public methods
	//

	public Date getDate() {

		return mDate;
	}

	public void setDate( Date date ) {

		mDate = date;
	}

	@UiComesAfter( "date" )
	@UiSection( "Embedded" )
	public IceFacesEmbeddedQuirks getEmbeddedQuirksInSection() {

		return mEmbeddedQuirksInSection;
	}

	public void setEmbeddedQuirksInSection( IceFacesEmbeddedQuirks embeddedQuirksInSection ) {

		mEmbeddedQuirksInSection = embeddedQuirksInSection;
	}

	@UiComesAfter( "embeddedQuirksInSection" )
	@UiSection( "" )
	public IceFacesEmbeddedQuirks getEmbeddedQuirks() {

		return mEmbeddedQuirks;
	}

	public void setEmbeddedQuirks( IceFacesEmbeddedQuirks embeddedQuirks ) {

		mEmbeddedQuirks = embeddedQuirks;
	}

	//
	// Inner class
	//

	public static class IceFacesEmbeddedQuirks {

		//
		// Private members
		//

		private Date						mDate;

		//
		// Public methods
		//

		public Date getDate() {

			return mDate;
		}

		public void setDate( Date date ) {

			mDate = date;
		}

		@UiLabel( "#{_this.recreatable}" )
		public String getRecreatable() {

			if ( mDate == null ) {
				return "Date is empty";
			}

			return "Date is populated";
		}
	}
}
