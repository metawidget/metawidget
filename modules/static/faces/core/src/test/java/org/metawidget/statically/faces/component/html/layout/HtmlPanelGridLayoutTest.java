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

		HtmlPanelGridLayout layout = new HtmlPanelGridLayout( new HtmlPanelGridLayoutConfig().setColumnStyleClasses( "labelStyleClass", "componentStyleClass", "requiredStyleClass" ) );

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlPanelGrid container = new HtmlPanelGrid();
		layout.startContainerLayout( container, metawidget );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		layout.layoutWidget( new HtmlInputText(), PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<h:panelGrid><h:panelGrid columnClasses=\"labelStyleClass,componentStyleClass,requiredStyleClass\" columns=\"3\"><h:outputLabel value=\"Foo:\"/><h:panelGroup><h:inputText/><h:message/></h:panelGroup><h:outputText/></h:panelGrid></h:panelGrid>", container.toString() );
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

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( HtmlPanelGridLayoutConfig.class, new HtmlPanelGridLayoutConfig() {
			// Subclass
		} );
	}
}
