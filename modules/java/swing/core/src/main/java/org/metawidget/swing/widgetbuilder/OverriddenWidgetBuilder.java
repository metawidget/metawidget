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
 * @author Richard Kennard
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
