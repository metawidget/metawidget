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

package org.metawidget.inspector.gwt.remote.server;

import java.io.Serializable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.metawidget.inspector.gwt.remote.iface.GwtRemoteInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.jsp.ServletConfigReader;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Servlet for running inspections in GWT environments.
 * <p>
 * This servlet recognizes the following &lt;init-param&gt;'s:
 * <ul>
 * <li><code>config</code> - fully qualified path to (optional) <code>metawidget.xml</code>,
 * for example <code>com/foo/metawidget.xml</code>.
 * </ul>
 *
 * @author Richard Kennard
 */

public class GwtRemoteInspectorImpl
	extends RemoteServiceServlet
	implements GwtRemoteInspector
{
	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1l;

	//
	// Private members
	//

	private Inspector			mInspector;

	//
	// Public methods
	//

	@Override
	public void init( ServletConfig servletConfig )
		throws ServletException
	{
		super.init( servletConfig );
		configure( servletConfig );
	}

	public String inspect( Serializable toInspect, String type, String[] names )
	{
		return mInspector.inspect( toInspect, type, names );
	}

	//
	// Protected methods
	//

	protected void setInspector( Inspector inspector )
	{
		mInspector = inspector;
	}

	protected String getDefaultConfiguration()
	{
		return "org/metawidget/inspector/gwt/remote/server/metawidget-gwt-default.xml";
	}

	/**
	 * Refactored to support <code>GwtRemoteInspectorTestImpl</code>.
	 */

	protected void configure( ServletConfig servletConfig )
	{
		// Locate metawidget.xml (if any)

		ServletConfigReader servletConfigReader = new ServletConfigReader( servletConfig.getServletContext() );
		String config = servletConfig.getInitParameter( "config" );

		if ( config != null )
		{
			setInspector( servletConfigReader.configure( config, Inspector.class ));
			return;
		}

		setInspector( servletConfigReader.configure( getDefaultConfiguration(), Inspector.class ));
	}
}
