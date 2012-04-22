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

import java.io.Serializable;
import java.util.ResourceBundle;

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

import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * Dialog box for Address Book Contacts.
 *
 * @author Loghman Barari
 */

public class ContactDialog
	implements Serializable {

	//
	// Package-level members
	//

	IndexedContainer						mCommunicationsModel;

	//
	// Private members
	//

	private ContactsControllerProvider		mProvider;

	/* package private */VaadinMetawidget	mContactMetawidget;

	private VaadinMetawidget				mButtonsMetawidget;

	private Window							mWindow;

	/* package private */boolean			mShowConfirmDialog	= true;

	//
	// Constructor
	//

	public ContactDialog( ContactsControllerProvider provider, final Contact contact ) {

		mProvider = provider;

		mWindow = new Window();
		mWindow.setHeight( "600px" );
		mWindow.setWidth( "800px" );

		// Bundle

		ResourceBundle bundle = ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 ) {
			builder.append( " - " );
		}

		// Personal/business icon

		CustomLayout body = new CustomLayout( "contact" );

		if ( contact instanceof PersonalContact ) {
			builder.append( bundle.getString( "personalContact" ) );
			body.addComponent( new Embedded( "", new ThemeResource( "../addressbook/img/personal.gif" ) ), "icon" );
		} else {
			builder.append( bundle.getString( "businessContact" ) );
			body.addComponent( new Embedded( "", new ThemeResource( "../addressbook/img/business.gif" ) ), "icon" );
		}

		mWindow.setCaption( builder.toString() );

		// Metawidget

		mContactMetawidget = new VaadinMetawidget();
		mContactMetawidget.setBundle( MainApplication.getBundle() );
		mContactMetawidget.setWidth( "100%" );
		mContactMetawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		mContactMetawidget.setReadOnly( contact.getId() != 0 );
		mContactMetawidget.setToInspect( contact );

		// Communications override

		Table communicationsTable = new Table();
		communicationsTable.setData( "communications" );
		communicationsTable.setHeight( "170px" );
		mContactMetawidget.addComponent( communicationsTable );

		// Embedded buttons

		body.addComponent( mContactMetawidget, "pagebody" );

		((Layout) mWindow.getContent()).setMargin( false );
		mWindow.addComponent( body );

		addEmbeddedButtons();
	}

	//
	// Public methods
	//

	@UiHidden
	public Window getContent() {

		return mWindow;
	}

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

		addEmbeddedButtons();
	}

	@UiAction
	@UiAttribute( name = HIDDEN, value = "${this.readOnly}" )
	public void save() {

		try {
			mContactMetawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( mContactMetawidget );
			Contact contact = mContactMetawidget.getToInspect();
			mProvider.getContactsController().save( contact );
		} catch ( SourceException e ) {
			mWindow.showNotification( "Save Error", e.getCause().getLocalizedMessage(), Notification.TYPE_ERROR_MESSAGE );

			return;

		} catch ( Exception e ) {
			e.printStackTrace();

			mWindow.showNotification( "Save Error", e.getLocalizedMessage(), Notification.TYPE_ERROR_MESSAGE );
			return;
		}

		if ( !MainApplication.isTestMode() ) {
			mWindow.getParent().removeWindow( mWindow );
		}

		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter( "save" )
	@UiAttribute( name = HIDDEN, value = "${this.readOnly || this.newContact}" )
	public void delete() {

		Contact contact = mContactMetawidget.getToInspect();

		if ( !MainApplication.isTestMode() ) {
			mWindow.getParent().removeWindow( mWindow );
		}

		mProvider.getContactsController().delete( contact );
		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiAttribute( name = LABEL, value = "${if ( this.readOnly ) 'Back'}" )
	public void cancel() {

		if ( !MainApplication.isTestMode() ) {
			mWindow.getParent().removeWindow( mWindow );
		}
	}

	//
	// Private Methods
	//

	private void addEmbeddedButtons() {

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setData( "buttons" );
		facetButtons.setWidth( "100%" );
		mContactMetawidget.addComponent( facetButtons );

		mButtonsMetawidget = new VaadinMetawidget();
		mButtonsMetawidget.setWidth( null );
		mButtonsMetawidget.setBundle( MainApplication.getBundle() );
		mButtonsMetawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		mButtonsMetawidget.setLayout( new HorizontalLayout() );
		mButtonsMetawidget.setToInspect( this );
		facetButtons.addComponent( mButtonsMetawidget );
		( (com.vaadin.ui.VerticalLayout) facetButtons.getContent() ).setComponentAlignment( mButtonsMetawidget, Alignment.MIDDLE_CENTER );
	}
}
