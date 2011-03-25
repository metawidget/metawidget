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

package org.metawidget.jsp.tagext.html.struts;

import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for Struts environments.
 *
 * @author Richard Kennard
 */

public class StrutsMetawidgetTag
	extends BaseHtmlMetawidgetTag {

	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1l;

	//
	// Public methods
	//

	public void setProperty( String property ) {

		super.setPathInternal( property );

		// Take the LHS minus the first property (if any), as we assume that will
		// be supplied by the form

		if ( property != null ) {
			int lastIndexOf = property.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 ) {
				int firstIndexOf = property.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

				if ( firstIndexOf != lastIndexOf ) {
					setPathPrefix( property.substring( firstIndexOf + 1, lastIndexOf + 1 ) );
				}
			}
		}
	}

	@Override
	public String getLocalizedKey( String key ) {

		String localizedKey = super.getLocalizedKey( key );

		if ( localizedKey != null ) {
			return localizedKey;
		}

		// Use Struts MessageResources (if any)

		MessageResources resources = (MessageResources) pageContext.getAttribute( Globals.MESSAGES_KEY, PageContext.APPLICATION_SCOPE );

		if ( resources == null ) {
			return null;
		}

		return resources.getMessage( key );
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return "org/metawidget/jsp/tagext/html/struts/metawidget-struts-default.xml";
	}

	@Override
	protected void beforeBuildCompoundWidget( Element element ) {

		// Take the whole path minus the first value (if any), as we assume that will
		// be supplied by the form

		int firstIndexOf = getPath().indexOf( StringUtils.SEPARATOR_DOT_CHAR );

		if ( firstIndexOf != -1 ) {
			setPathPrefix( getPath().substring( firstIndexOf + 1 ) + StringUtils.SEPARATOR_DOT_CHAR );
		}
	}
}
