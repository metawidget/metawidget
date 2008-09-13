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

package org.metawidget.faces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 * <p>
 * This Layout is suited to rendering single components, or for rendering components whose
 * layout relies entirely on CSS.
 * This implementation recognizes the following <code>&lt;f:facet&gt;</code> names:
 * <p>
 * <ul>
 * 	<li><code>after<code></li>
 * </ul>
 *
 * @author Richard Kennard
 */

public class SimpleLayoutRenderer
	extends LayoutRenderer
{
	//
	// Private statics
	//

	private final static String	AFTER_FACET	= "after";

	//
	// Public methods
	//

	@Override
	public void encodeEnd( FacesContext context, UIComponent component )
		throws IOException
	{
		// After facet

		UIComponent afterFacet = component.getFacet( AFTER_FACET );

		if ( afterFacet != null )
		{
			FacesUtils.render( context, afterFacet );
		}
	}
}
