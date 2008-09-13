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

package org.metawidget.test.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.JspUtils.BodyPreparer;

/**
 * @author Richard Kennard
 */

public class JspUtilsTest
	extends TestCase
{
	//
	// Package-level statics
	//

	final static String	NEWLINE	= System.getProperty( "line.separator" );

	//
	// Package-level members
	//

	int					mPageContextHits;

	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public JspUtilsTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testJspUtils()
		throws Exception
	{
		PageContext pageContext = new PageContext()
		{
			@Override
			public void forward( String relativeUrlPath )
			{
				mPageContextHits++;
			}

			@Override
			public Exception getException()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public Object getPage()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public ServletRequest getRequest()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public ServletResponse getResponse()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public ServletConfig getServletConfig()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public ServletContext getServletContext()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public HttpSession getSession()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public void handlePageException( Exception exception )
			{
				mPageContextHits++;
			}

			@Override
			public void handlePageException( Throwable throwable )
			{
				mPageContextHits++;
			}

			@Override
			public void include( String relativeUrlPath )
			{
				mPageContextHits++;
			}

			@Override
			public void include( String relativeUrlPath, boolean flush )
			{
				mPageContextHits++;
			}

			@Override
			public void initialize( Servlet servlet, ServletRequest request, ServletResponse response, String errorPageUrl, boolean needsSession, int bufferSize, boolean autoFlush )
			{
				mPageContextHits++;
			}

			@Override
			public void release()
			{
				mPageContextHits++;
			}

			@Override
			public Object findAttribute( String name )
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public Object getAttribute( String name )
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public Object getAttribute( String name, int scope )
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public Enumeration<?> getAttributeNamesInScope( int scope )
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public int getAttributesScope( String name )
			{
				mPageContextHits++;
				return 0;
			}

			@Override
			public ExpressionEvaluator getExpressionEvaluator()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public JspWriter getOut()
			{
				assertTrue( false );
				return null;
			}

			@Override
			public VariableResolver getVariableResolver()
			{
				mPageContextHits++;
				return null;
			}

			@Override
			public void removeAttribute( String name )
			{
				mPageContextHits++;
			}

			@Override
			public void removeAttribute( String name, int scope )
			{
				mPageContextHits++;
			}

			@Override
			public void setAttribute( String name, Object value )
			{
				mPageContextHits++;
			}

			@Override
			public void setAttribute( String name, Object value, int scope )
			{
				mPageContextHits++;
			}
		};

		Tag testTag = new Tag()
		{
			@Override
			public int doEndTag()
			{
				return 0;
			}

			@Override
			public int doStartTag()
			{
				return 0;
			}

			@Override
			public Tag getParent()
			{
				return null;
			}

			@Override
			public void release()
			{
				// Do nothing
			}

			@Override
			public void setPageContext( PageContext context )
			{
				// Do nothing
			}

			@Override
			public void setParent( Tag parent )
			{
				// Do nothing
			}

		};

		JspUtils.writeTag( pageContext, testTag, null, new BodyPreparer()
		{
			@Override
			public void prepareBody( PageContext delegateContext )
				throws IOException
			{
				JspWriter writer = delegateContext.getOut();

				// Write

				writer.newLine();
				writer.print( true );
				writer.print( 'a' );
				writer.print( Integer.MAX_VALUE );
				writer.print( Long.MAX_VALUE );
				writer.print( Float.MAX_VALUE );
				writer.print( Double.MAX_VALUE );
				writer.print( new char[] { 'b', 'c' } );
				Object object1 = new Object();
				writer.print( object1 );
				writer.println();
				writer.println( false );
				writer.println( 'b' );
				writer.println( Integer.MIN_VALUE );
				writer.println( Long.MIN_VALUE );
				writer.println( Float.MIN_VALUE );
				writer.println( Double.MIN_VALUE );
				writer.println( new char[] { 'c', 'd' } );
				Object object2 = new Object();
				writer.println( object2 );
				writer.println( "END" );
				writer.flush();
				writer.close();

				// Hit

				try
				{
					delegateContext.forward( null );
					delegateContext.getException();
					delegateContext.getPage();
					delegateContext.getRequest();
					delegateContext.getResponse();
					delegateContext.getServletConfig();
					delegateContext.getServletContext();
					delegateContext.getSession();
					delegateContext.handlePageException( (Exception) null );
					delegateContext.handlePageException( (Throwable) null );
					delegateContext.include( null );
					delegateContext.include( null, false );
					delegateContext.initialize( null, null, null, null, false, 0, false );
					delegateContext.release();
					delegateContext.findAttribute( null );
					delegateContext.getAttribute( null );
					delegateContext.getAttribute( null, 0 );
					delegateContext.getAttributeNamesInScope( 0 );
					delegateContext.getAttributesScope( null );
					delegateContext.getExpressionEvaluator();
					delegateContext.getOut();
					delegateContext.getVariableResolver();
					delegateContext.removeAttribute( null );
					delegateContext.removeAttribute( null, 0 );
					delegateContext.setAttribute( null, null );
					delegateContext.setAttribute( null, null, 0 );
				}
				catch( Exception e )
				{
					throw new IOException( e.getMessage() );
				}

				assertTrue( mPageContextHits == 25 );

				// Verify

				String verify = NEWLINE + "truea" + Integer.MAX_VALUE + Long.MAX_VALUE + Float.MAX_VALUE + Double.MAX_VALUE + "bc" + object1;
				verify += NEWLINE + "false" + NEWLINE;
				verify += "b" + NEWLINE;
				verify += Integer.MIN_VALUE + NEWLINE;
				verify += Long.MIN_VALUE + NEWLINE;
				verify += Float.MIN_VALUE + NEWLINE;
				verify += Double.MIN_VALUE + NEWLINE;
				verify += "cd" + NEWLINE;
				verify += object2 + NEWLINE;
				verify += "END" + NEWLINE;

				assertTrue( verify.equals( writer.toString() ) );
			}
		} );
	}
}
