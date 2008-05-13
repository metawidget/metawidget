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

package org.metawidget.gwt.client.inspector.remote;

import java.io.Serializable;

import org.metawidget.gwt.client.inspector.GwtInspector;
import org.metawidget.gwt.client.inspector.GwtInspectorAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Richard Kennard
 */

public class GwtRemoteInspectorProxy
	implements GwtInspector, GwtInspectorAsync
{
	//
	//
	// Private members
	//
	//

	private GwtRemoteInspectorAsync mInspector;

	//
	//
	// Constructor
	//
	//

	public GwtRemoteInspectorProxy()
	{
		mInspector = (GwtRemoteInspectorAsync) GWT.create( GwtRemoteInspector.class );
		( (ServiceDefTarget) mInspector ).setServiceEntryPoint( GWT.getModuleBaseURL() + "metawidget-inspector" );
	}

	//
	//
	// Public methods
	//
	//

	public String inspect( Serializable toInspect, String type, String[] names )
		throws Exception
	{
		throw new UnsupportedOperationException( "Use async inspection instead" );
	}

	public void inspect( Serializable toInspect, String type, String[] names, AsyncCallback<String> callback )
	{
		mInspector.inspect( toInspect, type, names, callback );
	}
}
