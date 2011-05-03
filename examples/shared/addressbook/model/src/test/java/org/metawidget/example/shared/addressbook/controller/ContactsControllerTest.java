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

package org.metawidget.example.shared.addressbook.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;

/**
 * @author Richard Kennard
 */

public class ContactsControllerTest
	extends TestCase {

	//
	// Public methods
	//

	public void testCopyConstructors() {

		// PersonalContact

		PersonalContact contact1 = new PersonalContact( "Mr", "John", "Smith" );
		contact1.setId( 42 );
		contact1.setGender( Gender.MALE );
		Date dateOfBirth = new GregorianCalendar( 1975, Calendar.APRIL, 9 ).getTime();
		contact1.setDateOfBirth( dateOfBirth );
		contact1.getAddress().setStreet( "1 Foobar Lane" );
		contact1.getAddress().setCity( "Barville" );
		contact1.getAddress().setState( "NSW" );
		contact1.getAddress().setPostcode( "1234" );
		Communication communication1 = new Communication( "Abc", "Def" );
		communication1.setId( 56 );
		contact1.addCommunication( communication1 );
		contact1.setNotes( "Notez" );

		PersonalContact contact2 = new PersonalContact( contact1 );

		assertEquals( 42, contact2.getId() );
		assertEquals( "Mr", contact2.getTitle() );
		assertEquals( "John", contact2.getFirstname() );
		assertEquals( "Smith", contact2.getSurname() );
		assertEquals( Gender.MALE, contact2.getGender() );
		assertEquals( dateOfBirth, contact2.getDateOfBirth() );
		assertEquals( "1 Foobar Lane", contact2.getAddress().getStreet() );
		assertEquals( "Barville", contact2.getAddress().getCity() );
		assertEquals( "NSW", contact2.getAddress().getState() );
		assertEquals( "1234", contact2.getAddress().getPostcode() );
		assertEquals( 1, contact2.getCommunications().size() );
		Communication communication2 = contact2.getCommunications().iterator().next();
		assertTrue( communication1 != communication2 );
		assertEquals( 56, communication2.getId() );
		assertEquals( "Abc", communication2.getType() );
		assertEquals( "Def", communication2.getValue() );
		assertEquals( "Notez", contact2.getNotes() );

		// BusinessContact

		BusinessContact contact3 = new BusinessContact( "Mrs", "Jane", "Jones" );
		contact3.setId( 43 );
		contact3.setCompany( "ACME" );
		contact3.setGender( Gender.FEMALE );
		contact3.getAddress().setStreet( "1 Foobar Street" );
		contact3.getAddress().setCity( "Baztown" );
		contact3.getAddress().setState( "QLD" );
		contact3.getAddress().setPostcode( "4567" );
		contact3.addCommunication( communication1 );
		contact3.setNumberOfStaff( 3 );
		contact3.setNotes( "Notes" );

		BusinessContact contact4 = new BusinessContact( contact3 );

		assertEquals( 43, contact4.getId() );
		assertEquals( "Mrs", contact4.getTitle() );
		assertEquals( "Jane", contact4.getFirstname() );
		assertEquals( "Jones", contact4.getSurname() );
		assertEquals( "ACME", contact4.getCompany() );
		assertEquals( Gender.FEMALE, contact4.getGender() );
		assertEquals( "1 Foobar Street", contact4.getAddress().getStreet() );
		assertEquals( "Baztown", contact4.getAddress().getCity() );
		assertEquals( "QLD", contact4.getAddress().getState() );
		assertEquals( "4567", contact4.getAddress().getPostcode() );
		assertEquals( 1, contact4.getCommunications().size() );
		communication2 = contact4.getCommunications().iterator().next();
		assertTrue( communication1 != communication2 );
		assertEquals( 56, communication2.getId() );
		assertEquals( "Abc", communication2.getType() );
		assertEquals( "Def", communication2.getValue() );
		assertEquals( 3, contact4.getNumberOfStaff() );
		assertEquals( "Notes", contact4.getNotes() );
	}

	public void testDefensiveCopying()
		throws Exception {

		ContactsController contactsController = new ContactsController();

		// getAllByExample

		List<Contact> contacts = contactsController.getAllByExample( null );
		Contact contact = contacts.get( 1 );

		assertEquals( "Homer", contact.getFirstname() );
		assertEquals( "Simpson", contact.getSurname() );

		contact.setFirstname( "Bob" );
		assertEquals( "Homer", contactsController.load( contact.getId() ).getFirstname() );

		// Load

		contact = contactsController.load( 1 );

		assertEquals( "Homer", contact.getFirstname() );
		assertEquals( "Simpson", contact.getSurname() );

		contact.setFirstname( "Bob" );
		assertEquals( "Homer", contactsController.load( contact.getId() ).getFirstname() );

		// Save

		contactsController.save( contact );
		assertEquals( "Bob", contactsController.load( contact.getId() ).getFirstname() );
		contact.setFirstname( "Jane" );
		assertEquals( "Bob", contactsController.load( contact.getId() ).getFirstname() );
	}
}
