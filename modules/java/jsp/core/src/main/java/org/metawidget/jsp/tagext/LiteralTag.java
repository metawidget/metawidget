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
