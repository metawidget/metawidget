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
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
// be used to endorse or promote products derived from this software without
// specific prior written permission.
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

package org.metawidget.example.vaadin.addressbook;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.ResourceBundle;

import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.vaadin.ui.Facet;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.layout.HorizontalLayout;
import org.metawidget.vaadin.ui.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Dialog box for Address Book Contacts.
 *
 * @author Loghman Barari
 */

public class ContactDialog
	extends Window {

	//
	// Private members
	//

	private AddressBookUI					mAddressBook;

	/* package private */VaadinMetawidget	mContactMetawidget;

	/* package private */Table				mCommunicationsTable;

	private com.vaadin.ui.HorizontalLayout	mCommunicationsButtons;

	private VaadinMetawidget				mButtonsMetawidget;

	//
	// Constructor
	//

	public ContactDialog( AddressBookUI addressBook, final Contact contact ) {

		mAddressBook = addressBook;

		setHeight( "600px" );
		setWidth( "800px" );

		CustomLayout body = new CustomLayout( "contact" );
		setContent( body );

		// Bundle

		ResourceBundle bundle = ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 ) {
			builder.append( " - " );
		}

		// Personal/business icon

		if ( contact instanceof PersonalContact ) {
			builder.append( bundle.getString( "personalContact" ) );
			body.addComponent( new Embedded( null, new ThemeResource( "../addressbook/img/personal.gif" ) ), "icon" );
		} else {
			builder.append( bundle.getString( "businessContact" ) );
			body.addComponent( new Embedded( null, new ThemeResource( "../addressbook/img/business.gif" ) ), "icon" );
		}

		setCaption( builder.toString() );

		// Metawidget

		mContactMetawidget = new VaadinMetawidget();
		mContactMetawidget.setWidth( "100%" );
		mContactMetawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		mContactMetawidget.setReadOnly( contact.getId() != 0 );
		mContactMetawidget.setToInspect( contact );

		// Communications override

		final TableDataSource<Communication> tableDataSource = new TableDataSource<Communication>( Communication.class, contact.getCommunications(), "type", "value" );
		mCommunicationsTable = new Table();
		mCommunicationsTable.setWidth( "100%" );
		mCommunicationsTable.setHeight( "170px" );

		final Button deleteButton = new Button( "Delete" );
		deleteButton.setEnabled( false );
		deleteButton.addClickListener( new ClickListener() {

			@Override
			@SuppressWarnings( "unchecked" )
			public void buttonClick( ClickEvent event ) {

				Communication communication = tableDataSource.getDataRow( mCommunicationsTable.getValue() );
				contact.removeCommunication( communication );

				( (TableDataSource<Communication>) mCommunicationsTable.getContainerDataSource() ).importCollection( contact.getCommunications() );
			}
		} );

		Button addNewButton = new Button( "Add" );
		addNewButton.addClickListener( new ClickListener() {

			@Override
			public void buttonClick( ClickEvent event ) {

				mCommunicationsTable.setValue( null );
				deleteButton.setEnabled( false );

				CommunicationDialog communicationDialog = new CommunicationDialog( ContactDialog.this, new Communication() );
				communicationDialog.setModal( true );
				( (UI) getParent() ).addWindow( communicationDialog );
			}
		} );

		mCommunicationsTable.setSelectable( false );
		mCommunicationsTable.addItemClickListener( new ItemClickListener() {

			@Override
			public void itemClick( ItemClickEvent event ) {

				if ( !mCommunicationsTable.isSelectable() ) {
					return;
				}

				if ( !event.isDoubleClick() ) {
					deleteButton.setEnabled( true );
					return;
				}

				deleteButton.setEnabled( false );

				Communication communication = tableDataSource.getDataRow( event.getItemId() );
				CommunicationDialog communicationDialog = new CommunicationDialog( ContactDialog.this, communication );
				communicationDialog.setModal( true );
				( (ComponentContainer) getParent() ).addComponent( communicationDialog );
			}
		} );

		mCommunicationsButtons = new com.vaadin.ui.HorizontalLayout();
		mCommunicationsButtons.setVisible( !mContactMetawidget.isReadOnly() );
		mCommunicationsButtons.setMargin( false );
		mCommunicationsButtons.setSpacing( true );
		mCommunicationsButtons.addComponent( addNewButton );
		mCommunicationsButtons.addComponent( deleteButton );

		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setData( "communications" );
		wrapper.addComponent( mCommunicationsTable );
		wrapper.addComponent( mCommunicationsButtons );
		wrapper.setComponentAlignment( mCommunicationsButtons, Alignment.MIDDLE_CENTER );
		mContactMetawidget.addComponent( wrapper );

		mCommunicationsTable.setContainerDataSource( tableDataSource );
		body.addComponent( mContactMetawidget, "pagebody" );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setData( "buttons" );
		facetButtons.setWidth( "100%" );
		mContactMetawidget.addComponent( facetButtons );

		mButtonsMetawidget = new VaadinMetawidget();
		mButtonsMetawidget.setWidth( null );
		mButtonsMetawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		mButtonsMetawidget.setLayout( new HorizontalLayout() );
		mButtonsMetawidget.setToInspect( this );
		( (VerticalLayout) facetButtons.getContent() ).addComponent( mButtonsMetawidget );
		( (com.vaadin.ui.VerticalLayout) facetButtons.getContent() ).setComponentAlignment( mButtonsMetawidget, Alignment.MIDDLE_CENTER );
	}

	//
	// Public methods
	//

	@UiHidden
	public boolean isContactReadOnly() {

		return mContactMetawidget.isReadOnly();
	}

	@UiAction
	@UiAttribute( name = HIDDEN, value = "${!this.contactReadOnly}" )
	public void edit() {

		mContactMetawidget.setReadOnly( false );
		mCommunicationsTable.setSelectable( true );
		mCommunicationsButtons.setVisible( true );
		mButtonsMetawidget.setToInspect( mButtonsMetawidget.getToInspect() );

		// Force a rebuild, else we will rebuild during 'response already being written'

		mContactMetawidget.getComponentCount();
	}

	@UiAction
	@UiAttribute( name = HIDDEN, value = "${this.contactReadOnly}" )
	public void save() {

		try {
			mContactMetawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( mContactMetawidget );
			Contact contact = mContactMetawidget.getToInspect();
			mAddressBook.getContactsController().save( contact );
		} catch ( Exception e ) {
			if ( Page.getCurrent() != null ) {
				Notification.show( "Save Error", e.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE );
			}
			return;
		}

		if ( getParent() != null ) {
			( (UI) getParent() ).removeWindow( this );
		}
		mAddressBook.fireRefresh();
	}

	@UiAction
	@UiComesAfter( "save" )
	@UiAttribute( name = HIDDEN, value = "${this.contactReadOnly || this.newContact}" )
	public void delete() {

		Contact contact = mContactMetawidget.getToInspect();

		if ( getParent() != null ) {
			( (UI) getParent() ).removeWindow( this );
		}

		mAddressBook.getContactsController().delete( contact );
		mAddressBook.fireRefresh();
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiAttribute( name = LABEL, value = "${if ( this.contactReadOnly ) 'Back'}" )
	public void cancel() {

		( (UI) getParent() ).removeWindow( this );
	}

	@SuppressWarnings( "unchecked" )
	public void addCommunication( Communication communication ) {

		Contact contact = (Contact) mContactMetawidget.getToInspect();
		contact.addCommunication( communication );

		( (TableDataSource<Communication>) mCommunicationsTable.getContainerDataSource() ).importCollection( contact.getCommunications() );
	}
}
