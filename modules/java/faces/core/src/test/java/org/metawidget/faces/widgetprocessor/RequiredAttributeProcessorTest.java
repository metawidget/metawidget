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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RequiredAttributeProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		RequiredAttributeProcessor processor = new RequiredAttributeProcessor();

		UIComponent component = new HtmlInputText();
		EditableValueHolder editableValueHolder = (EditableValueHolder) component;

		// Not required? Don't set the flag

		Map<String, String> attributes = CollectionUtils.newHashMap();
		processor.processWidget( component, PROPERTY, attributes, null );
		assertFalse( editableValueHolder.isRequired() );

		// Required? Set the flag

		attributes.put( REQUIRED, TRUE );
		processor.processWidget( component, PROPERTY, attributes, null );
		assertTrue( editableValueHolder.isRequired() );
	}
}
