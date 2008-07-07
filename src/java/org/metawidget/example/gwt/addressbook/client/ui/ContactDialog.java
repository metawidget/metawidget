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
 * Dialog box for Address Book Contacts.
 * <p>
 * Note: for performance, this example is optimized to use <code>GwtMetawidget.rebind</code> (see
 * 'rebinding' in the Reference Documentation). This results in slightly more complex code. For a
 * more straightforward example, see
 * <code>org.metawidget.example.swing.addressbook.ContactDialog</code>.
 *
 * @author Richard Kennard
 */

public class ContactDialog
	extends DialogBox
{
	//
	//
	// Package-level members
	//
	//

	AddressBookModule	mAddressBookModule;

	GwtMetawidget		mMetawidget;

	FlexTable			mCommunications;

	Button				mEditButton;

	Button				mSaveButton;

	Button				mDeleteButton;

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

		// Bundle

		Dictionary dictionary = Dictionary.getDictionary( "bundle" );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 )
			builder.append( " - " );

		// Personal/business icon

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

		// SimpleBinding

		@SuppressWarnings( "unchecked" )
		SimpleBindingAdapter<Contact> contactAdapter = (SimpleBindingAdapter<Contact>) GWT.create( Contact.class );
		SimpleBinding.registerAdapter( Contact.class, contactAdapter );
		SimpleBinding.registerConverter( Date.class, new DateConverter() );
		SimpleBinding.registerConverter( Gender.class, new EnumConverter<Gender>( Gender.class ) );
		SimpleBinding.registerConverter( Number.class, new NumberConverter() );

		// Metawidget

		mMetawidget = new GwtMetawidget();
		mMetawidget.setReadOnly( contact.getId() != 0 );
		mMetawidget.setDictionaryName( "bundle" );
		mMetawidget.setParameter( "tableStyleName", "table-form" );
		mMetawidget.setParameter( "columnStyleNames", "table-label-column,table-component-column" );
		mMetawidget.setParameter( "sectionStyleName", "section-heading" );
		mMetawidget.setParameter( "buttonsStyleName", "buttons" );
		mMetawidget.setBindingClass( SimpleBinding.class );
		mMetawidget.setToInspect( contact );
		grid.setWidget( 0, 1, mMetawidget );

		// Communications override

		Stub communicationsStub = new Stub();
		communicationsStub.setName( "communications" );
		mMetawidget.add( communicationsStub );

		mCommunications = new FlexTable();
		mCommunications.setStyleName( "data-table" );
		communicationsStub.add( mCommunications );

		// Header

		final CellFormatter cellFormatter = mCommunications.getCellFormatter();
		mCommunications.setText( 0, 0, "Type" );
		cellFormatter.setStyleName( 0, 0, "header" );
		cellFormatter.addStyleName( 0, 0, "column-half" );
		mCommunications.setText( 0, 1, "Value" );
		cellFormatter.setStyleName( 0, 1, "header" );
		cellFormatter.addStyleName( 0, 1, "column-half" );
		mCommunications.setHTML( 0, 2, "&nbsp;" );
		cellFormatter.setStyleName( 0, 2, "header" );
		cellFormatter.addStyleName( 0, 2, "column-tiny" );

		// Footer

		Communication communication = new Communication();

		final GwtMetawidget typeMetawidget = new GwtMetawidget();
		typeMetawidget.setPath( Communication.class.getName() + SEPARATOR_FORWARD_SLASH_CHAR + "type" );
		typeMetawidget.setLayoutClass( FlowLayout.class );
		typeMetawidget.setToInspect( communication );
		mCommunications.setWidget( 1, 0, typeMetawidget );

		final GwtMetawidget valueMetawidget = new GwtMetawidget();
		valueMetawidget.setPath( Communication.class.getName() + SEPARATOR_FORWARD_SLASH_CHAR + "value" );
		valueMetawidget.setLayoutClass( FlowLayout.class );
		valueMetawidget.setToInspect( communication );
		mCommunications.setWidget( 1, 1, valueMetawidget );

		Button addButton = new Button( dictionary.get( "add" ) );
		addButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				Communication communicationToAdd = new Communication();
				communicationToAdd.setType( (String) typeMetawidget.getValue( "type" ) );
				communicationToAdd.setValue( (String) valueMetawidget.getValue( "value" ) );

				Contact currentContact = (Contact) mMetawidget.getToInspect();

				try
				{
					currentContact.addCommunication( communicationToAdd );
				}
				catch ( Exception e )
				{
					Window.alert( e.getMessage() );
					return;
				}

				loadCommunications();

				typeMetawidget.setValue( "", "type" );
				valueMetawidget.setValue( "", "value" );
			}
		} );
		mCommunications.setWidget( 1, 2, addButton );
		cellFormatter.setStyleName( 1, 2, "table-buttons" );

		// Embedded buttons

		Facet buttonsFacet = new Facet();
		buttonsFacet.setName( "buttons" );
		mMetawidget.add( buttonsFacet );
		HorizontalPanel panel = new HorizontalPanel();
		buttonsFacet.add( panel );

		mSaveButton = new Button( dictionary.get( "save" ) );
		mSaveButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				mMetawidget.save();

				mAddressBookModule.getContactsService().save( (Contact) mMetawidget.getToInspect(), new AsyncCallback<Object>()
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
		panel.add( mSaveButton );

		mDeleteButton = new Button( dictionary.get( "delete" ) );
		mDeleteButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				if ( mAddressBookModule.getPanel() instanceof RootPanel )
				{
					if ( !Window.confirm( "Sure you want to delete this contact?" ) )
						return;
				}

				mAddressBookModule.getContactsService().delete( (Contact) mMetawidget.getToInspect(), new AsyncCallback<Boolean>()
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
		panel.add( mDeleteButton );

		mEditButton = new Button( dictionary.get( "edit" ) );
		mEditButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				mMetawidget.setReadOnly( false );
				setButtonVisibility();
			}
		} );
		panel.add( mEditButton );

		Button cancelButton = new Button( dictionary.get( "cancel" ) );
		cancelButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				ContactDialog.this.hide();
			}
		} );
		panel.add( cancelButton );

		// Display

		setButtonVisibility();
	}

	//
	//
	// Public methods
	//
	//

	public void rebind( Contact contact )
	{
		mMetawidget.rebind( contact );
		mMetawidget.setReadOnly( contact.getId() != 0 );

		setButtonVisibility();
	}

	//
	//
	// Package-level methods
	//
	//

	void setButtonVisibility()
	{
		boolean readOnly = mMetawidget.isReadOnly();
		loadCommunications();

		mEditButton.setVisible( readOnly );
		mSaveButton.setVisible( !readOnly );
		mDeleteButton.setVisible( !readOnly );
		mCommunications.getRowFormatter().setVisible( mCommunications.getRowCount() - 1, !readOnly );
	}

	void loadCommunications()
	{
		CellFormatter cellFormatter = mCommunications.getCellFormatter();
		final Contact contact = (Contact) mMetawidget.getToInspect();
		Set<Communication> communications = contact.getCommunications();
		final boolean readOnly = mMetawidget.isReadOnly();
		final boolean confirm = ( mAddressBookModule.getPanel() instanceof RootPanel );

		// Communications

		int row = 1;

		if ( communications != null )
		{
			for ( final Communication communication : communications )
			{
				// (push the footer down)

				if ( mCommunications.getRowCount() - 1 <= row )
					mCommunications.insertRow( row );

				mCommunications.setText( row, 0, communication.getType() );
				mCommunications.setText( row, 1, communication.getValue() );

				if ( readOnly )
				{
					if ( mCommunications.getCellCount( row ) == 3 )
						mCommunications.removeCell( row, 2 );
				}
				else
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
							loadCommunications();
						}
					} );

					mCommunications.setWidget( row, 2, deleteButton );
					cellFormatter.setStyleName( row, 2, "table-buttons" );
				}

				row++;
			}
		}

		// Cleanup any extra rows

		while ( mCommunications.getRowCount() - 1 > row )
		{
			mCommunications.removeRow( row );
		}
	}
}
