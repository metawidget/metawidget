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

package org.metawidget.config.impl;

import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

import org.metawidget.inspector.iface.InspectorException;

/**
 * Specialized ResourceResolver for Servlets.
 * <p>
 * Resolves references by looking in <code>/WEB-INF/</code> first. Defined here, rather than in
 * <code>org.metawidget.jsp</code>, because needs to be shared by various Web-based frameworks
 * including GWT.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ServletResourceResolver
	extends SimpleResourceResolver {

	//
	// Private members
	//

	private ServletContext	mContext;

	//
	// Constructor
	//

	public ServletResourceResolver( ServletContext context ) {

		mContext = context;
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to try <code>/WEB-INF/</code> first.
	 */

	@Override
	public InputStream openResource( String resource ) {

		try {
			URL url = mContext.getResource( "/WEB-INF/" + resource );

			if ( url != null ) {
				return url.openStream();
			}
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}

		return super.openResource( resource );
	}
}
