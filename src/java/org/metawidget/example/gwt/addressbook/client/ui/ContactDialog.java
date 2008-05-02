package org.metawidget.example.gwt.addressbook.client.ui;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;

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

		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setToInspect( contact );
		metawidget.setReadOnly( contact.getId() != 0 );
		metawidget.buildWidgets();
		grid.setWidget( 0, 1, metawidget );
		columnFormatter.setStyleName( 1, "content" );

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
		//RootPanel.get( "edit" ).add( edit );

		Button save = new Button( "Save" );
		save.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				contactsService.save( contact, null );
				ContactDialog.this.hide();
			}
		} );
		//RootPanel.get( "save" ).add( save );

		Button delete = new Button( "Delete" );
		delete.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				if ( Window.confirm( "Sure you want to delete this contact?" ))
				{
					contactsService.delete( contact, null );
					ContactDialog.this.hide();
				}
			}
		} );
		//RootPanel.get( "delete" ).add( delete );

		Button cancel = new Button( "Cancel" );
		cancel.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				ContactDialog.this.hide();
			}
		} );
		//RootPanel.get( "cancel" ).add( cancel );

		setWidget( grid );
	}
}
