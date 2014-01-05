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

package org.metawidget.swing.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for overridden widgets in Swing environments.
 * <p>
 * Locates overridden widgets based on their <code>name</code>. To support overriding nested
 * widgets, this includes names that are paths (eg. <code>foo/bar/baz</code>).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<JComponent, SwingMetawidget> {

	//
	// Public methods
	//

	public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		String name = attributes.get( NAME );

		if ( name == null ) {
			return null;
		}

		SwingMetawidget metawidgetToUse = metawidget;

		while ( true ) {

			// Search for an overridden component

			List<JComponent> existingUnusedComponents = metawidgetToUse.fetchExistingUnusedComponents();

			Component component = null;

			for ( Component componentExisting : existingUnusedComponents ) {
				if ( name.equals( componentExisting.getName() ) ) {
					component = componentExisting;
					break;
				}
			}

			if ( component != null ) {
				existingUnusedComponents.remove( component );
				return (JComponent) component;
			}

			// If no overridden components found, but we have a parent path...

			if ( ENTITY.equals( elementName ) ) {
				return null;
			}

			String[] names = PathUtils.parsePath( metawidgetToUse.getPath() ).getNamesAsArray();

			if ( names.length == 0 ) {
				break;
			}

			// ...traverse up the parent SwingMetawidgets

			name = names[names.length - 1] + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + name;

			if ( !( metawidgetToUse.getParent() instanceof SwingMetawidget ) ) {
				return null;
			}

			metawidgetToUse = (SwingMetawidget) metawidgetToUse.getParent();
		}

		return null;
	}
}
