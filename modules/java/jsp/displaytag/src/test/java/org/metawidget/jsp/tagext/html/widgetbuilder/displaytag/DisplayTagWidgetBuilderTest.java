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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
