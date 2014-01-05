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

package org.metawidget.faces.renderkit;

import javax.faces.render.Renderer;

/**
 * Renders a Stub.
 * <p>
 * Stubs render nothing at all, unless they have child components in which case the children are
 * rendered 'as is' with no further decoration (eg. no &lt;div&gt; tags or &lt;span&gt; tags).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StubRenderer
	extends Renderer {
	// Just renders children
}
