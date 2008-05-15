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

package org.metawidget.example.swing.applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.swing.addressbook.ContactDialog;
import org.metawidget.example.swing.addressbook.ContactsControllerProvider;
import org.metawidget.example.swing.addressbook.ListTableModel;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class AddressBookApplet
	extends Applet
	implements ContactsControllerProvider
{
	//
	//
	// Private members
	//
	//

	ContactSearch				mContactSearch;

	ListTableModel<Contact>		mModel;

	ContactsController			mContactsController;

	//
	//
	// Constructor
	//
	//

	public AddressBookApplet()
	{
		// Table model

		mContactsController = new ContactsController();
		mContactSearch = new ContactSearch();
		mModel = new ListTableModel<Contact>( Contact.class, mContactsController.getAllByExample( mContactSearch ), "Fullname", "Communications", "Class" );

		// Search and result sections

		setLayout( new BorderLayout() );
		add( createSearchSection(), BorderLayout.NORTH );
		add( createResultsSection(), BorderLayout.CENTER );
	}

	public ContactsController getContactsController()
	{
		return mContactsController;
	}

	//
	//
	// Private methods
	//
	//

	private JComponent createSearchSection()
	{
		// Bundle

		ResourceBundle bundle = ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" );

		// Metawidget

		final SwingMetawidget metawidgetSearch = new SwingMetawidget();
		metawidgetSearch.setInspectorConfig( "org/metawidget/example/swing/addressbook/inspector-config.xml" );
		metawidgetSearch.setBundle( bundle );
		metawidgetSearch.setToInspect( mContactSearch );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setName( "buttons" );
		metawidgetSearch.add( facetButtons );

		JButton buttonSearch = new JButton( new AbstractAction( bundle.getString( "search" ) )
		{
			public void actionPerformed( ActionEvent e )
			{
				// Example of manual mapping. See ContactDialog for an example of using automatic Bindings

				mContactSearch.setFirstnames( (String) metawidgetSearch.getValue( "firstnames" ) );
				mContactSearch.setSurname( (String) metawidgetSearch.getValue( "surname" ) );

				String type = (String) metawidgetSearch.getValue( "type" );

				if ( type == null )
					mContactSearch.setType( null );
				else
					mContactSearch.setType( ContactType.valueOf( type ) );

				mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
			}
		} );

		facetButtons.add( buttonSearch );

		facetButtons.add( new JButton( new AbstractAction( bundle.getString( "addPersonal" ) )
		{
			public void actionPerformed( ActionEvent e )
			{
				Container owner = AddressBookApplet.this.getParent();

				if ( !( owner instanceof Frame ))
					owner = null;

				new ContactDialog( AddressBookApplet.this, new PersonalContact() ).setVisible( true );
				mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
			}
		} ) );

		facetButtons.add( new JButton( new AbstractAction( bundle.getString( "addBusiness" ) )
		{
			public void actionPerformed( ActionEvent e )
			{
				Container owner = AddressBookApplet.this.getParent();

				if ( !( owner instanceof Frame ))
					owner = null;

				new ContactDialog( AddressBookApplet.this, new BusinessContact() ).setVisible( true );
				mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
			}
		} ) );

		return metawidgetSearch;
	}

	private JComponent createResultsSection()
	{
		final JTable table = new JTable( mModel );
		table.setAutoCreateColumnsFromModel( true );

		final ImageIcon iconPersonal = new ImageIcon( AddressBookApplet.class.getResource( "/org/metawidget/example/shared/addressbook/media/personal-small.gif" ) );
		final ImageIcon iconBusiness = new ImageIcon( AddressBookApplet.class.getResource( "/org/metawidget/example/shared/addressbook/media/business-small.gif" ) );

		table.setDefaultRenderer( Set.class, new DefaultTableCellRenderer()
		{
			@Override
			public void setValue( Object value )
			{
				setText( CollectionUtils.toString( (Set<?>) value ) );
			}
		} );

		table.setDefaultRenderer( Class.class, new DefaultTableCellRenderer()
		{
			{
				setHorizontalAlignment( SwingConstants.CENTER );
			}

			@Override
			public void setValue( Object value )
			{
				if ( PersonalContact.class.equals( value ) )
					setIcon( iconPersonal );
				else
					setIcon( iconBusiness );
			}
		} );

		table.setRowHeight( 35 );
		table.setShowVerticalLines( false );
		table.setCellSelectionEnabled( false );
		table.setRowSelectionAllowed( true );
		table.getColumnModel().getColumn( 2 ).setMaxWidth( 60 );

		table.addMouseListener( new MouseAdapter()
		{
			@Override
			public void mouseClicked( MouseEvent event )
			{
				// When table is double clicked...

				if ( event.getClickCount() != 2 )
					return;

				// ...fetch the Contact...

				@SuppressWarnings( "unchecked" )
				ListTableModel<Contact> model = (ListTableModel<Contact>) table.getModel();
				Contact contact = model.getValueAt( table.getSelectedRow() );

				// ...and display it

				Container owner = AddressBookApplet.this.getParent();

				if ( !( owner instanceof Frame ))
					owner = null;

				new ContactDialog( AddressBookApplet.this, contact ).setVisible( true );
				mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
			}
		} );

		return new JScrollPane( table );
	}
}
