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

package org.metawidget.jsp.tagext.html;

import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for 'plain' JSP environment (eg. just a servlet-based backend, no Struts/Spring etc)
 * that outputs HTML.
 * <p>
 * When used in a JSP 2.0 environment, automatically initializes tags using JSP EL.
 *
 * @author Richard Kennard
 */

public class HtmlMetawidgetTag
	extends BaseHtmlMetawidgetTag {

	//
	// Public methods
	//

	/**
	 * Sets the path of the object to inspect.
	 * <p>
	 * The value is set as a path (eg. <code>customerSearch</code>) rather than the object itself
	 * (eg. <code>${customerSearch}</code>) so that we can 'inspect from parent' if needed.
	 */

	public void setValue( String value ) {

		super.setPathInternal( value );

		// Take the whole LHS of the path as the prefix, so that names are unique

		if ( value != null ) {
			int lastIndexOf = value.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 ) {
				setPathPrefix( value.substring( 0, lastIndexOf + 1 ) );
			}
		}
	}

	/**
	 * Sets the LocalizationContext used to localize labels.
	 */

	public void setBundle( LocalizationContext bundle ) {

		super.setBundle( bundle.getResourceBundle() );
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( HtmlMetawidgetTag.class ) + "/metawidget-html-default.xml";
	}

	@Override
	protected void beforeBuildCompoundWidget( Element element ) {

		// Take the whole path as the name prefix, so that names are unique

		setPathPrefix( getPath() + StringUtils.SEPARATOR_DOT_CHAR );
	}
}
