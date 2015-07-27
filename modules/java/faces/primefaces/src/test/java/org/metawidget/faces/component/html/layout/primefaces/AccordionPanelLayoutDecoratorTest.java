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
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.tabview.Tab;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

import static org.metawidget.inspector.InspectionResultConstants.ENTITY;
import static org.metawidget.inspector.InspectionResultConstants.SECTION;

/**
 * @author DanilAREFY
 */

public class AccordionPanelLayoutDecoratorTest
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
        AccordionPanelLayoutDecorator layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> inputTextAttributes = CollectionUtils.newHashMap();

        InputText inputText = new InputText();
        layoutDecorator.layoutWidget( inputText, ENTITY, inputTextAttributes, metawidget, metawidget );
        assertTrue( inputText == metawidget.getChildren().get( 0 ) );
        assertEquals( 1, metawidget.getChildCount() );

        inputTextAttributes.put( SECTION, "Foo" );
        layoutDecorator.layoutWidget( inputText, ENTITY, inputTextAttributes, metawidget, metawidget );

        Calendar calendar = new Calendar();
        Map<String, String> calendarAttributes = CollectionUtils.newHashMap();
        calendarAttributes.put( SECTION, "Bar" );
        layoutDecorator.layoutWidget( calendar, ENTITY, calendarAttributes, metawidget, metawidget );
        assertEquals( 1, metawidget.getChildCount() );

        AccordionPanel accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( 2, accordionPanel.getChildCount() );

        Tab fooTab = (Tab) accordionPanel.getChildren().get( 0 );
        assertEquals( "Foo", fooTab.getTitle() );
        assertEquals( 1, fooTab.getChildCount() );

        HtmlMetawidget fooTabNestedMetawidget = (HtmlMetawidget) fooTab.getChildren().get( 0 );
        assertEquals( 1, fooTabNestedMetawidget.getChildCount() );
        assertTrue( inputText == fooTabNestedMetawidget.getChildren().get( 0 ) );

        Tab barTab = (Tab) accordionPanel.getChildren().get( 1 );
        assertEquals( "Bar", barTab.getTitle() );
        assertEquals( 1, barTab.getChildCount() );

        HtmlMetawidget barTabNestedMetawidget = (HtmlMetawidget) barTab.getChildren().get( 0 );
        assertEquals( 1, barTabNestedMetawidget.getChildCount() );
        assertTrue( calendar == barTabNestedMetawidget.getChildren().get( 0 ) );
    }

    public void testActiveIndex() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        AccordionPanelLayoutDecorator layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        AccordionPanel accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( "false", accordionPanel.getActiveIndex() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setActiveIndex("1") );

        attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget);

        accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( "1", accordionPanel.getActiveIndex() );
    }

    public void testDynamic() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        AccordionPanelLayoutDecorator layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget);

        AccordionPanel accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( false, accordionPanel.isDynamic() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setDynamic( true ) );

        attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( true, accordionPanel.isDynamic() );
    }

    public void testCache() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        AccordionPanelLayoutDecorator layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        AccordionPanel accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( true, accordionPanel.isCache() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setCache( false ) );

        attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( false, accordionPanel.isCache() );
    }

    public void testMultiple() {

        // Default

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        AccordionPanelLayoutDecorator layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        AccordionPanel accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( false, accordionPanel.isMultiple() );

        // Custom

        metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        layoutDecorator = new AccordionPanelLayoutDecorator( new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setMultiple( true ) );

        attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( true, accordionPanel.isMultiple() );
    }

    public void testStyle() {

        // Custom

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        AccordionPanelLayoutDecorator layoutDecorator = new AccordionPanelLayoutDecorator(
                new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setStyle( "color: black;" ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        Calendar calendar = new Calendar();
        layoutDecorator.layoutWidget( calendar, ENTITY, attributes, metawidget, metawidget );

        AccordionPanel accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( "color: black;", accordionPanel.getStyle() );
    }

    public void testStyleClass() {

        // Custom

        HtmlMetawidget metawidget = new HtmlMetawidget();
        metawidget.setInspector( new PropertyTypeInspector() );
        AccordionPanelLayoutDecorator layoutDecorator = new AccordionPanelLayoutDecorator(
                new AccordionPanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setStyleClass( "styleClazz" ) );

        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SECTION, "Foo" );

        InputText inputText = new InputText();
        layoutDecorator.layoutWidget( inputText, ENTITY, attributes, metawidget, metawidget );

        AccordionPanel accordionPanel = (AccordionPanel) metawidget.getChildren().get( 0 );
        assertEquals( "styleClazz", accordionPanel.getStyleClass() );
    }

    public void testConfig() {

        MetawidgetTestUtils.testEqualsAndHashcode( AccordionPanelLayoutDecoratorConfig.class, new AccordionPanelLayoutDecoratorConfig() {

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

            if ( AccordionPanel.COMPONENT_TYPE.equals( componentName ) ) {
                return new AccordionPanel();
            }

            if ( Tab.COMPONENT_TYPE.equals( componentName ) ) {
                return new Tab();
            }

            return super.createComponent( componentName );
        }
    }
}
