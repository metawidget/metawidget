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

package org.metawidget.example.swing.addressbook;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.swing.addressbook.converter.DateConverter;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingAddressBookTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( { "cast", "deprecation" } )
	public void testAddressBook()
		throws Exception {

		// Start app

		MainFrame frame = new MainFrame();
		AddressBook addressBook = frame.getAddressBook();
		JPanel panelRight = (JPanel) ( (ImagePanel) frame.getContentPane().getComponent( 0 ) ).getComponent( 1 );
		assertFalse( panelRight.isOpaque() );
		JTable contactsTable = ( (JTable) ( (JScrollPane) panelRight.getComponent( 1 ) ).getViewport().getView() );
		assertEquals( contactsTable.getRowCount(), 6 );

		// Check searching

		SwingMetawidget metawidgetSearch = (SwingMetawidget) panelRight.getComponent( 0 );

		metawidgetSearch.setValue( "Simpson", "surname" );
		metawidgetSearch.setValue( ContactType.PERSONAL, "type" );

		assertEquals( metawidgetSearch.getComponentCount(), 8 );
		JPanel buttonsPanel = (JPanel) metawidgetSearch.getComponent( metawidgetSearch.getComponentCount() - 1 );
		assertEquals( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).gridx, -1 );
		assertEquals( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).gridy, 4 );
		assertEquals( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).fill, GridBagConstraints.BOTH );
		assertEquals( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).anchor, GridBagConstraints.WEST );
		assertEquals( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).gridwidth, GridBagConstraints.REMAINDER );
		assertTrue( ( (Container) buttonsPanel.getComponent( 0 ) ).getLayout() instanceof FlowLayout );
		JButton buttonSearch = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Search", buttonSearch.getText() );
		assertEquals( "Add Personal Contact", ( (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 1 ) ).getText() );
		assertEquals( "Add Business Contact", ( (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 2 ) ).getText() );

		buttonSearch.getAction().actionPerformed( null );
		assertEquals( ContactType.PERSONAL, ( (JComboBox) metawidgetSearch.getComponent( "type" ) ).getSelectedItem() );

		ContactsController contactsController = addressBook.getContactsController();
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

		ContactDialog dialog = addressBook.createContactDialog( contact );
		dialog.setShowConfirmDialog( false );

		// Check loading

		SwingMetawidget metawidgetContact = (SwingMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );
		assertEquals( "Homer", metawidgetContact.getValue( "firstname" ) );
		assertTrue( ( (Component) metawidgetContact.getComponent( "firstname" ) ) instanceof JLabel );
		assertEquals( "Male", ( (JLabel) metawidgetContact.getComponent( "gender" ) ).getText() );
		assertTrue( metawidgetContact.getComponent( 10 ) instanceof JPanel );
		assertEquals( "Contact Details", ( (JLabel) ( (JPanel) metawidgetContact.getComponent( 10 ) ).getComponent( 0 ) ).getText() );
		assertEquals( java.awt.GridBagConstraints.REMAINDER, ( (GridBagLayout) metawidgetContact.getLayout() ).getConstraints( metawidgetContact.getComponent( 10 ) ).gridwidth );
		assertEquals( DateFormat.getDateInstance( DateFormat.SHORT ).format( new Date( 56, Calendar.MAY, 12 ) ), metawidgetContact.getValue( "dateOfBirth" ) );

		try {
			metawidgetContact.getValue( "bad-value" );
			fail();
		} catch ( MetawidgetException e ) {
			// Should throw MetawidgetException
		}

		JScrollPane scrollPane = metawidgetContact.getComponent( "communications" );
		JTable communications = (JTable) scrollPane.getViewport().getView();
		TableModel model = communications.getModel();

		assertEquals( model.getRowCount(), 1 );
		assertEquals( metawidgetContact.getComponentCount(), 19 );

		// Check painting (in that it at least doesn't NullPointer)

		Graphics graphics = new BufferedImage( 100, 100, BufferedImage.TYPE_INT_ARGB ).getGraphics();
		( (ImagePanel) ( (JPanel) ( (JLayeredPane) ( (JRootPane) dialog.getComponent( 0 ) ).getComponent( 1 ) ).getComponent( 0 ) ).getComponent( 0 ) ).paint( graphics );

		// Read-only editing does nothing

		assertFalse( model.isCellEditable( 0, 0 ) );
		JPopupMenu popupMenu = (JPopupMenu) communications.getComponent( 1 );
		MouseEvent popupMouseEvent = new MouseEvent( communications, 0, System.currentTimeMillis(), 0, 0, 0, 0, true );
		communications.getMouseListeners()[0].mouseReleased( popupMouseEvent );
		assertFalse( popupMenu.isVisible() );

		// Check editing

		buttonsPanel = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		assertTrue( ( (Container) buttonsPanel.getComponent( 0 ) ).getLayout() instanceof FlowLayout );
		JButton editButton = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Edit", editButton.getText() );
		editButton.getAction().actionPerformed( null );

		assertEquals( Gender.MALE, ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getSelectedItem() );
		metawidgetContact.setValue( "Sapien", "surname" );
		assertTrue( metawidgetContact.getComponent( 10 ) instanceof JPanel );
		assertEquals( "Contact Details", ( (JLabel) ( (JPanel) metawidgetContact.getComponent( 10 ) ).getComponent( 0 ) ).getText() );
		assertEquals( java.awt.GridBagConstraints.REMAINDER, ( (GridBagLayout) metawidgetContact.getLayout() ).getConstraints( metawidgetContact.getComponent( 10 ) ).gridwidth );
		assertEquals( metawidgetContact.getComponentCount(), 19 );

		// Check editing a communication

		assertEquals( model.getRowCount(), 2 );

		assertTrue( model.isCellEditable( 0, 0 ) );
		@SuppressWarnings( "unchecked" )
		ListTableModel<Communication> communicationModel = (ListTableModel<Communication>) model;

		communicationModel.importCollection( contact.getCommunications() );
		TableCellEditor editor = communications.getDefaultEditor( Object.class );

		SwingMetawidget metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 0, 0 ), true, 0, 0 );
		assertEquals( BoxLayout.class, metawidgetCommunications.getLayout().getClass() );
		JComboBox combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		assertEquals( "Telephone", combo.getSelectedItem() );

		// Check clicking blank space

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 0 ), true, 1, 0 );
		combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		combo.setSelectedItem( "" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 0 );
		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 1 ), true, 1, 1 );
		JTextField textField = (JTextField) metawidgetCommunications.getComponent( 0 );
		textField.setText( "" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 1 );
		assertEquals( model.getRowCount(), 2 );

		// Check adding a communication

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 0 ), true, 1, 0 );
		combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		combo.setSelectedItem( "Mobile" );
		assertEquals( model.getRowCount(), 2 );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 0 );
		assertEquals( model.getRowCount(), 3 );

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 1 ), true, 1, 1 );
		textField = (JTextField) metawidgetCommunications.getComponent( 0 );
		textField.setText( "(0402) 123 456" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 1 );
		assertEquals( model.getRowCount(), 3 );

		// Check popup calls stopCellEditing

		communications.setCellEditor( editor );
		final Set<String> stopCellEditing = CollectionUtils.newHashSet();

		editor.addCellEditorListener( new CellEditorListener() {

			public void editingStopped( ChangeEvent e ) {

				stopCellEditing.add( "editingStopped" );
			}

			public void editingCanceled( ChangeEvent e ) {

				// Do nothing
			}
		} );

		// Check deleting a communication

		communications.getMouseListeners()[communications.getMouseListeners().length - 1].mouseReleased( popupMouseEvent );
		assertTrue( popupMenu.isVisible() );
		assertEquals( "[editingStopped]", stopCellEditing.toString() );
		Rectangle cellRect = communications.getCellRect( 0, 0, true );
		popupMenu.setLocation( cellRect.x, cellRect.y );
		( (JMenuItem) popupMenu.getComponent( 0 ) ).getAction().actionPerformed( null );
		assertFalse( popupMenu.isVisible() );

		// Check 'adding' a blank communication

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 0 ), true, 1, 0 );
		combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		combo.setSelectedItem( "" );
		assertEquals( model.getRowCount(), 2 );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 0 );
		assertEquals( model.getRowCount(), 2 );

		// Check saving

		metawidgetContact.setValue( "foo", "dateOfBirth" );

		try {
			metawidgetContact.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidgetContact );
			fail();
		} catch ( Exception e ) {
			assertEquals( "Unparseable date: \"foo\"", e.getCause().getCause().getMessage() );
		}

		metawidgetContact.setValue( DateFormat.getDateInstance( DateFormat.SHORT ).format( new Date( 57, Calendar.MAY, 12 ) ), "dateOfBirth" );
		buttonsPanel = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonSave = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Save", buttonSave.getText() );
		buttonSave.getAction().actionPerformed( null );

		assertEquals( "Simpson", contact.getSurname() );
		contact = contactsController.load( 1 );
		assertEquals( "Sapien", contact.getSurname() );
		assertEquals( new DateConverter().convertReverse( "12/05/57" ), ( (PersonalContact) contact ).getDateOfBirth() );

		Iterator<Communication> iterator = contact.getCommunications().iterator();
		Communication communication = iterator.next();
		assertEquals( "Mobile", communication.getType() );
		assertEquals( "(0402) 123 456", communication.getValue() );
		assertFalse( iterator.hasNext() );

		// Check deleting the communication again

		assertEquals( contact.removeCommunication( new Communication() ), false );
		contact.removeCommunication( communicationModel.getValueAt( 1 ) );
		assertEquals( contact.getCommunications().size(), 1 );

		// Check re-viewing

		dialog = addressBook.createContactDialog( contact );
		metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Sapien", metawidgetContact.getValue( "surname" ) );
		assertEquals( "12/05/57", metawidgetContact.getValue( "dateOfBirth" ) );

		scrollPane = metawidgetContact.getComponent( "communications" );
		communications = (JTable) scrollPane.getViewport().getView();
		assertEquals( communications.getRowCount(), 1 );
		assertEquals( metawidgetContact.getComponentCount(), 19 );

		// Search everything

		metawidgetSearch.setValue( null, "surname" );
		metawidgetSearch.setValue( null, "type" );
		buttonSearch.getAction().actionPerformed( null );
		assertEquals( contactsTable.getRowCount(), 6 );

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertEquals( "Mr Charles Montgomery Burns", contact.getFullname() );
		dialog = addressBook.createContactDialog( contact );
		metawidgetContact = (SwingMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );

		buttonsPanel = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonBack = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 1 );
		assertEquals( "Back", buttonBack.getText() );
		editButton = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Edit", editButton.getText() );
		editButton.getAction().actionPerformed( null );
		assertEquals( "Charles Montgomery", metawidgetContact.getValue( "firstname" ) );
		assertTrue( Gender.MALE.equals( metawidgetContact.getValue( "gender" ) ) );
		assertTrue( 0 == (Integer) metawidgetContact.getValue( "numberOfStaff" ) );

		// Check saving

		metawidgetContact.setValue( 2, "numberOfStaff" );
		metawidgetContact.setValue( "A Company", "company" );

		assertEquals( metawidgetContact.getComponentCount(), 21 );
		buttonSave = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		buttonSave.getAction().actionPerformed( null );

		assertEquals( 0, ( (BusinessContact) contact ).getNumberOfStaff() );
		contact = contactsController.load( 5 );
		assertEquals( 2, ( (BusinessContact) contact ).getNumberOfStaff() );
		assertEquals( "A Company", ( (BusinessContact) contact ).getCompany() );

		// Check deleting

		JButton buttonDelete = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 1 );
		assertEquals( "Delete", buttonDelete.getText() );
		JButton buttonCancel = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 2 );
		assertEquals( "Cancel", buttonCancel.getText() );

		assertEquals( contactsController.getAllByExample( null ).size(), 6 );
		buttonDelete.getAction().actionPerformed( null );
		assertEquals( contactsController.getAllByExample( null ).size(), 5 );
		assertEquals( contactsTable.getRowCount(), 5 );

		// Open dialog for new Personal Contact

		dialog = addressBook.createContactDialog( new PersonalContact() );
		metawidgetContact = (SwingMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );

		// Check saving doesn't error on null date

		metawidgetContact.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidgetContact );

		buttonsPanel = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		buttonCancel = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 2 );
		assertEquals( "Cancel", buttonCancel.getText() );
		buttonCancel.getAction().actionPerformed( null );
		assertFalse( dialog.isVisible() );

		// Check adding

		assertEquals( "Mr", ( (JComboBox) metawidgetContact.getComponent( "title" ) ).getItemAt( 0 ) );

		// (don't preselect the first item - that's up to the binding implementation)

		assertEquals( -1, ( (JComboBox) metawidgetContact.getComponent( "title" ) ).getSelectedIndex() );
		assertEquals( 5, ( (JComboBox) metawidgetContact.getComponent( "title" ) ).getItemCount() );
		metawidgetContact.setValue( "Miss", "title" );
		metawidgetContact.setValue( "Business", "firstname" );
		metawidgetContact.setValue( "Contact", "surname" );
		assertEquals( 3, ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getItemCount() );
		assertEquals( null, ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getSelectedItem() );
		( (JComboBox) metawidgetContact.getComponent( "gender" ) ).setSelectedItem( Gender.FEMALE );
		assertTrue( metawidgetContact.getComponent( "address", "street" ) instanceof JTextField );

		buttonSave = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Save", buttonSave.getText() );
		buttonSave.getAction().actionPerformed( null );
		assertEquals( contactsTable.getRowCount(), 6 );

		// Check viewing

		contact = contactsController.load( 7 );
		assertEquals( "Miss Business Contact", contact.getFullname() );
		assertEquals( Gender.FEMALE, contact.getGender() );
		metawidgetContact.setReadOnly( true );
		assertEquals( "Female", ( (JLabel) metawidgetContact.getComponent( "gender" ) ).getText() );

		metawidgetContact.setReadOnly( false );
		assertEquals( Gender.FEMALE, ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getSelectedItem() );
	}

	public void testListTableModel()
		throws Exception {

		// Test nulls

		ListTableModel<Foo> model = new ListTableModel<Foo>( Foo.class, null );
		assertEquals( 0, model.getColumnCount() );
		assertEquals( 0, model.getRowCount() );
		assertEquals( null, model.getColumnClass( 0 ) );
		assertEquals( null, model.getValueAt( 0 ) );

		model = new ListTableModel<Foo>( Foo.class, null, "Foo" );
		model.setExtraBlankRow( true );
		model.setValueAt( null, 0, 0 );
		model.setValueAt( "", 0, 0 );

		// Test normal list

		List<Foo> fooList = CollectionUtils.newArrayList();
		Foo foo = new Foo();
		fooList.add( foo );
		model = new ListTableModel<Foo>( Foo.class, fooList, "Foo", "Bar" );
		assertEquals( String.class, model.getColumnClass( 0 ) );
		assertEquals( Boolean.class, model.getColumnClass( 1 ) );
		assertFalse( model.isCellEditable( 0, 0 ) );
		model.setEditable( true );
		assertTrue( model.isCellEditable( 0, 0 ) );

		// Getters

		assertEquals( null, model.getValueAt( 1 ) );
		assertEquals( foo, model.getValueAt( 0 ) );
		assertEquals( "myFoo", model.getValueAt( 0, 0 ) );
		assertEquals( Boolean.TRUE, model.getValueAt( 0, 1 ) );
		assertEquals( null, model.getValueAt( 0, 2 ) );

		// Setters

		model.setValueAt( "myFoo1", 0, 0 );
		model.setValueAt( Boolean.FALSE, 0, 1 );
		model.setValueAt( new Object(), 0, 2 );
		model.setValueAt( "myFoo2", 1, 0 );
		assertEquals( "myFoo1", model.getValueAt( 0, 0 ) );
		assertEquals( Boolean.FALSE, model.getValueAt( 0, 1 ) );
		assertEquals( null, model.getValueAt( 0, 2 ) );
		assertEquals( null, model.getValueAt( 1, 0 ) );

		// Extra blank row

		model.setExtraBlankRow( true );
		model.setValueAt( "myFoo2", 1, 0 );
		assertEquals( "myFoo2", model.getValueAt( 1, 0 ) );
		assertEquals( 3, model.getRowCount() );

		// Invalid column

		model = new ListTableModel<Foo>( Foo.class, fooList, "Baz" );

		try {
			model.getColumnClass( 0 );
			fail();
		} catch ( Exception e ) {
			assertEquals( "No such method getBaz (or boolean isBaz) on class org.metawidget.example.swing.addressbook.SwingAddressBookTest$Foo", e.getMessage() );
		}
	}

	public void testCreateContactDialog() {

		AddressBook addressBook = new AddressBook( new JFrame() );

		PersonalContact personalContact = new PersonalContact();
		assertTrue( personalContact != addressBook.createContactDialog( personalContact ).mContactMetawidget.getToInspect() );

		BusinessContact businessContact = new BusinessContact();
		assertTrue( businessContact != addressBook.createContactDialog( businessContact ).mContactMetawidget.getToInspect() );
	}

	//
	// Inner class
	//

	public static class Foo
		implements Comparable<Foo> {

		//
		// Private members
		//

		private String	mFoo	= "myFoo";

		private Boolean	mBar	= Boolean.TRUE;

		//
		// Public methods
		//

		public String getFoo() {

			return mFoo;
		}

		public void setFoo( String foo ) {

			mFoo = foo;
		}

		public Boolean getBar() {

			return mBar;
		}

		public void setBar( Boolean bar ) {

			mBar = bar;
		}

		public int compareTo( Foo that ) {

			return hashCode() - that.hashCode();
		}
	}
}
