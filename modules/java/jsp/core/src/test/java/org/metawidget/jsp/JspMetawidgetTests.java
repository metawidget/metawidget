// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.jsp;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JspMetawidgetTests {

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

	static class MockServletContext
		implements ServletContext {

		//
		// Private members
		//

		private Map<String, Object>	mAttributes	= CollectionUtils.newHashMap();

		//
		// Supported public methods
		//

		public Object getAttribute( String key ) {

			return mAttributes.get( key );
		}

		public void setAttribute( String key, Object value ) {

			mAttributes.put( key, value );
		}

		public URL getResource( String arg0 ) {

			return null;
		}

		//
		// Unsupported public methods
		//

		public Enumeration<?> getAttributeNames() {

			throw new UnsupportedOperationException();
		}

		public ServletContext getContext( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public String getInitParameter( String name ) {

			return null;
		}

		public Enumeration<?> getInitParameterNames() {

			throw new UnsupportedOperationException();
		}

		public int getMajorVersion() {

			throw new UnsupportedOperationException();
		}

		public String getMimeType( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public int getMinorVersion() {

			throw new UnsupportedOperationException();
		}

		public RequestDispatcher getNamedDispatcher( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public String getRealPath( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public RequestDispatcher getRequestDispatcher( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public InputStream getResourceAsStream( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public Set<?> getResourcePaths( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public String getServerInfo() {

			throw new UnsupportedOperationException();
		}

		public Servlet getServlet( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public String getServletContextName() {

			throw new UnsupportedOperationException();
		}

		public Enumeration<?> getServletNames() {

			throw new UnsupportedOperationException();
		}

		public Enumeration<?> getServlets() {

			throw new UnsupportedOperationException();
		}

		public void log( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public void log( Exception arg0, String arg1 ) {

			throw new UnsupportedOperationException();
		}

		public void log( String arg0, Throwable arg1 ) {

			throw new UnsupportedOperationException();
		}

		public void removeAttribute( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		public String getContextPath() {

			throw new UnsupportedOperationException();
		}
	}

	static class MockExpressionEvaluator
		extends ExpressionEvaluator {

		//
		// Public methods
		//

		@SuppressWarnings( "rawtypes" )
		@Override
		public Object evaluate( String expression, Class arg1, VariableResolver arg2, FunctionMapper arg3 ) {

			if ( expression.startsWith( "${array" ) ) {
				return new String[] { expression, expression };
			}

			if ( expression.startsWith( "${collection" ) ) {
				return CollectionUtils.newArrayList( expression, expression );
			}

			if ( "${null}".equals( expression ) ) {
				return null;
			}

			return "result of " + expression;
		}

		@Override
		@SuppressWarnings( "rawtypes" )
		public Expression parseExpression( String arg0, Class arg1, FunctionMapper arg2 ) {

			throw new UnsupportedOperationException();
		}
	}

	static class MockVariableResolver
		implements VariableResolver {

		public Object resolveVariable( String arg0 ) {

			throw new UnsupportedOperationException();
		}
	}

	//
	// Private constructor
	//

	private JspMetawidgetTests() {

		// Can never be called
	}
}
