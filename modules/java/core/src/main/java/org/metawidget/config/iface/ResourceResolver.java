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

package org.metawidget.config.iface;

import java.io.InputStream;

/**
 * Interface for resolving references to resources.
 * <p>
 * Most resources can be resolved using standard <code>ClassLoader.getResource</code> code. However
 * some environments have specialized resource areas which are inaccessible to
 * <code>ClassLoader</code>. For example, Web environments have <code>/WEB-INF/</code> which can
 * only be accessed through <code>ServletContext</code>. Similarly, Android environments must
 * resolve resources using <code>Context.getResources</code>.
 * <p>
 * Note: this class is not located under <code>org.metawidget.iface</code>, because GWT does not
 * like <code>java.io.InputStream</code>.
 *
 * @author Richard Kennard
 */

public interface ResourceResolver {

	//
	// Methods
	//

	InputStream openResource( String resource );
}
