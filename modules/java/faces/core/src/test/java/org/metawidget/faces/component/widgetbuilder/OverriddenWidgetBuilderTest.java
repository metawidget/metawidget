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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
