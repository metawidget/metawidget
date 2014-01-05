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

package org.metawidget.faces.component.widgetprocessor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * WidgetProcessor to set labels on a UIComponent.
 * <p>
 * Since JSF 1.2, some <code>UIComponents</code> support a <code>setLabel</code> method. This label
 * is used during validation errors and conversion errors (even if the <code>UIComponent</code> does
 * not explicitly have a converter).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LabelProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		Method method;

		try {
			method = component.getClass().getMethod( "setLabel", String.class );
		} catch ( NoSuchMethodException e ) {
			// Fail gracefully for JSF 1.1

			return component;
		}

		try {
			method.invoke( component, metawidget.getLabelString( attributes ) );
			return component;
		} catch ( Exception e ) {
			throw WidgetProcessorException.newException( e );
		}
	}
}
