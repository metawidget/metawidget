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
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.util.CollectionUtils;

/**
 * This controller crudely simulates a database for the purposes of the example application.
 *
 * @author Richard Kennard
 */

public class ContactsController {

	//
	// Protected statics
	//

	protected static final String[]	ALL_TITLES	= { "Mr", "Mrs", "Miss", "Dr", "Cpt" };

	//
	// Private members
	//

	private Map<Long, Contact>		mAll;

	private long					mNextContactId;

	private long					mNextCommunicationId;

	//
	// Constructor
	//

	/**
	 * Initializes the list with some dummy data.
	 */

	public ContactsController() {

		mAll = Collections.synchronizedMap( new HashMap<Long, Contact>() );

		Contact contact = new PersonalContact( "Mr", "Homer", "Simpson" );
		contact.setGender( Gender.MALE );
		contact.getAddress().setStreet( "742 Evergreen Terrace" );
		contact.getAddress().setCity( "Springfield" );
		contact.getAddress().setState( "Anytown" );
		contact.getAddress().setPostcode( "90701" );
		contact.addCommunication( new Communication( "Telephone", "(939) 555-0113" ) );
		( (PersonalContact) contact ).setDateOfBirth( new GregorianCalendar( 1956, Calendar.MAY, 12 ).getTime() );
		save( contact );

		contact = new PersonalContact( "Mrs", "Marjorie", "Simpson" );
		contact.setGender( Gender.FEMALE );
		contact.getAddress().setStreet( "742 Evergreen Terrace" );
		contact.getAddress().setCity( "Springfield" );
		contact.setNotes( "Known as 'Marge'" );
		save( contact );

		contact = new PersonalContact( "Mr", "Nedward", "Flanders" );
		contact.setGender( Gender.MALE );
		save( contact );

		contact = new PersonalContact( "Mrs", "Maude", "Flanders" );
		contact.setGender( Gender.FEMALE );
		save( contact );

		contact = new BusinessContact( "Mr", "Charles Montgomery", "Burns" );
		contact.setGender( Gender.MALE );
		save( contact );

		contact = new BusinessContact( "Mr", "Waylon", "Smithers" );
		contact.setGender( Gender.MALE );
		save( contact );
	}

	//
	// Public methods
	//

	/**
	 * A real-world implementation would likely use a database lookup.
	 */

	public String[] getAllTitles() {

		return ALL_TITLES;
	}

	/**
	 * Simulates a Query By Example.
	 */

	public List<Contact> getAllByExample( ContactSearch search ) {

		List<Contact> allByExample = CollectionUtils.newArrayList();

		for ( Contact contact : mAll.values() ) {
			if ( search != null ) {
				if ( !caseInsensitiveContains( contact.getFirstname(), search.getFirstname() ) ) {
					continue;
				}

				if ( !caseInsensitiveContains( contact.getSurname(), search.getSurname() ) ) {
					continue;
				}

				if ( search.getType() == ContactType.PERSONAL && !( contact instanceof PersonalContact ) ) {
					continue;

				} else if ( search.getType() == ContactType.BUSINESS && !( contact instanceof BusinessContact ) ) {
					continue;
				}
			}

			// Defensive copy

			allByExample.add( load( contact.getId() ) );
		}

		Collections.sort( allByExample );
		return allByExample;
	}

	public Contact load( long id ) {

		Contact contact = mAll.get( id );

		// Defensive copy

		if ( contact instanceof PersonalContact ) {
			return new PersonalContact( (PersonalContact) contact );
		}

		return new BusinessContact( (BusinessContact) contact );
	}

	public void save( Contact contact ) {

		// Validate required fields

		if ( contact == null ) {
			throw new RuntimeException( "Contact is required" );
		}

		if ( contact.getTitle() == null || "".equals( contact.getTitle() ) ) {
			throw new RuntimeException( "Title is required" );
		}

		if ( contact.getFirstname() == null || "".equals( contact.getFirstname() ) ) {
			throw new RuntimeException( "Firstname is required" );
		}

		if ( contact.getSurname() == null || "".equals( contact.getSurname() ) ) {
			throw new RuntimeException( "Surname is required" );
		}

		// Assign automatic Id
		//
		// Note: this is *not* done on the defensive copy, in order to mimic JPA

		long id = contact.getId();

		if ( id == 0 ) {
			id = incrementNextContactId();
			contact.setId( id );
		} else {
			if ( !mAll.containsKey( id ) ) {
				throw new RuntimeException( "Contact #" + id + " not found" );
			}
		}

		// Defensive copy

		Contact contactToSave;

		if ( contact instanceof PersonalContact ) {
			contactToSave = new PersonalContact( (PersonalContact) contact );
		} else {
			contactToSave = new BusinessContact( (BusinessContact) contact );
		}

		mAll.put( id, contactToSave );

		// Simulate cascading save

		Set<Communication> communications = contactToSave.getCommunications();

		if ( communications != null ) {
			// Remove those that need saving...

			Set<Communication> toSave = CollectionUtils.newHashSet();

			for ( Iterator<Communication> i = communications.iterator(); i.hasNext(); ) {
				Communication communication = i.next();

				if ( communication.getId() != 0 ) {
					continue;
				}

				i.remove();
				toSave.add( communication );
			}

			// ...and re-add them with their assigned id.
			//
			// This is necessary because of the whole 'Communication.equals uses id, but the
			// id can change' problem: if the id changes 'in-place' (eg. without a remove
			// and a re-add), subsequent Set operations (such as remove) can fail. JPA recommends
			// using a 'business key' instead of the id for this very reason, but Communication
			// has no unique 'business key'

			for ( Communication communication : toSave ) {
				communication.setId( incrementNextCommunicationId() );
				communications.add( communication );
			}
		}
	}

	public boolean delete( Contact contact ) {

		if ( contact == null ) {
			return false;
		}

		return ( mAll.remove( contact.getId() ) != null );
	}

	//
	// Private methods
	//

	private synchronized long incrementNextContactId() {

		mNextContactId++;

		return mNextContactId;
	}

	private synchronized long incrementNextCommunicationId() {

		mNextCommunicationId++;

		return mNextCommunicationId;
	}

	private boolean caseInsensitiveContains( String container, String contains ) {

		if ( contains == null || contains.length() == 0 ) {
			return true;
		}

		if ( container == null ) {
			return false;
		}

		return container.toLowerCase().contains( contains.toLowerCase() );
	}
}
