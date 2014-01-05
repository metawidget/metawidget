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
import org.richfaces.component.SwitchType;
import org.richfaces.component.UICollapsiblePanel;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CollapsiblePanelLayoutDecoratorTest
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
		CollapsiblePanelLayoutDecorator layoutDecorator = new CollapsiblePanelLayoutDecorator( new CollapsiblePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();

		HtmlInputText inputText = new HtmlInputText();
		layoutDecorator.layoutWidget( inputText, ENTITY, attributes, metawidget, metawidget );
		assertTrue( inputText == metawidget.getChildren().get( 0 ) );
		assertEquals( 1, metawidget.getChildren().size() );

		attributes.put( SECTION, "Foo" );
		layoutDecorator.layoutWidget( inputText, ENTITY, attributes, metawidget, metawidget );
		UICollapsiblePanel panel = (UICollapsiblePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Foo", panel.getHeader() );
		HtmlMetawidget nestedMetawdget = (HtmlMetawidget) panel.getChildren().get( 0 );
		assertEquals( 1, panel.getChildren().size() );
		assertTrue( inputText == nestedMetawdget.getChildren().get( 0 ) );
		assertEquals( 1, nestedMetawdget.getChildren().size() );
	}

	public void testSwitchType() {

		// Default

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		CollapsiblePanelLayoutDecorator layoutDecorator = new CollapsiblePanelLayoutDecorator( new CollapsiblePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( SECTION, "Bar" );

		layoutDecorator.layoutWidget( new HtmlInputText(), ENTITY, attributes, metawidget, metawidget );

		UICollapsiblePanel panel = (UICollapsiblePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Bar", panel.getHeader() );
		assertEquals( SwitchType.client, panel.getSwitchType() );

		// Server

		metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		layoutDecorator = new CollapsiblePanelLayoutDecorator( new CollapsiblePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setSwitchType( SwitchType.server ) );

		attributes.put( SECTION, "Bar" );
		layoutDecorator.layoutWidget( new HtmlInputText(), ENTITY, attributes, metawidget, metawidget );

		panel = (UICollapsiblePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Bar", panel.getHeader() );
		assertEquals( SwitchType.server, panel.getSwitchType() );
	}

	public void testExpanded() {

		// Default

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		CollapsiblePanelLayoutDecorator layoutDecorator = new CollapsiblePanelLayoutDecorator( new CollapsiblePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( SECTION, "Bar" );

		layoutDecorator.layoutWidget( new HtmlInputText(), ENTITY, attributes, metawidget, metawidget );

		UICollapsiblePanel panel = (UICollapsiblePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Bar", panel.getHeader() );
		assertTrue( panel.isExpanded() );

		// Server

		metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		layoutDecorator = new CollapsiblePanelLayoutDecorator( new CollapsiblePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ).setExpanded( false ) );

		attributes.put( SECTION, "Bar" );
		layoutDecorator.layoutWidget( new HtmlInputText(), ENTITY, attributes, metawidget, metawidget );

		panel = (UICollapsiblePanel) metawidget.getChildren().get( 0 );
		assertEquals( "Bar", panel.getHeader() );
		assertTrue( !panel.isExpanded() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( CollapsiblePanelLayoutDecoratorConfig.class, new CollapsiblePanelLayoutDecoratorConfig() {
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

			if ( UICollapsiblePanel.COMPONENT_TYPE.equals( componentName ) ) {
				return new UICollapsiblePanel();
			}

			return super.createComponent( componentName );
		}
	}
}
