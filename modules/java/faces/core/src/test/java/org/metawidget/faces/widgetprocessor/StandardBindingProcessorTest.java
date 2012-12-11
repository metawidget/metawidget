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
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.widgetprocessor.StandardBindingProcessor;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class StandardBindingProcessorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testWidgetProcessor()
		throws Exception {

		StandardBindingProcessor processor = new StandardBindingProcessor();
		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar}" ) );

		// Entity bindings

		Map<String, String> attributes = CollectionUtils.newHashMap();
		HtmlInputText component = new HtmlInputText();
		assertEquals( component, processor.processWidget( component, ENTITY, attributes, metawidget ) );
		assertEquals( "#{foo.bar}", component.getValueBinding( "value" ).getExpressionString() );

		// Do not overwrite existing value bindings

		attributes.put( NAME, "baz" );
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( "#{foo.bar}", component.getValueBinding( "value" ).getExpressionString() );

		// Property bindings

		component.setValueBinding( "value", null );
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( "#{foo.bar.baz}", component.getValueBinding( "value" ).getExpressionString() );

		// Action bindings

		HtmlCommandButton command = new HtmlCommandButton();
		assertEquals( command, processor.processWidget( command, ACTION, attributes, metawidget ) );
		assertEquals( "#{foo.bar.baz}", command.getAction().getExpressionString() );

		// Do not overwrite existing action bindings

		metawidget.setValueBinding( "value", null );
		assertEquals( command, processor.processWidget( command, ACTION, attributes, metawidget ) );
		assertEquals( "#{foo.bar.baz}", command.getAction().getExpressionString() );

		// Raw bindings

		command.setAction( null );
		assertEquals( command, processor.processWidget( command, ACTION, attributes, metawidget ) );
		assertEquals( "baz", command.getAction().getExpressionString() );

		// Faces expression for actions

		attributes.put( FACES_EXPRESSION, "#{abc}" );
		command.setAction( null );
		assertEquals( command, processor.processWidget( command, ACTION, attributes, metawidget ) );
		assertEquals( "#{abc}", command.getAction().getExpressionString() );

		component.setValueBinding( "value", null );
		assertEquals( component, processor.processWidget( component, PROPERTY, attributes, metawidget ) );
		assertEquals( "#{abc}", component.getValueBinding( "value" ).getExpressionString() );
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
