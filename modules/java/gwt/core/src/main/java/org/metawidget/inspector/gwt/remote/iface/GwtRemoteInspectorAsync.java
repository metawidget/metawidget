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

package org.metawidget.inspector.gwt.remote.iface;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GWT asynchronous AJAX interface to <code>GwtRemoteInspectorImpl</code> servlet.
 * <p>
 * Note: this interface is purely for the AJAX call. It is not related to
 * <code>GwtInspectorAsync</code>.
 * <p>
 * <strong>This interface is designed to work 'out of the box' for most cases. However, use of
 * Serializable as a parameter type is not optimal for GWT. We recommend deriving your own interface
 * with your own business-model-specific base class instead.</strong>
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface GwtRemoteInspectorAsync {

	//
	// Methods
	//

	void inspect( Serializable toInspect, String type, String[] names, AsyncCallback<String> callback );
}
