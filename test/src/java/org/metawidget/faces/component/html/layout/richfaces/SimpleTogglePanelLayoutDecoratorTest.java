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

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.layout.SimpleLayout;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator.UIComponentState;
import org.metawidget.layout.decorator.NestedSectionLayoutDecorator.State;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.TestUtils;
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

	public void testConfig() {

		TestUtils.testEqualsAndHashcode( SimpleTogglePanelLayoutDecoratorConfig.class, new SimpleTogglePanelLayoutDecoratorConfig() {
			// Subclass
		} );
	}

	public void testSectionTitle()
		throws Exception {

		HtmlMetawidget metawidget = new HtmlMetawidget();

		SimpleTogglePanelLayoutDecorator decorator = new SimpleTogglePanelLayoutDecorator( new SimpleTogglePanelLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );
		createState( metawidget, metawidget ).currentSection = "Foo";
		HtmlMetawidget nestedMetawidget = (HtmlMetawidget) decorator.createNewSectionWidget( null, null, metawidget, metawidget );

		HtmlSimpleTogglePanel panel = (HtmlSimpleTogglePanel) nestedMetawidget.getParent();
		assertEquals( "Foo", panel.getLabel() );
	}

	//
	// Private methods
	//

	private State<UIComponent> createState( UIComponent container, UIMetawidget metawidget ) {

		Map<UIComponent, UIComponentState> stateMap = CollectionUtils.newHashMap();
		metawidget.putClientProperty( SimpleTogglePanelLayoutDecorator.class, stateMap );

		UIComponentState state = new UIComponentState();
		stateMap.put( container, state );

		return state;
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

			if ( "org.richfaces.SimpleTogglePanel".equals( componentName ) ) {
				return new HtmlSimpleTogglePanel();
			}

			return super.createComponent( componentName );
		}
	}
}
