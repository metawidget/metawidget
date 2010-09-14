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

package org.metawidget.jsp.tagext;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * JSP tag to encapsulate a String literal.
 * <p>
 * The JSP component model is too lightweight for our needs. Specifically, whilst it is possible for
 * a tag to find its parent (<code>findAncestorWithClass</code>), there is no general purpose
 * mechanism for programmatically adding children to a <code>Tag</code>.
 * <p>
 * Therefore many tag evaluations (eg. evaluation of a Struts SelectTag with nested OptionsTag) must
 * be done 'ahead of time' (ie. in the WidgetBuilder, not in the Layout) and the result stored as a
 * String. This breaks the JSP component model because Strings are not Tags, and so cannot be
 * returned as Tags. <code>LiteralTag</code> wraps a String as a Tag so that the component model
 * remains intact, and we can genericize our WidgetBuilders, WidgetProcessors and Layouts as
 * &lt;Tag, MetawidgetTag&gt;.
 * <p>
 * The output of a LiteralTag is the original String, without escaping.
 * <p>
 * <strong>LiteralTag is an internal tag for use by WidgetBuilders, WidgetProcessors and Layouts. It
 * is not available in any TLD.</strong>
 *
 * @author Richard Kennard
 */

public class LiteralTag
	extends TagSupport {

	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1l;

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
