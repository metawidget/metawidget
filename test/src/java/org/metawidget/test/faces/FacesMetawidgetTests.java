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

package org.metawidget.test.faces;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlCommandButton;
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
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKit;
import javax.faces.validator.Validator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.faces.component.UIStub;
import org.metawidget.test.faces.widgetbuilder.HtmlWidgetBuilderTest;
import org.metawidget.test.faces.widgetbuilder.icefaces.IceFacesWidgetBuilderTest;
import org.metawidget.test.faces.widgetbuilder.richfaces.RichFacesWidgetBuilderTest;
import org.metawidget.test.faces.widgetbuilder.tomahawk.TomahawkWidgetBuilderTest;

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
		suite.addTestSuite( HtmlWidgetBuilderTest.class );
		suite.addTestSuite( IceFacesWidgetBuilderTest.class );
		suite.addTestSuite( RichFacesWidgetBuilderTest.class );
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
		// Constructor
		//

		public MockFacesContext()
		{
			FacesContext.setCurrentInstance( this );
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

				//
				// Unsupported public methods
				//

				@Override
				public void addComponent( String s, String s1 )
				{
					throw new UnsupportedOperationException();
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
				public MethodBinding createMethodBinding( String s, Class[] aclass )
					throws ReferenceSyntaxException
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public Validator createValidator( String s )
					throws FacesException
				{
					throw new UnsupportedOperationException();
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
				public Iterator<Class> getConverterTypes()
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
					throw new UnsupportedOperationException();
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
			throw new UnsupportedOperationException();
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

			if ( "javax.faces.SelectItems".equals( componentName ) )
				return new UISelectItems();

			if ( "javax.faces.SelectItem".equals( componentName ) )
				return new UISelectItem();

			if ( "org.metawidget.Stub".equals( componentName ) )
				return new UIStub();

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
}
