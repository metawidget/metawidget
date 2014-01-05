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

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;

/**
 * A simple <code>ResourceResolver</code> implementation that locates the given resource by trying,
 * in order:
 * <p>
 * <ul>
 * <li>the current Thread's context classloader, if any
 * <li>the classloader that loaded <code>SimpleResourceResolver</code>
 * </ul>
 * <p>
 * This can be useful for <code>xxxConfig</code> classes that want to create just-in-time
 * <code>ResourceResolver</code>s. This saves clients having to supply their own
 * <code>ResourceResolver</code>s (in cases where the <code>xxxConfig</code> is not created using
 * <code>ConfigReader</code>).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleResourceResolver
	implements ResourceResolver {

	//
	// Public methods
	//

	public InputStream openResource( String resource ) {

		if ( resource == null || "".equals( resource.trim() ) ) {
			throw InspectorException.newException( new FileNotFoundException( "No resource specified" ));
		}

		// Thread's ClassLoader

		ClassLoader loaderContext = Thread.currentThread().getContextClassLoader();

		if ( loaderContext != null ) {
			InputStream stream = loaderContext.getResourceAsStream( resource );

			if ( stream != null ) {
				return stream;
			}
		}

		// Our ClassLoader

		InputStream stream = getClass().getResourceAsStream( resource );

		if ( stream != null ) {
			return stream;
		}

		throw InspectorException.newException( new FileNotFoundException( "Unable to locate " + resource + " on CLASSPATH" ));
	}
}
