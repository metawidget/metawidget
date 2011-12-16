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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.metawidget.util.CollectionUtils;

/**
 * Utilities for working with JSPs.
 *
 * @author Richard Kennard
 */

public final class JspUtils {

	//
	// Public statics
	//

	/**
	 * @return true if the given HTML consists of nothing but hidden fields
	 */

	public static boolean isJustHiddenFields( String html ) {

		return PATTERN_HIDDEN_FIELDS.matcher( html ).matches();
	}

	/**
	 * Writes the given Tag to a String (<em>not</em> its usual pageContext.getOut).
	 */

	public static String writeTag( PageContext context, Tag tag, Tag parentTag )
		throws JspException {

		PageContextDelegate delegateContext = new PageContextDelegate( context );
		writeTagInternal( delegateContext, tag, parentTag );
		return delegateContext.getOut().toString();
	}

	/**
	 * Simulates adding the given child Tag to the given parent Tag.
	 * <p>
	 * Whilst the JSP component model provides <code>findAncestorWithClass</code> to allow Tags to
	 * find their parent, there is no mechanism for programmatically adding children to a Tag. This
	 * is a problem for Metawidget, which needs to be able to build Tags, separately process them,
	 * and <em>then</em> render them.
	 * <p>
	 * This method provides a way to defer the rendering of a child, to give us chance to process
	 * it.
	 */

	public static void addDeferredChild( TagSupport parentTag, Tag childTag ) {

		@SuppressWarnings( "unchecked" )
		List<Tag> deferredChildren = (List<Tag>) parentTag.getValue( DEFERRED_CHILD_KEY );

		if ( deferredChildren == null ) {
			deferredChildren = CollectionUtils.newArrayList();
			parentTag.setValue( DEFERRED_CHILD_KEY, deferredChildren );
		}

		deferredChildren.add( childTag );
	}

	public static void setBodyContent( BodyTag tag, String content ) {

		try {
			BufferedContent bodyContent = new BufferedContent();
			bodyContent.write( content );
			tag.setBodyContent( bodyContent );
		} catch( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	//
	// Private statics
	//

	private static final String	DEFERRED_CHILD_KEY	= JspUtils.class + ".DEFERRED_CHILD";

	private static void writeTagInternal( PageContext context, Tag tag, Tag parentTag )
		throws JspException {

		tag.setPageContext( context );
		tag.setParent( parentTag );

		try {
			int returnCode = tag.doStartTag();

			if ( returnCode != Tag.SKIP_BODY ) {
				if ( tag instanceof IterationTag ) {
					returnCode = IterationTag.EVAL_BODY_AGAIN;

					while ( returnCode == IterationTag.EVAL_BODY_AGAIN ) {
						returnCode = ( (IterationTag) tag ).doAfterBody();
					}
				}

				// Support deferred children

				if ( tag instanceof TagSupport ) {
					@SuppressWarnings( "unchecked" )
					List<Tag> deferredChildren = (List<Tag>) ( (TagSupport) tag ).getValue( DEFERRED_CHILD_KEY );

					if ( deferredChildren != null ) {

						for ( Tag childTag : deferredChildren ) {
							writeTagInternal( context, childTag, tag );
						}
					}
				}
			}

			tag.doEndTag();
		} finally {
			tag.release();
		}
	}

	//
	// Inner class
	//

	/**
	 * Subverts the <code>getOut</code> method of a <code>PageContext</code> to use
	 * <code>BufferedContent</code>.
	 */

	private static class PageContextDelegate
		extends PageContext {

		//
		// Private members
		//

		private PageContext	mContext;

		private JspWriter	mWriter;

		//
		// Constructor
		//

		public PageContextDelegate( PageContext context ) {

			mContext = context;
			mWriter = new BufferedContent();
		}

		//
		// Public methods
		//

		@Override
		public JspWriter getOut() {

			return mWriter;
		}

		//
		// Delegate methods
		//

		@Override
		public Object findAttribute( String attribute ) {

			return mContext.findAttribute( attribute );
		}

		@Override
		public void forward( String relativeUrlPath )
			throws ServletException, IOException {

			mContext.forward( relativeUrlPath );
		}

		@Override
		public Object getAttribute( String name, int scope ) {

			return mContext.getAttribute( name, scope );
		}

		@Override
		public Object getAttribute( String name ) {

			return mContext.getAttribute( name );
		}

		@Override
		public Enumeration<?> getAttributeNamesInScope( int scope ) {

			return mContext.getAttributeNamesInScope( scope );
		}

		@Override
		public int getAttributesScope( String name ) {

			return mContext.getAttributesScope( name );
		}

		@Override
		public Exception getException() {

			return mContext.getException();
		}

		@Override
		public ExpressionEvaluator getExpressionEvaluator() {

			return mContext.getExpressionEvaluator();
		}

		@Override
		public Object getPage() {

			return mContext.getPage();
		}

		@Override
		public ServletRequest getRequest() {

			return mContext.getRequest();
		}

		@Override
		public ServletResponse getResponse() {

			return mContext.getResponse();
		}

		@Override
		public ServletConfig getServletConfig() {

			return mContext.getServletConfig();
		}

		@Override
		public ServletContext getServletContext() {

			return mContext.getServletContext();
		}

		@Override
		public HttpSession getSession() {

			return mContext.getSession();
		}

		@Override
		public VariableResolver getVariableResolver() {

			return mContext.getVariableResolver();
		}

		@Override
		public void handlePageException( Exception exception )
			throws ServletException, IOException {

			mContext.handlePageException( exception );
		}

		@Override
		public void handlePageException( Throwable throwable )
			throws ServletException, IOException {

			mContext.handlePageException( throwable );
		}

		@Override
		public void include( String relativeUrlPath, boolean flush )
			throws ServletException, IOException {

			mContext.include( relativeUrlPath, flush );
		}

		@Override
		public void include( String relativeUrlPath )
			throws ServletException, IOException {

			mContext.include( relativeUrlPath );
		}

		@Override
		public void initialize( Servlet servlet, ServletRequest request, ServletResponse response, String errorPageUrl, boolean needsSession, int bufferSize, boolean autoFlush )
			throws IOException, IllegalStateException, IllegalArgumentException {

			mContext.initialize( servlet, request, response, errorPageUrl, needsSession, bufferSize, autoFlush );
		}

		@Override
		public void release() {

			mContext.release();
		}

		@Override
		public void removeAttribute( String name, int scope ) {

			mContext.removeAttribute( name, scope );
		}

		@Override
		public void removeAttribute( String name ) {

			mContext.removeAttribute( name );
		}

		@Override
		public void setAttribute( String name, Object value, int scope ) {

			mContext.setAttribute( name, value, scope );
		}

		@Override
		public void setAttribute( String name, Object value ) {

			mContext.setAttribute( name, value );
		}
	}

	/**
	 * Acts as a buffer for <code>BodyContent</code>. Allows <code>Tag</code> output to be buffered
	 * temporarily before deciding whether to use it or discard it.
	 * <p>
	 * Since <code>BodyContent</code> extends <code>JspWriter</code>, acts as a buffer for
	 * <code>JspWriter</code> too.
	 */

	private static class BufferedContent
		extends BodyContent {

		//
		// Private members
		//

		private StringWriter	mStringWriter;

		private PrintWriter		mPrintWriter;

		//
		// Constructor
		//

		public BufferedContent() {

			super( null );
			clear();
		}

		//
		// Public methods
		//

		@Override
		public void clear() {

			mStringWriter = new StringWriter();
			mPrintWriter = new PrintWriter( mStringWriter );
		}

		@Override
		public void clearBuffer() {

			// Do nothing
		}

		@Override
		public void close() {

			mPrintWriter.close();
		}

		@Override
		public void flush() {

			mPrintWriter.flush();
		}

		@Override
		public int getRemaining() {

			return 0;
		}

		@Override
		public void newLine() {

			mPrintWriter.println();
		}

		@Override
		public void print( boolean value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( char value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( int value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( long value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( float value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( double value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( char[] value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( String value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void print( Object value ) {

			mPrintWriter.print( value );
		}

		@Override
		public void println() {

			mPrintWriter.println();
		}

		@Override
		public void println( boolean value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( char value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( int value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( long value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( float value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( double value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( char[] value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( String value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void println( Object value ) {

			mPrintWriter.println( value );
		}

		@Override
		public void write( char[] cbuf, int off, int len ) {

			mPrintWriter.write( cbuf, off, len );
		}

		@Override
		public String toString() {

			return getString();
		}

		@Override
		public Reader getReader() {

			return new StringReader( getString() );
		}

		@Override
		public String getString() {

			return mStringWriter.toString();
		}

		@Override
		public void writeOut( Writer writer )
			throws IOException {

			writer.write( getString() );
		}
	}

	//
	// Private statics
	//

	private static final Pattern	PATTERN_HIDDEN_FIELDS	= Pattern.compile( "(\\s*<\\s*(input)\\s+[^>]*?(type)\\s*=\\s*\"\\s*hidden\\s*\"[^>]*?>\\s*)+?" );

	//
	// Private constructor
	//

	private JspUtils() {

		// Can never be called
	}
}
