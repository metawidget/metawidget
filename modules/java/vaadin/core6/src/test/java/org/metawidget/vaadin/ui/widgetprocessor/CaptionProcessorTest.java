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
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.widgetprocessor.CaptionProcessor;

import com.vaadin.ui.TextField;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CaptionProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		CaptionProcessor widgetProcessor = new CaptionProcessor();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		TextField textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, metawidget );
		assertEquals( "", textField.getCaption() );

		attributes.put( NAME, "fooBar" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, metawidget );
		assertEquals( "Foo Bar", textField.getCaption() );

		attributes.put( LABEL, "bazBaz" );
		textField = (TextField) widgetProcessor.processWidget( new TextField(), PROPERTY, attributes, metawidget );
		assertEquals( "bazBaz", textField.getCaption() );
	}
}
