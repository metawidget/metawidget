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

package org.metawidget.jsp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.jsp.tagext.MetawidgetTagTest;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTagTest;
import org.metawidget.jsp.tagext.html.layout.HeadingTagLayoutDecoratorTest;
import org.metawidget.jsp.tagext.html.layout.HtmlTableLayoutTest;
import org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTagTest;
import org.metawidget.jsp.tagext.html.struts.StrutsMetawidgetTagTest;
import org.metawidget.jsp.tagext.html.widgetbuilder.ReadOnlyWidgetBuilderTest;
import org.metawidget.jsp.tagext.html.widgetbuilder.displaytag.DisplayTagWidgetBuilderTest;
import org.metawidget.jsp.tagext.html.widgetprocessor.spring.HiddenFieldProcessorTest;
import org.metawidget.jsp.tagext.layout.JspFlatSectionLayoutDecoratorTest;
import org.metawidget.jsp.tagext.layout.SimpleLayoutTest;
import org.metawidget.util.CollectionUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

/**
 * @author Richard Kennard
 */

public class JspMetawidgetTests
	extends TestCase {

	//
	// Public statics
	//

	public static Test suite() {

		TestSuite suite = new TestSuite( "JSP Metawidget Tests" );
		suite.addTestSuite( DisplayTagWidgetBuilderTest.class );
		suite.addTestSuite( HeadingTagLayoutDecoratorTest.class );
		suite.addTestSuite( HiddenFieldProcessorTest.class );
		suite.addTestSuite( HtmlTableLayoutTest.class );
		suite.addTestSuite( HtmlMetawidgetTagTest.class );
		suite.addTestSuite( JspFlatSectionLayoutDecoratorTest.class );
		suite.addTestSuite( MetawidgetTagTest.class );
		suite.addTestSuite( ReadOnlyWidgetBuilderTest.class );
		suite.addTestSuite( SimpleLayoutTest.class );
		suite.addTestSuite( SpringMetawidgetTagTest.class );
		suite.addTestSuite( StrutsMetawidgetTagTest.class );

		return suite;
	}

	//
	// Inner class
	//

	public static class MockPageContext
		extends PageContext {

		//
		// Private members
		//

		private ServletContext	mServletContext	= new MockServletContext();

		Map<String, Object>		mAttributes		= CollectionUtils.newHashMap();

		//
		// Supported public methods
		//

		@Override
		public ServletContext getServletContext() {

			return mServletContext;
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

			return new MockHttpServletRequest();
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

			return null;
		}

		@Override
		public Enumeration<?> getAttributeNamesInScope( int arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributesScope( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public ExpressionEvaluator getExpressionEvaluator() {

			return new MockExpressionEvaluator();
		}

		@Override
		public JspWriter getOut() {

			throw new UnsupportedOperationException();
		}

		@Override
		public VariableResolver getVariableResolver() {

			return new MockVariableResolver();
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
	}

	@SuppressWarnings( "deprecation" )
	static class MockServletContext
		implements ServletContext {

		//
		// Private members
		//

		private Map<String, Object>	mAttributes	= CollectionUtils.newHashMap();

		//
		// Supported public methods
		//

		@Override
		public Object getAttribute( String key ) {

			return mAttributes.get( key );
		}

		@Override
		public void setAttribute( String key, Object value ) {

			mAttributes.put( key, value );
		}

		@Override
		public URL getResource( String arg0 ) {

			return null;
		}

		//
		// Unsupported public methods
		//

		@Override
		public Enumeration<?> getAttributeNames() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletContext getContext( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getContextPath() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getInitParameter( String name ) {

			return null;
		}

		@Override
		public Enumeration<?> getInitParameterNames() {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getMajorVersion() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getMimeType( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getMinorVersion() {

			throw new UnsupportedOperationException();
		}

		@Override
		public RequestDispatcher getNamedDispatcher( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getRealPath( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public RequestDispatcher getRequestDispatcher( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public InputStream getResourceAsStream( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Set<?> getResourcePaths( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getServerInfo() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Servlet getServlet( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getServletContextName() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration<?> getServletNames() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration<?> getServlets() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void log( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void log( Exception arg0, String arg1 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void log( String arg0, Throwable arg1 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void removeAttribute( String arg0 ) {

			throw new UnsupportedOperationException();
		}
	}

	static class MockHttpServletRequest
		implements HttpServletRequest {

		//
		// Public methods
		//

		@Override
		public Object getAttribute( String name ) {

			if ( "org.springframework.web.servlet.DispatcherServlet.CONTEXT".equals( name ) ) {
				return new GenericWebApplicationContext();
			}

			if ( "org.springframework.web.servlet.DispatcherServlet.LOCALE_RESOLVER".equals( name ) ) {
				return new FixedLocaleResolver();
			}

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration<?> getAttributeNames() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getCharacterEncoding() {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getContentLength() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getContentType() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletInputStream getInputStream() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getLocalAddr() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Locale getLocale() {

			return null;
		}

		@Override
		public Enumeration<?> getLocales() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getLocalName() {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getLocalPort() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getParameter( String name ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Map<?, ?> getParameterMap() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration<?> getParameterNames() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String[] getParameterValues( String name ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getProtocol() {

			throw new UnsupportedOperationException();
		}

		@Override
		public BufferedReader getReader() {

			throw new UnsupportedOperationException();
		}

		@SuppressWarnings( "deprecation" )
		@Override
		public String getRealPath( String path ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getRemoteAddr() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getRemoteHost() {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getRemotePort() {

			throw new UnsupportedOperationException();
		}

		@Override
		public RequestDispatcher getRequestDispatcher( String path ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getScheme() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getServerName() {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getServerPort() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isSecure() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void removeAttribute( String name ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttribute( String name, Object o ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setCharacterEncoding( String env ) {

			throw new UnsupportedOperationException();
		}

		//
		// HTTP methods
		//

		@Override
		public String getAuthType() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getContextPath() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Cookie[] getCookies() {

			throw new UnsupportedOperationException();
		}

		@Override
		public long getDateHeader( String name ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getHeader( String name ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration<?> getHeaderNames() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration<?> getHeaders( String name ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getIntHeader( String name ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getMethod() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getPathInfo() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getPathTranslated() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getQueryString() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getRemoteUser() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getRequestURI() {

			throw new UnsupportedOperationException();
		}

		@Override
		public StringBuffer getRequestURL() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getRequestedSessionId() {

			throw new UnsupportedOperationException();
		}

		@Override
		public String getServletPath() {

			throw new UnsupportedOperationException();
		}

		@Override
		public HttpSession getSession() {

			throw new UnsupportedOperationException();
		}

		@Override
		public HttpSession getSession( boolean create ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Principal getUserPrincipal() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {

			throw new UnsupportedOperationException();
		}

		@SuppressWarnings( "deprecation" )
		@Override
		public boolean isRequestedSessionIdFromUrl() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isRequestedSessionIdValid() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isUserInRole( String role ) {

			throw new UnsupportedOperationException();
		}
	}

	static class MockExpressionEvaluator
		extends ExpressionEvaluator {

		//
		// Public methods
		//

		@SuppressWarnings( "unchecked" )
		@Override
		public Object evaluate( String expression, Class arg1, VariableResolver arg2, FunctionMapper arg3 ) {

			if ( expression.startsWith( "${array" ) ) {
				return new int[] { 1, 2 };
			}

			if ( expression.startsWith( "${collection" ) ) {
				return CollectionUtils.newArrayList( expression, expression );
			}

			return "result of " + expression;
		}

		@Override
		@SuppressWarnings( "unchecked" )
		public Expression parseExpression( String arg0, Class arg1, FunctionMapper arg2 ) {

			throw new UnsupportedOperationException();
		}
	}

	static class MockVariableResolver
		implements VariableResolver {

		@Override
		public Object resolveVariable( String arg0 ) {

			throw new UnsupportedOperationException();
		}
	}
}
