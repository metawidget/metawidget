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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * GWT AJAX interface to <code>GwtRemoteInspectorImpl</code> servlet.
 * <p>
 * Note: this interface is purely for the AJAX call. It looks similar to <code>Inspector</code>, but
 * differs in that:
 * <p>
 * <ul>
 * <li>it limits <code>toInspect</code> to <code>Serializable</code>. This helps reduce the class
 * graph the GWT compiler must consider (ie. smaller than <code>Object</code>), as well as ensuring
 * only <code>Serializable</code> types are passed 'over the wire'</li>
 * <li>it uses <code>String[]</code> instead of <code>String...</code>. This is necessary because
 * <code>GwtRemoteInspectorAsync</code> needs to append an <code>AsyncCallback</code> as a last
 * argument</li>
 * </ul>
 * <p>
 * <strong>This interface is designed to work 'out of the box' for most cases. However, use of
 * Serializable as a parameter type is not optimal for GWT. We recommend deriving your own interface
 * with your own business-model-specific base class instead.</strong>
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@RemoteServiceRelativePath( "metawidget-inspector" )
public interface GwtRemoteInspector
	extends RemoteService {

	//
	// Methods
	//

	String inspect( Serializable toInspect, String type, String[] names );
}
