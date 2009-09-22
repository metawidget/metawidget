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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * WidgetProcessor that sets the <code>required</code> attribute.
 * <p>
 * Seam applications that use the <code>s:validateAll</code> tag should use this WidgetProcessor
 * <em>without</em> combining it with <code>StandardValidatorProcessor</code>. This is because
 * Seam's <code>s:validateAll</code> handles the validation process using Hibernate Validator and
 * does not work if the standard JSF validators are defined.
 *
 * @author Richard Kennard
 */

public class RequiredAttributeProcessor
	extends BaseWidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		// Only validate EditableValueHolders

		if ( !( component instanceof EditableValueHolder ))
			return component;

		if ( !TRUE.equals( attributes.get( REQUIRED ) ) )
			return component;

		EditableValueHolder editableValueHolder = (EditableValueHolder) component;

		// JSF 1.2 support

		try
		{
			Method method = editableValueHolder.getClass().getMethod( "setLabel", String.class );
			method.invoke( editableValueHolder, metawidget.getLabelString( attributes ) );
		}
		catch ( Exception e )
		{
			// Fail gracefully
		}

		// Required

		editableValueHolder.setRequired( true );
		return component;
	}
}
