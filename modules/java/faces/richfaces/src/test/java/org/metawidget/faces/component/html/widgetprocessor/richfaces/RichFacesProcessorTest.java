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

package org.metawidget.faces.component.html.widgetprocessor.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RichFacesProcessorTest
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

		RichFacesProcessor processor = new RichFacesProcessor();

		// Pass through

		Map<String, String> attributes = CollectionUtils.newHashMap();

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setId( "metawidget-id" );
		UIComponent component = new HtmlInputText();
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( 0, component.getChildCount() );

		// Ajax

		attributes.put( FACES_AJAX_EVENT, "onFoo" );
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( 1, component.getChildCount() );

		HtmlAjaxSupport ajaxSupport = (HtmlAjaxSupport) component.getChildren().get( 0 );

		assertTrue( ajaxSupport.getId() != null );
		assertEquals( "onFoo", ajaxSupport.getEvent() );
		assertEquals( "metawidget-id", ajaxSupport.getReRender() );
		assertEquals( null, ajaxSupport.getActionExpression() );

		// Ajax with action

		component = new HtmlInputText();
		attributes.put( FACES_AJAX_ACTION, "#{bar}" );
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( 1, component.getChildCount() );

		ajaxSupport = (HtmlAjaxSupport) component.getChildren().get( 0 );

		assertTrue( ajaxSupport.getId() != null );
		assertEquals( "onFoo", ajaxSupport.getEvent() );
		assertEquals( "metawidget-id", ajaxSupport.getReRender() );
		assertEquals( "#{bar}", ajaxSupport.getActionExpression().getExpressionString() );
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

			if ( "org.ajax4jsf.Support".equals( componentName ) ) {
				return new HtmlAjaxSupport();
			}

			return super.createComponent( componentName );
		}
	}
}
