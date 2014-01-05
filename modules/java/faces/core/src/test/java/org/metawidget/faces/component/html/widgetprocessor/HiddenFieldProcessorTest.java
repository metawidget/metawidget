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

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HiddenFieldProcessorTest
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

		HiddenFieldProcessor processor = new HiddenFieldProcessor();

		// Pass through

		UIComponent component = new HtmlInputText();
		assertEquals( component, processor.processWidget( component, PROPERTY, null, null ) );

		// No setter? No hidden field

		UIStub stub = new UIStub();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NO_SETTER, TRUE );
		assertEquals( stub, processor.processWidget( stub, PROPERTY, attributes, null ) );

		// Stubs become hidden fields directly

		attributes.remove( NO_SETTER );
		assertTrue( processor.processWidget( stub, PROPERTY, attributes, null ) instanceof HtmlInputHidden );

		// Non-empty Stubs get left alone

		component = new UIStub();
		component.getChildren().add( new HtmlOutputText() );
		assertEquals( processor.processWidget( component, PROPERTY, attributes, null ), component );

		// Everything else gets wrapped

		component = new HtmlOutputText();
		UIStub wrapped = (UIStub) processor.processWidget( component, PROPERTY, attributes, null );
		assertTrue( wrapped.getChildren().get( 0 ) instanceof HtmlInputHidden );
		assertEquals( component, wrapped.getChildren().get( 1 ) );
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
