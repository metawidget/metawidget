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

package org.metawidget.faces.component.html.layout.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.layout.SimpleLayout;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.richfaces.component.html.HtmlSimpleTogglePanel;

/**
 * @author Richard Kennard
 */

public class SimpleTogglePanelLayoutDecoratorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testLayoutDecorator() {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		SimpleTogglePanelLayoutDecorator layoutDecorator = new SimpleTogglePanelLayoutDecorator( new SimpleTogglePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();

		HtmlInputText inputText = new HtmlInputText();
		layoutDecorator.layoutWidget( inputText, ENTITY, attributes, metawidget, metawidget );
		assertTrue( inputText == metawidget.getChildren().get( 0 ) );
		assertEquals( 1, metawidget.getChildren().size() );

		attributes.put( SECTION, "Foo" );
		layoutDecorator.layoutWidget( inputText, ENTITY, attributes, metawidget, metawidget );
		HtmlSimpleTogglePanel panel = (HtmlSimpleTogglePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Foo", panel.getLabel() );
		HtmlMetawidget nestedMetawdget = (HtmlMetawidget) panel.getChildren().get( 0 );
		assertEquals( 1, panel.getChildren().size() );
		assertTrue( inputText == nestedMetawdget.getChildren().get( 0 ) );
		assertEquals( 1, nestedMetawdget.getChildren().size() );
	}

	public void testSwitchType() {

		// Default

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		SimpleTogglePanelLayoutDecorator layoutDecorator = new SimpleTogglePanelLayoutDecorator( new SimpleTogglePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( SECTION, "Bar" );

		layoutDecorator.layoutWidget( new HtmlInputText(), ENTITY, attributes, metawidget, metawidget );

		HtmlSimpleTogglePanel panel = (HtmlSimpleTogglePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Bar", panel.getLabel() );
		assertEquals( "client", panel.getSwitchType() );

		// Server

		metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		layoutDecorator = new SimpleTogglePanelLayoutDecorator( new SimpleTogglePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setSwitchType( "server" ) );

		attributes.put( SECTION, "Bar" );
		layoutDecorator.layoutWidget( new HtmlInputText(), ENTITY, attributes, metawidget, metawidget );

		panel = (HtmlSimpleTogglePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Bar", panel.getLabel() );
		assertEquals( "server", panel.getSwitchType() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( SimpleTogglePanelLayoutDecoratorConfig.class, new SimpleTogglePanelLayoutDecoratorConfig() {
			// Subclass
		} );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockRichFacesFacesContext();
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

	protected static class MockRichFacesFacesContext
		extends MockFacesContext {

		//
		// Protected methods
		//

		@Override
		public UIComponent createComponent( String componentName )
			throws FacesException {

			if ( HtmlSimpleTogglePanel.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSimpleTogglePanel();
			}

			return super.createComponent( componentName );
		}
	}
}
