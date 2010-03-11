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

package org.metawidget.example.swt.addressbook;

import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.swt.addressbook.converter.StringToDateConverter;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.swt.Facet;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessor;

/**
 * @author Richard Kennard
 */

public class SwtAddressBookTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testAddressBook()
		throws Exception
	{
		// Set Locale because we will be checking date formatting

		Locale.setDefault( Locale.UK );

		// Start app

		Shell shell = new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE );
		Main main = new Main( shell );
		assertTrue( shell.getChildren()[0] instanceof Label );
		Table contactsTable = (Table) shell.getChildren()[2];
		assertTrue( contactsTable.getItemCount() == 6 );

		// Check searching

		SwtMetawidget metawidgetSearch = (SwtMetawidget) shell.getChildren()[1];

		metawidgetSearch.setValue( "Simpson", "surname" );
		metawidgetSearch.setValue( "PERSONAL", "type" );

		assertTrue( metawidgetSearch.getChildren().length == 7 );

		Facet buttonFacet = (Facet) metawidgetSearch.getChildren()[metawidgetSearch.getChildren().length - 1];
		assertTrue( 2 == ( (GridData) buttonFacet.getLayoutData() ).horizontalSpan );
		assertTrue( SWT.FILL == ( (GridData) buttonFacet.getLayoutData() ).horizontalAlignment );
		assertTrue( ( (GridData) buttonFacet.getLayoutData() ).grabExcessHorizontalSpace );
		assertTrue( buttonFacet.getChildren().length == 1 );
		SwtMetawidget buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		assertTrue( buttonsMetawidget.getLayout() instanceof RowLayout );

		assertTrue( buttonsMetawidget.getChildren()[0] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[0].getLayoutData() ).exclude );
		assertTrue( buttonsMetawidget.getChildren()[1] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[1].getLayoutData() ).exclude );

		Button searchButton = (Button) buttonsMetawidget.getChildren()[2];
		assertEquals( "Search", searchButton.getText() );
		searchButton.notifyListeners( SWT.Selection, null );
		assertEquals( "PERSONAL", ( (Combo) metawidgetSearch.getControl( "type" ) ).getText() );

		ContactsController contactsController = main.getContactsController();
		assertTrue( contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size() == 2 );

		// Open dialog for Personal Contact

		Contact contact = contactsController.load( 1 );
		assertEquals( "Mr Homer Simpson", contact.getFullname() );
		assertEquals( "Mr Homer Simpson", contact.toString() );
		assertTrue( 32 == contact.hashCode() );
		assertFalse( contact.equals( new Object() ) );
		assertEquals( contact, contact );
		assertTrue( contact.compareTo( null ) == -1 );
		assertTrue( contact.compareTo( contact ) == 0 );
		assertEquals( "742 Evergreen Terrace", contact.getAddress().getStreet() );
		assertTrue( contact.getCommunications().size() == 1 );

		ContactDialog dialog = new ContactDialog( main );
		dialog.setShowConfirmDialog( false );
		dialog.open( contact );

		// Check loading

		SwtMetawidget metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Homer", metawidgetContact.getValue( "firstname" ) );
		assertTrue( metawidgetContact.getControl( "firstname" ) instanceof Label );
		assertEquals( "MALE", ( (Label) metawidgetContact.getControl( "gender" ) ).getText() );

		// TODO: assertEquals( "12/05/56", metawidgetContact.getValue( "dateOfBirth" ) );

		try
		{
			metawidgetContact.getValue( "bad-value" );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			// Should throw MetawidgetException
		}

		Table communicationsTable = metawidgetContact.getControl( "communications" );
		assertTrue( communicationsTable.getItemCount() == 1 );
		assertTrue( metawidgetContact.getChildren().length == 21 );

		// Check editing

		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		assertTrue( 2 == ( (GridData) buttonFacet.getLayoutData() ).horizontalSpan );
		assertTrue( SWT.FILL == ( (GridData) buttonFacet.getLayoutData() ).horizontalAlignment );
		assertTrue( ( (GridData) buttonFacet.getLayoutData() ).grabExcessHorizontalSpace );
		assertTrue( buttonFacet.getChildren().length == 1 );
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		assertTrue( buttonsMetawidget.getLayout() instanceof RowLayout );

		assertTrue( buttonsMetawidget.getChildren()[0] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[0].getLayoutData() ).exclude );
		assertTrue( buttonsMetawidget.getChildren()[1] instanceof Stub );
		assertTrue( ( (RowData) buttonsMetawidget.getChildren()[1].getLayoutData() ).exclude );

		Button editButton = (Button) buttonsMetawidget.getChildren()[3];
		assertEquals( "Edit", editButton.getText() );
		editButton.notifyListeners( SWT.Selection, null );

		assertEquals( "MALE", ( (Combo) metawidgetContact.getControl( "gender" ) ).getText() );
		metawidgetContact.setValue( "Sapien", "surname" );
		assertTrue( metawidgetContact.getChildren().length == 21 );

		// Check editing a communication

		assertTrue( communicationsTable.getItemCount() == 2 );

		/*
		 * @SuppressWarnings( "unchecked" ) ListTableModel<Communication> communicationModel =
		 * (ListTableModel<Communication>) model; communicationModel.importCollection(
		 * contact.getCommunications() ); TableCellEditor editor = communications.getDefaultEditor(
		 * Object.class ); SwtMetawidget metawidgetCommunications = (SwtMetawidget)
		 * editor.getTableCellEditorComponent( communications, model.getValueAt( 0, 0 ), true, 0, 0
		 * ); assertEquals( BoxLayout.class, metawidgetCommunications.getLayout().getClass() );
		 * JComboBox combo = (JComboBox) metawidgetCommunications.getComponent( 0 ); assertEquals(
		 * "Telephone", combo.getSelectedItem() ); // Check adding a communication
		 * metawidgetCommunications = (SwtMetawidget) editor.getTableCellEditorComponent(
		 * communications, model.getValueAt( 1, 0 ), true, 1, 0 ); combo = (JComboBox)
		 * metawidgetCommunications.getComponent( 0 ); combo.setSelectedItem( "Mobile" );
		 * editor.stopCellEditing(); model.setValueAt( editor.getCellEditorValue(), 1, 0 );
		 * metawidgetCommunications = (SwtMetawidget) editor.getTableCellEditorComponent(
		 * communications, model.getValueAt( 1, 1 ), true, 1, 1 ); JTextField textField =
		 * (JTextField) metawidgetCommunications.getComponent( 0 ); textField.setText(
		 * "(0402) 123 456" ); editor.stopCellEditing(); model.setValueAt(
		 * editor.getCellEditorValue(), 1, 1 );
		 */

		// Check saving

		metawidgetContact.setValue( "foo", "dateOfBirth" );

		try
		{
			metawidgetContact.getWidgetProcessor( DataBindingProcessor.class ).save( metawidgetContact );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertEquals( "java.text.ParseException: Unparseable date: \"foo\"", e.getCause().getMessage() );
		}

		metawidgetContact.setValue( "12/05/57", "dateOfBirth" );
		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		Button saveButton = (Button) buttonsMetawidget.getChildren()[4];
		assertEquals( "Save", saveButton.getText() );
		saveButton.notifyListeners( SWT.Selection, null );

		assertEquals( "Sapien", contact.getSurname() );
		assertEquals( new StringToDateConverter().convert( "12/05/57" ), ( (PersonalContact) contact ).getDateOfBirth() );
		assertTrue( ( (PersonalContact) contact ).getDateOfBirth().getTime() == -398944800000l );

		/*
		 * Iterator<Communication> iterator = contact.getCommunications().iterator();
		 * iterator.next(); Communication communication = iterator.next(); assertEquals( "Mobile",
		 * communication.getType() ); assertEquals( "(0402) 123 456", communication.getValue() );
		 * assertEquals( communication, communication ); assertFalse( communication.equals( new
		 * Object() ) ); assertTrue( communication.compareTo( null ) == -1 ); assertTrue(
		 * communication.compareTo( communication ) == 0 ); // Check deleting the communication
		 * again assertTrue( contact.removeCommunication( new Communication() ) == false );
		 * contact.removeCommunication( communicationModel.getValueAt( 1 ) ); assertTrue(
		 * contact.getCommunications().size() == 1 );
		 */

		// Search everything

		metawidgetSearch.setValue( "", "surname" );
		metawidgetSearch.setValue( "", "type" );
		searchButton.notifyListeners( SWT.Selection, null );
		assertTrue( contactsTable.getItemCount() == 6 );

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertEquals( "Mr Charles Montgomery Burns", contact.getFullname() );
		dialog.open( contact );

		metawidgetContact = dialog.mContactMetawidget;
		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		Button backButton = (Button) buttonsMetawidget.getChildren()[6];
		assertEquals( "Back", backButton.getText() );
		editButton = (Button) buttonsMetawidget.getChildren()[3];
		assertEquals( "Edit", editButton.getText() );
		editButton.notifyListeners( SWT.Selection, null );

		assertEquals( "Charles Montgomery", metawidgetContact.getValue( "firstname" ) );
		assertEquals( "MALE", metawidgetContact.getValue( "gender" ) );
		assertTrue( 0 == (Integer) metawidgetContact.getValue( "numberOfStaff" ) );

		// Check saving

		metawidgetContact.setValue( 2, "numberOfStaff" );
		metawidgetContact.setValue( "A Company", "company" );

		assertTrue( metawidgetContact.getChildren().length == 23 );
		saveButton = (Button) buttonsMetawidget.getChildren()[4];
		assertEquals( "Save", saveButton.getText() );
		saveButton.notifyListeners( SWT.Selection, null );

		assertTrue( 2 == ( (BusinessContact) contact ).getNumberOfStaff() );
		assertEquals( "A Company", ( (BusinessContact) contact ).getCompany() );

		// Check deleting

		dialog.open( contact );
		metawidgetContact = dialog.mContactMetawidget;
		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		editButton = (Button) buttonsMetawidget.getChildren()[3];
		assertEquals( "Edit", editButton.getText() );
		editButton.notifyListeners( SWT.Selection, null );
		Button deleteButton = (Button) buttonsMetawidget.getChildren()[5];
		assertEquals( "Delete", deleteButton.getText() );
		Button cancelButton = (Button) buttonsMetawidget.getChildren()[6];
		assertEquals( "Cancel", cancelButton.getText() );

		assertTrue( contactsController.getAllByExample( null ).size() == 6 );
		deleteButton.notifyListeners( SWT.Selection, null );
		assertTrue( contactsController.getAllByExample( null ).size() == 5 );
		assertTrue( contactsTable.getItemCount() == 5 );

		// Open dialog for new Personal Contact

		dialog.open( new PersonalContact() );
		metawidgetContact = dialog.mContactMetawidget;
		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		cancelButton = (Button) buttonsMetawidget.getChildren()[6];
		assertEquals( "Cancel", cancelButton.getText() );

		// Check saving doesn't error on null date

		metawidgetContact.getWidgetProcessor( DataBindingProcessor.class ).save( metawidgetContact );

		// Check adding

		dialog.open( new BusinessContact() );
		metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Mr", ( (Combo) metawidgetContact.getControl( "title" ) ).getItem( 0 ) );
		assertTrue( 5 == ( (Combo) metawidgetContact.getControl( "title" ) ).getItemCount() );
		metawidgetContact.setValue( "Miss", "title" );
		metawidgetContact.setValue( "Business", "firstname" );
		metawidgetContact.setValue( "Contact", "surname" );
		assertTrue( 3 == ( (Combo) metawidgetContact.getControl( "gender" ) ).getItemCount() );
		assertEquals( "", ( (Combo) metawidgetContact.getControl( "gender" ) ).getText() );
		( (Combo) metawidgetContact.getControl( "gender" ) ).setText( "FEMALE" );

		buttonFacet = (Facet) metawidgetContact.getChildren()[metawidgetContact.getChildren().length - 1];
		buttonsMetawidget = (SwtMetawidget) buttonFacet.getChildren()[0];
		saveButton = (Button) buttonsMetawidget.getChildren()[4];
		assertEquals( "Save", saveButton.getText() );
		saveButton.notifyListeners( SWT.Selection, null );
		assertTrue( contactsTable.getItemCount() == 6 );

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
	}
}
