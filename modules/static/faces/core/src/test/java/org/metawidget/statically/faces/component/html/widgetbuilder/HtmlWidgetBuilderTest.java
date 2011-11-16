package org.metawidget.statically.faces.component.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.StaticFacesInspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.statically.StaticPropertyStyle;
import org.metawidget.inspector.java5.Java5Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;

public class HtmlWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testFacesLookup()
		throws Exception {

		// Without 'required'

		HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<h:selectOneMenu><f:selectItem/><f:selectItems value=\"#{foo.bar}\"/></h:selectOneMenu>", widget.toString() );

		// With 'required'

		attributes.put( REQUIRED, TRUE );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<h:selectOneMenu><f:selectItems value=\"#{foo.bar}\"/></h:selectOneMenu>", widget.toString() );
	}

	public void testCollection()
		throws Exception {

		// Most basic

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<h:dataTable var=\"_item\"><h:column><f:facet name=\"header\"><h:outputText/></f:facet><h:outputText value=\"#{_item}\"/></h:column></h:dataTable>", widget.toString() );

		// With parent name

		attributes.put( NAME, "items" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<h:dataTable var=\"_item\"><h:column><f:facet name=\"header\"><h:outputText value=\"Items\"/></f:facet><h:outputText value=\"#{_item}\"/></h:column></h:dataTable>", widget.toString() );

		// With Array

		attributes.put( TYPE, Foo[].class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<h:dataTable var=\"_item\"><h:column><f:facet name=\"header\"><h:outputText value=\"Bar\"/></f:facet><h:outputText value=\"#{_item.bar}\"/></h:column><h:column><f:facet name=\"header\"><h:outputText value=\"Baz\"/></f:facet><h:outputText value=\"#{_item.baz}\"/></h:column></h:dataTable>", widget.toString() );

		// With PARAMETERIZED_TYPE

		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, Foo.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<h:dataTable var=\"_item\"><h:column><f:facet name=\"header\"><h:outputText value=\"Bar\"/></f:facet><h:outputText value=\"#{_item.bar}\"/></h:column><h:column><f:facet name=\"header\"><h:outputText value=\"Baz\"/></f:facet><h:outputText value=\"#{_item.baz}\"/></h:column></h:dataTable>", widget.toString() );

		// From Metawidget

		PropertyStyle propertyStyle = new StaticPropertyStyle();
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ), new Java5Inspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ), new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ) ) ) );
		metawidget.setValueExpression( "value", "#{foo.pageItems}" );
		metawidget.setPath( FooBean.class.getName() + "/pageItems" );
		metawidget.setLayout( new SimpleLayout() );

		String result = "<h:dataTable id=\"fooPageItems\" value=\"#{foo.pageItems}\" var=\"_item\">\r\n" +
				"\t<h:column>\r\n" +
				"\t\t<f:facet name=\"header\">\r\n" +
				"\t\t\t<h:outputText value=\"Bar\"/>\r\n" +
				"\t\t</f:facet>\r\n" +
				"\t\t<h:outputText value=\"#{_item.bar}\"/>\r\n" +
				"\t</h:column>\r\n" +
				"\t<h:column>\r\n" +
				"\t\t<f:facet name=\"header\">\r\n" +
				"\t\t\t<h:outputText value=\"Baz\"/>\r\n" +
				"\t\t</f:facet>\r\n" +
				"\t\t<h:outputText value=\"#{_item.baz}\"/>\r\n" +
				"\t</h:column>\r\n" +
				"</h:dataTable>\r\n";

		assertEquals( result, metawidget.toString() );

		// With required columns

		metawidget.setValueExpression( "value", null );
		metawidget.setPath( FooBean.class.getName() + "/requiredPageItems" );
		metawidget.setLayout( new SimpleLayout() );

		result = "<h:dataTable var=\"_item\">\r\n" +
				"\t<h:column>\r\n" +
				"\t\t<f:facet name=\"header\">\r\n" +
				"\t\t\t<h:outputText value=\"Bar\"/>\r\n" +
				"\t\t</f:facet>\r\n" +
				"\t\t<h:outputText value=\"#{_item.bar}\"/>\r\n" +
				"\t</h:column>\r\n" +
				"\t<h:column>\r\n" +
				"\t\t<f:facet name=\"header\">\r\n" +
				"\t\t\t<h:outputText value=\"Abc\"/>\r\n" +
				"\t\t</f:facet>\r\n" +
				"\t\t<h:outputText value=\"#{_item.abc}\"/>\r\n" +
				"\t</h:column>\r\n" +
				"</h:dataTable>\r\n";

		assertEquals( result, metawidget.toString() );
	}

	//
	// Inner class
	//

	static class FooBean {

		public List<Foo>			pageItems;

		public List<RequiredFoo>	requiredPageItems;
	}

	static class Foo {

		public String	bar;

		public String	baz;
	}

	static class RequiredFoo {

		@UiRequired
		public String	bar;

		public String	baz;

		@UiRequired
		@UiComesAfter( "baz" )
		public String	abc;
	}
}
