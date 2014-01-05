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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Facet for JSP environments.
 * <p>
 * FacetTags differ from ParamTags in that parameters contain values (usually Layout settings) and
 * are carried forward into nested Metawidgets, whereas facets contain JSP and are not to be carried
 * forward.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FacetTag
	extends BodyTagSupport {

	//
	// Private members
	//

	private String				mName;

	private String				mSavedBodyContent;

	//
	// Public methods
	//

	public void setName( String name ) {

		mName = name;
	}

	/**
	 * Get the body content as saved during <code>doEndTag</code>.
	 * <p>
	 * It seems <code>getBodyContent().toString</code> only returns a meaningful result while we are
	 * in the <code>doEndTag</code> method. We capture it there for use later.
	 */

	public String getSavedBodyContent() {

		return mSavedBodyContent;
	}

	@Override
	public int doEndTag()
		throws JspException {

		MetawidgetTag tagMetawidget = (MetawidgetTag) findAncestorWithClass( this, MetawidgetTag.class );

		if ( tagMetawidget == null ) {
			throw new JspTagException( getClass() + " must be used within " + MetawidgetTag.class );
		}

		mSavedBodyContent = bodyContent.getString();
		tagMetawidget.setFacet( mName, this );

		return super.doEndTag();
	}
}
