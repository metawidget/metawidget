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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that sets the <code>required</code> attribute.
 * <p>
 * Seam applications that use the <code>s:validateAll</code> tag should use this WidgetProcessor
 * <em>without</em> combining it with <code>StandardValidatorProcessor</code>. This is because
 * Seam's <code>s:validateAll</code> handles the validation process using Hibernate Validator and
 * does not work if the standard JSF validators are defined.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RequiredAttributeProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Only validate EditableValueHolders (ie. no labels, no Stubs)

		if ( !( component instanceof EditableValueHolder ) ) {
			return component;
		}

		if ( !TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return component;
		}

		EditableValueHolder editableValueHolder = (EditableValueHolder) component;

		// JSF 1.2 support

		try {
			Method method = editableValueHolder.getClass().getMethod( "setLabel", String.class );
			method.invoke( editableValueHolder, metawidget.getLabelString( attributes ) );
		} catch ( Exception e ) {
			// Fail gracefully
		}

		// Required

		editableValueHolder.setRequired( true );
		return component;
	}
}
