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

package org.metawidget.jsp.tagext.html.struts;

import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for Struts environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StrutsMetawidgetTag
	extends BaseHtmlMetawidgetTag {

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

		return ClassUtils.getPackagesAsFolderNames( StrutsMetawidgetTag.class ) + "/metawidget-struts-default.xml";
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
