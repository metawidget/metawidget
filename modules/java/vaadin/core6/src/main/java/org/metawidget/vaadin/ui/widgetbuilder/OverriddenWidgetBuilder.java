// Metawidget (licensed under LGPL)
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
