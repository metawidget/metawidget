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

package org.metawidget.vaadin.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

import com.vaadin.ui.TextField;

/**
 * @author Richard Kennard
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
