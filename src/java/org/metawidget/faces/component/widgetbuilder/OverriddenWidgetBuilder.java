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

package org.metawidget.faces.component.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for overridden widgets in Java Server Faces environments.
 * <p>
 * Locates overridden widgets based on matchin value binding.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class OverriddenWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		// Metawidget has no valueBinding? Not overridable, then

		ValueBinding metawidgetValueBinding = metawidget.getValueBinding( "value" );

		if ( metawidgetValueBinding == null )
		{
			return null;
		}

		// Actions

		String binding = attributes.get( FACES_EXPRESSION );

		if ( ACTION.equals( elementName ) )
		{
			if ( binding == null )
			{
				String facesExpressionPrefix = FacesUtils.unwrapExpression( metawidgetValueBinding.getExpressionString() );
				binding = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) );
			}

			return FacesUtils.findRenderedComponentWithMethodBinding( metawidget, binding );
		}

		// Properties

		if ( binding == null )
		{
			if ( ENTITY.equals( elementName ) )
			{
				binding = metawidgetValueBinding.getExpressionString();
			}
			else
			{
				String facesExpressionPrefix = FacesUtils.unwrapExpression( metawidgetValueBinding.getExpressionString() );
				binding = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) );
			}
		}

		return FacesUtils.findRenderedComponentWithValueBinding( metawidget, binding );
	}
}
