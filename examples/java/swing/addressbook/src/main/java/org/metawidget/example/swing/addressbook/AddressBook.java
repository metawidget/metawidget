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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
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
import org.metawidget.swing.layout.FlowLayout;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AddressBook {

	//
	// Private statics
	//

	private static final int		COMPONENT_SPACING	= 5;

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

	public AddressBook( Container container ) {

		// Nimbus look and feel (only available on JDK 6.0)
		//
		// Note: applies to both application and applet version of AddressBook

		try {
			for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() ) {
				if ( "Nimbus".equals( info.getName() ) ) {
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		} catch ( Exception e ) {
			// Okay to fail
		}

		container.setLayout( new BorderLayout() );

		// Model

		mContactSearch = new ContactSearch();
		mContactsController = new ContactsController();
		mModel = new ListTableModel<Contact>( Contact.class, mContactsController.getAllByExample( mContactSearch ), "Fullname", "Communications", "Class" );

		// Left-hand image

		JLabel label;
		URL url = Thread.currentThread().getContextClassLoader().getResource( "media/addressbook.gif" );

		if ( url != null ) {
			ImageIcon imageAddressBook = new ImageIcon( url );
			label = new JLabel( imageAddressBook );
		} else {
			label = new JLabel();
		}

		label.setVerticalAlignment( SwingConstants.TOP );
		label.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING * 2 ) );
		container.add( label, BorderLayout.WEST );

		// Right-hand panel

		JPanel panelRight = new JPanel();

		if ( container instanceof ImagePanel ) {
			panelRight.setOpaque( false );
		} else {
			panelRight.setBackground( Color.white );
		}

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
	public ContactsController getContactsController() {

		return mContactsController;
	}

	public void fireRefresh() {

		mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
	}

	@UiAction
	public void search() {

		// Example of manual mapping. See ContactDialog for an example of using automatic Bindings

		mContactSearch.setFirstname( (String) mSearchMetawidget.getValue( "firstname" ) );
		mContactSearch.setSurname( (String) mSearchMetawidget.getValue( "surname" ) );
		mContactSearch.setType( (ContactType) mSearchMetawidget.getValue( "type" ) );

		fireRefresh();
	}

	@UiAction
	@UiComesAfter( "search" )
	public void addPersonal() {

		new ContactDialog( AddressBook.this, new PersonalContact() ).setVisible( true );
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public void addBusiness() {

		new ContactDialog( AddressBook.this, new BusinessContact() ).setVisible( true );
	}

	//
	// Private methods
	//

	private JComponent createSearchSection() {

		// Metawidget

		mSearchMetawidget = new SwingMetawidget();
		mSearchMetawidget.setConfig( "org/metawidget/example/swing/addressbook/metawidget.xml" );
		mSearchMetawidget.setToInspect( mContactSearch );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setName( "buttons" );
		facetButtons.setOpaque( false );
		mSearchMetawidget.add( facetButtons );

		SwingMetawidget buttonsMetawidget = new SwingMetawidget();
		buttonsMetawidget.setConfig( "org/metawidget/example/swing/addressbook/metawidget.xml" );
		buttonsMetawidget.setToInspect( this );
		buttonsMetawidget.setMetawidgetLayout( new FlowLayout() );
		facetButtons.add( buttonsMetawidget );

		return mSearchMetawidget;
	}

	@SuppressWarnings( "serial" )
	private JComponent createResultsSection() {

		final JTable table = new JTable( mModel );
		table.setAutoCreateColumnsFromModel( true );

		table.setDefaultRenderer( Set.class, new DefaultTableCellRenderer() {

			@Override
			public void setValue( Object value ) {

				setText( CollectionUtils.toString( (Set<?>) value ) );
			}
		} );

		URL personalIconUrl = Thread.currentThread().getContextClassLoader().getResource( "media/personal-small.gif" );
		URL businessIconUrl = Thread.currentThread().getContextClassLoader().getResource( "media/business-small.gif" );

		if ( personalIconUrl != null && businessIconUrl != null ) {
			final ImageIcon iconPersonal = new ImageIcon( personalIconUrl );
			final ImageIcon iconBusiness = new ImageIcon( businessIconUrl );

			table.setDefaultRenderer( Class.class, new DefaultTableCellRenderer() {

				{
					setHorizontalAlignment( SwingConstants.CENTER );
				}

				@Override
				public void setValue( Object value ) {

					if ( PersonalContact.class.equals( value ) ) {
						setIcon( iconPersonal );
					} else {
						setIcon( iconBusiness );
					}
				}
			} );
		}

		table.setRowHeight( 35 );
		table.setShowVerticalLines( false );
		table.setCellSelectionEnabled( false );
		table.setRowSelectionAllowed( true );
		table.getColumnModel().getColumn( 2 ).setMaxWidth( 60 );

		table.addMouseListener( new MouseAdapter() {

			@Override
			public void mouseClicked( MouseEvent event ) {

				// When table is double clicked...

				if ( event.getClickCount() != 2 ) {
					return;
				}

				// ...fetch the Contact...

				@SuppressWarnings( "unchecked" )
				ListTableModel<Contact> model = (ListTableModel<Contact>) table.getModel();
				Contact contact = model.getValueAt( table.getSelectedRow() );

				// ...and display it

				createContactDialog( contact ).setVisible( true );
			}
		} );

		return new JScrollPane( table );
	}

	/* package private */ContactDialog createContactDialog( Contact contact ) {

		// Defensive copy (otherwise unsaved changes in the dialog appear in the summary list)

		if ( contact instanceof PersonalContact ) {
			return new ContactDialog( AddressBook.this, new PersonalContact( (PersonalContact) contact ) );
		}

		return new ContactDialog( AddressBook.this, new BusinessContact( (BusinessContact) contact ) );
	}
}
