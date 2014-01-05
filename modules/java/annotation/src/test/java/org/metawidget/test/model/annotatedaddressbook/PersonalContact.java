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

import java.util.Date;

import org.metawidget.inspector.annotation.UiComesAfter;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PersonalContact
	extends Contact {

	//
	// Private members
	//

	private Date				mDateOfBirth;

	//
	// Constructor
	//

	public PersonalContact() {

		// Default constructor
	}

	public PersonalContact( String title, String firstname, String surname ) {

		super( title, firstname, surname );
	}

	//
	// Public methods
	//

	@UiComesAfter( "surname" )
	public Date getDateOfBirth() {

		return mDateOfBirth;
	}

	public void setDateOfBirth( Date ofBirth ) {

		mDateOfBirth = ofBirth;
	}
}
