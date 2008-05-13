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

import org.metawidget.gwt.client.inspector.GwtInspectorAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * GwtInspector that sends its objects to the server for inspection.
 * <p>
 * Leveraging Metawidget's separated inspection architecture, GwtRemoteInspectorProxy uses AJAX to
 * pass objects to the server where the full power of Java introspection and reflection can be used
 * to inspect them. This includes inspecting their annotations.
 *
 * @author Richard Kennard
 */

public class GwtRemoteInspectorProxy
	implements GwtInspectorAsync
{
	//
	//
	// Private members
	//
	//

	private GwtRemoteInspectorAsync	mInspector;

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

	public Document inspect( Object toInspect, String type, String... names )
		throws Exception
	{
		throw new UnsupportedOperationException( "Use async inspection instead" );
	}

	public void inspect( Object toInspect, String type, String[] names, final AsyncCallback<Document> callback )
	{
		if ( !( toInspect instanceof Serializable ) )
			throw new RuntimeException( "Objects passed to GwtRemoteInspector must be Serializable" );

		mInspector.inspect( (Serializable) toInspect, type, names, new AsyncCallback<String>()
		{
			public void onFailure( Throwable caught )
			{
				callback.onFailure( caught );
			}

			public void onSuccess( String xml )
			{
				Document document;

				if ( xml == null )
					document = null;
				else
					document = XMLParser.parse( xml );

				callback.onSuccess( document );
			}
		} );

	}
}
