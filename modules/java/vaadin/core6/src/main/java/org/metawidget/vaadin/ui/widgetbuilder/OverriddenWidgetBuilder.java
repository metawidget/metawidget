// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * WidgetBuilder for overridden widgets in Vaadin environments.
 * <p>
 * Locates overridden widgets based on their <code>setData</code>.
 *
 * @author Loghman Barari
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<Component, VaadinMetawidget> {

	//
	// Public methods
	//

	public Component buildWidget( String elementName, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		String name = attributes.get( NAME );

		if ( name == null ) {
			return null;
		}

		Component component = null;
		List<AbstractComponent> existingUnusedComponents = metawidget.fetchExistingUnusedComponents();

		for ( AbstractComponent componentExisting : existingUnusedComponents ) {
			if ( name.equals( componentExisting.getData() ) ) {
				component = componentExisting;
				break;
			}
		}

		if ( component != null ) {
			existingUnusedComponents.remove( component );
		}

		return component;
	}
}
