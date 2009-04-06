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

package org.metawidget.jsp.tagext.spring;

import javax.servlet.http.HttpServletRequest;

import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.simple.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for Spring environments.
 * <p>
 * Automatically creates native Spring form tags, such as <code>&lt;form:input&gt;</code> and
 * <code>&lt;form:select&gt;</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class SpringMetawidgetTag
	extends BaseHtmlMetawidgetTag
{
	//
	// Private statics
	//

	private final static long			serialVersionUID	= 1l;

	//
	// Public methods
	//

	public void setPath( String path )
	{
		mPath = path;

		// Take the LHS minus the first path (if any), as we assume that will
		// be supplied by the form

		if ( path != null )
		{
			int lastIndexOf = path.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 )
			{
				int firstIndexOf = path.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

				if ( firstIndexOf != lastIndexOf )
					mPathPrefix = path.substring( firstIndexOf + 1, lastIndexOf + 1 );
			}
		}
	}

	@Override
	public String getLocalizedKey( String key )
	{
		String localizedKey = super.getLocalizedKey( key );

		if ( localizedKey != null )
			return localizedKey;

		// Use Spring MessageResources (if any)
		//
		// Note: we are unclear how, in Spring, to differentiate 'there is no key
		// in the bundle' from 'there are no bundles'. So for now we always assume
		// the latter (eg. we'll never return ???key???)

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		MessageSource context = RequestContextUtils.getWebApplicationContext( request );

		return context.getMessage( key, null, null, RequestContextUtils.getLocale( request ) );
	}

	//
	// Protected methods
	//

	@Override
	protected void beforeBuildCompoundWidget( Element element )
	{
		// Take the whole path minus the first value (if any), as we assume that will
		// be supplied by the form

		int firstIndexOf = mPath.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

		if ( firstIndexOf != -1 )
			mPathPrefix = mPath.substring( firstIndexOf + 1 ) + StringUtils.SEPARATOR_DOT_CHAR;
	}
}
