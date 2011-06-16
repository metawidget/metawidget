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

package org.metawidget.faces.component;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;

import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilder;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtilsTest;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * UIMetawidget test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class UIMetawidgetTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		UIMetawidget metawidget = new HtmlMetawidget();

		try {
			metawidget.encodeBegin( null );
		} catch ( Exception e ) {
			// Should fail with an IOException (not a MetawidgetException)

			assertTrue( e instanceof IOException );
		}
	}

	public void testValidationError()
		throws Exception {

		final StringBuilder result = new StringBuilder();

		UIMetawidget metawidget = new HtmlMetawidget() {

			@Override
			public void encodeBegin( FacesContext context )
				throws IOException {

				result.append( "encodeBegin called;" );

				super.encodeBegin( context );
			}

			@Override
			protected void configure() {

				// Should not be called

				result.append( "configure called;" );
			}

			@Override
			protected void startBuild() {

				// Should not be called

				result.append( "startBuild called;" );
			}

		};

		MockFacesContext context = new MockFacesContext() {

			@Override
			public Severity getMaximumSeverity() {

				return FacesMessage.SEVERITY_INFO;
			}

			@Override
			public RenderKit getRenderKit() {

				result.append( "getRenderKit called;" );
				return null;
			}

			@Override
			public Map<Object, Object> getAttributes() {

				result.append( "getAttributes called;" );
				return null;
			}
		};

		try {
			metawidget.encodeBegin( context );

			// Should throw a NullPointerException, because getRenderKit is null

			assertTrue( false );
		} catch ( NullPointerException e ) {
			// Should go straight to getRenderKit or pushComponentToEL, because validation error

			assertTrue( "encodeBegin called;getRenderKit called;".equals( result.toString() ) || "encodeBegin called;getAttributes called;".equals( result.toString() ) );
		} finally {
			context.unregisterCurrentInstance();
		}
	}

	@SuppressWarnings( "deprecation" )
	public void testNotRecreatable()
		throws Exception {

		UIMetawidget metawidget = new HtmlMetawidget();
		HtmlOutputText recreatableComponent1 = new HtmlOutputText();
		recreatableComponent1.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, "something" );
		HtmlOutputText notRecreatableComponent = new HtmlOutputText();
		notRecreatableComponent.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, "something" );
		notRecreatableComponent.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE, "true" );
		HtmlOutputText recreatableComponent2 = new HtmlOutputText();
		recreatableComponent2.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, "something" );

		metawidget.getChildren().add( recreatableComponent1 );
		metawidget.getChildren().add( notRecreatableComponent );
		metawidget.getChildren().add( recreatableComponent2 );

		assertTrue( 3 == metawidget.getChildCount() );
		metawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo}" ) );
		metawidget.startBuild();
		assertTrue( 1 == metawidget.getChildCount() );
		assertTrue( notRecreatableComponent == metawidget.getChildren().get( 0 ) );
	}

	public void testStub()
		throws Exception {

		UIStub stub = new UIStub();
		stub.setStubAttributes( "rendered:" );

		try {
			stub.getStubAttributesAsMap();
			assertTrue( false );
		} catch ( Exception e ) {
			// Should fail

			assertTrue( "Unrecognized value 'rendered:'".equals( e.getMessage() ) );
		}

		stub.setStubAttributes( "rendered:;" );

		try {
			stub.getStubAttributesAsMap();
			assertTrue( false );
		} catch ( Exception e ) {
			// Should fail

			assertTrue( "Unrecognized value 'rendered:'".equals( e.getMessage() ) );
		}

		stub.setStubAttributes( "rendered: true" );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( "rendered", TRUE );
		assertTrue( attributes.equals( stub.getStubAttributesAsMap() ) );

		stub.setStubAttributes( "rendered: false;" );
		attributes.put( "rendered", FALSE );
		assertTrue( attributes.equals( stub.getStubAttributesAsMap() ) );
	}

	public void testMissingConfig() {

		assertTrue( null == FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get( "metawidget-config-reader" ) );

		// Should not error (just log)

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.configure();

		assertEquals( "Could not locate metawidget.xml. This file is optional, but if you HAVE created one then Metawidget isn't finding it!", LogUtilsTest.getLastInfoMessage() );

		// Should have done something

		assertTrue( FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get( "metawidget-config-reader" ) instanceof ConfigReader );

		// Should error

		try {
			metawidget.setConfig( "does-not-exist.xml" );
			metawidget.configure();
			assertTrue( false );
		} catch ( MetawidgetException e ) {
			assertEquals( "java.io.FileNotFoundException: Unable to locate does-not-exist.xml on CLASSPATH", e.getMessage() );
		}

		// Should not re-log

		LogUtils.getLog( UIMetawidgetTest.class ).info( "" );
		metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.configure();

		assertFalse( "Could not locate metawidget.xml. This file is optional, but if you HAVE created one then Metawidget isn't finding it!".equals( LogUtilsTest.getLastInfoMessage() ) );
	}

	public void testSetWidgetBuilder() {

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = new HtmlWidgetBuilder();

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setWidgetBuilder( widgetBuilder );

		assertTrue( metawidget.getWidgetBuilder() == widgetBuilder );
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
