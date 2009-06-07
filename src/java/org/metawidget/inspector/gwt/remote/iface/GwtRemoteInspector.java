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

package org.metawidget.inspector.gwt.remote.iface;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * GWT AJAX interface to <code>GwtRemoteInspectorImpl</code> servlet.
 * <p>
 * Note: this interface is purely for the AJAX call. It looks similar to <code>Inspector</code>,
 * but differs in that:
 * <p>
 * <ul>
 * <li>it limits <code>toInspect</code> to <code>Serializable</code>. This helps reduce the
 * class graph the GWT compiler must consider, as well as ensuring only <code>Serializable</code>
 * types are passed 'over the wire'</li>
 * <li>it uses <code>String[]</code> instead of <code>String...</code>. This is necessary
 * because <code>GwtRemoteInspectorAsync</code> needs to append an <code>AsyncCallback</code> as
 * a last argument</li>
 * </ul>
 *
 * @author Richard Kennard
 */

@RemoteServiceRelativePath( "metawidget-inspector" )
public interface GwtRemoteInspector
	extends RemoteService
{
	//
	// Methods
	//

	String inspect( Serializable toInspect, String type, String[] names );
	// TODO: throws InspectorException?
}
