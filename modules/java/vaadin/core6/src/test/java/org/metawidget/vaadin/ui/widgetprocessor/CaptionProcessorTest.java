// Metawidget (licensed under LGPL)
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

package org.metawidget.vaadin.ui.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.widgetprocessor.CaptionProcessor;

import com.vaadin.ui.TextField;

/**
 * @author Richard Kennard
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
