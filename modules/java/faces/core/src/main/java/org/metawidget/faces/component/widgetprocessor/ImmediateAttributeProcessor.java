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

import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * WidgetProcessor that sets the <code>immediate</code> attribute.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ImmediateAttributeProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		String immediateString = attributes.get( FACES_IMMEDIATE );

		if ( immediateString == null ) {
			return component;
		}

		boolean immediate = Boolean.parseBoolean( immediateString );

		if ( component instanceof ActionSource ) {
			( (ActionSource) component ).setImmediate( immediate );
			return component;
		}

		if ( component instanceof EditableValueHolder ) {
			( (EditableValueHolder) component ).setImmediate( immediate );
			return component;
		}

		throw WidgetProcessorException.newException( "'Immediate' cannot be applied to " + component.getClass() );
	}
}
