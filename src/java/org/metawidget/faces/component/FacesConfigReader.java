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

package org.metawidget.faces.component;

import java.io.InputStream;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.metawidget.config.ConfigReader;
import org.metawidget.inspector.iface.InspectorException;

/**
 * Specialized ConfigReader for Java Server Faces.
 * <p>
 * Resolves references by looking in <code>/WEB-INF/</code> first.
 *
 * @author Richard Kennard
 */

public class FacesConfigReader
	extends ConfigReader
{
	//
	// Protected methods
	//

	/**
	 * Overridden to try <code>/WEB-INF</code> first.
	 */

	@Override
	public InputStream openResource( String resource )
	{
		try
		{
			URL url = FacesContext.getCurrentInstance().getExternalContext().getResource( "/WEB-INF/" + resource );

			if ( url != null )
				return url.openStream();
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}

		return super.openResource( resource );
	}
}
