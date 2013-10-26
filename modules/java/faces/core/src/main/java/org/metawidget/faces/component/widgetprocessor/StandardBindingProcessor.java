// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.faces.component.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor to add standard JSF value and action bindings to a UIComponent.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@SuppressWarnings( "deprecation" )
public class StandardBindingProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		String name = attributes.get( NAME );

		ValueBinding metawidgetValueBinding = metawidget.getValueBinding( "value" );
		String facesExpression = attributes.get( FACES_EXPRESSION );

		// Bind actions (if no action binding already)

		if ( ACTION.equals( elementName ) ) {
			ActionSource actionSource = (ActionSource) component;
			MethodBinding methodBinding = actionSource.getAction();

			if ( methodBinding == null ) {
				// If there is a faces-expression, use it...

				if ( facesExpression != null ) {
					methodBinding = application.createMethodBinding( facesExpression, null );
				}

				// ...otherwise try and construct a binding...

				else if ( name != null && !"".equals( name ) ) {
					if ( metawidgetValueBinding != null ) {
						String facesExpressionPrefix = FacesUtils.unwrapExpression( metawidgetValueBinding.getExpressionString() );
						facesExpression = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + name );
						methodBinding = application.createMethodBinding( facesExpression, null );
					}

					// ...or just use the raw value (for jBPM)

					else {
						methodBinding = application.createMethodBinding( name, null );
					}
				}

				if ( methodBinding != null ) {
					actionSource.setAction( methodBinding );
				}
			}

			return component;
		}

		// Bind properties (if no value binding already)

		ValueBinding valueBinding = component.getValueBinding( "value" );

		if ( valueBinding == null ) {
			// If there is a faces-expression, use it...

			String valueBindingExpression = facesExpression;

			if ( valueBindingExpression == null ) {
				// ...if we are at the top level...

				if ( ENTITY.equals( elementName ) ) {
					if ( metawidgetValueBinding != null ) {
						valueBindingExpression = metawidgetValueBinding.getExpressionString();
					}
				}

				// ...if we are not at the top level, try and construct the binding

				else if ( metawidgetValueBinding != null && name != null && !"".equals( name ) ) {
					String facesExpressionPrefix = FacesUtils.unwrapExpression( metawidgetValueBinding.getExpressionString() );
					valueBindingExpression = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + name );
				}
			}

			if ( valueBindingExpression != null ) {
				try {
					// JSF 1.2 mode: some components (such as
					// org.jboss.seam.core.Validators.validate()) expect ValueExpressions and do
					// not work with ValueBindings (see JBSEAM-3252)
					//
					// Note: we wrap the ValueExpression as an Object[] to stop link-time
					// dependencies on javax.el.ValueExpression, so that we still work with
					// JSF 1.1

					Object[] valueExpression = new Object[] { application.getExpressionFactory().createValueExpression( context.getELContext(), valueBindingExpression, Object.class ) };
					attachValueExpression( component, valueExpression[0], attributes );
				} catch ( NoSuchMethodError e ) {
					// JSF 1.1 mode

					attachValueBinding( component, application.createValueBinding( valueBindingExpression ), attributes );
				}
			}
		}

		return component;
	}

	//
	// Private methods
	//

	/**
	 * Attach value binding for component.
	 * <p>
	 * If the created component is a <code>UIStub</code>, we set the same value binding on
	 * <em>all</em> its children (as well as the <code>UIStub</code> itself). This allows us to
	 * build compound components, such as a <code>HtmlOutputText</code> combined with a
	 * <code>HtmlInputHidden</code>.
	 */

	private void attachValueBinding( UIComponent widget, ValueBinding valueBinding, Map<String, String> attributes ) {

		// Support stubs

		if ( widget instanceof UIStub ) {
			List<UIComponent> children = widget.getChildren();

			for ( UIComponent componentChild : children ) {
				attachValueBinding( componentChild, valueBinding, attributes );
			}
		}

		// Set binding

		widget.setValueBinding( "value", valueBinding );
	}

	/**
	 * JSF 1.2 version of attachValueBinding.
	 */

	private void attachValueExpression( UIComponent widget, Object valueExpression, Map<String, String> attributes ) {

		// Support stubs

		if ( widget instanceof UIStub ) {
			List<UIComponent> children = widget.getChildren();

			for ( UIComponent componentChild : children ) {
				attachValueExpression( componentChild, valueExpression, attributes );
			}
		}

		// Set binding

		widget.setValueExpression( "value", (javax.el.ValueExpression) valueExpression );
	}
}
