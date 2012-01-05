package org.metawidget.statically.jsp.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlWidgetBuilderConfig;
import org.metawidget.statically.jsp.html.StaticHtmlMetawidget;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;

import junit.framework.TestCase;

public class HtmlWidgetBuilderTest
    extends TestCase {
    
    //
    // Public methods
    //
    
    public void testJspLookup() {
        
        // Without 'required'
        
        HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( JSP_LOOKUP, "${foo.bar}" );
        StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<select><option/><option value=\"${foo.bar}\"/></select>", widget.toString() );
        
        // With 'required'
        
        attributes.put( REQUIRED, TRUE );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<select><option value=\"${foo.bar}\"/></select>", widget.toString() );
    }
    
    public void testCollection() {
        
        // Unsupported type
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( TYPE, Set.class.getName() );
        StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<stub/>", widget.toString() );
        
        // Most basic
        
        attributes.put( TYPE, List.class.getName() );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<table><tr/><c:forEach var=\"item\"><tr><td><c:out value=\"${item}\"/></td></tr></c:forEach></table>", widget.toString() );
        
        //  With parent name
        
        attributes.put( NAME, "items" );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<table><tr/><c:forEach items=\"${items}\" var=\"item\"><tr><td><c:out value=\"${item}\"/></td></tr></c:forEach></table>", widget.toString() );
        
        // With Array
        
        attributes.put( TYPE, Foo[].class.getName() );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
        
        String result = "<table><tr><td><h3>Bar</h3></td><td><h3>Baz</h3></td></tr>" +
            "<c:forEach items=\"${items}\" var=\"item\"><tr><td><c:out value=\"${item.bar}\"/></td>" +
            "<td><c:out value=\"${item.baz}\"/></td></tr></c:forEach></table>";
        
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
                new Java5Inspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ),
                new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( propertyStyle ) ) ) ) );
        metawidget.setValue( "${foo.pageItems}" );
        metawidget.setPath( FooBean.class.getName() + "/pageItems" );
        metawidget.setLayout( new SimpleLayout() );
        
        result = "<table name=\"fooPageItemsPageItems\"><tr><td><h3>Bar</h3></td><td><h3>Baz</h3></td></tr>" +
        "<c:forEach items=\"${pageItems}\" var=\"item\"><tr><td><c:out value=\"${item.bar}\"/></td>" +
        "<td><c:out value=\"${item.baz}\"/></td></tr></c:forEach></table>";        
        assertEquals( result, metawidget.toString() );
        
        // With required columns
        
        metawidget.setValue( "${foo.requiredPageItems}" );
        metawidget.setLayout( new SimpleLayout() );
        metawidget.setPath( FooBean.class.getName() + "/requiredPageItems" );
        result = "<table name=\"fooRequiredPageItemsRequiredPageItems\"><tr><td><h3>Bar</h3></td><td><h3>Abc</h3></td></tr>" +
            "<c:forEach items=\"${requiredPageItems}\" var=\"item\"><tr><td><c:out value=\"${item.bar}\"/></td>" +
            "<td><c:out value=\"${item.abc}\"/></td></tr></c:forEach></table>";
        assertEquals( result, metawidget.toString() );
    }
    
    public void testCollectionWithManyColumns() {
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        HtmlWidgetBuilder widgetBuilder = new HtmlWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        
        attributes.put( TYPE, List.class.getName() );
        attributes.put( PARAMETERIZED_TYPE, LargeFoo.class.getName() );
        
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
        
        // Test the output table; 'column6' should be suppressed due to a maximum number of columns of 5.
        
        String result = "<table><tr><td><h3>Column 1</h3></td><td><h3>Column 2</h3></td>"+
            "<td><h3>Column 3</h3></td><td><h3>Column 4</h3></td><td><h3>Column 5</h3></td></tr>" +
            "<c:forEach var=\"item\"><tr><td><c:out value=\"${item.column1}\"/></td><td><c:out value=\"${item.column2}\"/></td>" +
            "<td><c:out value=\"${item.column3}\"/></td><td><c:out value=\"${item.column4}\"/></td>" +
            "<td><c:out value=\"${item.column5}\"/></td></tr></c:forEach></table>";
        assertEquals( result, widget.toString() );
        
        // Try with a column maximum of 2
        
        widgetBuilder = new HtmlWidgetBuilder( new HtmlWidgetBuilderConfig().setMaximumColumnsInDataTable( 2 ) );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
        
        result = "<table><tr><td><h3>Column 1</h3></td><td><h3>Column 2</h3></td></tr><c:forEach var=\"item\">" +
        "<tr><td><c:out value=\"${item.column1}\"/></td><td><c:out value=\"${item.column2}\"/></td>" +
        "</tr></c:forEach></table>";
        assertEquals( result, widget.toString() );
        
        // A column "maximum" of 0 should not suppress any columns
        
        widgetBuilder = new HtmlWidgetBuilder( new HtmlWidgetBuilderConfig().setMaximumColumnsInDataTable( 0 ) );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
        result = "<table><tr><td><h3>Column 1</h3></td><td><h3>Column 2</h3></td>"+
        "<td><h3>Column 3</h3></td><td><h3>Column 4</h3></td><td><h3>Column 5</h3></td><td><h3>Column 6</h3></td></tr>" +
        "<c:forEach var=\"item\"><tr><td><c:out value=\"${item.column1}\"/></td><td><c:out value=\"${item.column2}\"/></td>" +
        "<td><c:out value=\"${item.column3}\"/></td><td><c:out value=\"${item.column4}\"/></td>" +
        "<td><c:out value=\"${item.column5}\"/></td><td><c:out value=\"${item.column6}\"/></td></tr></c:forEach></table>";
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

        public List<Foo>            pageItems;

        public List<RequiredFoo>    requiredPageItems;
    }

    static class Foo {

        public String   bar;

        public String   baz;
    }

    static class RequiredFoo {

        @UiRequired
        public String   bar;

        public String   baz;

        @UiRequired
        @UiComesAfter( "baz" )
        public String   abc;
    }

    static class LargeFoo {

        public String   column1;

        public String   column2;

        public String   column3;

        public String   column4;

        public String   column5;

        public String   column6;
    }
}
