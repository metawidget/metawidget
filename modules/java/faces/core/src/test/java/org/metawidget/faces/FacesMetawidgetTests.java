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

package org.metawidget.faces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
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
import javax.faces.event.SystemEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;

import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@SuppressWarnings( "all" )
public class FacesMetawidgetTests {

	//
	// Inner class
	//

	public static class MockPageContext
		extends PageContext {

		//
		// Private members
		//

		Map<String, Object>	mAttributes	= CollectionUtils.newHashMap();

		//
		// Supported public methods
		//

		@Override
		public ServletContext getServletContext() {

			return null;
		}

		@Override
		public Object getAttribute( String name ) {

			return mAttributes.get( name );
		}

		@Override
		public void setAttribute( String name, Object value ) {

			mAttributes.put( name, value );
		}

		//
		// Unsupported public methods
		//

		@Override
		public void forward( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Exception getException() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object getPage() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletRequest getRequest() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletResponse getResponse() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletConfig getServletConfig() {

			throw new UnsupportedOperationException();
		}

		@Override
		public HttpSession getSession() {

			return null;
		}

		@Override
		public void handlePageException( Exception arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void handlePageException( Throwable arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void include( String arg0, boolean arg1 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void include( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void initialize( Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3, boolean arg4, int arg5, boolean arg6 )
			throws IllegalStateException, IllegalArgumentException {

			throw new UnsupportedOperationException();
		}

		@Override
		public void release() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object findAttribute( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object getAttribute( String name, int index ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration getAttributeNamesInScope( int arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributesScope( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public ExpressionEvaluator getExpressionEvaluator() {

			throw new UnsupportedOperationException();
		}

		@Override
		public JspWriter getOut() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void removeAttribute( String arg0, int arg1 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void removeAttribute( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttribute( String arg0, Object arg1, int arg2 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public ELContext getELContext() {

			throw new UnsupportedOperationException();
		}

		@Override
		public javax.servlet.jsp.el.VariableResolver getVariableResolver() {

			throw new UnsupportedOperationException();
		}
	}

	public static class MockFacesContext
		extends FacesContext {

		//
		// Protected members
		//

		protected Map<String, Object>	mApplicationMap	= CollectionUtils.newHashMap();

		//
		// Private members
		//

		private ResponseWriter			mResponseWriter;

		private ExternalContext			mExternalContext;

		//
		// Constructor
		//

		public MockFacesContext() {

			FacesContext.setCurrentInstance( this );

			// We generally unit test JSF 1.x, and use webtest for JSF 2.x

			@SuppressWarnings( "unchecked" )
			Map<String, String> initParameterMap = getExternalContext().getInitParameterMap();
			initParameterMap.put( "org.metawidget.faces.component.DONT_USE_PRERENDER_VIEW_EVENT", "true" );
		}

		//
		// Public methods
		//

		public void unregisterCurrentInstance() {

			FacesContext.setCurrentInstance( null );
		}

		//
		// Supported public methods
		//

		@Override
		public Application getApplication() {

			return new Application() {

				//
				// Supported public methods
				//

				@Override
				public Behavior createBehavior( String behaviorId )
					throws FacesException {

					if ( AjaxBehavior.BEHAVIOR_ID.equals( behaviorId ) ) {
						return new AjaxBehavior();
					}

					throw new UnsupportedOperationException( "Unknown behavior '" + behaviorId + "'" );
				}

				@Override
				public UIComponent createComponent( String componentType )
					throws FacesException {

					return MockFacesContext.this.createComponent( componentType );
				}

				public UIComponent createComponent( FacesContext context, String componentType, String rendererType ) {

					return MockFacesContext.this.createComponent( componentType );
				}

				@Override
				public ValueBinding createValueBinding( String expressionString )
					throws ReferenceSyntaxException {

					return new MockValueBinding( expressionString );
				}

				@Override
				public Validator createValidator( String validatorName )
					throws FacesException {

					if ( LongRangeValidator.VALIDATOR_ID.equals( validatorName ) ) {
						return new LongRangeValidator();
					}

					if ( DoubleRangeValidator.VALIDATOR_ID.equals( validatorName ) ) {
						return new DoubleRangeValidator();
					}

					if ( LengthValidator.VALIDATOR_ID.equals( validatorName ) ) {
						return new LengthValidator();
					}

					if ( BeanValidator.VALIDATOR_ID.equals( validatorName ) ) {
						return new BeanValidator();
					}

					throw new UnsupportedOperationException( "Unknown validator '" + validatorName + "'" );
				}

				@Override
				public void publishEvent( FacesContext context, Class<? extends SystemEvent> systemEventClass, Object source ) {

					// Do nothing
				}

				@Override
				public void publishEvent( FacesContext context, Class<? extends SystemEvent> systemEventClass, Class<?> sourceBaseType, Object source ) {

					// Do nothing
				}

				//
				// Unsupported public methods
				//

				@Override
				public void addComponent( String s, String s1 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public ExpressionFactory getExpressionFactory() {

					return new ExpressionFactory() {

						@Override
						public Object coerceToType( Object arg0, Class<?> arg1 )
							throws ELException {

							throw new UnsupportedOperationException();
						}

						@Override
						public MethodExpression createMethodExpression( ELContext elContext, final String expression, final Class<?> returnType, final Class<?>[] parameters )
							throws ELException, NullPointerException {

							return new MethodExpression() {

								@Override
								public MethodInfo getMethodInfo( ELContext context )
									throws NullPointerException, javax.el.PropertyNotFoundException, javax.el.MethodNotFoundException, ELException {

									return new MethodInfo( FacesUtils.unwrapExpression( expression ), returnType, parameters );
								}

								@Override
								public String getExpressionString() {

									return expression;
								}

								@Override
								public Object invoke( ELContext context, Object[] arg1 )
									throws NullPointerException, javax.el.PropertyNotFoundException, javax.el.MethodNotFoundException, ELException {

									throw new UnsupportedOperationException();
								}

								@Override
								public boolean equals( Object arg0 ) {

									throw new UnsupportedOperationException();
								}

								@Override
								public int hashCode() {

									throw new UnsupportedOperationException();
								}

								@Override
								public boolean isLiteralText() {

									throw new UnsupportedOperationException();
								}
							};
						}

						@Override
						public ValueExpression createValueExpression( Object arg0, Class<?> arg1 ) {

							throw new NoSuchMethodError( "MockFacesContext mimics JSF 1.1" );
						}

						@Override
						public ValueExpression createValueExpression( ELContext elContext, String arg1, Class<?> arg2 )
							throws NullPointerException, ELException {

							throw new NoSuchMethodError( "MockFacesContext mimics JSF 1.1" );
						}
					};
				}

				@Override
				public void addConverter( String s, String s1 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void addConverter( Class class1, String s ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void addValidator( String s, String s1 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public UIComponent createComponent( ValueBinding valuebinding, FacesContext facescontext, String s )
					throws FacesException {

					throw new UnsupportedOperationException();
				}

				@Override
				public Converter createConverter( final String converterId ) {

					return new MockConverter( converterId );
				}

				@Override
				public Converter createConverter( Class class1 ) {

					return null;
				}

				@Override
				public MethodBinding createMethodBinding( String expression, Class[] params )
					throws ReferenceSyntaxException {

					return new MockMethodBinding( expression, params );
				}

				@Override
				public ActionListener getActionListener() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getComponentTypes() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getConverterIds() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<Class<?>> getConverterTypes() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Locale getDefaultLocale() {

					throw new UnsupportedOperationException();
				}

				@Override
				public String getDefaultRenderKitId() {

					throw new UnsupportedOperationException();
				}

				@Override
				public String getMessageBundle() {

					return null;
				}

				@Override
				public NavigationHandler getNavigationHandler() {

					return null;
				}

				@Override
				public PropertyResolver getPropertyResolver() {

					throw new UnsupportedOperationException();
				}

				@Override
				public StateManager getStateManager() {

					return new StateManager() {

						@Override
						public UIViewRoot restoreView( FacesContext context, String viewId, String renderKitId ) {

							throw new UnsupportedOperationException();
						}
					};
				}

				@Override
				public Iterator<Locale> getSupportedLocales() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getValidatorIds() {

					throw new UnsupportedOperationException();
				}

				@Override
				public VariableResolver getVariableResolver() {

					throw new UnsupportedOperationException();
				}

				@Override
				public ViewHandler getViewHandler() {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setActionListener( ActionListener actionlistener ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setDefaultLocale( Locale locale ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setDefaultRenderKitId( String s ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setMessageBundle( String s ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setNavigationHandler( NavigationHandler navigationhandler ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setPropertyResolver( PropertyResolver propertyresolver ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setStateManager( StateManager statemanager ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setSupportedLocales( Collection<Locale> arg0 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setVariableResolver( VariableResolver variableresolver ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setViewHandler( ViewHandler viewhandler ) {

					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public UIViewRoot getViewRoot() {

			return new UIViewRoot();
		}

		//
		// Unsupported public methods
		//

		@Override
		public ELContext getELContext() {

			return new ELContext() {

				@Override
				public ELResolver getELResolver() {

					throw new UnsupportedOperationException();
				}

				@Override
				public FunctionMapper getFunctionMapper() {

					throw new UnsupportedOperationException();
				}

				@Override
				public VariableMapper getVariableMapper() {

					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public void addMessage( String s, FacesMessage facesmessage ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<String> getClientIdsWithMessages() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ExternalContext getExternalContext() {

			if ( mExternalContext == null ) {
				mExternalContext = new ExternalContext() {

					//
					// Private members
					//

					private Map<String, String>	mInitParameters	= CollectionUtils.newHashMap();

					private Map<String, Object>	mRequestMap		= CollectionUtils.newHashMap();

					//
					// Supported public methods
					//

					@Override
					public Map<String, Object> getApplicationMap() {

						return MockFacesContext.this.mApplicationMap;
					}

					@Override
					public URL getResource( String arg0 )
						throws MalformedURLException {

						return null;
					}

					@Override
					public Map getInitParameterMap() {

						return mInitParameters;
					}

					@Override
					public String getInitParameter( String name ) {

						return mInitParameters.get( name );
					}

					//
					// Unsupported public methods
					//

					@Override
					public void dispatch( String arg0 )
						throws IOException {

						throw new UnsupportedOperationException();
					}

					@Override
					public String encodeActionURL( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public String encodeNamespace( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public String encodeResourceURL( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getAuthType() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getContext() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRemoteUser() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getRequest() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRequestContextPath() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, Object> getRequestCookieMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, String> getRequestHeaderMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, String[]> getRequestHeaderValuesMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Locale getRequestLocale() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Iterator<Locale> getRequestLocales() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, Object> getRequestMap() {

						return mRequestMap;
					}

					@Override
					public Map<String, String> getRequestParameterMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Iterator<String> getRequestParameterNames() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, String[]> getRequestParameterValuesMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRequestPathInfo() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRequestServletPath() {

						throw new UnsupportedOperationException();
					}

					@Override
					public InputStream getResourceAsStream( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public Set<String> getResourcePaths( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getResponse() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getSession( boolean arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, Object> getSessionMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Principal getUserPrincipal() {

						throw new UnsupportedOperationException();
					}

					@Override
					public boolean isUserInRole( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public void log( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public void log( String arg0, Throwable arg1 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public void redirect( String arg0 )
						throws IOException {

						throw new UnsupportedOperationException();
					}
				};
			}

			return mExternalContext;
		}

		@Override
		public RenderKit getRenderKit() {

			return new RenderKit() {

				@Override
				public void addRenderer( String family, String rendererType, Renderer renderer ) {

					// Do nothing
				}

				@Override
				public Renderer getRenderer( String family, String rendererType ) {

					return new MockRenderer();
				}

				@Override
				public ResponseStateManager getResponseStateManager() {

					return null;
				}

				@Override
				public ResponseWriter createResponseWriter( Writer writer, String contentTypeList, String characterEncoding ) {

					return null;
				}

				@Override
				public ResponseStream createResponseStream( OutputStream out ) {

					return null;
				}
			};
		}

		@Override
		public ResponseWriter getResponseWriter() {

			if ( mResponseWriter == null ) {
				mResponseWriter = new MockResponseWriter();
			}

			return mResponseWriter;
		}

		@Override
		public void release() {

			FacesContext.setCurrentInstance( null );
		}

		//
		// Unsupported public methods
		//

		@Override
		public Severity getMaximumSeverity() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<FacesMessage> getMessages() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<FacesMessage> getMessages( String s ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getRenderResponse() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getResponseComplete() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ResponseStream getResponseStream() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void renderResponse() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void responseComplete() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setResponseStream( ResponseStream responsestream ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setResponseWriter( ResponseWriter responsewriter ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setViewRoot( UIViewRoot uiviewroot ) {

			throw new UnsupportedOperationException();
		}

		//
		// Supported protected methods
		//

		protected UIComponent createComponent( String componentName ) {

			if ( HtmlOutputText.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlOutputText();
			}

			if ( HtmlInputHidden.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputHidden();
			}

			if ( HtmlInputText.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputText();
			}

			if ( HtmlInputTextarea.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputTextarea();
			}

			if ( HtmlInputSecret.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputSecret();
			}

			if ( HtmlCommandButton.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlCommandButton();
			}

			if ( HtmlSelectOneMenu.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSelectOneMenu();
			}

			if ( HtmlSelectManyCheckbox.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSelectManyCheckbox();
			}

			if ( HtmlSelectBooleanCheckbox.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSelectBooleanCheckbox();
			}

			if ( HtmlDataTable.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlDataTable();
			}

			if ( HtmlCommandLink.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlCommandLink();
			}

			if ( HtmlColumn.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlColumn();
			}

			if ( HtmlMessage.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlMessage();
			}

			if ( HtmlOutputLabel.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlOutputLabel();
			}

			if ( UISelectItems.COMPONENT_TYPE.equals( componentName ) ) {
				return new UISelectItems();
			}

			if ( UISelectItem.COMPONENT_TYPE.equals( componentName ) ) {
				return new UISelectItem();
			}

			if ( HtmlMetawidget.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlMetawidget();
			}

			if ( UIStub.COMPONENT_TYPE.equals( componentName ) ) {
				return new UIStub();
			}

			if ( UIParameter.COMPONENT_TYPE.equals( componentName ) ) {
				return new UIParameter();
			}

			return new MockComponent( componentName );
		}
	}

	public static class MockComponent
		extends UIComponentBase {

		//
		// Private members
		//

		private String	mFamily;

		//
		// Constructor
		//

		public MockComponent( String family ) {

			mFamily = family;
		}

		//
		// Public methods
		//

		@Override
		public String getFamily() {

			return mFamily;
		}
	}

	public static class MockValueBinding
		extends ValueBinding {

		//
		// Private members
		//

		private String	mExpressionString;

		//
		// Constructor
		//

		public MockValueBinding( String expressionString ) {

			mExpressionString = expressionString;
		}

		//
		// Public methods
		//

		@Override
		public String getExpressionString() {

			return mExpressionString;
		}

		@Override
		public Class getType( FacesContext context )
			throws EvaluationException, PropertyNotFoundException {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object getValue( FacesContext context )
			throws EvaluationException, PropertyNotFoundException {

			if ( mExpressionString.startsWith( "#{array" ) ) {
				return new String[] { mExpressionString, mExpressionString };
			}

			if ( mExpressionString.startsWith( "#{collection" ) ) {
				return CollectionUtils.newArrayList( mExpressionString, mExpressionString );
			}

			if ( "#{error}".equals( mExpressionString ) ) {
				throw new EvaluationException( "Forced error" );
			}

			if ( "#{null}".equals( mExpressionString ) ) {
				return null;
			}

			return "result of " + mExpressionString;
		}

		@Override
		public boolean isReadOnly( FacesContext context )
			throws EvaluationException, PropertyNotFoundException {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue( FacesContext context, Object value )
			throws EvaluationException, PropertyNotFoundException {

			throw new UnsupportedOperationException();
		}
	}

	public static class MockMethodBinding
		extends MethodBinding {

		//
		// Private members
		//

		private String	mExpressionString;

		private Class[]	mParams;

		//
		// Constructor
		//

		public MockMethodBinding( String expressionString, Class[] params ) {

			mExpressionString = expressionString;
			mParams = params;
		}

		//
		// Public methods
		//

		@Override
		public String getExpressionString() {

			return mExpressionString;
		}

		public Class[] getParams() {

			return mParams;
		}

		@Override
		public Class getType( FacesContext context )
			throws MethodNotFoundException {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object invoke( FacesContext context, Object[] args )
			throws EvaluationException, MethodNotFoundException {

			throw new UnsupportedOperationException();
		}
	}

	public static class MockConverter
		implements Converter {

		private String	mConverterId;

		//
		// Constructor
		//

		public MockConverter( String converterId ) {

			mConverterId = converterId;
		}

		//
		// Public methods
		//

		public Object getAsObject( FacesContext context, UIComponent component, String value ) {

			return value;
		}

		public String getAsString( FacesContext context, UIComponent component, Object value ) {

			return value + " (from converter " + mConverterId + ")";
		}

		public String toString() {

			return mConverterId;
		}
	};

	public static class MockResponseWriter
		extends ResponseWriter {

		//
		// Private members
		//

		private StringWriter		mWriter						= new StringWriter();

		private String				mPendingElementName;

		private Map<String, Object>	mPendingElementAttributes	= CollectionUtils.newLinkedHashMap();

		//
		// Public methods
		//

		@Override
		public void startDocument()
			throws IOException {

			// Do nothing
		}

		@Override
		public void endDocument()
			throws IOException {

			writePendingElement();
		}

		@Override
		public void startElement( String name, UIComponent component )
			throws IOException {

			writePendingElement();
			mPendingElementName = name;
		}

		@Override
		public void endElement( String name )
			throws IOException {

			writePendingElement();

			mWriter.write( "</" );
			mWriter.write( name );
			mWriter.write( ">" );
		}

		@Override
		public void writeAttribute( String name, Object value, String property )
			throws IOException {

			mPendingElementAttributes.put( name, value );
		}

		@Override
		public void writeText( Object text, String property )
			throws IOException {

			writePendingElement();
			mWriter.write( String.valueOf( text ) );
		}

		@Override
		public void writeText( char[] text, int off, int len )
			throws IOException {

			writePendingElement();
			mWriter.write( text, off, len );
		}

		@Override
		public void write( char[] cbuf, int off, int len )
			throws IOException {

			writePendingElement();
			mWriter.write( cbuf, off, len );
		}

		@Override
		public void flush()
			throws IOException {

			writePendingElement();
			mWriter.flush();
		}

		@Override
		public void close()
			throws IOException {

			writePendingElement();
			mWriter.close();
		}

		public String toString() {

			return mWriter.toString();
		}

		//
		// Unsupported public methods
		//

		@Override
		public String getContentType() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getCharacterEncoding() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void writeURIAttribute( String name, Object value, String property )
			throws IOException {

			throw new UnsupportedOperationException();
		}

		@Override
		public void writeComment( Object comment )
			throws IOException {

			throw new UnsupportedOperationException();
		}

		@Override
		public ResponseWriter cloneWithWriter( Writer writer ) {

			throw new UnsupportedOperationException();
		}

		//
		// Private methods
		//

		private void writePendingElement() {

			if ( mPendingElementName == null ) {
				return;
			}

			mWriter.write( '<' );
			mWriter.write( mPendingElementName );

			for ( Map.Entry<String, Object> entry : mPendingElementAttributes.entrySet() ) {

				if ( entry.getValue() == null ) {
					continue;
				}

				mWriter.write( ' ' );
				mWriter.write( entry.getKey() );
				mWriter.write( "=\"" );
				mWriter.write( String.valueOf( entry.getValue() ) );
				mWriter.write( '\"' );
			}

			mWriter.write( '>' );

			mPendingElementName = null;
			mPendingElementAttributes.clear();
		}
	}

	public static class MockRenderer
		extends Renderer {

		@Override
		public void encodeBegin( FacesContext context, UIComponent component )
			throws IOException {

			try {

				// Start the tag

				ResponseWriter writer = context.getResponseWriter();
				writer.write( '<' );
				writer.write( StringUtils.decapitalize( component.getClass().getSimpleName() ) );

				if ( component.getId() != null ) {
					writer.write( " id=\"" );
					writer.write( component.getId() );
					writer.write( "\"" );
				}

				// Write out significant attributes

				Field stateHelperField = UIComponent.class.getDeclaredField( "stateHelper" );
				stateHelperField.setAccessible( true );
				Object stateHelper = stateHelperField.get( component );
				Field defaultMapField = stateHelper.getClass().getDeclaredField( "defaultMap" );
				defaultMapField.setAccessible( true );

				// (sorted for unit tests)

				Map<Object, Object> defaultMap = (Map<Object, Object>) defaultMapField.get( stateHelper );
				Map<String, String> simpleMap = CollectionUtils.newTreeMap();

				for ( Map.Entry<Object, Object> entry : defaultMap.entrySet() ) {

					String key = String.valueOf( entry.getKey() );

					if ( "rendererType".equals( key ) ) {
						continue;
					}

					Object value = entry.getValue();

					if ( value == null ) {
						continue;
					}

					if ( !( value instanceof String ) && !( value instanceof Number ) ) {
						continue;
					}

					simpleMap.put( key, String.valueOf( value ) );
				}

				for ( Map.Entry<String, String> entry : simpleMap.entrySet() ) {

					writer.write( ' ' );
					writer.write( entry.getKey() );
					writer.write( "=\"" );
					writer.write( entry.getValue() );
					writer.write( '\"' );
				}

				writer.write( '>' );

				super.encodeBegin( context, component );
			} catch ( Exception e ) {
				throw new RuntimeException( e );
			}
		}

		@Override
		public void encodeEnd( FacesContext context, UIComponent component )
			throws IOException {

			super.encodeEnd( context, component );

			ResponseWriter writer = context.getResponseWriter();
			writer.write( "</" );
			writer.write( StringUtils.decapitalize( component.getClass().getSimpleName() ) );
			writer.write( '>' );
		}
	}

	//
	// Private constructor
	//

	private FacesMetawidgetTests() {

		// Can never be called
	}
}
