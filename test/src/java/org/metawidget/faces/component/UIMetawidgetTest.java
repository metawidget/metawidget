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
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;

import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.util.CollectionUtils;

/**
 * UIMetawidget test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class UIMetawidgetTest
	extends TestCase
{
	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception
	{
		UIMetawidget metawidget = new HtmlMetawidget();

		try
		{
			metawidget.encodeBegin( null );
		}
		catch ( Exception e )
		{
			// Should fail with an IOException (not a MetawidgetException)

			assertTrue( e instanceof IOException );
		}
	}

	public void testValidationError()
		throws Exception
	{
		final StringBuilder result = new StringBuilder();

		UIMetawidget metawidget = new HtmlMetawidget()
		{
			@Override
			public void encodeBegin( FacesContext context )
				throws IOException
			{
				result.append( "encodeBegin called;" );

				super.encodeBegin( context );
			}

			@Override
			protected void configure()
			{
				// Should not be called

				result.append( "configure called;" );
			}

			@Override
			protected void startBuild()
			{
				// Should not be called

				result.append( "startBuild called;" );
			}
		};

		MockFacesContext context = new MockFacesContext()
		{
			@Override
			public Severity getMaximumSeverity()
			{
				return FacesMessage.SEVERITY_INFO;
			}

			@Override
			public RenderKit getRenderKit()
			{
				result.append( "getRenderKit called;" );
				return null;
			}
		};

		try
		{
			metawidget.encodeBegin( context );

			// Should throw an IOException, because getRenderKit is null

			assertTrue( false );
		}
		catch ( IOException e )
		{
			assertTrue( "encodeBegin called;getRenderKit called;".equals( result.toString() ) );
		}
		finally
		{
			context.unregisterCurrentInstance();
		}
	}

	@SuppressWarnings("deprecation")
	public void testNotRecreatable()
		throws Exception
	{
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
		metawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo}" ));
		metawidget.startBuild();
		assertTrue( 1 == metawidget.getChildCount() );
		assertTrue( notRecreatableComponent == metawidget.getChildren().get( 0 ) );
	}

	public void testStub()
		throws Exception
	{
		UIStub stub = new UIStub();
		stub.setStubAttributes( "rendered:" );

		try
		{
			stub.getStubAttributes();
			assertTrue( false );
		}
		catch ( Exception e )
		{
			// Should fail

			assertTrue( "Unrecognized value 'rendered:'".equals( e.getMessage() ) );
		}

		stub.setStubAttributes( "rendered:;" );

		try
		{
			stub.getStubAttributes();
			assertTrue( false );
		}
		catch ( Exception e )
		{
			// Should fail

			assertTrue( "Unrecognized value 'rendered:'".equals( e.getMessage() ) );
		}

		stub.setStubAttributes( "rendered: true" );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( "rendered", TRUE );
		assertTrue( attributes.equals( stub.getStubAttributes() ) );

		stub.setStubAttributes( "rendered: false;" );
		attributes.put( "rendered", FALSE );
		assertTrue( attributes.equals( stub.getStubAttributes() ) );
	}

	@SuppressWarnings( "deprecation" )
	public void testOverriddenWidget()
		throws Exception
	{
		Method getOverriddenWidget = UIMetawidget.class.getDeclaredMethod( "getOverriddenWidget", new Class[] { String.class, Map.class } );
		getOverriddenWidget.setAccessible( true );

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

		assertTrue( null == getOverriddenWidget.invoke( metawidget, new Object[] { PROPERTY, attributes } ) );

		// Test Faces Expression overrides property name

		attributes.put( FACES_EXPRESSION, "#{foo}" );
		attributes.put( NAME, "bar" );
		assertTrue( htmlInputText1 == getOverriddenWidget.invoke( metawidget, new Object[] { PROPERTY, attributes } ) );

		// Test name

		attributes.remove( FACES_EXPRESSION );
		assertTrue( htmlInputText2 == getOverriddenWidget.invoke( metawidget, new Object[] { PROPERTY, attributes } ) );

		// Test action

		attributes.put( NAME, "baz" );
		assertTrue( htmlCommandButton1 == getOverriddenWidget.invoke( metawidget, new Object[] { ACTION, attributes } ) );

		// Test Faces Expression overrides action name

		attributes.put( FACES_EXPRESSION, "#{abc}" );
		assertTrue( htmlCommandButton2 == getOverriddenWidget.invoke( metawidget, new Object[] { ACTION, attributes } ) );

		// Test ENTITY looks for overrides at the top level (ie. the single widget scenario, where
		// the top-level type can be represented by a single widget, and then it gets POSTed back)

		attributes.remove( FACES_EXPRESSION );
		assertTrue( htmlInputText3 == getOverriddenWidget.invoke( metawidget, new Object[] { ENTITY, attributes } ) );
	}

	public void testMissingDefaultConfig()
	{
		assertTrue( null == FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get( "metawidget-config-reader" ));

		// Should not error (just log)

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.configure();

		// Should have done something

		assertTrue( FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get( "metawidget-config-reader" ) instanceof ConfigReader );

		// Should error

		try
		{
			metawidget.setConfig( "does-not-exist.xml" );
			metawidget.configure();
			assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "java.io.FileNotFoundException: Unable to locate does-not-exist.xml on CLASSPATH".equals( e.getMessage()));
		}
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
