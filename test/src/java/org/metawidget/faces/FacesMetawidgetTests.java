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

package org.metawidget.faces;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKit;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.faces.component.UIMetawidgetTest;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.layout.OutputTextLayoutDecoratorTest;
import org.metawidget.faces.component.html.layout.PanelGroupLayoutDecoratorTest;
import org.metawidget.faces.component.html.layout.richfaces.PanelLayoutDecoratorTest;
import org.metawidget.faces.component.html.layout.richfaces.SimpleTogglePanelLayoutDecoratorTest;
import org.metawidget.faces.component.html.layout.richfaces.TabPanelLayoutDecoratorTest;
import org.metawidget.faces.component.html.widgetprocessor.CssStyleProcessorTest;
import org.metawidget.faces.component.html.widgetprocessor.HiddenFieldProcessorTest;
import org.metawidget.faces.component.html.widgetprocessor.richfaces.RichFacesProcessorTest;
import org.metawidget.faces.component.widgetbuilder.OverriddenWidgetBuilderTest;
import org.metawidget.faces.widgetbuilder.HtmlWidgetBuilderTest;
import org.metawidget.faces.widgetbuilder.icefaces.IceFacesWidgetBuilderTest;
import org.metawidget.faces.widgetbuilder.richfaces.RichFacesWidgetBuilderTest;
import org.metawidget.faces.widgetbuilder.tomahawk.TomahawkWidgetBuilderTest;
import org.metawidget.faces.widgetprocessor.ImmediateAttributeProcessorTest;
import org.metawidget.faces.widgetprocessor.LabelProcessorTest;
import org.metawidget.faces.widgetprocessor.ReadableIdProcessorTest;
import org.metawidget.faces.widgetprocessor.RequiredAttributeProcessorTest;
import org.metawidget.faces.widgetprocessor.StandardBindingProcessorTest;
import org.metawidget.faces.widgetprocessor.StandardConverterProcessorTest;
import org.metawidget.faces.widgetprocessor.StandardValidatorProcessorTest;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

@SuppressWarnings( "all" )
public class FacesMetawidgetTests
	extends TestCase
{
	//
	// Public statics
	//

	public static Test suite()
	{
		TestSuite suite = new TestSuite( "Faces Metawidget Tests" );
		suite.addTestSuite( CssStyleProcessorTest.class );
		suite.addTestSuite( FacesUtilsTest.class );
		suite.addTestSuite( HiddenFieldProcessorTest.class );
		suite.addTestSuite( HtmlWidgetBuilderTest.class );
		suite.addTestSuite( LabelProcessorTest.class );
		suite.addTestSuite( IceFacesWidgetBuilderTest.class );
		suite.addTestSuite( ImmediateAttributeProcessorTest.class );
		suite.addTestSuite( OutputTextLayoutDecoratorTest.class );
		suite.addTestSuite( OverriddenWidgetBuilderTest.class );
		suite.addTestSuite( PanelGroupLayoutDecoratorTest.class );
		suite.addTestSuite( PanelLayoutDecoratorTest.class );
		suite.addTestSuite( ReadableIdProcessorTest.class );
		suite.addTestSuite( RequiredAttributeProcessorTest.class );
		suite.addTestSuite( RichFacesProcessorTest.class );
		suite.addTestSuite( RichFacesWidgetBuilderTest.class );
		suite.addTestSuite( SimpleTogglePanelLayoutDecoratorTest.class );
		suite.addTestSuite( StandardBindingProcessorTest.class );
		suite.addTestSuite( StandardConverterProcessorTest.class );
		suite.addTestSuite( StandardValidatorProcessorTest.class );
		suite.addTestSuite( TabPanelLayoutDecoratorTest.class );
		suite.addTestSuite( TomahawkWidgetBuilderTest.class );
		suite.addTestSuite( UIMetawidgetTest.class );

		return suite;
	}

	//
	// Inner class
	//

	public static class MockFacesContext
		extends FacesContext
	{
		//
		// Protected members
		//

		protected Map<String, Object>	mApplicationMap = CollectionUtils.newHashMap();

		//
		// Constructor
		//

		public MockFacesContext()
		{
			FacesContext.setCurrentInstance( this );
		}

		//
		// Public methods
		//

		public void unregisterCurrentInstance()
		{
			FacesContext.setCurrentInstance( null );
		}

		//
		// Supported public methods
		//

		@Override
		public Application getApplication()
		{
			return new Application()
			{
				//
				// Supported public methods
				//

				@Override
				public UIComponent createComponent( String componentName )
					throws FacesException
				{
					return MockFacesContext.this.createComponent( componentName );
				}

				@Override
				public ValueBinding createValueBinding( String expressionString )
					throws ReferenceSyntaxException
				{
					return new MockValueBinding( expressionString );
				}

				@Override
				public Validator createValidator( String validatorName )
					throws FacesException
				{
					if ( "javax.faces.LongRange".equals( validatorName ) )
						return new LongRangeValidator();

					if ( "javax.faces.DoubleRange".equals( validatorName ) )
						return new DoubleRangeValidator();

					if ( "javax.faces.Length".equals( validatorName ) )
						return new LengthValidator();

					throw new UnsupportedOperationException( "Unknown validator '" + validatorName + "'" );
				}

				//
				// Unsupported public methods
				//

				@Override
				public void addComponent( String s, String s1 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public ExpressionFactory getExpressionFactory()
				{
					throw new NoSuchMethodError( "MockFacesContext mimics JSF 1.1" );
				}

				@Override
				public void addConverter( String s, String s1 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void addConverter( Class class1, String s )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void addValidator( String s, String s1 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public UIComponent createComponent( ValueBinding valuebinding, FacesContext facescontext, String s )
					throws FacesException
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Converter createConverter( String s )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Converter createConverter( Class class1 )
				{
					return null;
				}

				@Override
				public MethodBinding createMethodBinding( String expression, Class[] params )
					throws ReferenceSyntaxException
				{
					return new MockMethodBinding( expression, params );
				}

				@Override
				public ActionListener getActionListener()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getComponentTypes()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getConverterIds()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<Class<?>> getConverterTypes()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Locale getDefaultLocale()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String getDefaultRenderKitId()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String getMessageBundle()
				{
					return null;
				}

				@Override
				public NavigationHandler getNavigationHandler()
				{
					return null;
				}

				@Override
				public PropertyResolver getPropertyResolver()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public StateManager getStateManager()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<Locale> getSupportedLocales()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getValidatorIds()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public VariableResolver getVariableResolver()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public ViewHandler getViewHandler()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setActionListener( ActionListener actionlistener )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setDefaultLocale( Locale locale )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setDefaultRenderKitId( String s )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setMessageBundle( String s )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setNavigationHandler( NavigationHandler navigationhandler )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setPropertyResolver( PropertyResolver propertyresolver )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setStateManager( StateManager statemanager )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setSupportedLocales( Collection<Locale> arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setVariableResolver( VariableResolver variableresolver )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setViewHandler( ViewHandler viewhandler )
				{
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public UIViewRoot getViewRoot()
		{
			return new UIViewRoot()
			{

				@Override
				public String createUniqueId()
				{
					return "unique-id";
				}
			};
		}

		//
		// Unsupported public methods
		//

		@Override
		public void addMessage( String s, FacesMessage facesmessage )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<String> getClientIdsWithMessages()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public ExternalContext getExternalContext()
		{
			return new ExternalContext()
			{
				//
				// Supported public methods
				//

				@Override
				public Map<String, Object> getApplicationMap()
				{
					return MockFacesContext.this.mApplicationMap;
				}

				@Override
				public URL getResource( String arg0 )
					throws MalformedURLException
				{
					return null;
				}

				@Override
				public String getInitParameter( String name )
				{
					return null;
				}

				//
				// Unsupported public methods
				//

				@Override
				public void dispatch( String arg0 )
					throws IOException
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String encodeActionURL( String arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String encodeNamespace( String arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String encodeResourceURL( String arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String getAuthType()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Object getContext()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map getInitParameterMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String getRemoteUser()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Object getRequest()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String getRequestContextPath()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map<String, Object> getRequestCookieMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map<String, String> getRequestHeaderMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map<String, String[]> getRequestHeaderValuesMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Locale getRequestLocale()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<Locale> getRequestLocales()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map<String, Object> getRequestMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map<String, String> getRequestParameterMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getRequestParameterNames()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map<String, String[]> getRequestParameterValuesMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String getRequestPathInfo()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public String getRequestServletPath()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public InputStream getResourceAsStream( String arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Set<String> getResourcePaths( String arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Object getResponse()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Object getSession( boolean arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Map<String, Object> getSessionMap()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Principal getUserPrincipal()
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean isUserInRole( String arg0 )
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void log( String arg0 )
				{
					throw new UnsupportedOperationException();

				}

				@Override
				public void log( String arg0, Throwable arg1 )
				{
					throw new UnsupportedOperationException();

				}

				@Override
				public void redirect( String arg0 )
					throws IOException
				{
					throw new UnsupportedOperationException();

				}

			};
		}

		@Override
		public Severity getMaximumSeverity()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<FacesMessage> getMessages()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<FacesMessage> getMessages( String s )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public RenderKit getRenderKit()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getRenderResponse()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getResponseComplete()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public ResponseStream getResponseStream()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public ResponseWriter getResponseWriter()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void release()
		{
			FacesContext.setCurrentInstance( null );
		}

		@Override
		public void renderResponse()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void responseComplete()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setResponseStream( ResponseStream responsestream )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setResponseWriter( ResponseWriter responsewriter )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setViewRoot( UIViewRoot uiviewroot )
		{
			throw new UnsupportedOperationException();
		}

		//
		// Supported protected methods
		//

		protected UIComponent createComponent( String componentName )
		{
			if ( "javax.faces.HtmlOutputText".equals( componentName ) )
				return new HtmlOutputText();

			if ( "javax.faces.HtmlInputHidden".equals( componentName ) )
				return new HtmlInputHidden();

			if ( "javax.faces.HtmlInputText".equals( componentName ) )
				return new HtmlInputText();

			if ( "javax.faces.HtmlInputTextarea".equals( componentName ) )
				return new HtmlInputTextarea();

			if ( "javax.faces.HtmlInputSecret".equals( componentName ) )
				return new HtmlInputSecret();

			if ( "javax.faces.HtmlCommandButton".equals( componentName ) )
				return new HtmlCommandButton();

			if ( "javax.faces.HtmlSelectOneListbox".equals( componentName ) )
				return new HtmlSelectOneListbox();

			if ( "javax.faces.HtmlSelectManyCheckbox".equals( componentName ) )
				return new HtmlSelectManyCheckbox();

			if ( "javax.faces.HtmlSelectBooleanCheckbox".equals( componentName ) )
				return new HtmlSelectBooleanCheckbox();

			if ( "javax.faces.HtmlDataTable".equals( componentName ) )
				return new HtmlDataTable();

			if ( "javax.faces.HtmlCommandLink".equals( componentName ) )
				return new HtmlCommandLink();

			if ( "javax.faces.Column".equals( componentName ) )
				return new HtmlColumn();

			if ( "javax.faces.SelectItems".equals( componentName ) )
				return new UISelectItems();

			if ( "javax.faces.SelectItem".equals( componentName ) )
				return new UISelectItem();

			if ( "org.metawidget.Stub".equals( componentName ) )
				return new UIStub();

			if ( "javax.faces.Parameter".equals( componentName ) )
				return new UIParameter();

			return new MockComponent( componentName );
		}
	}

	public static class MockComponent
		extends UIComponentBase
	{
		//
		// Private members
		//

		private String	mFamily;

		//
		// Constructor
		//

		public MockComponent( String family )
		{
			mFamily = family;
		}

		//
		// Public methods
		//

		@Override
		public String getFamily()
		{
			return mFamily;
		}
	}

	public static class MockValueBinding
		extends ValueBinding
	{
		//
		// Private members
		//

		private String	mExpressionString;

		//
		// Constructor
		//

		public MockValueBinding( String expressionString )
		{
			mExpressionString = expressionString;
		}

		//
		// Public methods
		//

		@Override
		public String getExpressionString()
		{
			return mExpressionString;
		}

		@Override
		public Class getType( FacesContext context )
			throws EvaluationException, PropertyNotFoundException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getValue( FacesContext context )
			throws EvaluationException, PropertyNotFoundException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isReadOnly( FacesContext context )
			throws EvaluationException, PropertyNotFoundException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue( FacesContext context, Object value )
			throws EvaluationException, PropertyNotFoundException
		{
			throw new UnsupportedOperationException();
		}
	}

	public static class MockMethodBinding
		extends MethodBinding
	{
		//
		// Private members
		//

		private String	mExpressionString;

		private Class[] mParams;

		//
		// Constructor
		//

		public MockMethodBinding( String expressionString, Class[] params )
		{
			mExpressionString = expressionString;
			mParams = params;
		}

		//
		// Public methods
		//

		@Override
		public String getExpressionString()
		{
			return mExpressionString;
		}

		public Class[] getParams()
		{
			return mParams;
		}

		@Override
		public Class getType( FacesContext context )
			throws MethodNotFoundException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Object invoke( FacesContext context, Object[] args )
			throws EvaluationException, MethodNotFoundException
		{
			throw new UnsupportedOperationException();
		}
	}
}
