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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableCellRenderer;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class AddressBook
	implements ContactsControllerProvider
{
	//
	// Private statics
	//

	private final static int		COMPONENT_SPACING	= 5;

	//
	// Private members
	//

	private ContactSearch			mContactSearch;

	private SwingMetawidget			mSearchMetawidget;

	private ListTableModel<Contact>	mModel;

	private ContactsController		mContactsController;

	//
	// Constructor
	//

	public AddressBook( Container container )
	{
		// Nimbus look and feel (if available)
		//
		// Note: applies to both application and applet version of AddressBook

		try
		{
			for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
			{
				if ( "Nimbus".equals( info.getName() ) )
				{
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		}
		catch ( Exception e )
		{
			// Okay to fail
		}

		container.setLayout( new BorderLayout() );

		// Table model

		mContactSearch = new ContactSearch();
		mContactsController = new ContactsController();
		mModel = new ListTableModel<Contact>( Contact.class, mContactsController.getAllByExample( mContactSearch ), "Fullname", "Communications", "Class" );

		// Left-hand image

		JLabel label;
		URL url = Thread.currentThread().getContextClassLoader().getResource( "org/metawidget/example/shared/addressbook/media/addressbook.gif" );

		if ( url != null )
		{
			ImageIcon imageAddressBook = new ImageIcon( url );
			label = new JLabel( imageAddressBook );
		}
		else
		{
			label = new JLabel();
		}

		label.setVerticalAlignment( SwingConstants.TOP );
		label.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING * 2 ) );
		container.add( label, BorderLayout.WEST );

		// Right-hand panel

		JPanel panelRight = new JPanel();

		if ( container instanceof ImagePanel )
			panelRight.setOpaque( false );
		else
			panelRight.setBackground( Color.white );

		panelRight.setLayout( new BorderLayout() );
		panelRight.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING ) );
		container.add( panelRight, BorderLayout.CENTER );

		// Search and result sections

		panelRight.add( createSearchSection(), BorderLayout.NORTH );
		panelRight.add( createResultsSection(), BorderLayout.CENTER );
	}

	//
	// Public methods
	//

	@UiHidden
	public ContactsController getContactsController()
	{
		return mContactsController;
	}

	@Override
	public void fireRefresh()
	{
		mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
	}

	@UiAction
	public void search()
	{
		// Example of manual mapping. See ContactDialog for an example of using automatic Bindings

		mContactSearch.setFirstname( (String) mSearchMetawidget.getValue( "firstname" ) );
		mContactSearch.setSurname( (String) mSearchMetawidget.getValue( "surname" ) );

		String type = (String) mSearchMetawidget.getValue( "type" );

		if ( type == null )
			mContactSearch.setType( null );
		else
			mContactSearch.setType( ContactType.valueOf( type ) );

		fireRefresh();
	}

	@UiAction
	@UiComesAfter( "search" )
	public void addPersonal()
	{
		new ContactDialog( AddressBook.this, new PersonalContact() ).setVisible( true );
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public void addBusiness()
	{
		new ContactDialog( AddressBook.this, new BusinessContact() ).setVisible( true );
	}

	//
	// Private methods
	//

	private JComponent createSearchSection()
	{
		// Bundle

		ResourceBundle bundle = ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" );

		// Metawidget

		mSearchMetawidget = new SwingMetawidget();
		mSearchMetawidget.setInspectorConfig( "org/metawidget/example/swing/addressbook/inspector-config.xml" );
		mSearchMetawidget.setBundle( bundle );
		mSearchMetawidget.setToInspect( mContactSearch );
		mSearchMetawidget.setOpaque( false );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setName( "buttons" );
		facetButtons.setOpaque( false );
		mSearchMetawidget.add( facetButtons );

		SwingMetawidget buttonsMetawidget = new SwingMetawidget();
		buttonsMetawidget.setInspectorConfig( "org/metawidget/example/swing/addressbook/inspector-config.xml" );
		buttonsMetawidget.setBundle( bundle );
		buttonsMetawidget.setToInspect( this );
		buttonsMetawidget.setLayoutClass( null );
		facetButtons.add( buttonsMetawidget );

		return mSearchMetawidget;
	}

	@SuppressWarnings( "serial" )
	private JComponent createResultsSection()
	{
		final JTable table = new JTable( mModel );
		table.setAutoCreateColumnsFromModel( true );

		table.setDefaultRenderer( Set.class, new DefaultTableCellRenderer()
		{
			@Override
			public void setValue( Object value )
			{
				setText( CollectionUtils.toString( (Set<?>) value ) );
			}
		} );

		URL personalIconUrl = Thread.currentThread().getContextClassLoader().getResource( "org/metawidget/example/shared/addressbook/media/personal-small.gif" );
		URL businessIconUrl = Thread.currentThread().getContextClassLoader().getResource( "org/metawidget/example/shared/addressbook/media/business-small.gif" );

		if ( personalIconUrl != null && businessIconUrl != null )
		{
			final ImageIcon iconPersonal = new ImageIcon( personalIconUrl );
			final ImageIcon iconBusiness = new ImageIcon( businessIconUrl );

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
		}

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

				new ContactDialog( AddressBook.this, contact ).setVisible( true );
			}
		} );

		return new JScrollPane( table );
	}
}
