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
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * WidgetProcessor that adds a hidden input field for hidden values.
 * <p>
 * Note: passing values via <code>&lt;input type="hidden"&gt;</code> tags is a potential security
 * risk: they can be modified by malicious clients before being returned to the server.
 *
 * @author Richard Kennard
 */

public class HiddenFieldProcessor
	extends BaseWidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public UIComponent onAdd( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		// Attributes without setters cannot use a hidden field

		if ( TRUE.equals( attributes.get( NO_SETTER )))
			return component;

		// UIInputs do not need a hidden field (they already POST back)

		if ( component instanceof UIInput )
			return component;

		Application application = FacesContext.getCurrentInstance().getApplication();

		// Empty stubs become hidden fields directly

		if ( component instanceof UIStub && component.getChildCount() == 0 )
			return application.createComponent( "javax.faces.HtmlInputHidden" );

		// Other components get wrapped in a Stub

		UIComponent componentStub = application.createComponent( "org.metawidget.Stub" );

		List<UIComponent> children = componentStub.getChildren();

		children.add( application.createComponent( "javax.faces.HtmlInputHidden" ) );
		children.add( component );

		return componentStub;
	}
}
