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

import static org.metawidget.inspector.InspectionResultConstants.MAXIMUM_LENGTH;
import static org.metawidget.inspector.InspectionResultConstants.MAXIMUM_VALUE;
import static org.metawidget.inspector.InspectionResultConstants.MINIMUM_VALUE;
import static org.metawidget.inspector.InspectionResultConstants.PROPERTY;
import static org.metawidget.inspector.InspectionResultConstants.REQUIRED;
import static org.metawidget.inspector.InspectionResultConstants.TRUE;
import static org.metawidget.inspector.InspectionResultConstants.TYPE;

import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * HtmlWidgetBuilderTest test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		WidgetBuilder<Tag, MetawidgetTag> widgetBuilder = new HtmlWidgetBuilder();
		MetawidgetTag dummyMetawidget = new HtmlMetawidgetTag();

		// Text

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, String.class.getName() );
		LiteralTag literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( "<input type=\"text\"/>", JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget ));

		attributes.put( MAXIMUM_LENGTH, "10" );
		literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( "<input type=\"text\" maxlength=\"10\"/>", JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget ));

		// Required
				
		attributes.put( REQUIRED, TRUE );
		literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( "<input type=\"text\" maxlength=\"10\" required/>", JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget ));
		
		// Number

		attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, int.class.getName() );
		literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( "<input type=\"number\"/>", JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget ));

		attributes.put( MINIMUM_VALUE, "1" );
		literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( "<input type=\"number\" min=\"1\"/>", JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget ));

		attributes.put( MAXIMUM_VALUE, "99" );
		literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( "<input type=\"number\" min=\"1\" max=\"99\"/>", JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget ));

		// Char

		attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, char.class.getName() );
		literalTag = (LiteralTag) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( "<input type=\"text\" maxlength=\"1\"/>", JspUtils.writeTag( dummyMetawidget.getPageContext(), literalTag, dummyMetawidget ));
	}
}
