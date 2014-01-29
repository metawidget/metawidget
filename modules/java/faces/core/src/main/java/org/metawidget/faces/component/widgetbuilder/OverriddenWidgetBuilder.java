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

package org.metawidget.faces.component.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for overridden widgets in Java Server Faces environments.
 * <p>
 * Locates overridden widgets based on matching value binding.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@SuppressWarnings( "deprecation" )
public class OverriddenWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Metawidget has no valueBinding? Not overridable, then

		ValueBinding metawidgetValueBinding = metawidget.getValueBinding( "value" );

		if ( metawidgetValueBinding == null ) {
			return null;
		}

		// Actions

		String binding = attributes.get( FACES_EXPRESSION );

		if ( ACTION.equals( elementName ) ) {
			if ( binding == null ) {
				String facesExpressionPrefix = FacesUtils.unwrapExpression( metawidgetValueBinding.getExpressionString() );
				binding = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) );
			}

			return findRenderedComponentWithMethodBinding( metawidget, binding );
		}

		// Properties

		if ( binding == null ) {
			if ( ENTITY.equals( elementName ) ) {
				binding = metawidgetValueBinding.getExpressionString();
			} else {
				String facesExpressionPrefix = FacesUtils.unwrapExpression( metawidgetValueBinding.getExpressionString() );
				binding = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) );
			}
		}

		return findRenderedComponentWithValueBinding( metawidget, binding );
	}

	//
	// Private methods
	//

	/**
	 * Finds the child component of the given component that is both rendered and has the given
	 * value expression.
	 */

	public UIComponent findRenderedComponentWithValueBinding( UIComponent component, String expressionString ) {

		// Try to find a child...

		for ( UIComponent child : component.getChildren() ) {

			// ...with the binding we're interested in

			ValueBinding childValueBinding = child.getValueBinding( "value" );

			if ( childValueBinding != null ) {

				// (note: ValueBinding.equals() does not compare expression strings)

				if ( expressionString.equals( childValueBinding.getExpressionString() ) && child.isRendered() ) {
					return child;
				}
			}

			// Recurse into section decorators. This is only needed if we have components marked
			// COMPONENT_ATTRIBUTE_NOT_RECREATABLE (such as a SelectInputDate) inside a section,
			// because these will survive POSTback
			//
			// Note: we must be careful not to recurse into arbitrary tags, such as
			// UISelectOneMenu which may have child UISelectItems that coincidentally match
			// our value binding

			if ( child.getAttributes().containsKey( UIMetawidget.COMPONENT_ATTRIBUTE_SECTION_DECORATOR ) ) {

				UIComponent found = findRenderedComponentWithValueBinding( child, expressionString );

				if ( found != null ) {
					return found;
				}
			}
		}

		return null;
	}

	/**
	 * Finds the child component of the given component that is both rendered and has the given
	 * method expression.
	 */

	public UIComponent findRenderedComponentWithMethodBinding( UIComponent component, String expressionString ) {

		// Try to find a child...

		for ( UIComponent child : component.getChildren() ) {

			if ( !( child instanceof ActionSource ) ) {
				continue;
			}

			// ...with the binding we're interested in

			MethodBinding childMethodBinding = ( (ActionSource) child ).getAction();

			if ( childMethodBinding == null ) {
				continue;
			}

			// (note: MethodBinding.equals() does not compare expression strings)

			if ( expressionString.equals( childMethodBinding.getExpressionString() ) && child.isRendered() ) {
				return child;
			}

			// Recurse into section decorators. This is only needed if we have components marked
			// COMPONENT_ATTRIBUTE_NOT_RECREATABLE (such as a SelectInputDate) inside a section,
			// because these will survive POSTback
			//
			// Note: we must be careful not to recurse into arbitrary tags which may coincidentally
			// match our method binding

			if ( child.getAttributes().containsKey( UIMetawidget.COMPONENT_ATTRIBUTE_SECTION_DECORATOR ) ) {

				UIComponent found = findRenderedComponentWithValueBinding( child, expressionString );

				if ( found != null ) {
					return found;
				}
			}
		}

		return null;
	}
}
