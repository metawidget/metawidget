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

package org.metawidget.test.example.swing.addressbook;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import junit.framework.TestCase;

import org.metawidget.MetawidgetException;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.swing.addressbook.AddressBook;
import org.metawidget.example.swing.addressbook.ContactDialog;
import org.metawidget.example.swing.addressbook.ImagePanel;
import org.metawidget.example.swing.addressbook.ListTableModel;
import org.metawidget.example.swing.addressbook.MainFrame;
import org.metawidget.example.swing.addressbook.converter.DateConverter;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.SwingUtils;

/**
 * @author Richard Kennard
 */

public class SwingAddressBookTest
	extends TestCase
{
	//
	//
	// Public methods
	//
	//

	public void testAddressBook()
		throws Exception
	{
		// Set Locale because we will be checking date formatting

		Locale.setDefault( Locale.UK );

		// Start app

		MainFrame frame = new MainFrame();
		AddressBook addressBook = frame.getAddressBook();
		JPanel panelRight = (JPanel) ((ImagePanel) frame.getContentPane().getComponent( 0 )).getComponent( 1 );
		assertTrue( !panelRight.isOpaque() );
		assertTrue( ((JTable) ((JScrollPane) panelRight.getComponent( 1 )).getViewport().getView()).getRowCount() == 6 );

		// Check searching

		SwingMetawidget metawidgetSearch = (SwingMetawidget) panelRight.getComponent( 0 );

		metawidgetSearch.setValue( "Simpson", "surname" );
		metawidgetSearch.setValue( ContactType.PERSONAL, "type" );

		assertTrue( metawidgetSearch.getComponentCount() == 7 );
		JPanel panelButtons = (JPanel) metawidgetSearch.getComponent( metawidgetSearch.getComponentCount() - 1 );
		JButton buttonSearch = (JButton) panelButtons.getComponent( 0 );
		assertTrue( "Search".equals( buttonSearch.getText() ) );

		buttonSearch.getAction().actionPerformed( null );
		ContactsController contactsController = addressBook.getContactsController();
		assertTrue( contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size() == 2 );

		// Open dialog for Personal Contact

		Contact contact = contactsController.load( 1 );
		assertTrue( "Mr Homer Simpson".equals( contact.getFullname() ) );
		assertTrue( "Mr Homer Simpson".equals( contact.toString() ) );
		assertTrue( 1 == contact.hashCode() );
		assertTrue( !contact.equals( new Object() ) );
		assertTrue( contact.equals( contact ) );
		assertTrue( contact.compareTo( null ) == -1 );
		assertTrue( contact.compareTo( contact ) == 0 );
		assertTrue( "742 Evergreen Terrace".equals( contact.getAddress().getStreet() ) );
		assertTrue( contact.getCommunications().size() == 1 );

		ContactDialog dialog = new ContactDialog( addressBook, contact );

		// Check loading

		SwingMetawidget metawidgetContact = (SwingMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );
		assertTrue( "Homer".equals( metawidgetContact.getValue( "firstnames" ) ) );
		assertTrue( SwingUtils.findComponentNamed( metawidgetContact, "firstnames" ) instanceof JLabel );
		assertTrue( "12/05/56".equals( metawidgetContact.getValue( "dateOfBirth" ) ) );

		try
		{
			metawidgetContact.getValue( "bad-value" );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			// Should throw MetawidgetException
		}

		JScrollPane scrollPane = (JScrollPane) SwingUtils.findComponentNamed( metawidgetContact, "communications" );
		JTable communications = (JTable) scrollPane.getViewport().getView();
		TableModel model = communications.getModel();

		assertTrue( model.getRowCount() == 1 );

		// Check painting (in that it at least doesn't NullPointer)

		Graphics graphics = new BufferedImage( 100, 100, BufferedImage.TYPE_INT_ARGB ).getGraphics();
		( (ImagePanel) ((JPanel) ((JLayeredPane) ((JRootPane) dialog.getComponent( 0 )).getComponent( 1 )).getComponent( 0 )).getComponent( 0 ) ).paint( graphics );

		// Check editing

		assertTrue( metawidgetContact.getComponentCount() == 19 );
		panelButtons = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonEdit = (JButton) panelButtons.getComponent( 2 );
		assertTrue( "Edit".equals( buttonEdit.getText() ) );
		buttonEdit.getAction().actionPerformed( null );

		metawidgetContact.setValue( "Sapien", "surname" );
		assertTrue( metawidgetContact.getComponentCount() == 19 );

		// Check editing a communication

		assertTrue( model.getRowCount() == 2 );

		@SuppressWarnings( "unchecked" )
		ListTableModel<Communication> communicationModel = (ListTableModel<Communication>) model;

		communicationModel.importCollection( contact.getCommunications() );
		TableCellEditor editor = communications.getDefaultEditor( Object.class );

		SwingMetawidget metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 0, 0 ), true, 0, 0 );
		JComboBox combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		assertTrue( "Telephone".equals( combo.getSelectedItem() ) );

		// Check adding a communication

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 0 ), true, 1, 0 );
		combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		combo.setSelectedItem( "Mobile" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 0 );

		metawidgetCommunications = (SwingMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 1 ), true, 1, 1 );
		JTextField textField = (JTextField) metawidgetCommunications.getComponent( 0 );
		textField.setText( "(0402) 123 456" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 1 );

		// Check saving

		metawidgetContact.setValue( "foo", "dateOfBirth" );

		try
		{
			metawidgetContact.save();
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertTrue( "Unparseable date: \"foo\"".equals( e.getCause().getCause().getMessage() ) );
		}

		metawidgetContact.setValue( "12/05/57", "dateOfBirth" );
		panelButtons = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonSave = (JButton) panelButtons.getComponent( 0 );
		assertTrue( "Save".equals( buttonSave.getText() ) );
		buttonSave.getAction().actionPerformed( null );

		assertTrue( "Sapien".equals( contact.getSurname() ) );
		assertTrue( new DateConverter().convertReverse( "12/05/57" ).equals( ( (PersonalContact) contact ).getDateOfBirth() ) );
		assertTrue( ( (PersonalContact) contact ).getDateOfBirth().getTime() == -398944800000l );

		Iterator<Communication> iterator = contact.getCommunications().iterator();
		iterator.next();
		Communication communication = iterator.next();
		assertTrue( "Mobile".equals( communication.getType() ) );
		assertTrue( "(0402) 123 456".equals( communication.getValue() ) );
		assertTrue( communication.equals( communication ) );
		assertTrue( !communication.equals( new Object() ) );
		assertTrue( communication.compareTo( null ) == -1 );
		assertTrue( communication.compareTo( communication ) == 0 );

		// Check deleting the communication again

		assertTrue( contact.removeCommunication( new Communication() ) == false );
		contact.removeCommunication( communicationModel.getValueAt( 1 ) );
		assertTrue( contact.getCommunications().size() == 1 );

		// Open dialog for Business Contact

		contact = contactsController.load( 6 );
		assertTrue( "Mr Charles Montgomery Burns".equals( contact.getFullname() ) );

		// Check re-loading

		metawidgetContact.setToInspect( contact );
		assertTrue( "Charles Montgomery".equals( metawidgetContact.getValue( "firstnames" ) ) );
		assertTrue( 0 == (Integer) metawidgetContact.getValue( "numberOfStaff" ) );

		// Check saving

		metawidgetContact.setValue( 2, "numberOfStaff" );
		metawidgetContact.setValue( "A Company", "company" );

		assertTrue( metawidgetContact.getComponentCount() == 21 );
		buttonSave.getAction().actionPerformed( null );

		assertTrue( 2 == ( (BusinessContact) contact ).getNumberOfStaff() );
		assertTrue( "A Company".equals( ( (BusinessContact) contact ).getCompany() ) );

		// Check deleting

		JButton buttonDelete = (JButton) panelButtons.getComponent( 1 );
		assertTrue( "Delete".equals( buttonDelete.getText() ) );

		assertTrue( contactsController.getAllByExample( null ).size() == 6 );
		buttonDelete.getAction().actionPerformed( null );
		assertTrue( contactsController.getAllByExample( null ).size() == 5 );
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public SwingAddressBookTest( String name )
	{
		super( name );
	}
}
