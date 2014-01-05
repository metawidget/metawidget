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

package org.metawidget.statically.jsp.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.statically.StaticPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JspWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJspLookup() {

		// Without 'required'

		JspWidgetBuilder widgetBuilder = new JspWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( JSP_LOOKUP, "${foo.bar}" );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<select><option value=\"\"/><option value=\"${foo.bar}\"/></select>", widget.toString() );

		// With 'required'

		attributes.put( REQUIRED, TRUE );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<select><option value=\"${foo.bar}\"/></select>", widget.toString() );
	}

	public void testCollection() {

		// Most basic

		StaticJspMetawidget metawidget = new StaticJspMetawidget();
		JspWidgetBuilder widgetBuilder = new JspWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, Set.class.getName() );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<table><thead><tr/></thead><tbody><c:forEach var=\"item\"><tr><td><c:out value=\"${item}\"/></td></tr></c:forEach></tbody></table>", widget.toString() );

		// With parent name

		attributes.put( NAME, "items" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<table><thead><tr/></thead><tbody><c:forEach items=\"${items}\" var=\"item\"><tr><td><c:out value=\"${item}\"/></td></tr></c:forEach></tbody></table>", widget.toString() );

		// With Array

		attributes.put( TYPE, Foo[].class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );

		String result = "<table><thead><tr><th>Bar</th><th>Baz</th></tr></thead><tbody>" +
				"<c:forEach items=\"${items}\" var=\"item\"><tr><td><c:out value=\"${item.bar}\"/></td>" +
				"<td><c:out value=\"${item.baz}\"/></td></tr></c:forEach></tbody></table>";

		assertEquals( result, widget.toString() );

		// With PARAMETERIZED_TYPE

		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, Foo.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( result, widget.toString() );

		// From Metawidget

		PropertyStyle propertyStyle = new StaticPropertyStyle();
		metawidget.setInspector( new CompositeInspector(
				new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ),
						new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ) ) ) );
		metawidget.setValue( "${foo.pageItems}" );
		metawidget.setPath( FooBean.class.getName() + "/pageItems" );
		metawidget.setLayout( new SimpleLayout() );

		result = "<table id=\"pageItems\"><thead><tr><th>Bar</th><th>Baz</th></tr></thead>" +
				"<tbody><c:forEach items=\"${pageItems}\" var=\"item\"><tr><td><c:out value=\"${item.bar}\"/></td>" +
				"<td><c:out value=\"${item.baz}\"/></td></tr></c:forEach></tbody></table>";
		assertEquals( result, metawidget.toString() );

		// With required columns

		metawidget.setValue( "${foo.requiredPageItems}" );
		metawidget.setLayout( new SimpleLayout() );
		metawidget.setPath( FooBean.class.getName() + "/requiredPageItems" );
		result = "<table id=\"requiredPageItems\"><thead><tr><th>Bar</th>" +
				"<th>Abc</th></tr></thead><tbody><c:forEach items=\"${requiredPageItems}\" var=\"item\"><tr><td>" +
				"<c:out value=\"${item.bar}\"/></td><td><c:out value=\"${item.abc}\"/></td></tr></c:forEach></tbody></table>";
		assertEquals( result, metawidget.toString() );
	}

	public void testCollectionWithManyColumns() {

		StaticJspMetawidget metawidget = new StaticJspMetawidget();
		JspWidgetBuilder widgetBuilder = new JspWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, LargeFoo.class.getName() );

		StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );

		// Test the output table; 'column6' should be suppressed due to a maximum number of columns
		// of 5.

		String result = "<table><thead><tr><th>Column 1</th><th>Column 2</th><th>Column 3</th><th>Column 4</th>" +
				"<th>Column 5</th></tr></thead><tbody><c:forEach var=\"item\"><tr><td><c:out value=\"${item.column1}\"/>" +
				"</td><td><c:out value=\"${item.column2}\"/></td><td><c:out value=\"${item.column3}\"/></td><td><c:out value=\"${item.column4}\"/>" +
				"</td><td><c:out value=\"${item.column5}\"/></td></tr></c:forEach></tbody></table>";
		assertEquals( result, widget.toString() );

		// Try with a column maximum of 2

		widgetBuilder = new JspWidgetBuilder( new JspWidgetBuilderConfig().setMaximumColumnsInDataTable( 2 ) );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );

		result = "<table><thead><tr><th>Column 1</th><th>Column 2</th></tr></thead><tbody><c:forEach var=\"item\">" +
				"<tr><td><c:out value=\"${item.column1}\"/></td><td><c:out value=\"${item.column2}\"/></td>" +
				"</tr></c:forEach></tbody></table>";
		assertEquals( result, widget.toString() );

		// A column "maximum" of 0 should not suppress any columns

		widgetBuilder = new JspWidgetBuilder( new JspWidgetBuilderConfig().setMaximumColumnsInDataTable( 0 ) );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		result = "<table><thead><tr><th>Column 1</th><th>Column 2</th>" +
				"<th>Column 3</th><th>Column 4</th><th>Column 5</th><th>Column 6</th></tr></thead><tbody>" +
				"<c:forEach var=\"item\"><tr><td><c:out value=\"${item.column1}\"/></td><td><c:out value=\"${item.column2}\"/></td>" +
				"<td><c:out value=\"${item.column3}\"/></td><td><c:out value=\"${item.column4}\"/></td>" +
				"<td><c:out value=\"${item.column5}\"/></td><td><c:out value=\"${item.column6}\"/></td></tr></c:forEach></tbody></table>";

		assertEquals( result, widget.toString() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JspWidgetBuilderConfig.class, new JspWidgetBuilderConfig() {
			// Subclass
		} );
	}

	//
	// Inner classes
	//

	static class FooBean {

		public List<Foo> getPageItems() {

			return null;
		}

		public List<RequiredFoo> getRequiredPageItems() {

			return null;
		}
	}

	static class Foo {

		public String getBar() {

			return null;
		}

		public String getBaz() {

			return null;
		}
	}

	static class RequiredFoo {

		@UiRequired
		public String getBar() {

			return null;
		}

		public String getBaz() {

			return null;
		}

		@UiRequired
		@UiComesAfter( "bar" )
		public String getAbc() {

			return null;
		}
	}

	static class LargeFoo {

		public String getColumn1() {

			return null;
		}

		public String getColumn2() {

			return null;
		}

		public String getColumn3() {

			return null;
		}

		public String getColumn4() {

			return null;
		}

		public String getColumn5() {

			return null;
		}

		public String getColumn6() {

			return null;
		}
	}
}
