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

package org.metawidget.jsp.tagext.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
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

		// TODO: tests!

		HtmlMetawidgetTag metawidget = new HtmlMetawidgetTag();
		ReadOnlyWidgetBuilder widgetBuilder = new ReadOnlyWidgetBuilder();

		// No type

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );
		attributes.remove( HIDDEN );

		// Lookup

		attributes.put( LOOKUP, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );
		attributes.remove( LOOKUP );

		// Bad type

		attributes.put( TYPE, "foo" );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Non-collection

		attributes.put( TYPE, String.class.getName() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );
	}
}
