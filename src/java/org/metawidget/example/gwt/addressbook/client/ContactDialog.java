package org.metawidget.example.gwt.addressbook.client;

import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.user.client.ui.DialogBox;

public class ContactDialog
	extends DialogBox
{
	//
	//
	// Constructor
	//
	//

	public ContactDialog( Contact contact )
	{
		setText( "My First Dialog" );

		GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setToInspect( contact );
		metawidget.buildWidgets();

		setWidget( metawidget );
	}
}
