package org.metawidget.example.gwt.addressbook.client;

import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AddressBook
	implements EntryPoint
{

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setToInspect( new ContactSearch() );
		metawidget.buildWidgets();
		RootPanel.get( "metawidget" ).add( metawidget );

		final Button search = new Button( "Search" );
		search.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				String firstnames = (String) metawidget.getValue( "firstnames" );
				String surname = (String) metawidget.getValue( "surname" );
				ContactType type = null;

				String typeString = (String) metawidget.getValue( "type" );

				if ( typeString != null )
					type = ContactType.valueOf( typeString );
			}
		} );
		RootPanel.get( "search" ).add( search );

		final Button addPersonal = new Button( "Add Personal" );
		addPersonal.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				new ContactDialog( new PersonalContact() ).show();
			}
		} );
		RootPanel.get( "addPersonal" ).add( addPersonal );

		final Button addBusiness = new Button( "Add Business" );
		addBusiness.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				new ContactDialog( new BusinessContact() ).show();
			}
		} );
		RootPanel.get( "addBusiness" ).add( addBusiness );
	}
}
