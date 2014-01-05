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

package org.metawidget.vaadin.ui.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.text.MessageFormat;
import java.util.Map;

import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * WidgetProcessor that sets the 'required' property on a Vaadin Field.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RequiredProcessor
	implements WidgetProcessor<Component, VaadinMetawidget> {

	//
	// Public methods
	//

	public Component processWidget( Component component, String elementName, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		if ( !( component instanceof Field )) {
			return component;
		}

		Field field = (Field) component;

		if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
			field.setRequired( true );
			field.setRequiredError( MessageFormat.format( "{0} is required", field.getCaption() ) );
		}

		return component;
	}
}
