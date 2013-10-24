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
	}
}
