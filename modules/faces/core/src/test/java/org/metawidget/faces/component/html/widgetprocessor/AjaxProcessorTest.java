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

package org.metawidget.faces.component.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.BehaviorBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorListener;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetprocessor.AjaxProcessor.AjaxBehaviorListenerImpl;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * @author Richard Kennard
 */

public class AjaxProcessorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		AjaxProcessor processor = new AjaxProcessor();

		// Pass through

		Map<String, String> attributes = CollectionUtils.newHashMap();

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setId( "metawidget-id" );
		UIComponent component = new HtmlInputText();
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertTrue( ( (ClientBehaviorHolder) component ).getClientBehaviors().isEmpty() );

		// Bad event?

		attributes.put( FACES_AJAX_EVENT, "onclick" );

		try {
			processor.processWidget( component, PROPERTY, attributes, metawidget );
			assertTrue( false );
		} catch( WidgetProcessorException e ) {

			// Should fail

			assertEquals( "'onclick' not a valid event for class javax.faces.component.html.HtmlInputText. Must be one of blur,change,valueChange,click,dblclick,focus,keydown,keypress,keyup,mousedown,mousemove,mouseout,mouseover,mouseup,select", e.getMessage() );
		}

		// Ajax

		attributes.put( FACES_AJAX_EVENT, "click" );
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( 1, ( (ClientBehaviorHolder) component ).getClientBehaviors().size() );

		List<ClientBehavior> clientBehaviors = ( (ClientBehaviorHolder) component ).getClientBehaviors().get( "click" );
		assertEquals( clientBehaviors.size(), 1 );
		AjaxBehavior ajaxBehaviour = (AjaxBehavior) clientBehaviors.get( 0 );

		assertEquals( CollectionUtils.newArrayList( "metawidget-id" ), ajaxBehaviour.getRender() );
		Field listenersField = BehaviorBase.class.getDeclaredField( "listeners" );
		listenersField.setAccessible( true );
		assertEquals( null, listenersField.get( ajaxBehaviour ));

		// Ajax with action

		component = new HtmlInputText();
		attributes.put( FACES_AJAX_ACTION, "#{bar}" );
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( ( (ClientBehaviorHolder) component ).getClientBehaviors().size(), 1 );

		clientBehaviors = ( (ClientBehaviorHolder) component ).getClientBehaviors().get( "click" );
		assertEquals( clientBehaviors.size(), 1 );
		ajaxBehaviour = (AjaxBehavior) clientBehaviors.get( 0 );

		assertEquals( CollectionUtils.newArrayList( "metawidget-id" ), ajaxBehaviour.getRender() );

		@SuppressWarnings( "unchecked" )
		List<AjaxBehaviorListener> listeners = (List<AjaxBehaviorListener>) listenersField.get( ajaxBehaviour );
		assertEquals( listeners.size(), 1 );

		AjaxBehaviorListener ajaxBehaviorListener = listeners.get( 0 );
		assertTrue( ajaxBehaviorListener instanceof Serializable );
		assertEquals( "#{bar}", ((AjaxBehaviorListenerImpl) ajaxBehaviorListener).mListenerMethod.getExpressionString() );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}
}
