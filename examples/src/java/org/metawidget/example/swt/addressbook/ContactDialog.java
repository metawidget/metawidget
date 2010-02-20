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

import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.commons.jexl.UiJexlAttribute;
import org.metawidget.swt.Facet;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.layout.FillLayout;
import org.metawidget.swt.layout.RowLayout;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class ContactDialog
	extends Dialog
{
	//
	// Private members
	//

	private Shell						mShell;

	private Main						mMain;

	/* package private */SwtMetawidget	mContactMetawidget;

	/* package private */Table			mCommunicationsTable;

	private SwtMetawidget				mButtonsMetawidget;

	//
	// Constructor
	//

	public ContactDialog( Main main )
	{
		super( main.getShell(), SWT.NONE );

		mMain = main;
	}

	//
	// Public methods
	//

	public Object open( final Contact contact )
	{
		mShell = new Shell( getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		mShell.setSize( 800, 600 );

		// Background

		mShell.setBackgroundMode( SWT.INHERIT_DEFAULT );
		mShell.setBackground( new Color( mShell.getDisplay(), 255, 255, 255 ) );
		mShell.setBackgroundImage( new Image( mShell.getDisplay(), ClassLoader.getSystemResourceAsStream( "org/metawidget/example/shared/addressbook/media/background.jpg" ) ) );

		mShell.setLayout( new GridLayout( 2, false ) );

		// Left-hand image

		Label imageLabel = new Label( mShell, SWT.None );
		GridData data = new GridData();
		data.verticalAlignment = SWT.TOP;
		imageLabel.setLayoutData( data );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 )
			builder.append( " - " );

		// Personal/business icon

		if ( contact instanceof PersonalContact )
		{
			builder.append( "Personal Contact" );
			imageLabel.setImage( new Image( mShell.getDisplay(), ClassLoader.getSystemResourceAsStream( "org/metawidget/example/shared/addressbook/media/personal.gif" ) ) );
		}
		else
		{
			builder.append( "Business Contact" );
			imageLabel.setImage( new Image( mShell.getDisplay(), ClassLoader.getSystemResourceAsStream( "org/metawidget/example/shared/addressbook/media/business.gif" ) ) );
		}

		mShell.setText( builder.toString() );

		// Metawidget

		mContactMetawidget = new SwtMetawidget( mShell, SWT.NONE );
		mContactMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
		mContactMetawidget.setToInspect( contact );
		mContactMetawidget.setReadOnly( contact.getId() != 0 );
		data = new GridData();
		data.verticalAlignment = SWT.FILL;
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		mContactMetawidget.setLayoutData( data );

		// Communications override

		mCommunicationsTable = new Table( mContactMetawidget, SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.V_SCROLL );
		mCommunicationsTable.setData( "name", "communications" );
		mCommunicationsTable.setHeaderVisible( true );
		mCommunicationsTable.setLinesVisible( true );
		TableColumn column = new TableColumn( mCommunicationsTable, SWT.NONE );
		column.setText( "Id" );
		column.setWidth( 0 );
		column.setResizable( false );
		column = new TableColumn( mCommunicationsTable, SWT.NONE );
		column.setText( "Type" );
		column.setWidth( 275 );
		column = new TableColumn( mCommunicationsTable, SWT.NONE );
		column.setText( "Value" );
		column.setWidth( 275 );

		final TableEditor communicationEditor = new TableEditor( mCommunicationsTable );
		communicationEditor.horizontalAlignment = SWT.LEFT;
		communicationEditor.grabHorizontal = true;

		mCommunicationsTable.addMouseListener( new MouseAdapter()
		{
			@Override
			public void mouseDown( MouseEvent event )
			{
				if ( mContactMetawidget.isReadOnly() )
					return;

				// Commit any previous editor control

				if ( communicationEditor.getEditor() != null )
				{
					SwtMetawidget communicationMetawidget = (SwtMetawidget) communicationEditor.getEditor();
					communicationMetawidget.getWidgetProcessor( DataBindingProcessor.class ).save( communicationMetawidget );
					communicationMetawidget.dispose();
					fireRefresh();
				}

				// Identify the selected row...

				Point point = new Point( event.x, event.y );
				TableItem item = mCommunicationsTable.getItem( point );

				if ( item == null )
					return;

				// ...and column...

				int selectedColumn = -1;

				for ( int loop = 0, length = mCommunicationsTable.getColumnCount(); loop < length; loop++ )
				{
					Rectangle rect = item.getBounds( loop );
					if ( rect.contains( point ) )
					{
						selectedColumn = loop;
						break;
					}
				}

				// ...if any...

				if ( selectedColumn == -1 )
					return;

				// ...load the Communication...

				Communication communication = null;
				long communicationId = Long.valueOf( item.getText( 0 ) );

				if ( communicationId == 0 )
				{
					communication = new Communication();
				}
				else
				{
					for ( Communication existingCommunication : contact.getCommunications() )
					{
						if ( existingCommunication.getId() == communicationId )
						{
							communication = existingCommunication;
							break;
						}
					}
				}

				// ...and create the appropriate control

				SwtMetawidget communicationMetawidget = new SwtMetawidget( mCommunicationsTable, SWT.NONE );
				communicationMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
				communicationMetawidget.setMetawidgetLayout( new FillLayout() );
				communicationMetawidget.setToInspect( communication );
				String columnName = StringUtils.lowercaseFirstLetter( mCommunicationsTable.getColumn( selectedColumn ).getText() );
				communicationMetawidget.setInspectionPath( Communication.class.getName() + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + columnName );

				communicationEditor.setEditor( communicationMetawidget, item, selectedColumn );
			}
		} );

		fireRefresh();

		// Embedded buttons

		Facet facetButtons = new Facet( mContactMetawidget, SWT.NONE );
		facetButtons.setData( "name", "buttons" );
		facetButtons.setLayout( new GridLayout() );

		mButtonsMetawidget = new SwtMetawidget( facetButtons, SWT.NONE );
		mButtonsMetawidget.setConfig( "org/metawidget/example/swt/addressbook/metawidget.xml" );
		mButtonsMetawidget.setMetawidgetLayout( new RowLayout() );
		mButtonsMetawidget.setToInspect( this );
		data = new GridData();
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

	public void fireRefresh()
	{
		Set<Communication> communications = ( (Contact) mContactMetawidget.getToInspect() ).getCommunications();

		if ( communications == null )
			return;

		List<Communication> communicationsAsList = CollectionUtils.newArrayList( communications );

		// Add blank entry at bottom

		if ( communicationsAsList.isEmpty() || communicationsAsList.get(  communicationsAsList.size() - 1 ).getType() != null )
			communicationsAsList.add( new Communication() );

		int loop = 0;

		for ( ; loop < communicationsAsList.size(); loop++ )
		{
			// Add/edit row...

			TableItem item;

			if ( loop < mCommunicationsTable.getItemCount() )
				item = mCommunicationsTable.getItem( loop );
			else
				item = new TableItem( mCommunicationsTable, SWT.NONE );

			// ...with contact text

			Communication communication = communicationsAsList.get( loop );
			item.setText( 0, StringUtils.quietValueOf( communication.getId() ) );
			item.setText( 1, StringUtils.quietValueOf( communication.getType() ));
			item.setText( 2, StringUtils.quietValueOf( communication.getValue() ));
		}

		// Delete hanging rows

		if ( loop < mCommunicationsTable.getItemCount() )
			mCommunicationsTable.remove( loop, mCommunicationsTable.getItemCount() - 1 );
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

		// setReadOnly invalidates the widget, but because SWT uses heavyweight, native components
		// there is no 'paint' event unless explicitly requested

		mContactMetawidget.redraw();

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

			mMain.getContactsController().save( contact );
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
		mMain.fireRefresh();
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

		mMain.getContactsController().delete( contact );
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
