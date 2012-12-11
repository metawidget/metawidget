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

package org.metawidget.faces.component.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * UIMetawidget test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class OverriddenWidgetBuilderTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testOverriddenWidget()
		throws Exception {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{root}" ) );
		HtmlInputText htmlInputText1 = new HtmlInputText();
		htmlInputText1.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo}" ) );
		metawidget.getChildren().add( htmlInputText1 );
		HtmlInputText htmlInputText2 = new HtmlInputText();
		htmlInputText2.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{root.bar}" ) );
		metawidget.getChildren().add( htmlInputText2 );
		HtmlInputText htmlInputText3 = new HtmlInputText();
		htmlInputText3.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{root}" ) );
		metawidget.getChildren().add( htmlInputText3 );
		HtmlCommandButton htmlCommandButton1 = new HtmlCommandButton();
		htmlCommandButton1.setAction( mContext.getApplication().createMethodBinding( "#{root.baz}", null ) );
		metawidget.getChildren().add( htmlCommandButton1 );
		HtmlCommandButton htmlCommandButton2 = new HtmlCommandButton();
		htmlCommandButton2.setAction( mContext.getApplication().createMethodBinding( "#{abc}", null ) );
		metawidget.getChildren().add( htmlCommandButton2 );

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Pass through

		OverriddenWidgetBuilder overriddenWidgetBuilder = new OverriddenWidgetBuilder();
		assertEquals( null, overriddenWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Test Faces Expression overrides property name

		attributes.put( FACES_EXPRESSION, "#{foo}" );
		attributes.put( NAME, "bar" );
		assertEquals( htmlInputText1, overriddenWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Test name

		attributes.remove( FACES_EXPRESSION );
		assertEquals( htmlInputText2, overriddenWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Test action

		attributes.put( NAME, "baz" );
		assertEquals( htmlCommandButton1, overriddenWidgetBuilder.buildWidget( ACTION, attributes, metawidget ) );

		// Test Faces Expression overrides action name

		attributes.put( FACES_EXPRESSION, "#{abc}" );
		assertEquals( htmlCommandButton2, overriddenWidgetBuilder.buildWidget( ACTION, attributes, metawidget ) );

		// Test ENTITY looks for overrides at the top level (ie. the single widget scenario, where
		// the top-level type can be represented by a single widget, and then it gets POSTed back)

		attributes.remove( FACES_EXPRESSION );
		assertEquals( htmlInputText3, overriddenWidgetBuilder.buildWidget( ENTITY, attributes, metawidget ) );
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
