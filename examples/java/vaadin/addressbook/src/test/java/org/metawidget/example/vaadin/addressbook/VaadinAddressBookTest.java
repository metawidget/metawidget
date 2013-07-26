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
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
// be used to endorse or promote products derived from this software without
// specific prior written permission.
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

package org.metawidget.example.vaadin.addressbook;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.vaadin.ui.Facet;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Loghman Barari
 */

public class VaadinAddressBookTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testAddressBook()
		throws Exception {

		// Start app

		AddressBookUI addressBook = new AddressBookUI();
		CustomLayout customLayout = (CustomLayout) addressBook.mContent;
		assertEquals( customLayout.getTemplateName(), "addressbook" );
		VerticalLayout contactsMainBody = (VerticalLayout) customLayout.getComponent( "pagebody" );
		assertEquals( contactsMainBody.getComponentCount(), 2 );
		Table contactsTable = (Table) contactsMainBody.getComponent( 1 );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 6 );

		// Check searching

		VaadinMetawidget metawidgetSearch = (VaadinMetawidget) contactsMainBody.getComponent( 0 );
		assertEquals( "", ( (TextField) metawidgetSearch.getComponent( "firstname" ) ).getValue() );
		assertEquals( "", ( (TextField) metawidgetSearch.getComponent( "surname" ) ).getValue() );
		assertEquals( null, ( (com.vaadin.ui.Select) metawidgetSearch.getComponent( "type" ) ).getValue() );

		( (TextField) metawidgetSearch.getComponent( "surname" ) ).setValue( "Simpson" );
		( (com.vaadin.ui.Select) metawidgetSearch.getComponent( "type" ) ).setValue( ContactType.PERSONAL );

		FormLayout layout = metawidgetSearch.getContent();
		assertEquals( 4, layout.getComponentCount() );
		Facet buttons = (Facet) layout.getComponent( 3 );
		assertEquals( buttons.getWidth(), 100f );
		HorizontalLayout buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		Button buttonSearch = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Search", buttonSearch.getCaption() );
		assertEquals( "Add Personal Contact", ( (Button) buttonsLayout.getComponent( 1 ) ).getCaption() );
		assertEquals( "Add Business Contact", ( (Button) buttonsLayout.getComponent( 2 ) ).getCaption() );

		buttonSearch.click();
		assertEquals( ContactType.PERSONAL, ( (com.vaadin.ui.Select) metawidgetSearch.getComponent( "type" ) ).getValue() );

		ContactsController contactsController = addressBook.getContactsController();
		assertEquals( 2, contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size() );

		// Open dialog for Personal Contact

		Contact contact = contactsController.load( 1 );
		assertEquals( "Mr Homer Simpson", contact.getFullname() );
		assertEquals( "Mr Homer Simpson", contact.toString() );
		assertEquals( 32, contact.hashCode() );
		assertFalse( contact.equals( new Object() ) );
		assertEquals( contact, contact );
		assertEquals( contact.compareTo( contact ), 0 );
		assertEquals( "742 Evergreen Terrace", contact.getAddress().getStreet() );
		assertEquals( 1, contact.getCommunications().size() );

		ContactDialog contactDialog = addressBook.createContactDialog( contact );
		assertTrue( !contactDialog.isReadOnly() );

		// Check loading

		VaadinMetawidget metawidgetContact = (VaadinMetawidget) ( (CustomLayout) contactDialog.getContent() ).getComponent( "pagebody" );
		assertEquals( "Homer", ( (Label) metawidgetContact.getComponent( "firstname" ) ).getValue() );
		assertEquals( "Male", ( (Label) metawidgetContact.getComponent( "gender" ) ).getValue() );
		layout = (FormLayout) metawidgetContact.getContent();
		assertEquals( "Contact Details", ( (Label) layout.getComponent( 5 ) ).getValue() );
		assertEquals( DateFormat.getDateInstance( DateFormat.SHORT ).format( new Date( 56, Calendar.MAY, 12 ) ), ( (Label) metawidgetContact.getComponent( "dateOfBirth" ) ).getValue() );
		assertEquals( null, metawidgetContact.getComponent( "bad-value" ) );
		assertTrue( ( (TextArea) metawidgetContact.getComponent( "notes" ) ).isReadOnly() );

		VerticalLayout communicationsWrapper = metawidgetContact.getComponent( "communications" );
		Table table = (Table) communicationsWrapper.getComponent( 0 );
		@SuppressWarnings( "unchecked" )
		TableDataSource<Communication> dataSource = (TableDataSource<Communication>) table.getContainerDataSource();

		assertEquals( 1, dataSource.getItemIds().size() );
		assertEquals( ( (FormLayout) metawidgetContact.getContent() ).getComponentCount(), 11 );

		// Read-only editing does nothing

		assertTrue( !table.isEditable() );
		assertTrue( !table.isSelectable() );

		// Check editing

		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		Button editButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Edit", editButton.getCaption() );
		editButton.click();

		assertEquals( Gender.MALE, ( (com.vaadin.ui.Select) metawidgetContact.getComponent( "gender" ) ).getValue() );
		( (TextField) metawidgetContact.getComponent( "surname" ) ).setValue( "Sapien" );
		layout = (FormLayout) metawidgetContact.getContent();
		assertEquals( "Contact Details", ( (Label) layout.getComponent( 5 ) ).getValue() );
		assertTrue( !( (TextArea) metawidgetContact.getComponent( "notes" ) ).isReadOnly() );
		assertEquals( layout.getComponentCount(), 11 );

		// Check editing a communication

		communicationsWrapper = metawidgetContact.getComponent( "communications" );
		table = (Table) communicationsWrapper.getComponent( 0 );
		assertTrue( !table.isEditable() );
		assertTrue( table.isSelectable() );
		table = (Table) communicationsWrapper.getComponent( 0 );

		@SuppressWarnings( "unchecked" )
		TableDataSource<Communication> tableDataSource = (TableDataSource<Communication>) table.getContainerDataSource();
		Communication communication = tableDataSource.getDataRow( 1 );
		CommunicationDialog communicationDialog = new CommunicationDialog( contactDialog, communication );

		VaadinMetawidget communicationMetawidget = (VaadinMetawidget) communicationDialog.getContent();
		assertEquals( "dialog", communicationMetawidget.getStyleName() );
		assertEquals( "Telephone", ( (com.vaadin.ui.Select) communicationMetawidget.getComponent( "type" ) ).getValue() );
		assertEquals( "(939) 555-0113", ( (TextField) communicationMetawidget.getComponent( "value" ) ).getValue() );

		// Check adding a communication

		communication = new Communication();
		communicationDialog = new CommunicationDialog( contactDialog, communication );

		communicationMetawidget = (VaadinMetawidget) communicationDialog.getContent();
		( (com.vaadin.ui.Select) communicationMetawidget.getComponent( "type" ) ).setValue( "Mobile" );
		( (TextField) communicationMetawidget.getComponent( "value" ) ).setValue( "(0402) 123 456" );

		layout = (FormLayout) communicationMetawidget.getContent();
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = (HorizontalLayout) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 );
		Button saveButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Save", saveButton.getCaption() );
		saveButton.click();

		assertEquals( 2, dataSource.getItemIds().size() );

		// Check deleting a communication

		buttonsLayout = (HorizontalLayout) communicationsWrapper.getComponent( 1 );
		Button deleteButton = (Button) buttonsLayout.getComponent( 1 );
		assertEquals( "Delete", deleteButton.getCaption() );
		assertTrue( !deleteButton.isEnabled() );
		Object itemId = dataSource.getIdByIndex( 1 );
		assertEquals( "Telephone", dataSource.getDataRow( itemId ).getType() );
		table.select( itemId );
		deleteButton.setEnabled( true );
		deleteButton.click();

		assertEquals( 1, dataSource.getItemIds().size() );

		// Check saving

		( (PopupDateField) metawidgetContact.getComponent( "dateOfBirth" ) ).setValue( new Date( 57, Calendar.MAY, 12 ) );
		layout = (FormLayout) metawidgetContact.getContent();
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		saveButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Save", saveButton.getCaption() );
		assertEquals( "Simpson", contact.getSurname() );
		saveButton.click();

		assertEquals( "Simpson", contact.getSurname() );
		contact = contactsController.load( 1 );
		assertEquals( "Sapien", contact.getSurname() );
		assertEquals( new Date( 57, Calendar.MAY, 12 ), ( (PopupDateField) metawidgetContact.getComponent( "dateOfBirth" ) ).getValue() );

		assertEquals( 1, contact.getCommunications().size() );
		communication = contact.getCommunications().iterator().next();
		assertEquals( "Mobile", communication.getType() );
		assertEquals( "(0402) 123 456", communication.getValue() );

		// Check re-viewing

		contactDialog = addressBook.createContactDialog( contact );
		metawidgetContact = (VaadinMetawidget) ( (CustomLayout) contactDialog.getContent() ).getComponent( "pagebody" );
		assertEquals( "Sapien", ( (Label) metawidgetContact.getComponent( "surname" ) ).getValue() );
		assertEquals( DateFormat.getDateInstance( DateFormat.SHORT ).format( new Date( 57, Calendar.MAY, 12 ) ), ( (Label) metawidgetContact.getComponent( "dateOfBirth" ) ).getValue() );

		communicationsWrapper = metawidgetContact.getComponent( "communications" );
		table = (Table) communicationsWrapper.getComponent( 0 );
		assertEquals( 1, table.getItemIds().size() );

		// Search everything

		( (TextField) metawidgetSearch.getComponent( "surname" ) ).setValue( null );
		( (com.vaadin.ui.Select) metawidgetSearch.getComponent( "type" ) ).setValue( null );
		buttonSearch.click();
		assertEquals( 6, contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size() );

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertEquals( "Mr Charles Montgomery Burns", contact.getFullname() );
		contactDialog = addressBook.createContactDialog( contact );
		metawidgetContact = (VaadinMetawidget) ( (CustomLayout) contactDialog.getContent() ).getComponent( "pagebody" );
		layout = (FormLayout) metawidgetContact.getContent();
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		Button backButton = (Button) buttonsLayout.getComponent( 1 );
		assertEquals( "Back", backButton.getCaption() );
		editButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Edit", editButton.getCaption() );
		editButton.click();

		assertEquals( "Charles Montgomery", ( (TextField) metawidgetContact.getComponent( "firstname" ) ).getValue() );
		assertEquals( Gender.MALE, ( (com.vaadin.ui.Select) metawidgetContact.getComponent( "gender" ) ).getValue() );
		assertEquals( 0d, ( (Slider) metawidgetContact.getComponent( "numberOfStaff" ) ).getValue() );

		// Check saving

		( (Slider) metawidgetContact.getComponent( "numberOfStaff" ) ).setValue( 2d );
		( (TextField) metawidgetContact.getComponent( "company" ) ).setValue( "A Company" );

		layout = (FormLayout) metawidgetContact.getContent();
		assertEquals( 12, layout.getComponentCount() );
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		saveButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Save", saveButton.getCaption() );
		saveButton.click();

		assertEquals( 0, ( (BusinessContact) contact ).getNumberOfStaff() );
		contact = contactsController.load( 5 );
		assertEquals( 2, ( (BusinessContact) contact ).getNumberOfStaff() );
		assertEquals( "A Company", ( (BusinessContact) contact ).getCompany() );

		// Check deleting

		deleteButton = (Button) buttonsLayout.getComponent( 1 );
		assertEquals( "Delete", deleteButton.getCaption() );
		Button cancelButton = (Button) buttonsLayout.getComponent( 2 );
		assertEquals( "Cancel", cancelButton.getCaption() );

		assertEquals( contactsController.getAllByExample( null ).size(), 6 );
		deleteButton.click();
		assertEquals( contactsController.getAllByExample( null ).size(), 5 );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 5 );

		// Open dialog for new Personal Contact

		contactDialog = addressBook.createContactDialog( new PersonalContact() );
		assertTrue( !contactDialog.isReadOnly() );
		metawidgetContact = (VaadinMetawidget) ( (CustomLayout) contactDialog.getContent() ).getComponent( "pagebody" );

		// Open dialog for new Business Contact

		contactDialog = addressBook.createContactDialog( new BusinessContact() );
		assertTrue( !contactDialog.isReadOnly() );
		metawidgetContact = (VaadinMetawidget) ( (CustomLayout) contactDialog.getContent() ).getComponent( "pagebody" );

		( (Slider) metawidgetContact.getComponent( "numberOfStaff" ) ).getValue();

		// Check adding

		com.vaadin.ui.Select select = metawidgetContact.getComponent( "title" );
		assertEquals( "Mr", ( (com.vaadin.data.util.IndexedContainer) select.getContainerDataSource() ).getIdByIndex( 0 ) );
		assertEquals( null, select.getValue() );
		assertFalse( select.isNullSelectionAllowed() );
		assertEquals( 5, select.getItemIds().size() );
		( (com.vaadin.ui.Select) metawidgetContact.getComponent( "title" ) ).setValue( "Miss" );
		( (TextField) metawidgetContact.getComponent( "firstname" ) ).setValue( "Business" );
		( (TextField) metawidgetContact.getComponent( "surname" ) ).setValue( "Contact" );
		assertEquals( 2, ( (com.vaadin.ui.Select) metawidgetContact.getComponent( "gender" ) ).getItemIds().size() );
		assertTrue( ( (com.vaadin.ui.Select) metawidgetContact.getComponent( "gender" ) ).isNullSelectionAllowed() );
		assertEquals( null, ( (com.vaadin.ui.Select) metawidgetContact.getComponent( "gender" ) ).getValue() );
		( (com.vaadin.ui.Select) metawidgetContact.getComponent( "gender" ) ).setValue( Gender.FEMALE );
		assertTrue( ( metawidgetContact.getComponent( "address", "street" ) ) instanceof TextField );

		layout = (FormLayout) metawidgetContact.getContent();
		assertEquals( 12, layout.getComponentCount() );
		buttons = (Facet) layout.getComponent( layout.getComponentCount() - 1 );
		buttonsLayout = ( (VaadinMetawidget) ( (VerticalLayout) buttons.getContent() ).getComponent( 0 ) ).getContent();
		saveButton = (Button) buttonsLayout.getComponent( 0 );
		assertEquals( "Save", saveButton.getCaption() );
		saveButton.click();

		assertEquals( 2, ( (BusinessContact) contact ).getNumberOfStaff() );
		assertEquals( "A Company", ( (BusinessContact) contact ).getCompany() );
		assertEquals( contactsController.getAllByExample( null ).size(), 6 );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 6 );

		// Check viewing

		contact = contactsController.load( 7 );
		assertEquals( "Miss Business Contact", contact.getFullname() );
		assertEquals( Gender.FEMALE, contact.getGender() );
		metawidgetContact.setReadOnly( true );
		assertEquals( "Female", ( (Label) metawidgetContact.getComponent( "gender" ) ).getValue() );

		metawidgetContact.setReadOnly( false );
		assertEquals( Gender.FEMALE, ( (com.vaadin.ui.Select) metawidgetContact.getComponent( "gender" ) ).getValue() );
	}
}
