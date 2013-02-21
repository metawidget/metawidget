// Metawidget (licensed under LGPL)
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
 * @author Richard Kennard
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
