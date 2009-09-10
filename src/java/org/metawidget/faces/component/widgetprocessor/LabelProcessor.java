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

package org.metawidget.faces.component.widgetprocessor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * WidgetProcessor to set labels on a UIComponent.
 * <p>
 * Since JSF 1.2, some <code>UIComponents</code> support a <code>setLabel</code> method. This label
 * is used during validation errors and conversion errors (even if the <code>UIComponent</code> does
 * not explicitly have a converter).
 *
 * @author Richard Kennard
 */

public class LabelProcessor
	extends BaseWidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		Method method;

		try
		{
			method = component.getClass().getMethod( "setLabel", String.class );
		}
		catch( NoSuchMethodException e )
		{
			// Fail gracefully for JSF 1.1

			return component;
		}

		try
		{
			method.invoke( component, metawidget.getLabelString( FacesContext.getCurrentInstance(), attributes ) );
			return component;
		}
		catch ( Exception e )
		{
			throw WidgetProcessorException.newException( e );
		}
	}
}
