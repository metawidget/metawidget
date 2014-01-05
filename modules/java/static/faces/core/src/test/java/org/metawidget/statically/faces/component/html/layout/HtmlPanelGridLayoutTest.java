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

package org.metawidget.statically.faces.component.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlOutputText;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlPanelGridLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testHtmlPanelGridLayout()
		throws Exception {

		HtmlPanelGridLayout layout = new HtmlPanelGridLayout();

		// Normal

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlPanelGrid container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		layout.layoutWidget( new HtmlInputText(), PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:panelGroup><h:inputText/><h:message/></h:panelGroup><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );

		// Read-only widget

		metawidget.setReadOnly( true );
		container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		layout.layoutWidget( new HtmlOutputText(), PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:outputText/><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );

		// Read-only attributes

		metawidget.setReadOnly( true );
		container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		attributes.put( READ_ONLY, TRUE );
		layout.layoutWidget( new HtmlInputText(), PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:inputText/><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );

		// Read-only Metawidget

		metawidget.setReadOnly( true );
		container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "bar" );
		layout.layoutWidget( new HtmlInputText(), PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel value=\"Bar:\"/><h:inputText/><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );
	}

	public void testCss()
		throws Exception {

		HtmlPanelGridLayout layout = new HtmlPanelGridLayout( new HtmlPanelGridLayoutConfig().setColumnStyleClasses( "labelStyleClass", "componentStyleClass", "requiredStyleClass" ).setMessageStyleClass( "error" ) );

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlPanelGrid container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		layout.layoutWidget( new HtmlInputText(), PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columnClasses=\"labelStyleClass,componentStyleClass,requiredStyleClass\" columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:panelGroup><h:inputText/><h:message styleClass=\"error\"/></h:panelGroup><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );
	}

	public void testStub()
		throws Exception {

		HtmlPanelGridLayout layout = new HtmlPanelGridLayout();

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlPanelGrid container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		layout.layoutWidget( new StaticXmlStub(), PROPERTY, attributes, container, metawidget );
		layout.layoutWidget( new HtmlInputText(), PROPERTY, attributes, container, metawidget );
		layout.layoutWidget( new StaticXmlStub(), PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:panelGroup><h:inputText/><h:message/></h:panelGroup><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );
	}

	public void testNestedId()
		throws Exception {

		HtmlPanelGridLayout layout = new HtmlPanelGridLayout();

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlPanelGrid container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );

		HtmlInputText htmlInputText = new HtmlInputText();
		htmlInputText.putAttribute( "id", "fooBeanCurrent" );
		HtmlPanelGroup htmlPanelGroup = new HtmlPanelGroup();
		htmlPanelGroup.getChildren().add( htmlInputText );

		layout.layoutWidget( htmlPanelGroup, PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel for=\"fooBeanCurrent\" value=\"Foo:\"/><h:panelGroup><h:inputText id=\"fooBeanCurrent\"/></h:panelGroup><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );
	}

	public void testRequired()
		throws Exception {

		HtmlPanelGridLayout layout = new HtmlPanelGridLayout();

		// Not required

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlPanelGrid container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		HtmlInputText htmlInputText = new HtmlInputText();
		layout.layoutWidget( htmlInputText, PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:panelGroup><h:inputText/><h:message/></h:panelGroup><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );

		// Required

		attributes.put( REQUIRED, TRUE );
		container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		layout.layoutWidget(htmlInputText, PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:panelGroup><h:inputText/><h:message/></h:panelGroup><h:outputText value=\"*\"/></h:panelGrid></h:panelGrid>", container.toString() );
	}

	public void testTopLevelAttributes()
		throws Exception {

		HtmlPanelGridLayout layout = new HtmlPanelGridLayout();

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.putAttribute( "rendered", "#{foo.rendered}" );
		layout.startContainerLayout( metawidget, metawidget );
		layout.endContainerLayout( metawidget, metawidget );

		assertEquals( "<h:panelGrid columns=\"3\" rendered=\"#{foo.rendered}\"/>", metawidget.toString() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( HtmlPanelGridLayoutConfig.class, new HtmlPanelGridLayoutConfig() {
			// Subclass
		} );
	}
}
