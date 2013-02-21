// Metawidget (licensed under LGPL)
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

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;

/**
 * @author Richard Kennard
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
