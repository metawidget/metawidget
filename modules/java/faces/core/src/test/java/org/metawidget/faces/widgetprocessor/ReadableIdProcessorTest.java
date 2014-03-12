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

package org.metawidget.faces.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.widgetprocessor.ReadableIdProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadableIdProcessorTest
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

		ReadableIdProcessor processor = new ReadableIdProcessor();
		HtmlMetawidget metawidget = new HtmlMetawidget();

		// Property bindings

		HtmlInputText textComponent = new HtmlInputText();
		textComponent.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo}" ) );
		assertEquals( textComponent, processor.processWidget( textComponent, PROPERTY, null, metawidget ) );
		assertEquals( "foo", textComponent.getId() );

		// Existing ids should not get overwritten

		textComponent.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar}" ) );
		assertEquals( textComponent, processor.processWidget( textComponent, ENTITY, null, metawidget ) );
		assertEquals( "foo", textComponent.getId() );

		// Multi-name bindings

		textComponent.setId( null );
		textComponent.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar}" ) );
		assertEquals( textComponent, processor.processWidget( textComponent, ENTITY, null, metawidget ) );
		assertEquals( "fooBar", textComponent.getId() );

		// Duplicate bindings

		textComponent.setId( null );
		metawidget.getChildren().add( textComponent );
		assertEquals( textComponent, processor.processWidget( textComponent, PROPERTY, null, metawidget ) );
		assertEquals( "fooBar_2", textComponent.getId() );

		// Action bindings

		HtmlCommandButton command = new HtmlCommandButton();
		command.setAction( mContext.getApplication().createMethodBinding( "#{foo.bar.action}", null ) );
		assertEquals( command, processor.processWidget( command, ACTION, null, metawidget ) );
		assertEquals( "fooBarAction", command.getId() );

		// Metawidgets

		HtmlMetawidget childMetawidget = new HtmlMetawidget();
		childMetawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar.baz}" ) );
		assertEquals( childMetawidget, processor.processWidget( childMetawidget, PROPERTY, null, metawidget ) );
		assertEquals( "fooBarBaz_Metawidget", childMetawidget.getId() );

		// Duplicate metawidgets

		metawidget.getChildren().add( childMetawidget );
		childMetawidget.setId( null );
		childMetawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar.baz}" ) );
		assertEquals( childMetawidget, processor.processWidget( childMetawidget, PROPERTY, null, metawidget ) );
		assertEquals( "fooBarBaz_Metawidget_2", childMetawidget.getId() );

		// Stubs

		UIStub stub = new UIStub();
		stub.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar}" ) );
		stub.getChildren().add( textComponent );
		textComponent.setId( null );
		HtmlInputText textComponent2 = new HtmlInputText();
		stub.getChildren().add( textComponent2 );
		assertEquals( stub, processor.processWidget( stub, PROPERTY, null, metawidget ) );
		assertTrue( stub.getId() != null );
		assertEquals( "fooBar_3", textComponent.getId() );
		assertEquals( "fooBar_3_2", textComponent2.getId() );

		// Array indexes

		textComponent = new HtmlInputText();
		textComponent.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{something.list[0].property}" ) );
		assertEquals( textComponent, processor.processWidget( textComponent, PROPERTY, null, metawidget ) );
		assertEquals( "somethingList_0_Property", textComponent.getId() );

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
