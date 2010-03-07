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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.swt.SwtMetawidgetTests;

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
		/*
		JPanel panelRight = (Label)
		assertTrue( !panelRight.isOpaque() );
		JTable contactsTable = ( (JTable) ( (JScrollPane) panelRight.getComponent( 1 ) ).getViewport().getView() );
		assertTrue( contactsTable.getRowCount() == 6 );

		// Check searching

		SwtMetawidget metawidgetSearch = (SwtMetawidget) panelRight.getComponent( 0 );

		metawidgetSearch.setValue( "Simpson", "surname" );
		metawidgetSearch.setValue( ContactType.PERSONAL, "type" );

		assertTrue( metawidgetSearch.getComponentCount() == 8 );
		JPanel panelButtons = (JPanel) metawidgetSearch.getComponent( metawidgetSearch.getComponentCount() - 1 );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( panelButtons ).gridx == -1 );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( panelButtons ).gridy == 4 );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( panelButtons ).fill == GridBagConstraints.BOTH );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( panelButtons ).anchor == GridBagConstraints.WEST );
		assertTrue( ( (GridBagLayout) metawidgetSearch.getLayout() ).getConstraints( panelButtons ).gridwidth == GridBagConstraints.REMAINDER );
		assertTrue( ((Container) panelButtons.getComponent( 0 )).getLayout() instanceof FlowLayout );
		JButton buttonSearch = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 0 );
		assertTrue( "Search".equals( buttonSearch.getText() ) );

		buttonSearch.getAction().actionPerformed( null );
		assertTrue( ContactType.PERSONAL == ((JComboBox) metawidgetSearch.getComponent( "type" )).getSelectedItem() );

		ContactsController contactsController = addressBook.getContactsController();
		assertTrue( contactsController.getAllByExample( (ContactSearch) metawidgetSearch.getToInspect() ).size() == 2 );

		// Open dialog for Personal Contact

		Contact contact = contactsController.load( 1 );
		assertTrue( "Mr Homer Simpson".equals( contact.getFullname() ) );
		assertTrue( "Mr Homer Simpson".equals( contact.toString() ) );
		assertTrue( 32 == contact.hashCode() );
		assertTrue( !contact.equals( new Object() ) );
		assertTrue( contact.equals( contact ) );
		assertTrue( contact.compareTo( null ) == -1 );
		assertTrue( contact.compareTo( contact ) == 0 );
		assertTrue( "742 Evergreen Terrace".equals( contact.getAddress().getStreet() ) );
		assertTrue( contact.getCommunications().size() == 1 );

		ContactDialog dialog = new ContactDialog( addressBook, contact );
		dialog.setShowConfirmDialog( false );

		// Check loading

		SwtMetawidget metawidgetContact = (SwtMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );
		assertTrue( "Homer".equals( metawidgetContact.getValue( "firstname" ) ) );
		assertTrue( metawidgetContact.getComponent( "firstname" ) instanceof JLabel );
		assertTrue( "Male".equals( ((JLabel) metawidgetContact.getComponent( "gender" )).getText() ));
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

		JScrollPane scrollPane = metawidgetContact.getComponent( "communications" );
		JTable communications = (JTable) scrollPane.getViewport().getView();
		TableModel model = communications.getModel();

		assertTrue( model.getRowCount() == 1 );

		// Check painting (in that it at least doesn't NullPointer)

		Graphics graphics = new BufferedImage( 100, 100, BufferedImage.TYPE_INT_ARGB ).getGraphics();
		( (ImagePanel) ( (JPanel) ( (JLayeredPane) ( (JRootPane) dialog.getComponent( 0 ) ).getComponent( 1 ) ).getComponent( 0 ) ).getComponent( 0 ) ).paint( graphics );

		// Check editing

		assertTrue( metawidgetContact.getComponentCount() == 19 );
		panelButtons = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		assertTrue( ((Container) panelButtons.getComponent( 0 )).getLayout() instanceof FlowLayout );
		JButton buttonEdit = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 0 );
		assertTrue( "Edit".equals( buttonEdit.getText() ) );
		buttonEdit.getAction().actionPerformed( null );

		assertTrue( Gender.MALE == ((JComboBox) metawidgetContact.getComponent( "gender" )).getSelectedItem() );
		metawidgetContact.setValue( "Sapien", "surname" );
		assertTrue( metawidgetContact.getComponentCount() == 19 );

		// Check editing a communication

		assertTrue( model.getRowCount() == 2 );

		@SuppressWarnings( "unchecked" )
		ListTableModel<Communication> communicationModel = (ListTableModel<Communication>) model;

		communicationModel.importCollection( contact.getCommunications() );
		TableCellEditor editor = communications.getDefaultEditor( Object.class );

		SwtMetawidget metawidgetCommunications = (SwtMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 0, 0 ), true, 0, 0 );
		assertTrue( BoxLayout.class.equals( metawidgetCommunications.getLayout().getClass() ) );
		JComboBox combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		assertTrue( "Telephone".equals( combo.getSelectedItem() ) );

		// Check adding a communication

		metawidgetCommunications = (SwtMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 0 ), true, 1, 0 );
		combo = (JComboBox) metawidgetCommunications.getComponent( 0 );
		combo.setSelectedItem( "Mobile" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 0 );

		metawidgetCommunications = (SwtMetawidget) editor.getTableCellEditorComponent( communications, model.getValueAt( 1, 1 ), true, 1, 1 );
		JTextField textField = (JTextField) metawidgetCommunications.getComponent( 0 );
		textField.setText( "(0402) 123 456" );
		editor.stopCellEditing();
		model.setValueAt( editor.getCellEditorValue(), 1, 1 );

		// Check saving

		metawidgetContact.setValue( "foo", "dateOfBirth" );

		try
		{
			metawidgetContact.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidgetContact );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertTrue( "Unparseable date: \"foo\"".equals( e.getCause().getCause().getMessage() ) );
		}

		metawidgetContact.setValue( "12/05/57", "dateOfBirth" );
		panelButtons = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonSave = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 0 );
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

		// Search everything

		metawidgetSearch.setValue( null, "surname" );
		metawidgetSearch.setValue( null, "type" );
		buttonSearch.getAction().actionPerformed( null );
		assertTrue( contactsTable.getRowCount() == 6 );

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertTrue( "Mr Charles Montgomery Burns".equals( contact.getFullname() ) );
		dialog = new ContactDialog( addressBook, contact );
		metawidgetContact = (SwtMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );

		panelButtons = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		JButton buttonBack = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 1 );
		assertTrue( "Back".equals( buttonBack.getText() ) );
		buttonEdit = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 0 );
		assertTrue( "Edit".equals( buttonEdit.getText() ) );
		buttonEdit.getAction().actionPerformed( null );
		assertTrue( "Charles Montgomery".equals( metawidgetContact.getValue( "firstname" ) ) );
		assertTrue( Gender.MALE == metawidgetContact.getValue( "gender" ) );
		assertTrue( 0 == (Integer) metawidgetContact.getValue( "numberOfStaff" ) );

		// Check saving

		metawidgetContact.setValue( 2, "numberOfStaff" );
		metawidgetContact.setValue( "A Company", "company" );

		assertTrue( metawidgetContact.getComponentCount() == 21 );
		buttonSave = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 0 );
		buttonSave.getAction().actionPerformed( null );

		assertTrue( 2 == ( (BusinessContact) contact ).getNumberOfStaff() );
		assertTrue( "A Company".equals( ( (BusinessContact) contact ).getCompany() ) );

		// Check deleting

		JButton buttonDelete = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 1 );
		assertTrue( "Delete".equals( buttonDelete.getText() ) );
		JButton buttonCancel = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 2 );
		assertTrue( "Cancel".equals( buttonCancel.getText() ) );

		assertTrue( contactsController.getAllByExample( null ).size() == 6 );
		buttonDelete.getAction().actionPerformed( null );
		assertTrue( contactsController.getAllByExample( null ).size() == 5 );
		assertTrue( contactsTable.getRowCount() == 5 );

		// Open dialog for new Personal Contact

		dialog = new ContactDialog( addressBook, new PersonalContact() );
		metawidgetContact = (SwtMetawidget) ( (Container) dialog.getContentPane().getComponent( 0 ) ).getComponent( 1 );
		panelButtons = (JPanel) metawidgetContact.getComponent( metawidgetContact.getComponentCount() - 1 );
		buttonCancel = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 1 );
		assertTrue( "Cancel".equals( buttonCancel.getText() ) );

		// Check saving doesn't error on null date

		metawidgetContact.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidgetContact );

		// Check adding

		assertTrue( "Mr".equals( ((JComboBox) metawidgetContact.getComponent( "title" )).getItemAt( 0 )));
		assertTrue( 5 == ((JComboBox) metawidgetContact.getComponent( "title" )).getItemCount() );
		metawidgetContact.setValue( "Miss", "title" );
		metawidgetContact.setValue( "Business", "firstname" );
		metawidgetContact.setValue( "Contact", "surname" );
		assertTrue( 3 == ((JComboBox) metawidgetContact.getComponent( "gender" )).getItemCount() );
		assertTrue( null == ((JComboBox) metawidgetContact.getComponent( "gender" )).getSelectedItem() );
		((JComboBox) metawidgetContact.getComponent( "gender" )).setSelectedItem( Gender.FEMALE );

		buttonSave = (JButton) ((SwtMetawidget) panelButtons.getComponent( 0 )).getComponent( 0 );
		assertTrue( "Save".equals( buttonSave.getText() ) );
		buttonSave.getAction().actionPerformed( null );
		assertTrue( contactsTable.getRowCount() == 6 );

		// Check viewing

		contact = contactsController.load( 7 );
		assertTrue( "Miss Business Contact".equals( contact.getFullname() ) );
		assertTrue( Gender.FEMALE == contact.getGender() );
		metawidgetContact.setReadOnly( true );
		assertTrue( "Female".equals( ((JLabel) metawidgetContact.getComponent( "gender" )).getText() ));

		metawidgetContact.setReadOnly( false );
		assertTrue( Gender.FEMALE == ((JComboBox) metawidgetContact.getComponent( "gender" )).getSelectedItem() );
		 */
	}
}
