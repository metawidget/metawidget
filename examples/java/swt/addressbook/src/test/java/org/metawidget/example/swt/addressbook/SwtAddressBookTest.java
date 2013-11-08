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

package org.metawidget.example.swt.addressbook;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.swt.addressbook.converter.StringToDateConverter;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.swt.Facet;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwtAddressBookTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( { "cast", "deprecation" } )
	public void testAddressBook()
		throws Exception {

		// Start app

		Shell shell = new Shell( new Display(), SWT.NONE );
		Main main = new Main( shell );
		assertTrue( shell.getChildren()[0] instanceof Label );
		Table contactsTable = (Table) shell.getChildren()[2];
		assertEquals( contactsTable.getItemCount(), 6 );

		// Check searching

		SwtMetawidget metawidgetSearch = (SwtMetawidget) shell.getChildren()[1];

		metawidgetSearch.setValue( "Simpson", "surname" );
		metawidgetSearch.setValue( "PERSONAL", "type" );

		assertEquals( metawidgetSearch.getChildren().length, 7 );

		Facet buttonFacet = (Facet) metawidgetSearch.getChildren()[metawidgetSearch.getChildren().length - 1];
		assertEquals( 2, ( (GridData) buttonFacet.getLayoutData() ).horizontalSpan );
		assertEquals( SWT.FILL, ( (GridData) buttonFacet.getLayoutData() ).horizontalAlignment );
		assertTrue( ( (GridData) buttonFacet.getLayoutData() ).grabExcessHorizontalSpace );
		assertEquals( buttonFacet.getChildren().length, 1 );
		SwtMetawidget buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		assertTrue( buttonsMetawidget.getLayout() instanceof RowLayout );

		assertTrue( buttonsMetawidget.getChildren()[0] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[0].getLayoutData() ).exclude );
		assertTrue( buttonsMetawidget.getChildren()[1] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[1].getLayoutData() ).exclude );

		Button searchButton = (Button) buttonsMetawidget.getChildren()[2];
		assertEquals( "Search", searchButton.getText() );
		assertEquals( "Add Personal Contact", ( (Button) buttonsMetawidget.getChildren()[3] ).getText() );
		assertEquals( "Add Business Contact", ( (Button) buttonsMetawidget.getChildren()[4] ).getText() );
		searchButton.notifyListeners( SWT.Selection, null );
		assertEquals( "PERSONAL", ( (Combo) metawidgetSearch.getControl( "type" ) ).getText() );

		ContactsController contactsController = main.getContactsController();
		assertEquals( contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size(), 2 );

		// Open dialog for Personal Contact

		Contact contact = contactsController.load( 1 );
		assertEquals( "Mr Homer Simpson", contact.getFullname() );
		assertEquals( "Mr Homer Simpson", contact.toString() );
		assertEquals( 32, contact.hashCode() );
		assertFalse( contact.equals( new Object() ) );
		assertEquals( contact, contact );
		assertEquals( contact.compareTo( contact ), 0 );
		assertEquals( "742 Evergreen Terrace", contact.getAddress().getStreet() );
		assertEquals( contact.getCommunications().size(), 1 );

		ContactDialog dialog = new ContactDialog( main );
		dialog.setShowConfirmDialog( false );
		dialog.open( contact );

		// Check loading

		SwtMetawidget metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Homer", metawidgetContact.getValue( "firstname" ) );
		assertTrue( ( (Control) metawidgetContact.getControl( "firstname" ) ) instanceof Label );
		assertEquals( "MALE", ( (Label) metawidgetContact.getControl( "gender" ) ).getText() );
		assertTrue( metawidgetContact.getChildren()[12] instanceof Composite );
		assertEquals( "Contact Details", ( (Label) ( (Composite) metawidgetContact.getChildren()[10] ).getChildren()[0] ).getText() );
		assertEquals( 1, ( (GridData) metawidgetContact.getChildren()[13].getLayoutData() ).horizontalSpan );
		assertEquals( "Address:", ( (Label) metawidgetContact.getControl( "address_label" ) ).getText() );
		assertEquals( 1, ( (GridData) ( (Control) metawidgetContact.getControl( "address_label" ) ).getLayoutData() ).horizontalSpan );
		assertEquals( 1, ( (GridData) ( (Control) metawidgetContact.getControl( "address" ) ).getLayoutData() ).horizontalSpan );

		assertEquals( DateFormat.getDateInstance( DateFormat.SHORT ).format( new Date( 56, Calendar.MAY, 12 ) ), metawidgetContact.getValue( "dateOfBirth" ) );

		try {
			metawidgetContact.getValue( "bad-value" );
			fail();
		} catch ( MetawidgetException e ) {
			// Should throw MetawidgetException
		}

		Table communicationsTable = metawidgetContact.getControl( "communications" );
		assertEquals( communicationsTable.getItemCount(), 1 );
		assertEquals( metawidgetContact.getChildren().length, 21 );

		// Check read-only editing does nothing

		assertEquals( null, dialog.mCommunicationsEditor.getEditor() );
		Event event = new Event();
		event.button = 1;
		event.x = communicationsTable.getItem( 0 ).getBounds( 1 ).x;
		event.y = communicationsTable.getItem( 0 ).getBounds( 1 ).y;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		assertEquals( null, dialog.mCommunicationsEditor.getEditor() );

		dialog.mCommunicationsTable.getMenu().notifyListeners( SWT.Show, null );
		assertFalse( dialog.mCommunicationsTable.getMenu().getItem( 0 ).getEnabled() );

		// Check editing

		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		assertEquals( 2, ( (GridData) buttonFacet.getLayoutData() ).horizontalSpan );
		assertEquals( SWT.FILL, ( (GridData) buttonFacet.getLayoutData() ).horizontalAlignment );
		assertTrue( ( (GridData) buttonFacet.getLayoutData() ).grabExcessHorizontalSpace );
		assertEquals( buttonFacet.getChildren().length, 1 );
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		assertTrue( buttonsMetawidget.getLayout() instanceof RowLayout );

		assertTrue( buttonsMetawidget.getChildren()[0] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[0].getLayoutData() ).exclude );
		assertTrue( buttonsMetawidget.getChildren()[1] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[1].getLayoutData() ).exclude );

		Button editButton = (Button) buttonsMetawidget.getChildren()[2];
		assertEquals( "Edit", editButton.getText() );
		editButton.notifyListeners( SWT.Selection, null );

		assertEquals( "MALE", ( (Combo) metawidgetContact.getControl( "gender" ) ).getText() );
		metawidgetContact.setValue( "Sapien", "surname" );
		assertTrue( metawidgetContact.getChildren()[12] instanceof Composite );
		assertEquals( "Contact Details", ( (Label) ( (Composite) metawidgetContact.getChildren()[10] ).getChildren()[0] ).getText() );
		assertEquals( 1, ( (GridData) metawidgetContact.getChildren()[13].getLayoutData() ).horizontalSpan );
		assertEquals( metawidgetContact.getChildren().length, 21 );

		// Check editing a communication

		assertEquals( communicationsTable.getItemCount(), 2 );

		assertEquals( null, dialog.mCommunicationsEditor.getEditor() );
		event.button = 0;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		assertEquals( null, dialog.mCommunicationsEditor.getEditor() );

		event.button = 1;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		SwtMetawidget communicationMetawidget = (SwtMetawidget) dialog.mCommunicationsEditor.getEditor();
		assertEquals( org.eclipse.swt.layout.FillLayout.class, communicationMetawidget.getLayout().getClass() );
		Combo combo = (Combo) communicationMetawidget.getChildren()[0];
		assertEquals( "Telephone", combo.getText() );

		// Check clicking blank space

		event.y = communicationsTable.getItem( 1 ).getBounds( 1 ).y;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		communicationMetawidget = (SwtMetawidget) dialog.mCommunicationsEditor.getEditor();
		combo = (Combo) communicationMetawidget.getChildren()[0];
		combo.setText( "" );
		event.x = communicationsTable.getItem( 1 ).getBounds( 2 ).x;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		communicationMetawidget = (SwtMetawidget) dialog.mCommunicationsEditor.getEditor();
		Text text = (Text) communicationMetawidget.getChildren()[0];
		text.setText( "" );
		event.y = 0;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		assertEquals( communicationsTable.getItemCount(), 2 );

		// Check adding a communication

		event.x = communicationsTable.getItem( 1 ).getBounds( 1 ).x;
		event.y = communicationsTable.getItem( 1 ).getBounds( 1 ).y;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		communicationMetawidget = (SwtMetawidget) dialog.mCommunicationsEditor.getEditor();
		combo = (Combo) communicationMetawidget.getChildren()[0];
		combo.setText( "Mobile" );
		event.y = 0;
		assertEquals( communicationsTable.getItemCount(), 2 );
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		assertEquals( communicationsTable.getItemCount(), 3 );

		event.x = communicationsTable.getItem( 1 ).getBounds( 2 ).x;
		event.y = communicationsTable.getItem( 1 ).getBounds( 2 ).y;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		communicationMetawidget = (SwtMetawidget) dialog.mCommunicationsEditor.getEditor();
		text = (Text) communicationMetawidget.getChildren()[0];
		text.setText( "(0402) 123 456" );
		event.y = 0;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		assertEquals( communicationsTable.getItemCount(), 3 );

		// Check deleting a communication

		dialog.mCommunicationsTable.getMenu().notifyListeners( SWT.Show, null );
		assertTrue( dialog.mCommunicationsTable.getMenu().getItem( 0 ).getEnabled() );
		dialog.mCommunicationsTable.setSelection( 0 );
		dialog.mCommunicationsTable.getMenu().getItem( 0 ).notifyListeners( SWT.Selection, null );

		// Check 'adding' a blank communication

		event.x = communicationsTable.getItem( 1 ).getBounds( 1 ).x;
		event.y = communicationsTable.getItem( 1 ).getBounds( 1 ).y;
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		communicationMetawidget = (SwtMetawidget) dialog.mCommunicationsEditor.getEditor();
		combo = (Combo) communicationMetawidget.getChildren()[0];
		combo.setText( "" );
		event.y = 0;
		assertEquals( communicationsTable.getItemCount(), 2 );
		communicationsTable.notifyListeners( SWT.MouseDown, event );
		assertEquals( communicationsTable.getItemCount(), 2 );

		// Check saving

		metawidgetContact.setValue( "foo", "dateOfBirth" );

		try {
			metawidgetContact.getWidgetProcessor( DataBindingProcessor.class ).save( metawidgetContact );
			fail();
		} catch ( Exception e ) {
			assertEquals( "java.text.ParseException: Unparseable date: \"foo\"", e.getCause().getMessage() );
		}

		metawidgetContact.setValue( "12/05/57", "dateOfBirth" );
		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		Button saveButton = (Button) buttonsMetawidget.getChildren()[3];
		assertEquals( "Save", saveButton.getText() );
		saveButton.notifyListeners( SWT.Selection, null );

		assertEquals( "Sapien", contact.getSurname() );
		assertEquals( new StringToDateConverter().convert( "12/05/57" ), ( (PersonalContact) contact ).getDateOfBirth() );

		Iterator<Communication> iterator = contact.getCommunications().iterator();
		Communication communication = iterator.next();
		assertEquals( "Mobile", communication.getType() );
		assertEquals( "(0402) 123 456", communication.getValue() );
		assertFalse( iterator.hasNext() );
		dialog.dispose();

		// Check re-viewing

		dialog.open( contact );
		metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Sapien", metawidgetContact.getValue( "surname" ) );
		assertEquals( DateFormat.getDateInstance( DateFormat.SHORT ).format( new Date( 57, Calendar.MAY, 12 ) ), metawidgetContact.getValue( "dateOfBirth" ) );

		communicationsTable = metawidgetContact.getControl( "communications" );
		assertEquals( communicationsTable.getItemCount(), 1 );
		assertEquals( metawidgetContact.getChildren().length, 21 );

		// Search everything

		metawidgetSearch.setValue( "", "surname" );
		metawidgetSearch.setValue( "", "type" );
		searchButton.notifyListeners( SWT.Selection, null );
		assertEquals( contactsTable.getItemCount(), 6 );
		dialog.dispose();

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertEquals( "Mr Charles Montgomery Burns", contact.getFullname() );
		dialog.open( contact );

		metawidgetContact = dialog.mContactMetawidget;
		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		Button backButton = (Button) buttonsMetawidget.getChildren()[5];
		assertEquals( "Back", backButton.getText() );
		editButton = (Button) buttonsMetawidget.getChildren()[2];
		assertEquals( "Edit", editButton.getText() );
		editButton.notifyListeners( SWT.Selection, null );

		assertEquals( "Charles Montgomery", metawidgetContact.getValue( "firstname" ) );
		assertEquals( "MALE", metawidgetContact.getValue( "gender" ) );
		assertTrue( 0 == (Integer) metawidgetContact.getValue( "numberOfStaff" ) );

		// Check saving

		metawidgetContact.setValue( 2, "numberOfStaff" );
		metawidgetContact.setValue( "A Company", "company" );

		assertEquals( metawidgetContact.getChildren().length, 23 );
		saveButton = (Button) buttonsMetawidget.getChildren()[3];
		assertEquals( "Save", saveButton.getText() );
		saveButton.notifyListeners( SWT.Selection, null );

		assertEquals( 2, ( (BusinessContact) contact ).getNumberOfStaff() );
		assertEquals( "A Company", ( (BusinessContact) contact ).getCompany() );
		dialog.dispose();

		// Check deleting

		dialog.open( contact );
		metawidgetContact = dialog.mContactMetawidget;
		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		editButton = (Button) buttonsMetawidget.getChildren()[2];
		assertEquals( "Edit", editButton.getText() );
		editButton.notifyListeners( SWT.Selection, null );
		Button deleteButton = (Button) buttonsMetawidget.getChildren()[4];
		assertEquals( "Delete", deleteButton.getText() );
		Button cancelButton = (Button) buttonsMetawidget.getChildren()[5];
		assertEquals( "Cancel", cancelButton.getText() );

		assertEquals( contactsController.getAllByExample( null ).size(), 6 );
		deleteButton.notifyListeners( SWT.Selection, null );
		assertEquals( contactsController.getAllByExample( null ).size(), 5 );
		assertEquals( contactsTable.getItemCount(), 5 );
		dialog.dispose();

		// Open dialog for new Personal Contact

		dialog.open( new PersonalContact() );
		metawidgetContact = dialog.mContactMetawidget;

		// Check saving doesn't error on null date

		metawidgetContact.getWidgetProcessor( DataBindingProcessor.class ).save( metawidgetContact );

		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		cancelButton = (Button) buttonsMetawidget.getChildren()[5];
		assertEquals( "Cancel", cancelButton.getText() );
		cancelButton.notifyListeners( SWT.Selection, null );
		assertTrue( metawidgetContact.isDisposed() );
		dialog.dispose();

		// Check adding

		dialog.open( new BusinessContact() );
		metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Mr", ( (Combo) metawidgetContact.getControl( "title" ) ).getItem( 0 ) );

		// (don't preselect the first item - that's up to the binding implementation)

		assertEquals( -1, ( (Combo) metawidgetContact.getControl( "title" ) ).getSelectionIndex() );
		assertEquals( 5, ( (Combo) metawidgetContact.getControl( "title" ) ).getItemCount() );
		metawidgetContact.setValue( "Miss", "title" );
		metawidgetContact.setValue( "Business", "firstname" );
		metawidgetContact.setValue( "Contact", "surname" );
		assertEquals( 3, ( (Combo) metawidgetContact.getControl( "gender" ) ).getItemCount() );
		assertEquals( "", ( (Combo) metawidgetContact.getControl( "gender" ) ).getText() );
		( (Combo) metawidgetContact.getControl( "gender" ) ).setText( "FEMALE" );
		assertTrue( metawidgetContact.getControl( "address", "street" ) instanceof Text );

		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		saveButton = (Button) buttonsMetawidget.getChildren()[3];
		assertEquals( "Save", saveButton.getText() );
		saveButton.notifyListeners( SWT.Selection, null );
		assertEquals( contactsTable.getItemCount(), 6 );
		dialog.dispose();

		// Check viewing

		contact = contactsController.load( 7 );
		assertEquals( "Miss Business Contact", contact.getFullname() );
		assertEquals( Gender.FEMALE, contact.getGender() );
		dialog.open( contact );
		metawidgetContact = dialog.mContactMetawidget;
		metawidgetContact.setReadOnly( true );
		assertEquals( "FEMALE", ( (Label) metawidgetContact.getControl( "gender" ) ).getText() );

		metawidgetContact.setReadOnly( false );
		assertEquals( "FEMALE", ( (Combo) metawidgetContact.getControl( "gender" ) ).getText() );
		dialog.dispose();

		// All done

		main.dispose();
		shell.dispose();
	}
}
