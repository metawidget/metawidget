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

package org.metawidget.faces.widgetprocessor;

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
import org.metawidget.faces.component.html.widgetprocessor.HiddenFieldProcessor;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class HiddenFieldProcessorTest
	extends TestCase
{
	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception
	{
		HiddenFieldProcessor processor = new HiddenFieldProcessor();

		// Pass through

		UIComponent component = new HtmlInputText();
		assertTrue( component == processor.processWidget( component, PROPERTY, null, null ));

		// No setter? No hidden field

		UIStub stub = new UIStub();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NO_SETTER, TRUE );
		assertTrue( stub == processor.processWidget( stub, PROPERTY, attributes, null ));

		// Stubs become hidden fields directly

		attributes.remove( NO_SETTER );
		assertTrue( processor.processWidget( stub, PROPERTY, attributes, null ) instanceof HtmlInputHidden );

		// Everything else gets wrapped

		component = new HtmlOutputText();
		UIStub wrapped = (UIStub) processor.processWidget( component, PROPERTY, attributes, null );
		assertTrue( wrapped.getChildren().get( 0 ) instanceof HtmlInputHidden );
		assertTrue( component == wrapped.getChildren().get( 1 ) );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception
	{
		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected final void tearDown()
		throws Exception
	{
		super.tearDown();

		mContext.release();
	}
}
