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

package org.metawidget.example.vaadin.addressbook;

import java.util.Collection;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.util.CollectionUtils;
import org.metawidget.vaadin.ui.Facet;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.layout.HorizontalLayout;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Loghman Barari
 */

public class AddressBook
	extends Application {

	//
	// Private members
	//

	private ContactSearch							mContactSearch;

	private VaadinMetawidget						mSearchMetawidget;

	/* package private */TableDataSource<Contact>	mModel;

	private ContactsController						mContactsController;

	/* package private */ComponentContainer			mContent;

	//
	// Constructor
	//

	public AddressBook() {

		// Model

		mContactSearch = new ContactSearch();
		mContactsController = new ContactsController();

		mModel = new TableDataSource<Contact>( Contact.class, mContactsController.getAllByExample( mContactSearch ), "class", "fullname", "communications" ) {

			@Override
			protected Class<?> getColumnType( String column ) {

				if ( "class".equals( column ) ) {
					return ThemeResource.class;
				}

				if ( "communications".equals( column ) ) {
					return String.class;
				}

				return super.getColumnType( column );
			}

			@Override
			protected Object getValue( Contact contact, String column ) {

				Object value = super.getValue( contact, column );

				if ( value instanceof Class<?> ) {

					if ( value.equals( BusinessContact.class ) ) {
						return new ThemeResource( "../addressbook/img/business-small.gif" );
					}

					return new ThemeResource( "../addressbook/img/personal-small.gif" );
				}

				if ( "communications".equals( column ) ) {
					return CollectionUtils.toString( (Collection<?>) value );
				}

				return value;
			}
		};

		// Page body

		VerticalLayout pagebody = new VerticalLayout();

		// Search and result sections

		pagebody.addComponent( createSearchSection() );
		pagebody.addComponent( createResultsSection() );

		mContent = new CustomLayout( "addressbook" );

		( (CustomLayout) mContent ).addComponent( pagebody, "pagebody" );
	}

	//
	// Public methods
	//

	@Override
	public void init() {

		setTheme( "addressbook" );
		Window mainWindow = new Window( "Address Book (Metawidget Vaadin Example)" );
		( (Layout) mainWindow.getContent() ).setMargin( false );
		mainWindow.addComponent( mContent );
		setMainWindow( mainWindow );
	}

	@UiHidden
	public ContactsController getContactsController() {

		return mContactsController;
	}

	public void fireRefresh() {

		mModel.importCollection( mContactsController.getAllByExample( mContactSearch ) );
	}

	@UiAction
	public void search() {

		// Example of manual mapping. See ContactDialog for an example of using
		// automatic Bindings

		mContactSearch.setFirstname( (String) ( (Property) mSearchMetawidget.getComponent( "firstname" ) ).getValue() );
		mContactSearch.setSurname( (String) ( (Property) mSearchMetawidget.getComponent( "surname" ) ).getValue() );
		mContactSearch.setType( (ContactType) ( (Property) mSearchMetawidget.getComponent( "type" ) ).getValue() );

		fireRefresh();
	}

	@UiAction
	@UiComesAfter( "search" )
	public void addPersonal() {

		ContactDialog contactDialog = new ContactDialog( AddressBook.this, new PersonalContact() );
		showModalWindow( contactDialog );
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public void addBusiness() {

		ContactDialog contactDialog = new ContactDialog( AddressBook.this, new BusinessContact() );
		showModalWindow( contactDialog );
	}

	//
	// Private methods
	//

	/* package private */void showModalWindow( Window window ) {

		window.setModal( true );
		mContent.getWindow().addWindow( window );
	}

	private Component createSearchSection() {

		// Metawidget

		mSearchMetawidget = new VaadinMetawidget();
		mSearchMetawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		mSearchMetawidget.setToInspect( mContactSearch );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setData( "buttons" );
		mSearchMetawidget.addComponent( facetButtons );

		VaadinMetawidget buttonsMetawidget = new VaadinMetawidget();
		buttonsMetawidget.setWidth( null );
		buttonsMetawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		buttonsMetawidget.setLayout( new HorizontalLayout() );
		buttonsMetawidget.setToInspect( this );
		facetButtons.addComponent( buttonsMetawidget );
		( (com.vaadin.ui.VerticalLayout) facetButtons.getContent() ).setComponentAlignment( buttonsMetawidget, Alignment.MIDDLE_CENTER );

		return mSearchMetawidget;
	}

	private Component createResultsSection() {

		Table table = new Table();
		table.setSelectable( true );
		table.setWidth( "100%" );
		table.setContainerDataSource( mModel );
		table.setRowHeaderMode( Table.ROW_HEADER_MODE_ICON_ONLY );
		table.setVisibleColumns( new Object[] { "fullname", "communications" } );
		table.setItemIconPropertyId( "class" );
		table.setHeight( "275px" );

		table.addListener( new ItemClickListener() {

			public void itemClick( ItemClickEvent event ) {

				// When table is clicked...

				Contact contact = mModel.getDataRow( event.getItemId() );

				// ...display the Contact

				ContactDialog contactDialog = createContactDialog( contact );
				AddressBook.this.showModalWindow( contactDialog );
			}
		} );

		return table;
	}

	/* package private */ContactDialog createContactDialog( Contact contact ) {

		// Defensive copy (otherwise unsaved changes in the dialog appear in the summary list)

		if ( contact instanceof PersonalContact ) {
			return new ContactDialog( AddressBook.this, new PersonalContact( (PersonalContact) contact ) );
		}

		return new ContactDialog( AddressBook.this, new BusinessContact( (BusinessContact) contact ) );
	}
}
