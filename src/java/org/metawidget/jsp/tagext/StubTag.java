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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.metawidget.util.CollectionUtils;

/**
 * Base Stub for JSP environments.
 * <p>
 * A Stub takes a <code>path</code> but does nothing with it. Stubs are used to 'stub out' what
 * Metawidget would normally create - either to suppress component creation entirely or to create
 * child components with a different path.
 * <p>
 * StubTags differ from FacetTags in that they define a path and override component creation.
 *
 * @author Richard Kennard
 */

public abstract class StubTag
	extends BodyTagSupport
{
	//
	//
	// Protected members
	//
	//

	protected String			mPath;

	//
	//
	// Private members
	//
	//

	private Map<String, String>	mAttributes;

	//
	//
	// Public methods
	//
	//

	public void setAttributes( String attributes )
		throws JspException
	{
		if ( mAttributes == null )
			mAttributes = CollectionUtils.newHashMap();

		for ( String nameAndValue : CollectionUtils.fromString( attributes, ';' ) )
		{
			List<String> nameAndValueList = CollectionUtils.fromString( nameAndValue, ':' );

			if ( nameAndValueList.size() != 2 )
				throw new JspException( "Unrecognized value '" + nameAndValue + "'" );

			mAttributes.put( nameAndValueList.get( 0 ), nameAndValueList.get( 1 ) );
		}
	}

	@Override
	public int doStartTag()
	{
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag()
		throws JspException
	{
		MetawidgetTag tagMetawidget = (MetawidgetTag) findAncestorWithClass( this, MetawidgetTag.class );

		if ( tagMetawidget == null )
			throw new JspTagException( getClass() + " must be used within " + MetawidgetTag.class );

		String stubContent = null;

		if ( bodyContent != null )
			stubContent = bodyContent.getString();

		tagMetawidget.setStub( mPath, new StubContent( stubContent, mAttributes ) );

		return super.doEndTag();
	}

	@Override
	public void release()
	{
		super.release();

		mPath = null;
		mAttributes = null;
	}

	//
	//
	// Inner class
	//
	//

	public static class StubContent
	{
		//
		//
		// Private members
		//
		//

		private String				mContent;

		private Map<String, String>	mAttributes;

		//
		//
		// Constructor
		//
		//

		public StubContent( String content, Map<String, String> attributes )
		{
			mContent = content;
			mAttributes = attributes;
		}

		//
		//
		// Public methods
		//
		//

		public String getContent()
		{
			return mContent;
		}

		public Map<String, String> getAttributes()
		{
			if ( mAttributes == null )
			{
				// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that we're 1.4 compatible)

				@SuppressWarnings( "unchecked" )
				Map<String, String> empty = Collections.EMPTY_MAP;
				return empty;
			}

			return mAttributes;
		}
	}
}
