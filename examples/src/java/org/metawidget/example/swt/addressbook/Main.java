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

package org.metawidget.example.swt.addressbook;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.swt.Facet;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.layout.RowLayout;
import org.metawidget.util.CollectionUtils;

/**
 * @author Stefan Ackermann, Richard Kennard
 */

public class Main
{
	//
	// Public statics
	//

	public static void main( String[] args )
	{
		Display display = new Display();
		Shell shell = new Shell( display, SWT.DIALOG_TRIM | SWT.RESIZE );

		new Main( shell );

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() )
		{
			if ( !display.readAndDispatch() )
				display.sleep();
		}

		display.dispose();
	}

	//
	// Private members
	//

	private Shell							mShell;

	private ContactSearch					mContactSearch;

	/* package private */Table				mResultsTable;

	private Image							mPersonalContactImage;

	private Image							mBusinessContactImage;

	private SwtMetawidget					mSearchMetawidget;

	/* package private */ContactsController	mContactsController;

	/* package private */ContactDialog		mContactDialog;

	//
	// Constructor
	//

	public Main( Shell shell )
	{
		mShell = shell;
		mShell.setText( "Address Book (Metawidget SWT Example)" );
		mShell.setLayout( new GridLayout( 2, false ) );
		mShell.setSize( 615, 615 );

		mShell.setBackgroundMode( SWT.INHERIT_DEFAULT );
		mShell.setBackground( new Color( mShell.getDisplay(), 255, 255, 255 ) );
		mShell.setBackgroundImage( new Image( mShell.getDisplay(), ClassLoader.getSystemResourceAsStream( "org/metawidget/example/shared/addressbook/media/background.jpg" ) ) );

		// Model

		mContactSearch = new ContactSearch();
		mContactsController = new ContactsController();

		// Left-hand image

		Label label = new Label( mShell, SWT.NONE );
		label.setImage( new Image( mShell.getDisplay(), ClassLoader.getSystemResourceAsStream( "org/metawidget/example/shared/addressbook/media/addressbook.gif" ) ) );
		GridData data = new GridData();
		data.verticalAlignment = SWT.TOP;
		data.verticalSpan = 2;
		label.setLayoutData( data );

		// Right-hand panel

		createSearchSection();
		createResultsSection();

		mContactDialog = new ContactDialog( this );
	}

	//
	// Public methods
	//

	@UiHidden
	public Shell getShell()
	{
		return mShell;
	}

	@UiHidden
	public ContactsController getContactsController()
	{
		return mContactsController;
	}

	@UiHidden
	public void fireRefresh()
	{
		List<Contact> contacts = mContactsController.getAllByExample( mContactSearch );
		int loop = 0;

		for ( ; loop < contacts.size(); loop++ )
		{
			// Add/edit row...

			TableItem item;

			if ( loop < mResultsTable.getItemCount() )
				item = mResultsTable.getItem( loop );
			else
				item = new TableItem( mResultsTable, SWT.NONE );

			// ...with contact text

			Contact contact = contacts.get( loop );
			item.setText( 0, String.valueOf( contact.getId() ) );
			item.setText( 1, contact.getFullname() );
			item.setText( 2, CollectionUtils.toString( contact.getCommunications() ) );

			if ( contact instanceof PersonalContact )
				item.setImage( 3, mPersonalContactImage );
			else
				item.setImage( 3, mBusinessContactImage );
		}

		// Delete hanging rows

		if ( loop < mResultsTable.getItemCount() )
			mResultsTable.remove( loop, mResultsTable.getItemCount() - 1 );
	}

	@UiAction
	public void search()
	{
		// Example of manual mapping. See ContactDialog for an example of using automatic Bindings

		mContactSearch.setFirstname( (String) mSearchMetawidget.getValue( "firstname" ) );
		mContactSearch.setSurname( (String) mSearchMetawidget.getValue( "surname" ) );
		String type = (String) mSearchMetawidget.getValue( "type" );

		if ( "".equals( type ) )
			mContactSearch.setType( null );
		else
			mContactSearch.setType( ContactType.valueOf( type ) );

		fireRefresh();
	}

	@UiAction
	@UiComesAfter( "search" )
	public void addPersonal()
	{
		mContactDialog.open( new PersonalContact() );
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public void addBusiness()
	{
		mContactDialog.open( new BusinessContact() );
	}

	//
	// Private methods
	//

	private SwtMetawidget createSearchSection()
	{
		// Metawidget

		mSearchMetawidget = new SwtMetawidget( mShell, SWT.None );
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		mSearchMetawidget.setLayoutData( data );
		mSearchMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );

		// Embedded buttons

		Facet facetButtons = new Facet( mSearchMetawidget, SWT.NONE );
		facetButtons.setData( "name", "buttons" );
		facetButtons.setLayout( new GridLayout() );

		SwtMetawidget buttonsMetawidget = new SwtMetawidget( facetButtons, SWT.NONE );
		buttonsMetawidget.setMetawidgetLayout( new RowLayout() );
		buttonsMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
		buttonsMetawidget.setToInspect( this );
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.CENTER;
		buttonsMetawidget.setLayoutData( data );

		mSearchMetawidget.setToInspect( mContactSearch );
		return mSearchMetawidget;
	}

	private void createResultsSection()
	{
		// Create table

		mResultsTable = new Table( mShell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL );
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.FILL;
		mResultsTable.setLayoutData( data );
		mResultsTable.setHeaderVisible( true );
		mResultsTable.setLinesVisible( true );

		TableColumn column = new TableColumn( mResultsTable, SWT.NONE );
		column.setText( "Id" );
		column.setWidth( 0 );
		column.setResizable( false );
		column = new TableColumn( mResultsTable, SWT.NONE );
		column.setText( "Fullname" );
		column.setWidth( 175 );
		column = new TableColumn( mResultsTable, SWT.NONE );
		column.setText( "Communications" );
		column.setWidth( 250 );
		column = new TableColumn( mResultsTable, SWT.CENTER );
		column.setWidth( 23 );
		column.setResizable( false );

		// When table is double clicked...

		mResultsTable.addSelectionListener( new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected( SelectionEvent event )
			{
				// ...fetch the Contact...

				Contact contact = mContactsController.load( Long.parseLong( ((TableItem) event.item).getText( 0 ) ) );

				// ...and display it

				mContactDialog.open( contact );
			}

			@Override
			public void widgetSelected( SelectionEvent arg0 )
			{
				// Do nothing
			}
		} );

		mPersonalContactImage = new Image( mShell.getDisplay(), ClassLoader.getSystemResourceAsStream( "org/metawidget/example/shared/addressbook/media/personal-small.gif" ) );
		mBusinessContactImage = new Image( mShell.getDisplay(), ClassLoader.getSystemResourceAsStream( "org/metawidget/example/shared/addressbook/media/business-small.gif" ) );

		fireRefresh();
	}
}
