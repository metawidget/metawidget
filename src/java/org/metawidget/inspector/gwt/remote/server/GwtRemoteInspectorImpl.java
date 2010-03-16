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
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.gwt.remote.iface.GwtRemoteInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.jsp.ServletConfigReader;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Servlet for running inspections in GWT environments.
 * <p>
 * This servlet recognizes the following &lt;init-param&gt;'s:
 * <ul>
 * <li><code>config</code> - fully qualified path to (optional) <code>metawidget.xml</code>, for
 * example <code>com/foo/metawidget.xml</code>.
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

	private final static long				serialVersionUID	= 1l;

	//
	// Private members
	//

	private GwtRemoteInspectorImplPipeline	mPipeline;

	//
	// Constructor
	//

	public GwtRemoteInspectorImpl()
	{
		mPipeline = newPipeline();
	}

	//
	// Public methods
	//

	@Override
	public void init( ServletConfig servletConfig )
		throws ServletException
	{
		super.init( servletConfig );

		// Locate metawidget.xml (if any)

		ServletConfigReader servletConfigReader = new ServletConfigReader( servletConfig.getServletContext() );
		String config = getConfigInitParameter( servletConfig );

		if ( config != null )
			servletConfigReader.configure( config, this );

		// Use default configuration

		mPipeline.configureDefaults( servletConfigReader, getDefaultConfiguration(), GwtRemoteInspectorImpl.class );
	}

	public String inspect( Serializable toInspect, String type, String[] names )
	{
		Element inspectionResult = mPipeline.inspect( toInspect, type, names );

		if ( inspectionResult == null )
			return null;

		return XmlUtils.documentToString( inspectionResult.getOwnerDocument(), false );
	}

	public void setInspector( Inspector inspector )
	{
		mPipeline.setInspector( inspector );
	}

	public void setInspectionResultProcessors( InspectionResultProcessor<Element, GwtRemoteInspectorImpl>... inspectionResultProcessors )
	{
		mPipeline.setInspectionResultProcessors( CollectionUtils.newArrayList( inspectionResultProcessors ) );
	}

	//
	// Protected methods
	//

	/**
	 * Instantiate the Pipeline used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own Pipeline should override this method to instantiate their
	 * version.
	 */

	protected GwtRemoteInspectorImplPipeline newPipeline()
	{
		return new GwtRemoteInspectorImplPipeline();
	}

	protected String getDefaultConfiguration()
	{
		return "org/metawidget/inspector/gwt/remote/server/metawidget-gwt-default.xml";
	}

	/**
	 * Refactored to support <code>GwtRemoteInspectorTestImpl</code>.
	 */

	protected String getConfigInitParameter( ServletConfig servletConfig )
	{
		return servletConfig.getInitParameter( "config" );
	}

	//
	// Inner class
	//

	/**
	 * Use the Pipeline for its Inspector/InspectionResultProcessor support.
	 */

	protected class GwtRemoteInspectorImplPipeline
		extends W3CPipeline<Object, Object, GwtRemoteInspectorImpl>
	{
		//
		// Protected methods
		//

		@Override
		protected GwtRemoteInspectorImpl getPipelineOwner()
		{
			// For passing to processInspectionResult

			return GwtRemoteInspectorImpl.this;
		}

		//
		// Unsupported protected methods (these are for client-side)
		//

		@Override
		protected Map<String, String> getAdditionalAttributes( Object stub )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected GwtRemoteInspectorImpl buildNestedMetawidget( Map<String, String> attributes )
			throws Exception
		{
			throw new UnsupportedOperationException();
		}
	}
}
