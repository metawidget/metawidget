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

package org.metawidget.config;

import java.io.InputStream;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ClassUtils;

/**
 * A simple <code>ResourceResolver</code> implementation that uses
 * <code>ClassUtils.openResource</code>.
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

	@Override
	public InputStream openResource( String resource ) {

		try {
			return ClassUtils.openResource( resource );
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}
}
