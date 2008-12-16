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

package org.metawidget.gwt.client.actionbinding.jsni;

import java.util.Map;

import org.metawidget.gwt.client.actionbinding.ActionBindingImpl;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.util.simple.PathUtils;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Action binding implementation based on JSNI.
 *
 * @author Richard Kennard
 */

public class JsniBinding
	extends ActionBindingImpl
{
	//
	// Constructor
	//

	public JsniBinding( GwtMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	public void bind( Widget widget, Map<String, String> attributes, String path )
	{
		// How can we bind without addClickListener?

		if ( !( widget instanceof FocusWidget ))
			throw new RuntimeException( "JsniBinding only supports binding actions to FocusWidgets" );

		// How can we bind to null?

		final Object toInspect = getMetawidget().getToInspect();

		if ( toInspect == null )
			return;

		// Bind the action

		String[] names = PathUtils.parsePath( path ).getNamesAsArray();
		final int last = names.length - 1;
		final String actionName = names[last];

		FocusWidget focusWidget = (FocusWidget) widget;
		focusWidget.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				// How can we reflect the full JS name of obj['@com.foo.Foo::action()']?

				if ( last > 0 )
					throw new RuntimeException( "JsniBinding does not support nested actions" );

				invokeMethod( toInspect, toInspect.getClass().getName(), actionName );
			}
		} );
	}

	//
	// Native methods
	//

	/**
	 * Invoke JavaScript method using special GWT naming convention
	 */

	native void invokeMethod( Object obj, String type, String method )
	/*-{
		obj['@' + type + '::' + method + '()']();
	}-*/;
}
