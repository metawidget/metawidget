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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
