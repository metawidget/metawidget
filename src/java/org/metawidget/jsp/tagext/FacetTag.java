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
 * @author Richard Kennard
 */

public class FacetTag
	extends BodyTagSupport
{
	//
	// Private statics
	//

	private final static long	serialVersionUID	= 1l;

	//
	// Private members
	//

	private String				mName;

	private String				mSavedBodyContent;

	//
	// Public methods
	//

	public void setName( String name )
	{
		mName = name;
	}

	/**
	 * Get the body content as saved during <code>doEndTag</code>.
	 * <p>
	 * It seems <code>getBodyContent().toString</code> only returns a meaningful result
	 * while we are in the <code>doEndTag</code> method. We capture it there for use
	 * later.
	 */

	public String getSavedBodyContent()
	{
		return mSavedBodyContent;
	}

	@Override
	public int doEndTag()
		throws JspException
	{
		MetawidgetTag tagMetawidget = (MetawidgetTag) findAncestorWithClass( this, MetawidgetTag.class );

		if ( tagMetawidget == null )
			throw new JspTagException( getClass() + " must be used within " + MetawidgetTag.class );

		mSavedBodyContent = bodyContent.getString();
		tagMetawidget.setFacet( mName, this );

		return super.doEndTag();
	}
}
