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

package org.metawidget.example.gwt.clientside.client.ui;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.gwt.client.actionbinding.BaseActionBinding;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.util.simple.PathUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class AlertActionBinding
	extends BaseActionBinding
{
	//
	// Constructor
	//

	public AlertActionBinding( GwtMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	@Override
	public void bindAction( Widget widget, Map<String, String> attributes, final String path )
	{
		// How can we bind without addClickListener?

		if ( !( widget instanceof FocusWidget ) )
			throw new RuntimeException( "DialogBoxActionBinding only supports binding actions to FocusWidgets - '" + attributes.get( NAME ) + "' is using a " + widget.getClass().getName() );

		// Bind the action

		FocusWidget focusWidget = (FocusWidget) widget;
		focusWidget.addClickHandler( new ClickHandler()
		{
			public void onClick( ClickEvent event )
			{
				Window.alert( "AlertActionBinding detected button click for: " + PathUtils.parsePath( path ).getNames() );
			}
		} );
	}
}
