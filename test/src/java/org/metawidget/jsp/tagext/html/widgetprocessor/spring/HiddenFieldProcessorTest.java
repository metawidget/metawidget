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

package org.metawidget.jsp.tagext.html.widgetprocessor.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import junit.framework.TestCase;

import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author Richard Kennard
 */

public class HiddenFieldProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		WidgetProcessor<Tag, MetawidgetTag> widgetProcessor = new HiddenFieldProcessor();
		MetawidgetTag dummyMetawdget = new SpringMetawidgetTag();

		// Not for us?

		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertTrue( null == widgetProcessor.processWidget( null, PROPERTY, attributes, dummyMetawdget ));

		// With content

		MockPageContext pageContext = new MockPageContext();
		Field field = TagSupport.class.getDeclaredField( "pageContext" );
		field.setAccessible( true );
		field.set( dummyMetawdget, pageContext );

		attributes.put( MetawidgetTag.ATTRIBUTE_NEEDS_HIDDEN_FIELD, TRUE );
		LiteralTag tag = new LiteralTag( "Foo" );
		//TODO: Tag returned = widgetProcessor.processWidget( tag, PROPERTY, attributes, dummyMetawdget );
		//assertTrue( "Foo".equals( JspUtils.writeTag( dummyMetawdget.getPageContext(), returned, dummyMetawdget, null )));
	}
}
