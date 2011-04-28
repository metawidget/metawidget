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

			allByExample.add( contact );
		}

		Collections.sort( allByExample );
		return allByExample;
	}

	public Contact load( long id ) {

		// TODO: needs some defensive copying
		
		return mAll.get( id );
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

		long id = contact.getId();

		if ( id == 0 ) {
			id = incrementNextContactId();
			contact.setId( id );
		} else {
			if ( !mAll.containsKey( id ) ) {
				throw new RuntimeException( "Contact #" + id + " not found" );
			}
		}

		mAll.put( id, contact );

		// Simulate cascading save

		Set<Communication> communications = contact.getCommunications();

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
