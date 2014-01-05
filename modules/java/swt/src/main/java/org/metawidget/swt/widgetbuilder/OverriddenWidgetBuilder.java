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

package org.metawidget.swt.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for overridden widgets in SWT environments.
 * <p>
 * Locates overridden widgets based on a <code>name</code> attribute in their <code>setData</code>.
 * <p>
 * Because of the way SWT requires <code>Control</code>s to nominate their <code>Composite</code>
 * up-front (ie. in their constructor), the behaviour of <code>OverriddenWidgetBuilder</code> is
 * limited to only overriding <code>Control</code>s within their immediate <code>Composite</code>.
 * Therefore <code>OverriddenWidgetBuilder</code> will not work if used to override
 * <code>Control</code>s that are laid out inside, say, a <code>TabFolderLayoutDecorator</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<Control, SwtMetawidget> {

	//
	// Public methods
	//

	public Control buildWidget( String elementName, Map<String, String> attributes, SwtMetawidget metawidget ) {

		String name = attributes.get( NAME );

		if ( name == null ) {
			return null;
		}

		for ( Control componentExisting : metawidget.getChildren() ) {
			if ( name.equals( componentExisting.getData( NAME ) ) ) {
				return componentExisting;
			}
		}

		return null;
	}
}
