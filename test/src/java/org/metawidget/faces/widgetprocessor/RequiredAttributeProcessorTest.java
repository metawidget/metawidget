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

package org.metawidget.faces.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;

import junit.framework.TestCase;

import org.metawidget.faces.component.widgetprocessor.RequiredAttributeProcessor;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class RequiredAttributeProcessorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception
	{
		RequiredAttributeProcessor processor = new RequiredAttributeProcessor();

		UIComponent component = new HtmlInputText();
		EditableValueHolder editableValueHolder = (EditableValueHolder) component;

		// Not required? Don't set the flag

		Map<String, String> attributes = CollectionUtils.newHashMap();
		processor.processWidget( component, PROPERTY, attributes, null );
		assertTrue( !editableValueHolder.isRequired() );

		// Required? Set the flag

		attributes.put( REQUIRED, TRUE );
		processor.processWidget( component, PROPERTY, attributes, null );
		assertTrue( editableValueHolder.isRequired() );
	}
}
