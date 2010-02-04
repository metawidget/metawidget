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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
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
import org.metawidget.swt.Facet;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.layout.RowLayout;

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
		Shell shell = new Shell( display );

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

	private Shell				mShell;

	private ContactSearch		mContactSearch;

	private SwtMetawidget		mSearchMetawidget;

	private ContactsController	mContactsController;

	//
	// Constructor
	//

	public Main( Shell shell )
	{
		mShell = shell;
		mShell.setLayout( new GridLayout() );

		// Model

		mContactSearch = new ContactSearch();
		mContactsController = new ContactsController();

		createSearchSection();
		createResultsSection();
	}

	//
	// Public methods
	//

	@UiAction
	public void search()
	{
		// Example of manual mapping. See ContactDialog for an example of using automatic Bindings

		mContactSearch.setFirstname( (String) mSearchMetawidget.getValue( "firstname" ) );
		mContactSearch.setSurname( (String) mSearchMetawidget.getValue( "surname" ) );
		mContactSearch.setType( (ContactType) mSearchMetawidget.getValue( "type" ) );
	}

	@UiAction
	@UiComesAfter( "search" )
	public void addPersonal()
	{
		Contact contact = new PersonalContact();
		contact.setFirstname( "Foo" );
		contact.getAddress().setCity( "Bar" );
		contact.getAddress().setState( "Anytown" );
		new ContactDialog( mShell, SWT.None ).open( contact );
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public void addBusiness()
	{
		new ContactDialog( mShell, SWT.None ).open( new BusinessContact() );
	}

	//
	// Private methods
	//

	private SwtMetawidget createSearchSection()
	{
		// Metawidget

		mSearchMetawidget = new SwtMetawidget( mShell, SWT.NONE );
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
		final Table table = new Table( mShell, SWT.BORDER | SWT.V_SCROLL );
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.FILL;
		table.setLayoutData( data );
		table.setHeaderVisible( true );
		table.setLinesVisible( true );

		TableColumn column = new TableColumn( table, SWT.NONE );
		column.setText( "Fullname" );
		column = new TableColumn( table, SWT.NONE );
		column.setText( "Communications" );
		column = new TableColumn( table, SWT.RIGHT );
		column.setText( "Class" );

		for ( TableColumn columnToPack : table.getColumns() )
		{
			columnToPack.pack();
		}

		TableItem item = new TableItem( table, SWT.NONE );
		item.setText( 0, "Item 1" );
	}
}
