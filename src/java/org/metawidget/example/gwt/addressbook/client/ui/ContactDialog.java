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

import static org.metawidget.util.StringUtils.*;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;

/**
 * @author Richard Kennard
 */

public class ContactDialog
	extends DialogBox
{
	//
	//
	// Constructor
	//
	//

	public ContactDialog( final AddressBook addressBook, final Contact contact )
	{
		setStyleName( "contact-dialog" );
		setPopupPosition( 100, 50 );
		Grid grid = new Grid( 1, 2 );
		setWidget( grid );

		// Left-hand image

		ColumnFormatter columnFormatter = grid.getColumnFormatter();
		Image image = new Image();
		columnFormatter.setStyleName( 0, "page-image" );
		grid.setWidget( 0, 0, image );

		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setReadOnly( contact.getId() != 0 );
		metawidget.setToInspect( contact );
		grid.setWidget( 0, 1, metawidget );
		columnFormatter.setStyleName( 1, "content" );

		// Binding

		metawidget.setBindingClass( SimpleBinding.class );

		@SuppressWarnings( "unchecked" )
		SimpleBindingAdapter<Contact> contactAdapter = (SimpleBindingAdapter<Contact>) GWT.create( Contact.class );
		SimpleBinding.registerAdapter( Contact.class, contactAdapter );
		SimpleBinding.registerConverter( Date.class, new DateConverter() );
		SimpleBinding.registerConverter( Gender.class, new EnumConverter<Gender>( Gender.class ) );
		SimpleBinding.registerConverter( Number.class, new NumberConverter() );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 )
			builder.append( " - " );

		// Personal/business icon

		if ( contact instanceof PersonalContact )
		{
			builder.append( "personalContact" );
			image.setUrl( "media/personal.gif" );
		}
		else
		{
			builder.append( "businessContact" );
			image.setUrl( "media/business.gif" );
		}

		setText( builder.toString() );

		// Communications

		Stub stub = new Stub();
		stub.setName( "communications" );
		metawidget.add( stub );

		final FlexTable communications = new FlexTable();
		stub.add( communications );

		// Header

		communications.setText( 0, 0, "Type" );
		communications.setText( 0, 1, "Value" );

		// Footer

		Communication communication = new Communication();

		final GwtMetawidget typeMetawidget = new GwtMetawidget();
		typeMetawidget.setPath( Communication.class.getName() + SEPARATOR_SLASH + "type" );
		typeMetawidget.setToInspect( communication );
		typeMetawidget.buildWidgets();
		communications.setWidget( 1, 0, typeMetawidget );
		typeMetawidget.setVisible( !metawidget.isReadOnly() );

		final GwtMetawidget valueMetawidget = new GwtMetawidget();
		valueMetawidget.setPath( Communication.class.getName() + SEPARATOR_SLASH + "value" );
		valueMetawidget.setToInspect( communication );
		valueMetawidget.buildWidgets();
		communications.setWidget( 1, 1, valueMetawidget );
		valueMetawidget.setVisible( !metawidget.isReadOnly() );

		final Button addButton = new Button( "Add" );
		addButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				Communication communicationToAdd = new Communication();
				communicationToAdd.setType( (String) typeMetawidget.getValue( "type" ));
				communicationToAdd.setValue( (String) valueMetawidget.getValue( "value" ));

				contact.addCommunication( communicationToAdd );
				loadCommunications( communications, contact );

				typeMetawidget.setValue( "", "type" );
				valueMetawidget.setValue( "", "value" );
			}
		} );
		communications.setWidget( 1, 2, addButton );
		addButton.setVisible( !metawidget.isReadOnly() );

		loadCommunications( communications, contact );

		// Embedded buttons

		Facet buttonsFacet = new Facet();
		buttonsFacet.setName( "buttons" );
		metawidget.add( buttonsFacet );

		HorizontalPanel panel = new HorizontalPanel();
		buttonsFacet.add( panel );

		final Button saveButton = new Button( "Save" );
		saveButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				metawidget.save();

				addressBook.getContactsService().save( contact, new AsyncCallback<Object>()
				{
					public void onFailure( Throwable caught )
					{
						Window.alert( caught.getMessage() );
					}

					public void onSuccess( Object result )
					{
						ContactDialog.this.hide();
						addressBook.reloadContacts();
					}
				} );
			}
		} );
		panel.add( saveButton );

		final Button deleteButton = new Button( "Delete" );
		deleteButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				if ( Window.confirm( "Sure you want to delete this contact?" ) )
				{
					addressBook.getContactsService().delete( contact, new AsyncCallback<Boolean>()
					{
						public void onFailure( Throwable caught )
						{
							Window.alert( caught.getMessage() );
						}

						public void onSuccess( Boolean result )
						{
							ContactDialog.this.hide();
							addressBook.reloadContacts();
						}
					} );
				}
			}
		} );
		panel.add( deleteButton );

		saveButton.setVisible( !metawidget.isReadOnly() );
		deleteButton.setVisible( !metawidget.isReadOnly() && contact.getId() != 0 );

		final Button editButton = new Button( "Edit" );
		editButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				metawidget.setReadOnly( false );
				editButton.setVisible( false );
				saveButton.setVisible( true );
				deleteButton.setVisible( true );

				for( int loop = 1, length = communications.getRowCount(); loop < length; loop++ )
				{
					communications.getWidget( loop, 2 ).setVisible( true );
				}

				typeMetawidget.setVisible( true );
				valueMetawidget.setVisible( true );
				metawidget.buildWidgets();
			}
		} );
		editButton.setVisible( metawidget.isReadOnly() );
		panel.add( editButton );

		Button cancelButton = new Button( "Cancel" );
		cancelButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				ContactDialog.this.hide();
			}
		} );
		panel.add( cancelButton );

		metawidget.buildWidgets();
	}

	//
	//
	// Package-level methods
	//
	//

	void loadCommunications( final FlexTable table, final Contact contact )
	{
		Set<Communication> communications = contact.getCommunications();

		// Communications

		int row = 1;

		if ( communications != null )
		{
			for( final Communication communication : communications )
			{
				// (push the footer down)

				if ( table.getRowCount() - 1 <= row )
					table.insertRow( row );

				table.setText( row, 0, communication.getType() );
				table.setText( row, 1, communication.getValue() );

				final Button deleteButton = new Button( "Delete" );
				deleteButton.addClickListener( new ClickListener()
				{
					public void onClick( Widget sender )
					{
						if ( Window.confirm( "Sure you want to delete this communication?" ) )
						{
							contact.removeCommunication( communication );
							loadCommunications( table, contact );
						}
					}
				} );
				deleteButton.setVisible( false );

				table.setWidget( row, 2, deleteButton );

				row++;
			}
		}

		// Cleanup any extra rows

		while( table.getRowCount() - 1 > row )
		{
			table.removeRow( row );
		}
	}
}
