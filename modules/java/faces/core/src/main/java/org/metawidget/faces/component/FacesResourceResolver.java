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

package org.metawidget.faces.component;

import java.io.InputStream;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.iface.InspectorException;

/**
 * Specialized ResourceResolver for Java Server Faces.
 * <p>
 * Resolves references by looking in <code>/WEB-INF/</code> first.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FacesResourceResolver
	extends SimpleResourceResolver {

	//
	// Protected methods
	//

	/**
	 * Overridden to try <code>/WEB-INF</code> first.
	 */

	@Override
	public InputStream openResource( String resource ) {

		try {
			URL url = FacesContext.getCurrentInstance().getExternalContext().getResource( "/WEB-INF/" + resource );

			if ( url != null ) {
				return url.openStream();
			}
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}

		return super.openResource( resource );
	}
}
