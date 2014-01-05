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

package org.metawidget.jsp;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import junit.framework.TestCase;

import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JspUtilsTest
	extends TestCase {

	//
	// Package-level statics
	//

	static final String	NEWLINE	= System.getProperty( "line.separator" );

	//
	// Public methods
	//

	public void testBodyTag()
		throws Exception {

		TagSupport parentTag = new BodyTagSupport();
		final Object object1 = new Object();
		final Object object2 = new Object();

		Tag childTag = new TagSupport() {

			@Override
			public int doEndTag()
				throws JspException {

				try {
					// Write

					JspWriter writer = pageContext.getOut();

					writer.newLine();
					writer.print( true );
					writer.print( 'a' );
					writer.print( Integer.MAX_VALUE );
					writer.print( Long.MAX_VALUE );
					writer.print( Float.MAX_VALUE );
					writer.print( Double.MAX_VALUE );
					writer.print( new char[] { 'b', 'c' } );
					writer.print( object1 );
					writer.println();
					writer.println( false );
					writer.println( 'b' );
					writer.println( Integer.MIN_VALUE );
					writer.println( Long.MIN_VALUE );
					writer.println( Float.MIN_VALUE );
					writer.println( Double.MIN_VALUE );
					writer.println( new char[] { 'c', 'd' } );
					writer.println( object2 );
					writer.println( "END" );
					writer.flush();
					writer.close();

					// Hit

					pageContext.forward( null );
					pageContext.getException();
					pageContext.getPage();
					pageContext.getRequest();
					pageContext.getResponse();
					pageContext.getServletConfig();
					pageContext.getServletContext();
					pageContext.getSession();
					pageContext.handlePageException( (Exception) null );
					pageContext.handlePageException( (Throwable) null );
					pageContext.include( null );
					pageContext.include( null, false );
					pageContext.initialize( null, null, null, null, false, 0, false );
					pageContext.release();
					pageContext.findAttribute( null );
					pageContext.getAttribute( null );
					pageContext.getAttribute( null, 0 );
					pageContext.getAttributeNamesInScope( 0 );
					pageContext.getAttributesScope( null );
					pageContext.getExpressionEvaluator();
					pageContext.getOut();
					pageContext.getVariableResolver();
					pageContext.removeAttribute( null );
					pageContext.removeAttribute( null, 0 );
					pageContext.setAttribute( null, null );
					pageContext.setAttribute( null, null, 0 );
				} catch ( Exception e ) {
					throw new JspException( e );
				}

				return Tag.EVAL_PAGE;
			}
		};

		JspUtils.addDeferredChild( parentTag, childTag );

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

		final DummyPageContext dummyPageContext = new DummyPageContext();
		assertEquals( verify, JspUtils.writeTag( dummyPageContext, parentTag, null ) );
		assertEquals( dummyPageContext.getPageContextHits(), 25 );
	}

	public void testSkipBody()
		throws Exception {

		final Set<Integer> toReturn = CollectionUtils.newHashSet();

		TagSupport testTag = new TagSupport() {

			@Override
			public int doStartTag() {

				return toReturn.iterator().next();
			}
		};

		final DummyPageContext dummyPageContext = new DummyPageContext();
		JspUtils.addDeferredChild( testTag, new LiteralTag( "Foo" ) );

		toReturn.add( Tag.EVAL_BODY_INCLUDE );
		assertEquals( "Foo", JspUtils.writeTag( dummyPageContext, testTag, null ) );

		toReturn.add( Tag.SKIP_BODY );
		assertEquals( "", JspUtils.writeTag( dummyPageContext, testTag, null ) );
	}

	int	mRepeat;

	public void testRepeatBody()
		throws Exception {

		BodyTag testTag = new BodyTag() {

			public int doEndTag() {

				return Tag.EVAL_PAGE;
			}

			public int doStartTag() {

				return Tag.EVAL_BODY_INCLUDE;
			}

			public Tag getParent() {

				return null;
			}

			public void release() {

				// Do nothing
			}

			public void setPageContext( PageContext context ) {

				// Do nothing
			}

			public void setParent( Tag parent ) {

				// Do nothing
			}

			public void doInitBody() {

				// Do nothing
			}

			public void setBodyContent( BodyContent bodycontent ) {

				// Do nothing
			}

			public int doAfterBody() {

				mRepeat++;

				if ( mRepeat < 5 ) {
					return IterationTag.EVAL_BODY_AGAIN;
				}

				return Tag.EVAL_PAGE;
			}
		};

		mRepeat = 0;

		JspUtils.writeTag( null, testTag, null );

		assertEquals( mRepeat, 5 );
	}

	//
	// Inner class
	//

	@SuppressWarnings( "deprecation" )
	public static class DummyPageContext
		extends PageContext {

		//
		// Private members
		//

		private int	mPageContextHits;

		//
		// Public methods
		//

		public int getPageContextHits() {

			return mPageContextHits;
		}

		@Override
		public void forward( String relativeUrlPath ) {

			mPageContextHits++;
		}

		@Override
		public Exception getException() {

			mPageContextHits++;
			return null;
		}

		@Override
		public Object getPage() {

			mPageContextHits++;
			return null;
		}

		@Override
		public ServletRequest getRequest() {

			mPageContextHits++;
			return null;
		}

		@Override
		public ServletResponse getResponse() {

			mPageContextHits++;
			return null;
		}

		@Override
		public ServletConfig getServletConfig() {

			mPageContextHits++;
			return null;
		}

		@Override
		public ServletContext getServletContext() {

			mPageContextHits++;
			return new ServletContext() {

				public String getInitParameter( String arg0 ) {

					return null;
				}

				public Object getAttribute( String arg0 ) {

					return null;
				}

				public void setAttribute( String arg0, Object arg1 ) {

					// Do nothing
				}

				//
				// Unsupported methods
				//

				public Enumeration<?> getAttributeNames() {

					throw new UnsupportedOperationException();
				}

				public ServletContext getContext( String arg0 ) {

					throw new UnsupportedOperationException();
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

				public URL getResource( String arg0 ) {

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
			};
		}

		@Override
		public HttpSession getSession() {

			mPageContextHits++;
			return null;
		}

		@Override
		public void handlePageException( Exception exception ) {

			mPageContextHits++;
		}

		@Override
		public void handlePageException( Throwable throwable ) {

			mPageContextHits++;
		}

		@Override
		public void include( String relativeUrlPath ) {

			mPageContextHits++;
		}

		@Override
		public void include( String relativeUrlPath, boolean flush ) {

			mPageContextHits++;
		}

		@Override
		public void initialize( Servlet servlet, ServletRequest request, ServletResponse response, String errorPageUrl, boolean needsSession, int bufferSize, boolean autoFlush ) {

			mPageContextHits++;
		}

		@Override
		public void release() {

			mPageContextHits++;
		}

		@Override
		public Object findAttribute( String name ) {

			mPageContextHits++;
			return null;
		}

		@Override
		public Object getAttribute( String name ) {

			mPageContextHits++;
			return null;
		}

		@Override
		public Object getAttribute( String name, int scope ) {

			mPageContextHits++;
			return null;
		}

		@Override
		public Enumeration<?> getAttributeNamesInScope( int scope ) {

			mPageContextHits++;
			return null;
		}

		@Override
		public int getAttributesScope( String name ) {

			mPageContextHits++;
			return 0;
		}

		@Override
		public ExpressionEvaluator getExpressionEvaluator() {

			mPageContextHits++;
			return null;
		}

		@Override
		public JspWriter getOut() {

			fail();
			return null;
		}

		@Override
		public VariableResolver getVariableResolver() {

			mPageContextHits++;
			return null;
		}

		@Override
		public void removeAttribute( String name ) {

			mPageContextHits++;
		}

		@Override
		public void removeAttribute( String name, int scope ) {

			mPageContextHits++;
		}

		@Override
		public void setAttribute( String name, Object value ) {

			mPageContextHits++;
		}

		@Override
		public void setAttribute( String name, Object value, int scope ) {

			mPageContextHits++;
		}
	}
}
