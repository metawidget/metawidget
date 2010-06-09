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

import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that sets the <code>immediate</code> attribute.
 *
 * @author Richard Kennard
 */

public class ImmediateAttributeProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		String immediateString = attributes.get( FACES_IMMEDIATE );

		if ( immediateString == null )
		{
			return component;
		}

		boolean immediate = Boolean.parseBoolean( immediateString );

		if ( component instanceof ActionSource )
		{
			( (ActionSource) component ).setImmediate( immediate );
			return component;
		}

		if ( component instanceof EditableValueHolder )
		{
			( (EditableValueHolder) component ).setImmediate( immediate );
			return component;
		}

		throw WidgetProcessorException.newException( "'Immediate' cannot be applied to " + component.getClass() );
	}
}
