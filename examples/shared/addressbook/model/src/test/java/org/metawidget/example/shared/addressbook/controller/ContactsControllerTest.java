// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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

	public void testIdAssignment()
		throws Exception {

		ContactsController contactsController = new ContactsController();
		Contact contact = new PersonalContact( "Mr", "Jason", "Montgomery" );

		assertEquals( 0, contact.getId() );
		contactsController.save( contact );
		assertEquals( 7, contact.getId() );
	}
}
