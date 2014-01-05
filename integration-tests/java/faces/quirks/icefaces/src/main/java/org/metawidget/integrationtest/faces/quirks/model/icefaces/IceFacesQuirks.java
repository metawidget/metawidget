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

package org.metawidget.integrationtest.faces.quirks.model.icefaces;

import java.util.Date;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiSection;

/**
 * Models an entity that tests some ICEfaces-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
