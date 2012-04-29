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

package org.metawidget.example.vaadin.addressbook;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Loghman Barari
 */

public class VaadinAddressBookTest
	extends TestCase {

	//
	// Public methods
	//

	public void testAddressBook()
		throws Exception {

		// Set Locale because we will be checking date formatting

		Locale.setDefault( Locale.UK );
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );
		DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, Locale.UK );

		// Start app

		AddressBook addressBook = new AddressBook();
		CustomLayout customLayout = (CustomLayout) addressBook.getContent();
		assertEquals( customLayout.getTemplateName(), "addressbook" );
		VerticalLayout contactsMainBody = (VerticalLayout) customLayout.getComponent( "pagebody" );
		assertEquals( contactsMainBody.getComponentCount(), 2 );
		Table contactsTable = (Table) contactsMainBody.getComponent( 1 );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 6 );
	}
}
