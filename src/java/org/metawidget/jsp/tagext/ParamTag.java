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
 * Parameter for JSP environments.
 *
 * @author Richard Kennard
 */

public class ParamTag
	extends BodyTagSupport
{
	//
	// Private statics
	//

	private final static long	serialVersionUID	= 1l;

	//
	// Private members
	//

	private String	mName;

	private String	mValue;

	//
	// Public methods
	//

	public void setName( String name )
	{
		mName = name;
	}

	public void setValue( String value )
	{
		mValue = value;
	}

	@Override
	public int doEndTag()
		throws JspException
	{
		MetawidgetTag tagMetawidget = (MetawidgetTag) findAncestorWithClass( this, MetawidgetTag.class );

		if ( tagMetawidget == null )
			throw new JspTagException( getClass() + " must be used within " + MetawidgetTag.class );

		tagMetawidget.setParameter( mName, mValue );

		return super.doEndTag();
	}
}
