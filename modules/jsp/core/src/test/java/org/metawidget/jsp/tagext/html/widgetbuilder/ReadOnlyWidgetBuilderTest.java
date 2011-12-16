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
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

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

		WidgetBuilder<Tag, MetawidgetTag> widgetBuilder = new ReadOnlyWidgetBuilder();
		MetawidgetTag dummyMetawdget = new HtmlMetawidgetTag();

		// Read only

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof StubTag );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( MASKED, TRUE );
		LiteralTag literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget );
		assertTrue( "".equals( JspUtils.writeTag( dummyMetawdget.getPageContext(), literalTag, dummyMetawdget )));
		attributes.remove( MASKED );

		// Lookups

		attributes.put( LOOKUP, "foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );

		attributes.put( LOOKUP_LABELS, "Foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );
		attributes.remove( LOOKUP_LABELS );
		attributes.remove( LOOKUP );

		// Faces lookup

		attributes.put( JSP_LOOKUP, "${foo.bar}" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );
		attributes.remove( JSP_LOOKUP );

		// Other types

		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );
		attributes.put( TYPE, int.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );
		attributes.put( TYPE, Integer.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );
		attributes.put( TYPE, Date.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );

		// Arrays

		attributes.put( TYPE, String[].class.getName() );
		dummyMetawdget.setInspector( new PropertyTypeInspector() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) );

		// Lists

		attributes.put( TYPE, List.class.getName() );
		attributes.put( NAME, "bar" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof StubTag );

		// Other collections

		attributes.put( TYPE, Set.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof StubTag );

		// Unsupported types

		attributes.put( TYPE, Color.class.getName() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof LiteralTag );
		attributes.remove( DONT_EXPAND );
	}
}
