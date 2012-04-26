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

package org.metawidget.example.vaadin.addressbook;

import java.io.Serializable;
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
import org.metawidget.vaadin.Facet;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.layout.HorizontalLayout;

import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Loghman Barari
 */

public class AddressBook
	implements ContactsControllerProvider, Serializable {

	//
	// Private members
	//

	private ContactSearch							mContactSearch;

	private VaadinMetawidget						mSearchMetawidget;

	/* package private */TableDataSource<Contact>	mModel;

	private ContactsController						mContactsController;

	private ComponentContainer						mContent;

	//
	// Constructor
	//

	public AddressBook() {

		// Model

		mContactSearch = new ContactSearch();
		mContactsController = new ContactsController();

		mModel = new TableDataSource<Contact>( Contact.class, mContactsController.getAllByExample( mContactSearch ), "Class", "Fullname", "Communications" ) {

			@Override
			protected Class<?> getColumnType( String column ) {

				if ( "Class".equals( column ) ) {
					return ThemeResource.class;
				}

				if ( "Communications".equals( column ) ) {
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

				if ( "Communications".equals( column ) ) {
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

	@UiHidden
	public ContactsController getContactsController() {

		return mContactsController;
	}

	@UiHidden
	public ComponentContainer getContent() {

		return mContent;
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
		mContent.getWindow().addWindow( contactDialog.getContent() );
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public void addBusiness() {

		ContactDialog contactDialog = new ContactDialog( AddressBook.this, new BusinessContact() );
		showModalWindow( contactDialog.getContent() );
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
		mSearchMetawidget.setBundle( MainApplication.getBundle() );
		mSearchMetawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		mSearchMetawidget.setToInspect( mContactSearch );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setData( "buttons" );
		mSearchMetawidget.addComponent( facetButtons );

		VaadinMetawidget buttonsMetawidget = new VaadinMetawidget();
		buttonsMetawidget.setWidth( null );
		buttonsMetawidget.setBundle( MainApplication.getBundle() );
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
		table.setColumnCollapsingAllowed( true );
		table.setItemIconPropertyId( "Class" );
		table.setColumnCollapsed( "Class", true );
		table.setHeight( "275px" );

		table.addListener( new ItemClickListener() {

			public void itemClick( ItemClickEvent event ) {

				// When table is double clicked...

				Contact contact = mModel.getDataRow( event.getItemId() );

				// ...display the Contact

				ContactDialog contactDialog = createContactDialog( contact );
				AddressBook.this.showModalWindow( contactDialog.getContent() );
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
