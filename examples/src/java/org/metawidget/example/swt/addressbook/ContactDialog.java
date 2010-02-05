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

import static org.metawidget.inspector.InspectionResultConstants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.commons.jexl.UiJexlAttribute;
import org.metawidget.swt.Facet;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.layout.RowLayout;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessor;

/**
 * @author Richard Kennard
 */

public class ContactDialog
	extends Dialog
{
	//
	// Private members
	//

	private Shell				mShell;

	private ContactsController	mContactsController;

	private SwtMetawidget		mContactMetawidget;

	private SwtMetawidget		mButtonsMetawidget;

	//
	// Constructor
	//

	public ContactDialog( Shell parent, ContactsController contactsController, int style )
	{
		super( parent, style );

		mContactsController = contactsController;
	}

	//
	// Public methods
	//

	public Object open( Contact contact )
	{
		mShell = new Shell( getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		mShell.setLayout( new FillLayout() );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 )
			builder.append( " - " );

		if ( contact instanceof PersonalContact )
		{
			builder.append( "Personal Contact" );
		}
		else
		{
			builder.append( "Business Contact" );
		}

		mShell.setText( builder.toString() );

		// Metawidget

		mContactMetawidget = new SwtMetawidget( mShell, SWT.NONE );
		mContactMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
		mContactMetawidget.setToInspect( contact );
		mContactMetawidget.setReadOnly( contact.getId() != 0 );

		// Embedded buttons

		Facet facetButtons = new Facet( mContactMetawidget, SWT.BORDER );
		facetButtons.setData( "name", "buttons" );
		facetButtons.setLayout( new GridLayout() );

		mButtonsMetawidget = new SwtMetawidget( facetButtons, SWT.BORDER );
		mButtonsMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
		mButtonsMetawidget.setMetawidgetLayout( new RowLayout() );
		mButtonsMetawidget.setToInspect( this );
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.CENTER;
		mButtonsMetawidget.setLayoutData( data );

		// Wait for close

		mShell.open();
		Display display = getParent().getDisplay();
		while ( !mShell.isDisposed() )
		{
			if ( !display.readAndDispatch() )
				display.sleep();
		}
		return null;
	}

	@UiHidden
	public boolean isReadOnly()
	{
		return mContactMetawidget.isReadOnly();
	}

	@UiHidden
	public boolean isNewContact()
	{
		if ( mContactMetawidget.getToInspect() == null )
			return true;

		return ( ( (Contact) mContactMetawidget.getToInspect() ).getId() == 0 );
	}

	@UiAction
	@UiJexlAttribute( name = HIDDEN, expression = "!this.readOnly" )
	public void edit()
	{
		mContactMetawidget.setReadOnly( false );

		mButtonsMetawidget.setToInspect( mButtonsMetawidget.getToInspect() );
	}

	@UiAction
	@UiJexlAttribute( name = HIDDEN, expression = "this.readOnly" )
	public void save()
	{
		try
		{
			mContactMetawidget.getWidgetProcessor( DataBindingProcessor.class ).save( mContactMetawidget );
			Contact contact = mContactMetawidget.getToInspect();

			mContactsController.save( contact );
		}
		catch ( Exception e )
		{
			MessageBox messageBox = new MessageBox( getParent(), SWT.ICON_ERROR | SWT.OK );
			messageBox.setText( "Save Error" );
			messageBox.setMessage( e.getMessage() );

			messageBox.open();
			return;
		}

		mShell.dispose();
	}

	@UiAction
	@UiComesAfter( "save" )
	@UiJexlAttribute( name = HIDDEN, expression = "this.readOnly || this.newContact" )
	public void delete()
	{
		Contact contact = mContactMetawidget.getToInspect();

		MessageBox messageBox = new MessageBox( getParent(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL );
		messageBox.setText( getText() );
		messageBox.setMessage( "Sure you want to delete this contact?" );

		if ( messageBox.open() != SWT.OK )
			return;

		mContactsController.delete( contact );
		mShell.dispose();
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiJexlAttribute( name = LABEL, expression = "if ( this.readOnly ) 'Back'" )
	public void cancel()
	{
		mShell.dispose();
	}
}
