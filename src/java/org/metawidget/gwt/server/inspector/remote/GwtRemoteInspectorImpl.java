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

package org.metawidget.gwt.server.inspector.remote;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.metawidget.gwt.client.inspector.remote.GwtRemoteInspector;
import org.metawidget.inspector.Inspector;
import org.metawidget.jsp.tagext.ServletConfigReader;
import org.w3c.dom.Document;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Servlet for running inspections in GWT environments.
 * <p>
 * This servlet recognizes the following &lt;init-param&gt;'s:
 * <ul>
 * <li><code>inspector-config</code> - fully qualified path to <code>inspector-config.xml</code>,
 * for example <code>com/foo/inspector-config.xml</code>. Defaults to simply
 * <code>inspector-config.xml</code>.
 * </ul>
 *
 * @author Richard Kennard
 */

public class GwtRemoteInspectorImpl
	extends RemoteServiceServlet
	implements GwtRemoteInspector
{
	//
	//
	// Private members
	//
	//

	private Inspector			mInspector;

	private TransformerFactory	mTransformerFactory;

	//
	//
	// Public methods
	//
	//

	@Override
	public void init( ServletConfig config )
		throws ServletException
	{
		super.init( config );

		// Locate inspector-config.xml

		String inspectorConfig = config.getInitParameter( "inspector-config" );

		if ( inspectorConfig == null )
			inspectorConfig = "inspector-config.xml";

		// TODO: delete test GWT files
		//mInspector = new ServletConfigReader( config.getServletContext() ).read( "org/metawidget/example/gwt/addressbook/server/inspector-config.xml" );

		mInspector = new ServletConfigReader( config.getServletContext() ).read( inspectorConfig );
		mTransformerFactory = TransformerFactory.newInstance();
	}

	public String inspect( Serializable toInspect, String type, String[] names )
		throws Exception
	{
		// Inspect the object...

		Document document = mInspector.inspect( toInspect, type, names );

		// ...and return the DOM as a String

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Transformer transformer = mTransformerFactory.newTransformer();
		transformer.transform( new DOMSource( document ), new StreamResult( out ) );

		return out.toString();
	}
}
