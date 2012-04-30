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

package org.metawidget.example.vaadin.addressbook;

import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.vaadin.Facet;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Dialog for editing Communications.
 *
 * @author Loghman Barari
 */

public class CommunicationDialog
	extends Window {

	//
	// Constructor
	//

	public CommunicationDialog( final ContactDialog contactDialog, final Communication communication ) {

		setCaption( "Communication" );
		setWidth( "350px" );
		setHeight( "175px" );
		setResizable( false );

		// Metawidget

		final VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setWidth( "100%" );
		metawidget.setConfig( "org/metawidget/example/vaadin/addressbook/metawidget.xml" );
		metawidget.setToInspect( communication );

		// Buttons

		final Button saveButton = new Button( "Save" );
		saveButton.addListener( new ClickListener() {

			public void buttonClick( ClickEvent event ) {

				try {
					metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
					contactDialog.addCommunication( communication );
					getParent().removeWindow( CommunicationDialog.this );
				} catch ( Exception e ) {
					showNotification( "Save Error", e.getLocalizedMessage(), Notification.TYPE_ERROR_MESSAGE );
					return;
				}
			}
		} );

		final Button cancelButton = new Button( "Cancel" );
		cancelButton.addListener( new ClickListener() {

			public void buttonClick( ClickEvent event ) {

				getParent().removeWindow( CommunicationDialog.this );
			}
		} );

		Facet facetButtons = new Facet();
		facetButtons.setData( "buttons" );
		facetButtons.setWidth( "100%" );
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin( false );
		layout.setSpacing( true );
		layout.addComponent( saveButton );
		layout.addComponent( cancelButton );
		facetButtons.addComponent( layout );
		( (com.vaadin.ui.VerticalLayout) facetButtons.getContent() ).setComponentAlignment( layout, Alignment.MIDDLE_CENTER );

		metawidget.addComponent( facetButtons );
		addComponent( metawidget );
		( (VerticalLayout) getContent() ).setMargin( true, true, false, true );
	}
}
