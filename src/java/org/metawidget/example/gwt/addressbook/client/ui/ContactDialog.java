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

package org.metawidget.example.gwt.addressbook.client.ui;

import static org.metawidget.util.simple.StringUtils.*;

import java.util.Date;
import java.util.Set;

import org.metawidget.example.gwt.addressbook.client.ui.converter.DateConverter;
import org.metawidget.example.gwt.addressbook.client.ui.converter.EnumConverter;
import org.metawidget.example.gwt.addressbook.client.ui.converter.NumberConverter;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.gwt.client.binding.simple.SimpleBinding;
import org.metawidget.gwt.client.binding.simple.SimpleBindingAdapter;
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.gwt.client.ui.layout.FlowLayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

/**
 * @author Richard Kennard
 */

// TODO: do a Swing version of this
// TODO: smarter inspectionDepth
public class ContactDialog
	extends DialogBox
{
	//
	//
	// Package-level members
	//
	//

	AddressBookModule	mAddressBookModule;

	GwtMetawidget		mActiveMetawidget;

	FlexTable			mActiveCommunications;

	GwtMetawidget		mReadOnlyMetawidget;

	FlexTable			mReadOnlyCommunications;

	//
	//
	// Constructor
	//
	//

	public ContactDialog( AddressBookModule addressBookModule, final Contact contact )
	{
		mAddressBookModule = addressBookModule;

		setStyleName( "contact-dialog" );
		setPopupPosition( 100, 50 );
		Grid grid = new Grid( 1, 2 );
		grid.setWidth( "100%" );
		setWidget( grid );

		// Left-hand image

		CellFormatter gridFormatter = grid.getCellFormatter();
		Image image = new Image();
		gridFormatter.setStyleName( 0, 0, "page-image" );
		grid.setWidget( 0, 0, image );
		gridFormatter.setStyleName( 0, 1, "content" );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 )
			builder.append( " - " );

		// Personal/business icon

		Dictionary dictionary = Dictionary.getDictionary( "bundle" );

		if ( contact instanceof PersonalContact )
		{
			builder.append( dictionary.get( "personalContact" ) );
			image.setUrl( "media/personal.gif" );
		}
		else
		{
			builder.append( dictionary.get( "businessContact" ) );
			image.setUrl( "media/business.gif" );
		}

		setText( builder.toString() );

		// Binding

		@SuppressWarnings( "unchecked" )
		SimpleBindingAdapter<Contact> contactAdapter = (SimpleBindingAdapter<Contact>) GWT.create( Contact.class );
		SimpleBinding.registerAdapter( Contact.class, contactAdapter );
		SimpleBinding.registerConverter( Date.class, new DateConverter() );
		SimpleBinding.registerConverter( Gender.class, new EnumConverter<Gender>( Gender.class ) );
		SimpleBinding.registerConverter( Number.class, new NumberConverter() );

		//
		// Display either read-only or active Metawidget
		//

		rebind( contact );
	}

	//
	//
	// Public methods
	//
	//

	public void rebind( Contact contact )
	{
		display( contact, ( contact.getId() != 0 ) );
	}

	//
	//
	// Package-level methods
	//
	//

	void display( Contact contact, boolean readOnly )
	{
		Grid grid = (Grid) getWidget();

		if ( readOnly )
		{
			//
			// Read-only Metawidget
			//

			if ( mReadOnlyMetawidget == null )
			{
				mReadOnlyMetawidget = new GwtMetawidget();
				mReadOnlyMetawidget.setReadOnly( true );
				mReadOnlyMetawidget.setDictionaryName( "bundle" );
				mReadOnlyMetawidget.setParameter( "tableStyleName", "table-form" );
				mReadOnlyMetawidget.setParameter( "columnStyleNames", "table-label-column,table-component-column" );
				mReadOnlyMetawidget.setParameter( "sectionStyleName", "section-heading" );
				mReadOnlyMetawidget.setParameter( "buttonsStyleName", "buttons" );
				mReadOnlyMetawidget.setBindingClass( SimpleBinding.class );
				mReadOnlyMetawidget.setToInspect( contact );

				// Communications

				Stub communicationsStub = new Stub();
				communicationsStub.setName( "communications" );
				mReadOnlyMetawidget.add( communicationsStub );

				mReadOnlyCommunications = new FlexTable();
				mReadOnlyCommunications.setStyleName( "data-table" );
				communicationsStub.add( mReadOnlyCommunications );

				// Header

				CellFormatter readOnlyCellFormatter = mReadOnlyCommunications.getCellFormatter();
				mReadOnlyCommunications.setText( 0, 0, "Type" );
				readOnlyCellFormatter.setStyleName( 0, 0, "header" );
				readOnlyCellFormatter.addStyleName( 0, 0, "column-half" );
				mReadOnlyCommunications.setText( 0, 1, "Value" );
				readOnlyCellFormatter.setStyleName( 0, 1, "header" );
				readOnlyCellFormatter.addStyleName( 0, 1, "column-half" );

				// Embedded buttons

				Facet buttonsFacet = new Facet();
				buttonsFacet.setName( "buttons" );
				mReadOnlyMetawidget.add( buttonsFacet );
				HorizontalPanel panel = new HorizontalPanel();
				buttonsFacet.add( panel );

				Dictionary dictionary = Dictionary.getDictionary( "bundle" );
				final Button editButton = new Button( dictionary.get( "edit" ) );
				editButton.addClickListener( new ClickListener()
				{
					public void onClick( Widget sender )
					{
						display( (Contact) mReadOnlyMetawidget.getToInspect(), false );
					}
				} );
				panel.add( editButton );

				Button cancelButton = new Button( dictionary.get( "cancel" ) );
				cancelButton.addClickListener( new ClickListener()
				{
					public void onClick( Widget sender )
					{
						ContactDialog.this.hide();
					}
				} );
				panel.add( cancelButton );
			}
			else
			{
				mReadOnlyMetawidget.rebind( contact );
			}

			loadCommunications( mReadOnlyCommunications, contact, true );
			grid.setWidget( 0, 1, mReadOnlyMetawidget );
		}
		else
		{
			if ( mActiveCommunications == null )
			{
				//
				// Active Metawidget
				//

				mActiveMetawidget = new GwtMetawidget();
				mActiveMetawidget.setDictionaryName( "bundle" );
				mActiveMetawidget.setParameter( "tableStyleName", "table-form" );
				mActiveMetawidget.setParameter( "columnStyleNames", "table-label-column,table-component-column" );
				mActiveMetawidget.setParameter( "sectionStyleName", "section-heading" );
				mActiveMetawidget.setParameter( "buttonsStyleName", "buttons" );
				mActiveMetawidget.setBindingClass( SimpleBinding.class );
				mActiveMetawidget.setToInspect( contact );

				// Communications

				Stub communicationsStub = new Stub();
				communicationsStub.setName( "communications" );
				mActiveMetawidget.add( communicationsStub );

				mActiveCommunications = new FlexTable();
				mActiveCommunications.setStyleName( "data-table" );
				communicationsStub.add( mActiveCommunications );

				// Header

				final CellFormatter cellFormatter = mActiveCommunications.getCellFormatter();
				mActiveCommunications.setText( 0, 0, "Type" );
				cellFormatter.setStyleName( 0, 0, "header" );
				cellFormatter.addStyleName( 0, 0, "column-half" );
				mActiveCommunications.setText( 0, 1, "Value" );
				cellFormatter.setStyleName( 0, 1, "header" );
				cellFormatter.addStyleName( 0, 1, "column-half" );
				mActiveCommunications.setHTML( 0, 2, "&nbsp;" );
				cellFormatter.setStyleName( 0, 2, "header" );
				cellFormatter.addStyleName( 0, 2, "column-tiny" );

				// Footer

				Communication communication = new Communication();

				final GwtMetawidget typeMetawidget = new GwtMetawidget();
				typeMetawidget.setPath( Communication.class.getName() + SEPARATOR_FORWARD_SLASH_CHAR + "type" );
				typeMetawidget.setLayoutClass( FlowLayout.class );
				typeMetawidget.setToInspect( communication );
				mActiveCommunications.setWidget( 1, 0, typeMetawidget );

				final GwtMetawidget valueMetawidget = new GwtMetawidget();
				valueMetawidget.setPath( Communication.class.getName() + SEPARATOR_FORWARD_SLASH_CHAR + "value" );
				valueMetawidget.setLayoutClass( FlowLayout.class );
				valueMetawidget.setToInspect( communication );
				mActiveCommunications.setWidget( 1, 1, valueMetawidget );

				Dictionary dictionary = Dictionary.getDictionary( "bundle" );
				Button addButton = new Button( dictionary.get( "add" ) );
				addButton.addClickListener( new ClickListener()
				{
					public void onClick( Widget sender )
					{
						Communication communicationToAdd = new Communication();
						communicationToAdd.setType( (String) typeMetawidget.getValue( "type" ) );
						communicationToAdd.setValue( (String) valueMetawidget.getValue( "value" ) );

						Contact currentContact = (Contact) mActiveMetawidget.getToInspect();

						try
						{
							currentContact.addCommunication( communicationToAdd );
						}
						catch ( Exception e )
						{
							Window.alert( e.getMessage() );
							return;
						}

						loadCommunications( mActiveCommunications, currentContact, false );

						typeMetawidget.setValue( "", "type" );
						valueMetawidget.setValue( "", "value" );
					}
				} );
				mActiveCommunications.setWidget( 1, 2, addButton );
				cellFormatter.setStyleName( 1, 2, "table-buttons" );

				// Embedded buttons

				Facet buttonsFacet = new Facet();
				buttonsFacet.setName( "buttons" );
				mActiveMetawidget.add( buttonsFacet );
				HorizontalPanel panel = new HorizontalPanel();
				buttonsFacet.add( panel );

				Button saveButton = new Button( dictionary.get( "save" ) );
				saveButton.addClickListener( new ClickListener()
				{
					public void onClick( Widget sender )
					{
						mActiveMetawidget.save();

						mAddressBookModule.getContactsService().save( (Contact) mActiveMetawidget.getToInspect(), new AsyncCallback<Object>()
						{
							public void onFailure( Throwable caught )
							{
								Window.alert( caught.getMessage() );
							}

							public void onSuccess( Object result )
							{
								ContactDialog.this.hide();
								mAddressBookModule.reloadContacts();
							}
						} );
					}
				} );
				panel.add( saveButton );

				final Button deleteButton = new Button( dictionary.get( "delete" ) );
				deleteButton.addClickListener( new ClickListener()
				{
					public void onClick( Widget sender )
					{
						if ( mAddressBookModule.getPanel() instanceof RootPanel )
						{
							if ( !Window.confirm( "Sure you want to delete this contact?" ) )
								return;
						}

						mAddressBookModule.getContactsService().delete( (Contact) mActiveMetawidget.getToInspect(), new AsyncCallback<Boolean>()
						{
							public void onFailure( Throwable caught )
							{
								Window.alert( caught.getMessage() );
							}

							public void onSuccess( Boolean result )
							{
								ContactDialog.this.hide();
								mAddressBookModule.reloadContacts();
							}
						} );
					}
				} );
				panel.add( deleteButton );

				Button cancelButton = new Button( dictionary.get( "cancel" ) );
				cancelButton.addClickListener( new ClickListener()
				{
					public void onClick( Widget sender )
					{
						ContactDialog.this.hide();
					}
				} );
				panel.add( cancelButton );
			}
			else
			{
				mActiveMetawidget.rebind( contact );
			}

			loadCommunications( mActiveCommunications, contact, false );
			grid.setWidget( 0, 1, mActiveMetawidget );
		}
	}

	void loadCommunications( final FlexTable table, final Contact contact, final boolean readOnly )
	{
		CellFormatter cellFormatter = table.getCellFormatter();
		Set<Communication> communications = contact.getCommunications();
		final boolean confirm = ( mAddressBookModule.getPanel() instanceof RootPanel );

		// Communications

		int row = 1;

		if ( communications != null )
		{
			for ( final Communication communication : communications )
			{
				// (push the footer down)

				if ( !readOnly && table.getRowCount() - 1 <= row )
					table.insertRow( row );

				table.setText( row, 0, communication.getType() );
				table.setText( row, 1, communication.getValue() );

				if ( !readOnly )
				{
					final Button deleteButton = new Button( "Delete" );
					deleteButton.addClickListener( new ClickListener()
					{
						public void onClick( Widget sender )
						{
							if ( confirm )
							{
								if ( !Window.confirm( "Sure you want to delete this communication?" ) )
									return;
							}

							contact.removeCommunication( communication );
							loadCommunications( table, contact, false );
						}
					} );

					table.setWidget( row, 2, deleteButton );
					cellFormatter.setStyleName( row, 2, "table-buttons" );
				}

				row++;
			}
		}

		// Cleanup any extra rows

		while ( table.getRowCount() - 1 > row )
		{
			table.removeRow( row );
		}
	}
}
