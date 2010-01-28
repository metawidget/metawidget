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

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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
		shell.setLayout( new MigLayout( new LC().fill().debug( 500 ) ) );

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

	private Shell			mShell;

	private ContactSearch	mContactSearch;

	private SwtMetawidget	mSearchMetawidget;

	//
	// Constructor
	//

	public Main( Shell shell )
	{
		mShell = shell;
		mContactSearch = new ContactSearch();

		createSearchSection();
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
		new ContactDialog( mShell, SWT.None ).open();
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public void addBusiness()
	{
		// addBusiness
	}

	//
	// Private methods
	//

	private SwtMetawidget createSearchSection()
	{
		// Metawidget

		mSearchMetawidget = new SwtMetawidget( mShell, SWT.None );
		mSearchMetawidget.setLayoutData( new CC().grow() );
		mSearchMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );

		// Embedded buttons

		Facet facetButtons = new Facet( mSearchMetawidget, SWT.None );
		facetButtons.setData( "name", "buttons" );

		SwtMetawidget buttonsMetawidget = new SwtMetawidget( facetButtons, SWT.None );
		buttonsMetawidget.setMetawidgetLayout( new RowLayout() );
		buttonsMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
		buttonsMetawidget.setToInspect( this );

		mSearchMetawidget.setToInspect( mContactSearch );
		return mSearchMetawidget;
	}

	//
	// Inner class
	//

	static class ContactDialog
		extends Dialog
	{
		//
		// Constructor
		//

		public ContactDialog( Shell parent, int style )
		{
			super( parent, style );
		}

		//
		// Public methods
		//

		public Object open()
		{
			Shell parent = getParent();
			Shell shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
			shell.setLayout( new MigLayout( new LC().fill().debug( 500 ) ) );

			// Create Metawidget

			SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.None );
			metawidget.setLayoutData( new CC().grow() );
			metawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
			metawidget.setToInspect( new PersonalContact() );

			// Wait for close

			shell.open();
			Display display = parent.getDisplay();
			while ( !shell.isDisposed() )
			{
				if ( !display.readAndDispatch() )
					display.sleep();
			}
			return null;
		}
	}
}
