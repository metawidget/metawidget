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

package org.metawidget.example.swing.addressbook;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * @author Richard Kennard
 */

public class SwingAddressBookTest
	extends TestCase {

	//
	// Public methods
	//

	public void testAddressBook()
		throws Exception {

		// Set Locale because we will be checking date formatting

		Locale.setDefault( Locale.UK );

		// Start app

		MainFrame frame = new MainFrame();
		AddressBook addressBook = frame.getAddressBook();
		JPanel panelRight = (JPanel) ( (ImagePanel) frame.getContentPane().getComponent( 0 ) ).getComponent( 1 );
		assertFalse( panelRight.isOpaque() );
		JTable contactsTable = ( (JTable) ( (JScrollPane) panelRight.getComponent( 1 ) ).getViewport().getView() );
		assertTrue( contactsTable.getRowCount() == 6 );

		// Check searching

		SwingMetawidget metawidgetSearch = (SwingMetawidget) panelRight.getComponent( 0 );

		metawidgetSearch.setValue( "Simpson", "surname" );
		metawidgetSearch.setValue( ContactType.PERSONAL, "type" );

		assertTrue( metawidgetSearch.getComponentCount() == 8 );
		JPanel buttonsPanel = (JPanel) metawidgetSearch.getComponent( metawidgetSearch.getComponentCount() - 1 );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).gridx == -1 );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).gridy == 4 );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).fill == GridBagConstraints.BOTH );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).anchor == GridBagConstraints.WEST );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( buttonsPanel ).gridwidth == GridBagConstraints.REMAINDER );
		assertTrue( ( (Container) buttonsPanel.getComponent( 0 ) ).getLayout() instanceof FlowLayout );
		JButton buttonSearch = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Search", buttonSearch.getText() );

		buttonSearch.getAction().actionPerformed( null );
		assertTrue( ContactType.PERSONAL == ( (JComboBox) metawidgetSearch.getComponent( "type" ) ).getSelectedItem() );

		ContactsController contactsController = addressBook.getContactsController();
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

		ContactDialog dialog = new ContactDialog( addressBook, contact );
		dialog.setShowConfirmDialog( false );

		// Check loading

		SwingMetawidget metawidgetContact = (SwingMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );
		assertEquals( "Homer", metawidgetContact.getValue( "firstname" ) );
		assertTrue( metawidgetContact.getComponent( "firstname" ) instanceof JLabel );
		assertEquals( "Male", ( (JLabel) metawidgetContact.getComponent( "gender" ) ).getText() );
		assertEquals( "12/05/56", metawidgetContact.getValue( "dateOfBirth" ) );

		try {
			metawidgetContact.getValue( "bad-value" );
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			// Should throw MetawidgetException
		}

		JScrollPane scrollPane = metawidgetContact.getComponent( "communications" );
		JTable communications = (JTable) scrollPane.getViewport().getView();
		TableModel model = communications.getModel();

		assertTrue( model.getRowCount() == 1 );
		assertTrue( metawidgetContact.getComponentCount() == 19 );

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

		assertTrue( Gender.MALE == ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getSelectedItem() );
		metawidgetContact.setValue( "Sapien", "surname" );
		assertTrue( metawidgetContact.getComponentCount() == 19 );

		// Check editing a communication

		assertTrue( model.getRowCount() == 2 );

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
		assertTrue( model.getRowCount() == 2 );

		// Check adding a communication

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 0 ), true, 1, 0 );
		combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		combo.setSelectedItem( "Mobile" );
		assertTrue( model.getRowCount() == 2 );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 0 );
		assertTrue( model.getRowCount() == 3 );

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 1 ), true, 1, 1 );
		textField = (JTextField) metawidgetCommunications.getComponent( 0 );
		textField.setText( "(0402) 123 456" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 1 );
		assertTrue( model.getRowCount() == 3 );

		// Check popup calls stopCellEditing

		communications.setCellEditor( editor );
		final Set<String> stopCellEditing = CollectionUtils.newHashSet();

		editor.addCellEditorListener( new CellEditorListener() {

			@Override
			public void editingStopped( ChangeEvent e ) {

				stopCellEditing.add( "editingStopped" );
			}

			@Override
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
		assertTrue( model.getRowCount() == 2 );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 0 );
		assertTrue( model.getRowCount() == 2 );

		// Check saving

		metawidgetContact.setValue( "foo", "dateOfBirth" );

		try {
			metawidgetContact.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidgetContact );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "Unparseable date: \"foo\"", e.getCause().getCause().getMessage() );
		}

		metawidgetContact.setValue( "12/05/57", "dateOfBirth" );
		buttonsPanel = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonSave = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Save", buttonSave.getText() );
		buttonSave.getAction().actionPerformed( null );

		assertEquals( "Sapien", contact.getSurname() );
		assertEquals( new DateConverter().convertReverse( "12/05/57" ), ( (PersonalContact) contact ).getDateOfBirth() );
		assertTrue( ( (PersonalContact) contact ).getDateOfBirth().getTime() == -398944800000l );

		Iterator<Communication> iterator = contact.getCommunications().iterator();
		Communication communication = iterator.next();
		assertEquals( "Mobile", communication.getType() );
		assertEquals( "(0402) 123 456", communication.getValue() );
		assertFalse( iterator.hasNext() );

		// Check deleting the communication again

		assertTrue( contact.removeCommunication( new Communication() ) == false );
		contact.removeCommunication( communicationModel.getValueAt( 1 ) );
		assertTrue( contact.getCommunications().size() == 1 );

		// Check re-viewing

		dialog = new ContactDialog( addressBook, contact );
		metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Sapien", metawidgetContact.getValue( "surname" ) );
		assertEquals( "12/05/57", metawidgetContact.getValue( "dateOfBirth" ) );

		scrollPane = metawidgetContact.getComponent( "communications" );
		communications = (JTable) scrollPane.getViewport().getView();
		assertTrue( communications.getRowCount() == 1 );
		assertTrue( metawidgetContact.getComponentCount() == 19 );

		// Search everything

		metawidgetSearch.setValue( null, "surname" );
		metawidgetSearch.setValue( null, "type" );
		buttonSearch.getAction().actionPerformed( null );
		assertTrue( contactsTable.getRowCount() == 6 );

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertEquals( "Mr Charles Montgomery Burns", contact.getFullname() );
		dialog = new ContactDialog( addressBook, contact );
		metawidgetContact = (SwingMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );

		buttonsPanel = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonBack = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 1 );
		assertEquals( "Back", buttonBack.getText() );
		editButton = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Edit", editButton.getText() );
		editButton.getAction().actionPerformed( null );
		assertEquals( "Charles Montgomery", metawidgetContact.getValue( "firstname" ) );
		assertTrue( Gender.MALE == metawidgetContact.getValue( "gender" ) );
		assertTrue( 0 == (Integer) metawidgetContact.getValue( "numberOfStaff" ) );

		// Check saving

		metawidgetContact.setValue( 2, "numberOfStaff" );
		metawidgetContact.setValue( "A Company", "company" );

		assertTrue( metawidgetContact.getComponentCount() == 21 );
		buttonSave = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		buttonSave.getAction().actionPerformed( null );

		assertTrue( 2 == ( (BusinessContact) contact ).getNumberOfStaff() );
		assertEquals( "A Company", ( (BusinessContact) contact ).getCompany() );

		// Check deleting

		JButton buttonDelete = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 1 );
		assertEquals( "Delete", buttonDelete.getText() );
		JButton buttonCancel = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 2 );
		assertEquals( "Cancel", buttonCancel.getText() );

		assertTrue( contactsController.getAllByExample( null ).size() == 6 );
		buttonDelete.getAction().actionPerformed( null );
		assertTrue( contactsController.getAllByExample( null ).size() == 5 );
		assertTrue( contactsTable.getRowCount() == 5 );

		// Open dialog for new Personal Contact

		dialog = new ContactDialog( addressBook, new PersonalContact() );
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
		assertTrue( 5 == ( (JComboBox) metawidgetContact.getComponent( "title" ) ).getItemCount() );
		metawidgetContact.setValue( "Miss", "title" );
		metawidgetContact.setValue( "Business", "firstname" );
		metawidgetContact.setValue( "Contact", "surname" );
		assertTrue( 3 == ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getItemCount() );
		assertTrue( null == ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getSelectedItem() );
		( (JComboBox) metawidgetContact.getComponent( "gender" ) ).setSelectedItem( Gender.FEMALE );

		buttonSave = (JButton) ( (SwingMetawidget) buttonsPanel.getComponent( 0 ) ).getComponent( 0 );
		assertEquals( "Save", buttonSave.getText() );
		buttonSave.getAction().actionPerformed( null );
		assertTrue( contactsTable.getRowCount() == 6 );

		// Check viewing

		contact = contactsController.load( 7 );
		assertEquals( "Miss Business Contact", contact.getFullname() );
		assertTrue( Gender.FEMALE == contact.getGender() );
		metawidgetContact.setReadOnly( true );
		assertEquals( "Female", ( (JLabel) metawidgetContact.getComponent( "gender" ) ).getText() );

		metawidgetContact.setReadOnly( false );
		assertTrue( Gender.FEMALE == ( (JComboBox) metawidgetContact.getComponent( "gender" ) ).getSelectedItem() );
	}

	public void testListTableModel()
		throws Exception {

		// Test nulls

		ListTableModel<Foo> model = new ListTableModel<Foo>( Foo.class, null );
		assertTrue( 0 == model.getColumnCount() );
		assertTrue( 0 == model.getRowCount() );
		assertTrue( null == model.getColumnClass( 0 ) );
		assertTrue( null == model.getValueAt( 0 ) );

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

		assertTrue( null == model.getValueAt( 1 ) );
		assertEquals( foo, model.getValueAt( 0 ) );
		assertEquals( "myFoo", model.getValueAt( 0, 0 ) );
		assertEquals( Boolean.TRUE, model.getValueAt( 0, 1 ) );
		assertTrue( null == model.getValueAt( 0, 2 ) );

		// Setters

		model.setValueAt( "myFoo1", 0, 0 );
		model.setValueAt( Boolean.FALSE, 0, 1 );
		model.setValueAt( new Object(), 0, 2 );
		model.setValueAt( "myFoo2", 1, 0 );
		assertEquals( "myFoo1", model.getValueAt( 0, 0 ) );
		assertEquals( Boolean.FALSE, model.getValueAt( 0, 1 ) );
		assertTrue( null == model.getValueAt( 0, 2 ) );
		assertTrue( null == model.getValueAt( 1, 0 ) );

		// Extra blank row

		model.setExtraBlankRow( true );
		model.setValueAt( "myFoo2", 1, 0 );
		assertEquals( "myFoo2", model.getValueAt( 1, 0 ) );
		assertTrue( 3 == model.getRowCount() );

		// Invalid column

		model = new ListTableModel<Foo>( Foo.class, fooList, "Baz" );

		try {
			model.getColumnClass( 0 );
			assertTrue( false );
		} catch ( Exception e ) {
			assertTrue( e.getCause() instanceof NoSuchMethodException );
			assertTrue( e.getCause().getMessage().contains( "Baz()" ) );
		}
	}

	//
	// Inner class
	//

	public static class Foo
		implements Comparable<Foo> {

		//
		//
		// Private members
		//
		//

		private String	mFoo	= "myFoo";

		private Boolean	mBar	= Boolean.TRUE;

		//
		//
		// Public methods
		//
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

		@Override
		public int compareTo( Foo that ) {

			return hashCode() - that.hashCode();
		}
	}
}
