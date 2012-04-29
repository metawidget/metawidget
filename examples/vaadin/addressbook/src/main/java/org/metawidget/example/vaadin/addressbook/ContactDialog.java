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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.ResourceBundle;

import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.vaadin.Facet;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.layout.HorizontalLayout;
import org.metawidget.vaadin.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
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

	private AddressBook						mAddressBook;

	/* package private */VaadinMetawidget	mContactMetawidget;

	/* package private */Table				mCommunicationsTable;

	private com.vaadin.ui.HorizontalLayout	mCommunicationsButtons;

	private VaadinMetawidget				mButtonsMetawidget;

	/* package private */boolean			mShowConfirmDialog	= true;

	//
	// Constructor
	//

	public ContactDialog( AddressBook addressBook, final Contact contact ) {

		mAddressBook = addressBook;

		setHeight( "600px" );
		setWidth( "800px" );
		( (Layout) getContent() ).setMargin( false );

		CustomLayout body = new CustomLayout( "contact" );
		addComponent( body );

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
			body.addComponent( new Embedded( "", new ThemeResource( "../addressbook/img/personal.gif" ) ), "icon" );
		} else {
			builder.append( bundle.getString( "businessContact" ) );
			body.addComponent( new Embedded( "", new ThemeResource( "../addressbook/img/business.gif" ) ), "icon" );
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
		deleteButton.addListener( new ClickListener() {

			public void buttonClick( ClickEvent event ) {

				mCommunicationsTable.removeItem( mCommunicationsTable.getValue() );
			}
		} );

		final Button addNewButton = new Button( "Add" );
		addNewButton.addListener( new ClickListener() {

			public void buttonClick( ClickEvent event ) {

				mCommunicationsTable.setValue( null );
				deleteButton.setEnabled( false );

				CommunicationDialog communicationDialog = new CommunicationDialog( ContactDialog.this, new Communication() );
				communicationDialog.setModal( true );
				getParent().addWindow( communicationDialog );
			}
		} );

		mCommunicationsTable.setSelectable( false );
		mCommunicationsTable.addListener( new ItemClickListener() {

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
				getParent().addWindow( communicationDialog );
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
		facetButtons.addComponent( mButtonsMetawidget );
		( (com.vaadin.ui.VerticalLayout) facetButtons.getContent() ).setComponentAlignment( mButtonsMetawidget, Alignment.MIDDLE_CENTER );
	}

	//
	// Public methods
	//

	@Override
	@UiHidden
	public boolean isReadOnly() {

		return mContactMetawidget.isReadOnly();
	}

	/**
	 * For unit tests
	 */

	@UiHidden
	public void setShowConfirmDialog( boolean showConfirmDialog ) {

		mShowConfirmDialog = showConfirmDialog;
	}

	@UiAction
	@UiAttribute( name = HIDDEN, value = "${!this.readOnly}" )
	public void edit() {

		mContactMetawidget.setReadOnly( false );
		mCommunicationsTable.setSelectable( true );
		mCommunicationsButtons.setVisible( true );
		mButtonsMetawidget.setToInspect( mButtonsMetawidget.getToInspect() );
	}

	@UiAction
	@UiAttribute( name = HIDDEN, value = "${this.readOnly}" )
	public void save() {

		try {
			mContactMetawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( mContactMetawidget );
			Contact contact = mContactMetawidget.getToInspect();
			mAddressBook.getContactsController().save( contact );
		} catch ( Exception e ) {
			showNotification( "Save Error", e.getLocalizedMessage(), Notification.TYPE_ERROR_MESSAGE );
			return;
		}

		getParent().removeWindow( this );
		mAddressBook.fireRefresh();
	}

	@UiAction
	@UiComesAfter( "save" )
	@UiAttribute( name = HIDDEN, value = "${this.readOnly || this.newContact}" )
	public void delete() {

		Contact contact = mContactMetawidget.getToInspect();

		getParent().removeWindow( this );
		mAddressBook.getContactsController().delete( contact );
		mAddressBook.fireRefresh();
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiAttribute( name = LABEL, value = "${if ( this.readOnly ) 'Back'}" )
	public void cancel() {

		getParent().removeWindow( this );
	}

	@SuppressWarnings( "unchecked" )
	public void addCommunication( Communication communication ) {

		Contact contact = (Contact) mContactMetawidget.getToInspect();
		contact.addCommunication( communication );

		( (TableDataSource<Communication>) mCommunicationsTable.getContainerDataSource() ).importCollection( contact.getCommunications() );
	}
}
