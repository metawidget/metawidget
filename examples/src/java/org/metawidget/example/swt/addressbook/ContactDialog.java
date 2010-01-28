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
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.commons.jexl.UiJexlAttribute;
import org.metawidget.swt.Facet;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.layout.RowLayout;

/**
 * @author Richard Kennard
 */

public class ContactDialog
	extends Dialog
{
	//
	// Private members
	//

	private SwtMetawidget	mContactMetawidget;

	private SwtMetawidget	mButtonsMetawidget;

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

	public Object open( Contact contact )
	{
		Shell parent = getParent();
		Shell shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		shell.setLayout( new MigLayout( new LC().fill().debug( 500 ) ) );

		// Metawidget

		mContactMetawidget = new SwtMetawidget( shell, SWT.None );
		mContactMetawidget.setLayoutData( new CC().grow() );
		mContactMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );

		// Embedded buttons

		Facet facetButtons = new Facet( mContactMetawidget, SWT.None );
		facetButtons.setData( "name", "buttons" );

		mButtonsMetawidget = new SwtMetawidget( facetButtons, SWT.None );
		mButtonsMetawidget.setMetawidgetLayout( new RowLayout() );
		mButtonsMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
		mButtonsMetawidget.setToInspect( this );

		mContactMetawidget.setToInspect( contact );

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
		// Nop
	}

	@UiAction
	@UiComesAfter( "save" )
	@UiJexlAttribute( name = HIDDEN, expression = "this.readOnly || this.newContact" )
	public void delete()
	{
		// Nop
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiJexlAttribute( name = LABEL, expression = "if ( this.readOnly ) 'Back'" )
	public void cancel()
	{
		mContactMetawidget.getParent().dispose();
	}
}
