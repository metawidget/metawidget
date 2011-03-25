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

package org.metawidget.swing.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		ReadOnlyWidgetBuilder widgetBuilder = new ReadOnlyWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Read-only JTextArea

		attributes.put( TYPE, String.class.getName() );
		attributes.put( READ_ONLY, TRUE );
		attributes.put( LARGE, TRUE );

		JScrollPane scrollPane = (JScrollPane) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( null == scrollPane.getBorder() );
		JTextArea textarea = (JTextArea) scrollPane.getViewport().getView();
		assertTrue( true == textarea.getLineWrap() );
		assertTrue( true == textarea.getWrapStyleWord() );
		assertTrue( false == textarea.isEditable() );
		assertTrue( 2 == textarea.getRows() );
	}
}
