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

package org.metawidget.jsp.tagext;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * JSP tag to encapsulate a String literal.
 * <p>
 * LiteralTag provides a way to return fragments of HTML as Tags. The output of a LiteralTag is the
 * given String, without escaping. Plain JSP lacks a component model that abstracts &lt;input&gt;,
 * &lt;select&gt; and other sorts of tag, and Metawidget does not try to introduce one.
 * <p>
 * <strong>LiteralTag is an internal tag for use by WidgetBuilders, WidgetProcessors and Layouts. It
 * is not available in any TLD.</strong>
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LiteralTag
	extends TagSupport {

	//
	// Private members
	//

	private String				mLiteral;

	//
	// Constructor
	//

	public LiteralTag( String literal ) {

		mLiteral = literal;
	}

	//
	// Public methods
	//

	@Override
	public int doEndTag()
		throws JspException {

		try {
			JspWriter writer = pageContext.getOut();
			writer.write( mLiteral );

			return super.doEndTag();
		} catch ( IOException e ) {
			throw new JspException( e );
		}
	}
}
