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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class MapBindingProcessor
	implements AdvancedWidgetProcessor<Widget, GwtMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( GwtMetawidget metawidget ) {

		// Clear our state

		metawidget.putClientProperty( MapBindingProcessor.class, null );
	}

	public Widget processWidget( Widget widget, String elementName, Map<String, String> attributes, final GwtMetawidget metawidget ) {

		// Don't bind to Actions

		if ( ACTION.equals( elementName ) ) {
			return widget;
		}

		// Nested Metawidgets are not bound, only remembered

		if ( widget instanceof GwtMetawidget ) {
			State state = getState( metawidget );

			if ( state.nestedMetawidgets == null ) {
				state.nestedMetawidgets = new HashSet<GwtMetawidget>();
			}

			state.nestedMetawidgets.add( (GwtMetawidget) widget );
			return widget;
		}

		// MapBindingProcessor doesn't bind to Stubs or FlexTables

		if ( widget instanceof Stub || widget instanceof FlexTable ) {
			return widget;
		}

		String path = metawidget.getPath();

		if ( PROPERTY.equals( elementName ) ) {
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME );
		}

		try {
			if ( TRUE.equals( attributes.get( READ_ONLY ) ) ) {
				return widget;
			}

			State state = getState( metawidget );

			if ( state.bindings == null ) {
				state.bindings = new HashSet<String[]>();
			}

			state.bindings.add( PathUtils.parsePath( path ).getNamesAsArray() );
		} catch ( Exception e ) {
			Window.alert( path + ": " + e.getMessage() );
		}

		return widget;
	}

	public void save( GwtMetawidget metawidget ) {

		State state = getState( metawidget );

		// Our bindings

		if ( state.bindings != null ) {
			@SuppressWarnings( "unchecked" )
			Map<String, Object> model = (Map<String, Object>) metawidget.getToInspect();

			// For each bound property...

			for ( String[] binding : state.bindings ) {
				// ...fetch the value...

				Object value = metawidget.getValue( binding[binding.length - 1] );

				// ...and set it back to the model

				model.put( GwtUtils.toString( binding, '.' ), value );
			}
		}

		// Nested bindings

		if ( state.nestedMetawidgets != null ) {
			for ( GwtMetawidget nestedMetawidget : state.nestedMetawidgets ) {
				save( nestedMetawidget );
			}
		}
	}

	public void onEndBuild( GwtMetawidget metawidget ) {

		// Do nothing
	}

	//
	// Private methods
	//

	private State getState( GwtMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( MapBindingProcessor.class );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( MapBindingProcessor.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State {

		/* package private */Set<String[]>		bindings;

		/* package private */Set<GwtMetawidget>	nestedMetawidgets;
	}
}
