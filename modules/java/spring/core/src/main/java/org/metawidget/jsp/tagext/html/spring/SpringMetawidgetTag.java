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

package org.metawidget.jsp.tagext.html.spring;

import javax.servlet.http.HttpServletRequest;

import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for Spring environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringMetawidgetTag
	extends BaseHtmlMetawidgetTag {

	//
	// Public methods
	//

	public void setPath( String path ) {

		super.setPathInternal( path );

		// Take the LHS minus the first path (if any), as we assume that will
		// be supplied by the form

		if ( path != null ) {
			int lastIndexOf = path.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 ) {
				int firstIndexOf = path.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

				if ( firstIndexOf != lastIndexOf ) {
					setPathPrefix( path.substring( firstIndexOf + 1, lastIndexOf + 1 ) );
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
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( SpringMetawidgetTag.class ) + "/metawidget-spring-default.xml";
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
