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

package org.metawidget.faces.component.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds HTML <code>&lt;input type="hidden"&gt;</code> tags to hidden and
 * read-only values, so that they POST back.
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author Richard Kennard
 */

public class HiddenFieldProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Ignore actions

		if ( ACTION.equals( elementName ) ) {
			return component;
		}

		// UIInputs do not need a hidden field (they already POST back)

		if ( component instanceof UIInput ) {
			return component;
		}

		// Ignore nested Metawidgets

		if ( component instanceof UIMetawidget ) {
			return component;
		}

		// Attributes without setters cannot use a hidden field

		if ( TRUE.equals( attributes.get( NO_SETTER ) ) ) {
			return component;
		}

		// Ignore manually overridden components

		if ( component.getAttributes().containsKey( UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE ) ) {
			return component;
		}

		return wrapWithHiddenField( component, attributes );
	}

	//
	// Protected methods
	//

	/**
	 * @param attributes
	 *            attributes of the widget. Never null
	 */

	protected UIComponent wrapWithHiddenField( UIComponent component, Map<String, String> attributes ) {

		Application application = FacesContext.getCurrentInstance().getApplication();

		if ( component instanceof UIStub ) {
			// Empty stubs become hidden fields directly

			if ( component.getChildCount() == 0 ) {
				return application.createComponent( "javax.faces.HtmlInputHidden" );
			}

			// We cannot say whether non-empty Stubs will POST-back by themselves (unless we
			// recursed them)

			return component;
		}

		// Other components get wrapped in a Stub

		UIComponent componentStub = application.createComponent( "org.metawidget.Stub" );

		List<UIComponent> children = componentStub.getChildren();

		children.add( application.createComponent( "javax.faces.HtmlInputHidden" ) );
		children.add( component );

		return componentStub;
	}
}
