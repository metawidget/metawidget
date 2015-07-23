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

package org.metawidget.faces.component.html.layout.primefaces;

import junit.framework.TestCase;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.layout.SimpleLayout;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

import static org.metawidget.inspector.InspectionResultConstants.ENTITY;
import static org.metawidget.inspector.InspectionResultConstants.SECTION;

/**
 * @author DanilAREFY
 */

public class TabViewLayoutDecoratorTest
        extends TestCase {

    //
    // Private members
    //

    private FacesContext    mContext;

    //
    // Public methods
    //

    public void testLayoutDecorator() {

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();

        InputText inputText = new InputText();
        layoutDecorator.layoutWidget( inputText, ENTITY, attributes, metawidget, metawidget );
        assertTrue(inputText == metawidget.getChildren().get( 0 ) );
        assertEquals( 1, metawidget.getChildren().size() );

        attributes.put( SECTION, "Foo" );
        layoutDecorator.layoutWidget( inputText, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( 1, tabView.getChildCount() );

        Tab tab = (Tab) tabView.getChildren().get( 0 );
        assertEquals( "Foo", tab.getTitle() );
        assertEquals( 1, tab.getChildCount() );

        HtmlMetawidget nestedMetawidget = (HtmlMetawidget) tab.getChildren().get( 0 );
        assertEquals( 1, nestedMetawidget.getChildCount() );
        assertTrue( inputText == nestedMetawidget.getChildren().get( 0 ) );
    }

    public void testActiveIndex() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> calendarAttributes = CollectionUtils.newHashMap();
        calendarAttributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, calendarAttributes, metawidget, metawidget );

        Map<String, String> inputTextAttributes = CollectionUtils.newHashMap();
        inputTextAttributes.put( SECTION, "Bar" );

        InputText inputText = new InputText();
        layoutDecorator.layoutWidget( inputText, ENTITY, inputTextAttributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( 2, tabView.getChildCount() );
        assertEquals( 0, tabView.getActiveIndex() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setActiveIndex( 1 ) );

        calendarAttributes.put( SECTION, "Foo" );
        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, calendarAttributes, metawidget, metawidget );

        inputTextAttributes.put( SECTION, "Bar");
        inputText = new InputText();
        layoutDecorator.layoutWidget( inputText, ENTITY, inputTextAttributes, metawidget, metawidget );

        tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( 2, tabView.getChildCount() );
        assertEquals( 1, tabView.getActiveIndex() );
    }

    public void testEffect() {

        // Custom

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setEffect( "fade" ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( "fade", tabView.getEffect() );
    }

    public void testEffectDuration() {

        // Custom

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setEffectDuration("slow") );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( "slow", tabView.getEffectDuration() );
    }

    public void testDynamic() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( false, tabView.isDynamic() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setDynamic( true ) );

        attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( true, tabView.isDynamic() );
    }

    public void testCache() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( true, tabView.isCache() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setCache( false ) );

        attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( false, tabView.isCache() );
    }

    public void testOrientation() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( "top", tabView.getOrientation() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setOrientation( "bottom" ) );

        attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( "bottom", tabView.getOrientation() );
    }

    public void testStyle() {

        // Custom

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setStyle( "color: black;" ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( "color: black;", tabView.getStyle() );
    }

    public void testStyleClass() {

        // Custom

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        TabViewLayoutDecorator layoutDecorator = new TabViewLayoutDecorator( new TabViewLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setStyleClass( "styleClazz" ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        TabView tabView = (TabView) metawidget.getChildren().get( 0 );
        assertEquals( "styleClazz", tabView.getStyleClass() );
    }

    public void testConfig() {

        MetawidgetTestUtils.testEqualsAndHashcode( TabViewLayoutDecoratorConfig.class, new TabViewLayoutDecoratorConfig() {
            // Subclass
        });
    }

    //
    // Protected methods
    //

    @Override
    protected void setUp()
            throws Exception {
        super.setUp();

        mContext = new MockPrimeFacesFacesContext();
    }

    @Override
    protected void tearDown()
            throws Exception {

        super.tearDown();

        mContext.release();
    }

    //
    // Inner class
    //

    protected static class MockPrimeFacesFacesContext
            extends MockFacesContext {

        //
        // Protected methods
        //

        @Override
        public UIComponent createComponent( String componentName )
                throws FacesException {

            if ( TabView.COMPONENT_TYPE.equals( componentName ) ) {
                return new TabView();
            }

            if ( Tab.COMPONENT_TYPE.equals( componentName ) ) {
                return new Tab();
            }

            return super.createComponent( componentName );
        }
    }
}
