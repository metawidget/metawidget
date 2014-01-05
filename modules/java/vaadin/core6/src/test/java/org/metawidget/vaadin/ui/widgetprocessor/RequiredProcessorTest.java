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

package org.metawidget.vaadin.ui.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;
import org.metawidget.vaadin.ui.widgetprocessor.RequiredProcessor;

import com.vaadin.ui.TextField;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RequiredProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		RequiredProcessor widgetProcessor = new RequiredProcessor();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		TextField textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		assertTrue( !textField.isRequired() );

		attributes.put( REQUIRED, FALSE );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		assertTrue( !textField.isRequired() );

		attributes.put( REQUIRED, TRUE );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, null );
		assertTrue( textField.isRequired() );
	}
}
