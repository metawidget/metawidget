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

package org.metawidget.inspector.gwt.remote.client;

import java.io.Serializable;

import org.metawidget.inspector.gwt.remote.iface.GwtRemoteInspector;
import org.metawidget.inspector.gwt.remote.iface.GwtRemoteInspectorAsync;
import org.metawidget.inspector.iface.Inspector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GwtInspector that sends its objects to the server for inspection.
 * <p>
 * Leveraging Metawidget's separated inspection architecture, GwtRemoteInspectorProxy uses AJAX to
 * pass objects to the server where the full power of Java introspection and reflection can be used
 * to inspect them. This includes inspecting their annotations.
 * <p>
 * Note it is not possible to generically optimize this call to, say, only pass the class name
 * rather than the entire object. This is because some <code>Inspectors</code> inspect the
 * <em>value</em> of the properties (eg. <code>PropertyTypeInspector</code>). On a case-by-case
 * basis, however, such optimization is possible - see <code>GwtMetawidget.rebind</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GwtRemoteInspectorProxy
	implements Inspector {

	//
	// Private members
	//

	private GwtRemoteInspectorAsync	mInspector;

	//
	// Constructor
	//

	/**
	 * Create a GwtRemoteInspectorProxy.
	 */

	public GwtRemoteInspectorProxy() {

		mInspector = (GwtRemoteInspectorAsync) GWT.create( GwtRemoteInspector.class );
	}

	/**
	 * Create a GwtRemoteInspectorProxy.
	 * <p>
	 * This version of the constructor allows the caller to override the 'service entry point' of
	 * the <code>GwtRemoteInspectorImpl</code> servlet. This can be useful for setting up multiple
	 * servlets, each with their own <code>metawidget.xml</code> configuration.
	 *
	 * @param serviceEntryPoint
	 *            override servlet path of serviceEntryPoint
	 */

	public GwtRemoteInspectorProxy( String serviceEntryPoint ) {

		this();

		( (ServiceDefTarget) mInspector ).setServiceEntryPoint( GWT.getModuleBaseURL() + serviceEntryPoint );
	}

	//
	// Public methods
	//

	public String inspect( Object toInspect, String type, String... names ) {

		throw new UnsupportedOperationException( "Use async inspection instead" );
	}

	public void inspect( Object toInspect, String type, String[] names, final AsyncCallback<String> callback ) {

		if ( !( toInspect instanceof Serializable ) ) {
			throw new RuntimeException( "Objects passed to GwtRemoteInspector must be Serializable" );
		}

		mInspector.inspect( (Serializable) toInspect, type, names, new AsyncCallback<String>() {

			public void onFailure( Throwable caught ) {

				callback.onFailure( caught );
			}

			public void onSuccess( String xml ) {

				callback.onSuccess( xml );
			}
		} );

	}
}
