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
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
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

package org.metawidget.example.gwt.clientside.client.ui;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class AlertActionProcessor
	implements WidgetProcessor<Widget, GwtMetawidget> {

	//
	// Public methods
	//

	public Widget processWidget( Widget widget, final String elementName, final Map<String, String> attributes, final GwtMetawidget metawidget ) {

		// Only bind to Actions

		if ( !ACTION.equals( elementName ) ) {
			return widget;
		}

		// How can we bind without addClickListener?

		if ( !( widget instanceof FocusWidget ) ) {
			throw new RuntimeException( "AlertActionProcessor only supports binding actions to FocusWidgets - '" + attributes.get( NAME ) + "' is using a " + widget.getClass().getName() );
		}

		@SuppressWarnings( "unchecked" )
		final Map<String, Object> model = (Map<String, Object>) metawidget.getToInspect();
		final Widget parent = metawidget.getParent();

		// Bind the action

		FocusWidget focusWidget = (FocusWidget) widget;
		focusWidget.addClickHandler( new ClickHandler() {

			public void onClick( ClickEvent event ) {

				String names = PathUtils.parsePath( metawidget.getPath() ).getNames();

				if ( names.length() != 0 ) {
					names += StringUtils.SEPARATOR_DOT_CHAR;
				}

				names += attributes.get( NAME );

				model.put( names, "clicked" );

				// (do not Window.alert during unit tests)

				if ( parent instanceof RootPanel ) {
					Window.alert( "AlertActionProcessor detected button click for: " + names );
				}
			}
		} );

		return widget;
	}
}
