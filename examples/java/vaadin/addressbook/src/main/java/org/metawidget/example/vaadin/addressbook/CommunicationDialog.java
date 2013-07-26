// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
// be used to endorse or promote products derived from this software without
// specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.example.vaadin.addressbook;

import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.vaadin.ui.Facet;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
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
		saveButton.addClickListener( new ClickListener() {

			@Override
			public void buttonClick( ClickEvent event ) {

				try {
					metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
					contactDialog.addCommunication( communication );
					( (UI) getParent() ).removeWindow( CommunicationDialog.this );
				} catch ( Exception e ) {
					if ( Page.getCurrent() != null ) {
						Notification.show( "Save Error", e.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE );
					}
					return;
				}
			}
		} );

		final Button cancelButton = new Button( "Cancel" );
		cancelButton.addClickListener( new ClickListener() {

			@Override
			public void buttonClick( ClickEvent event ) {

				( (UI) getParent() ).removeWindow( CommunicationDialog.this );
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
		( (VerticalLayout) facetButtons.getContent() ).addComponent( layout );
		( (com.vaadin.ui.VerticalLayout) facetButtons.getContent() ).setComponentAlignment( layout, Alignment.MIDDLE_CENTER );

		metawidget.addComponent( facetButtons );

		metawidget.addStyleName( "dialog" );
		setContent( metawidget );
	}
}
