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

package org.metawidget.jsp.tagext.html.widgetbuilder.displaytag;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.displaytag.tags.TableTag;
import org.metawidget.jsp.JspUtilsTest.DummyPageContext;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class DisplayTagWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		DisplayTagWidgetBuilder widgetBuilder = new DisplayTagWidgetBuilder();

		// No type

		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );

		// Lookup

		attributes.put( LOOKUP, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( LOOKUP );

		// Bad type

		attributes.put( TYPE, "foo" );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// Non-collection

		attributes.put( TYPE, String.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
	}

	public void testCollectionWithManyColumns()
		throws Exception {

		HtmlMetawidgetTag metawidgetTag = new HtmlMetawidgetTag();
		metawidgetTag.setPageContext( new DummyPageContext() );

		Document document = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"column1\"/><property name=\"column2\"/><property name=\"column3\"/><property name=\"column4\"/><property name=\"column5\"/><property name=\"column6\"/></entity></inspection-result>" );
		Element entity = (Element) document.getDocumentElement().getFirstChild();

		final List<String> columnsAdded = CollectionUtils.newArrayList();

		DisplayTagWidgetBuilder widgetBuilder = new DisplayTagWidgetBuilder() {

			@Override
			protected boolean addColumnTag( TableTag tableTag, Map<String, String> tableAttributes, Map<String, String> columnAttributes, MetawidgetTag innerMetawidgetTag ) {

				columnsAdded.add( innerMetawidgetTag.getLabelString( columnAttributes ));
				return true;
			}
		};

		TableTag tableTag = new TableTag();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		widgetBuilder.addColumnTags( tableTag, attributes, entity.getChildNodes(), metawidgetTag );
		assertEquals( 5, columnsAdded.size() );
	}

	//
	// Inner class
	//

	static class LargeFoo {

		public String	column1;

		public String	column2;

		public String	column3;

		public String	column4;

		public String	column5;

		public String	column6;
	}
}
