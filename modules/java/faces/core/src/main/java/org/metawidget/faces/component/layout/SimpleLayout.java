// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.faces.component.layout;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.layout.iface.Layout;

/**
 * Simple layout.
 * <p>
 * Just adds the component to the given container using <code>getChildren().add()</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleLayout
	implements Layout<UIComponent, UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public void layoutWidget( UIComponent widget, String elementName, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		List<UIComponent> children = container.getChildren();

		// Note: delegating to the Renderer to do the adding, such that it can decorate
		// the UIComponent if necessary (eg. adding a UIMessage) doesn't work out too well.
		// Specifically, the Renderer should not care whether a UIComponent is manually created
		// or overridden, but if it wraps a UIComponent with a UIStub then it needs to specify
		// whether the UIStub is for a manually created component or an overridden one, so that
		// UIMetawidget will clean it up again during startBuild. This just smells wrong,
		// because Renderers should render, not manipulate the UIComponent tree.

		children.add( widget );
	}
}
