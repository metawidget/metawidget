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
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.jsp.tagext.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		WidgetBuilder<Tag, MetawidgetTag> widgetBuilder = new ReadOnlyWidgetBuilder();
		MetawidgetTag dummyMetawidget = new HtmlMetawidgetTag();

		// Read only

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlStubTag );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( MASKED, TRUE );
		LiteralTag literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertTrue( "".equals( JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget )));
		attributes.remove( MASKED );

		// Lookups

		attributes.put( LOOKUP, "foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );

		attributes.put( LOOKUP_LABELS, "Foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );
		attributes.remove( LOOKUP_LABELS );
		attributes.remove( LOOKUP );

		// JSP lookup

		attributes.put( JSP_LOOKUP, "${foo.bar}" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );
		attributes.remove( JSP_LOOKUP );

		// Other types

		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );
		attributes.put( TYPE, int.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );
		attributes.put( TYPE, Integer.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );
		attributes.put( TYPE, Date.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );

		// Arrays

		attributes.put( TYPE, String[].class.getName() );
		dummyMetawidget.setInspector( new PropertyTypeInspector() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) );

		// Lists

		attributes.put( TYPE, List.class.getName() );
		attributes.put( NAME, "bar" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof StubTag );

		// Other collections

		attributes.put( TYPE, Set.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof StubTag );

		// Unsupported types

		attributes.put( TYPE, Color.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );
		attributes.remove( DONT_EXPAND );

		dummyMetawidget.setLayout( new SimpleLayout() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof LiteralTag );
	}
}
