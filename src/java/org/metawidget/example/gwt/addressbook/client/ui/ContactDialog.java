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

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.gwt.client.binding.BindingAdapter;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
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

	public ContactDialog( final ContactsServiceAsync contactsService, final Contact contact )
	{
		setStyleName( "contact-dialog" );
		setPopupPosition( 100, 50 );
		Grid grid = new Grid( 1, 2 );

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

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 )
			builder.append( " - " );

		// Personal/business icon

		if ( contact instanceof PersonalContact )
		{
			builder.append( "personalContact" );
			image.setUrl( "media/personal.gif" );

			@SuppressWarnings( "unchecked" )
			BindingAdapter<PersonalContact> bindingAdapter = (BindingAdapter<PersonalContact>) GWT.create( PersonalContact.class );
			bindingAdapter.setAdaptee( (PersonalContact) contact );

			metawidget.setBinding( bindingAdapter );
		}
		else
		{
			builder.append( "businessContact" );
			image.setUrl( "media/business.gif" );

			@SuppressWarnings( "unchecked" )
			BindingAdapter<BusinessContact> bindingAdapter = (BindingAdapter<BusinessContact>) GWT.create( BusinessContact.class );
			bindingAdapter.setAdaptee( (BusinessContact) contact );

			metawidget.setBinding( bindingAdapter );
		}

		setText( builder.toString() );

		metawidget.buildWidgets();

		// Embedded buttons

		Button edit = new Button( "Edit" );
		edit.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				metawidget.setReadOnly( false );
				metawidget.buildWidgets();
			}
		} );
		// RootPanel.get( "edit" ).add( edit );

		Button save = new Button( "Save" );
		save.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				contactsService.save( contact, null );
				ContactDialog.this.hide();
			}
		} );
		// RootPanel.get( "save" ).add( save );

		Button delete = new Button( "Delete" );
		delete.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				if ( Window.confirm( "Sure you want to delete this contact?" ) )
				{
					contactsService.delete( contact, null );
					ContactDialog.this.hide();
				}
			}
		} );
		// RootPanel.get( "delete" ).add( delete );

		Button cancel = new Button( "Cancel" );
		cancel.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				ContactDialog.this.hide();
			}
		} );
		// RootPanel.get( "cancel" ).add( cancel );

		setWidget( grid );
	}
}
