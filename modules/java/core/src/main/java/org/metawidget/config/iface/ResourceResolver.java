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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface ResourceResolver {

	//
	// Methods
	//

	InputStream openResource( String resource );
}
