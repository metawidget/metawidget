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

import java.util.Map;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * WidgetProcessor that adds CSS styles to a UIComponent.
 *
 * @author Richard Kennard
 */

public class CssStyleProcessor
	extends BaseWidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		// Note: this only applies the styles to UIStubs at the top-level. In practice, this seemed
		// to give more 'expected' behaviour than drilling into the UIStubs and applying the styles
		// to all their subcomponents too

		Map<String, Object> componentAttributes = component.getAttributes();
		String style = ( (HtmlMetawidget) metawidget ).getStyle();
		String styleClass = ( (HtmlMetawidget) metawidget ).getStyleClass();

		if ( style != null )
		{
			String existingStyle = (String) componentAttributes.get( "style" );

			if ( existingStyle == null || "".equals( existingStyle ) )
				componentAttributes.put( "style", style );
			else
				componentAttributes.put( "style", existingStyle + " " + style );
		}

		if ( styleClass != null )
		{
			String existingStyleClass = (String) componentAttributes.get( "styleClass" );

			if ( existingStyleClass == null || "".equals( existingStyleClass ) )
				componentAttributes.put( "styleClass", styleClass );
			else
				componentAttributes.put( "styleClass", existingStyleClass + " " + styleClass );
		}

		return component;
	}
}
