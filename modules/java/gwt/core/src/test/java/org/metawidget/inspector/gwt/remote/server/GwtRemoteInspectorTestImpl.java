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

package org.metawidget.inspector.gwt.remote.server;


/**
 * Version of <code>GwtRemoteInspectorTestImpl</code> for running unit tests.
 * <p>
 * This code can be removed once GWT's &lt;servlet&gt; module element supports &lt;init-param&gt;
 * (see http://code.google.com/p/google-web-toolkit/issues/detail?id=4079)
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GwtRemoteInspectorTestImpl
	extends GwtRemoteInspectorImpl {

	//
	// Protected methods
	//

	@Override
	protected String getConfigInitParameter() {

		return "metawidget.xml";
	}
}
