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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class MainFrame
	extends JFrame
{
	//
	//
	// Private statics
	//
	//

	private final static long	serialVersionUID	= -50915934635254102L;

	private final static int	COMPONENT_SPACING	= 5;

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

	public MainFrame( ContactsController contactsController )
	{
		super( "Address Book (Metawidget Swing Example)" );

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		getContentPane().setBackground( Color.white );

		// Background

		ImagePanel panelBackground = new ImagePanel();
		panelBackground.setImage( Main.class.getResource( "/org/metawidget/example/shared/addressbook/media/background.jpg" ) );
		panelBackground.setLayout( new BorderLayout() );
		add( panelBackground );

		// Table model

		mContactSearch = new ContactSearch();
		mModel = new ListTableModel<Contact>( Contact.class, contactsController.getAllByExample( mContactSearch ), "Fullname", "Communications", "Class" );
		mContactsController = contactsController;

		// Left-hand image

		ImageIcon imageAddressBook = new ImageIcon( Main.class.getResource( "/org/metawidget/example/shared/addressbook/media/addressbook.gif" ) );
		JLabel label = new JLabel( imageAddressBook );
		label.setVerticalAlignment( SwingConstants.TOP );
		label.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING * 2 ) );
		panelBackground.add( label, BorderLayout.WEST );

		// Right-hand panel

		JPanel panelRight = new JPanel();
		panelRight.setOpaque( false );
		panelRight.setLayout( new BorderLayout() );
		panelRight.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING ) );
		panelBackground.add( panelRight, BorderLayout.CENTER );

		// Search and result sections

		panelRight.add( createSearchSection(), BorderLayout.NORTH );
		panelRight.add( createResultsSection(), BorderLayout.CENTER );
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
		metawidgetSearch.setOpaque( false );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setName( "buttons" );
		facetButtons.setOpaque( false );
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
		getRootPane().setDefaultButton( buttonSearch );

		facetButtons.add( new JButton( new AbstractAction( bundle.getString( "addPersonal" ) )
		{
			public void actionPerformed( ActionEvent e )
			{
				new ContactDialog( MainFrame.this, mContactsController, new PersonalContact() ).setVisible( true );
				mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
			}
		} ) );

		facetButtons.add( new JButton( new AbstractAction( bundle.getString( "addBusiness" ) )
		{
			public void actionPerformed( ActionEvent e )
			{
				new ContactDialog( MainFrame.this,  mContactsController, new BusinessContact() ).setVisible( true );
				mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
			}
		} ) );

		return metawidgetSearch;
	}

	private JComponent createResultsSection()
	{
		final JTable table = new JTable( mModel );
		table.setAutoCreateColumnsFromModel( true );

		final ImageIcon iconPersonal = new ImageIcon( Main.class.getResource( "/org/metawidget/example/shared/addressbook/media/personal-small.gif" ) );
		final ImageIcon iconBusiness = new ImageIcon( Main.class.getResource( "/org/metawidget/example/shared/addressbook/media/business-small.gif" ) );

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

				new ContactDialog( MainFrame.this, mContactsController, contact ).setVisible( true );
				mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
			}
		} );

		return new JScrollPane( table );
	}
}
