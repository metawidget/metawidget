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

package org.metawidget.statically.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
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
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;

public class HtmlWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testPoh5() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Maximum length

		attributes.put( MAXIMUM_LENGTH, "30" );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input maxlength=\"30\" type=\"text\"/>", widget.toString() );

		// Pattern

		attributes.put( VALIDATION_PATTERN, "[A-Za-z ]*" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input maxlength=\"30\" pattern=\"[A-Za-z ]*\" type=\"text\"/>", widget.toString() );

		// Masked

		attributes.put( MASKED, TRUE );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input maxlength=\"30\" type=\"secret\"/>", widget.toString() );
		attributes.remove( MASKED );

		// Date

		attributes.put( TYPE, Date.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input type=\"date\"/>", widget.toString() );

		// Color

		attributes.put( TYPE, Color.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input type=\"color\"/>", widget.toString() );

		// Number

		attributes.put( TYPE, int.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input type=\"number\"/>", widget.toString() );

		// Email

		attributes.put( TYPE, String.class.getName() );
		attributes.put( "validation-email", TRUE );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input maxlength=\"30\" type=\"email\"/>", widget.toString() );
		attributes.remove( "validation-email" );

		// With minimum/maximum

		attributes.put( TYPE, long.class.getName() );
		attributes.put( MINIMUM_VALUE, "2" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input min=\"2\" type=\"number\"/>", widget.toString() );

		attributes.put( TYPE, float.class.getName() );
		attributes.put( MAXIMUM_VALUE, "42" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<input max=\"42\" min=\"2\" type=\"number\"/>", widget.toString() );
	}

	public void testCollection() {

		// Most basic

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, Set.class.getName() );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<table><thead><tr><th/></tr></thead><tbody/></table>", widget.toString() );

		// With parent name

		attributes.put( NAME, "items" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<table><thead><tr><th id=\"items\">Items</th></tr></thead><tbody/></table>", widget.toString() );

		// With Array

		attributes.put( TYPE, Foo[].class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		String result = "<table id=\"items\"><thead><tr><th id=\"items-bar\">Bar</th><th id=\"items-baz\">Baz</th></tr></thead><tbody/></table>";

		assertEquals( result, widget.toString() );

		// With PARAMETERIZED_TYPE

		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, Foo.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( result, widget.toString() );

		// From Metawidget (direct class)

		PropertyStyle propertyStyle = new StaticPropertyStyle();
		metawidget.setInspector( new CompositeInspector(
				new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ),
						new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ) ) ) );
		metawidget.setId( "search-results" );
		metawidget.setPath( List.class.getName() + '<' + Foo.class.getName() + '>' );
		metawidget.setLayout( new SimpleLayout() );

		assertEquals( "<table id=\"search-results\"><thead><tr><th id=\"search-results-bar\">Bar</th><th id=\"search-results-baz\">Baz</th></tr></thead><tbody/></table>", metawidget.toString() );

		// From Metawidget

		metawidget = new StaticHtmlMetawidget();
		metawidget.setInspector( new CompositeInspector(
				new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ),
						new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ) ) ) );
		metawidget.setPath( FooBean.class.getName() + "/pageItems" );
		metawidget.setLayout( new SimpleLayout() );

		assertEquals( "<table id=\"pageItems\"><thead><tr><th id=\"pageItems-bar\">Bar</th><th id=\"pageItems-baz\">Baz</th></tr></thead><tbody/></table>", metawidget.toString() );

		// With required columns

		metawidget.setLayout( new SimpleLayout() );
		metawidget.setPath( FooBean.class.getName() + "/requiredPageItems" );
		result = "<table id=\"requiredPageItems\"><thead><tr><th id=\"requiredPageItems-bar\">Bar</th><th id=\"requiredPageItems-abc\">Abc</th></tr></thead><tbody/></table>";
		assertEquals( result, metawidget.toString() );
	}

	public void testCollectionWithManyColumns() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, LargeFoo.class.getName() );

		StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );

		// Test the output table; 'column6' should be suppressed due to a maximum number of columns
		// of 5.

		String result = "<table><thead><tr><th id=\"column1\">Column 1</th><th id=\"column2\">Column 2</th><th id=\"column3\">Column 3</th><th id=\"column4\">Column 4</th><th id=\"column5\">Column 5</th></tr></thead><tbody/></table>";
		assertEquals( result, widget.toString() );

		// Try with a column maximum of 2

		widgetBuilder = new HtmlWidgetBuilder( new HtmlWidgetBuilderConfig().setMaximumColumnsInDataTable( 2 ) );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );

		result = "<table><thead><tr><th id=\"column1\">Column 1</th><th id=\"column2\">Column 2</th></tr></thead><tbody/></table>";
		assertEquals( result, widget.toString() );

		// A column "maximum" of 0 should not suppress any columns

		widgetBuilder = new HtmlWidgetBuilder( new HtmlWidgetBuilderConfig().setMaximumColumnsInDataTable( 0 ) );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		result = "<table><thead><tr><th id=\"column1\">Column 1</th><th id=\"column2\">Column 2</th><th id=\"column3\">Column 3</th><th id=\"column4\">Column 4</th><th id=\"column5\">Column 5</th><th id=\"column6\">Column 6</th></tr></thead><tbody/></table>";

		assertEquals( result, widget.toString() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( HtmlWidgetBuilderConfig.class, new HtmlWidgetBuilderConfig() {
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
		@UiComesAfter( "baz" )
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
