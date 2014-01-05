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

package org.metawidget.faces.component.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CssStyleProcessorTest
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

		CssStyleProcessor processor = new CssStyleProcessor();

		// Pass through

		HtmlMetawidget metawidget = new HtmlMetawidget();
		UIComponent component = new HtmlInputText();
		assertEquals( component, processor.processWidget( component, PROPERTY, null, metawidget ) );
		assertEquals( null, component.getAttributes().get( "style" ) );
		assertEquals( null, component.getAttributes().get( "styleClass" ) );

		// Simple styles and styleClasses

		metawidget.setStyle( "foo1" );
		metawidget.setStyleClass( "bar1" );
		assertEquals( component, processor.processWidget( component, PROPERTY, null, metawidget ) );
		assertEquals( "foo1", component.getAttributes().get( "style" ) );
		assertEquals( "bar1", component.getAttributes().get( "styleClass" ) );

		// Compound styles and styleClasses

		metawidget.setStyle( "foo2" );
		metawidget.setStyleClass( "bar2" );
		assertEquals( component, processor.processWidget( component, PROPERTY, null, metawidget ) );
		assertEquals( "foo1 foo2", component.getAttributes().get( "style" ) );
		assertEquals( "bar1 bar2", component.getAttributes().get( "styleClass" ) );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}
}
