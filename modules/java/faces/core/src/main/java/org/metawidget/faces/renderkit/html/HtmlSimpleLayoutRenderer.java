// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.faces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.metawidget.faces.FacesUtils;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 * <p>
 * This Layout is suited to rendering single components, or for rendering components whose layout
 * relies entirely on CSS. This implementation recognizes the following <code>&lt;f:facet&gt;</code>
 * names:
 * <p>
 * <ul>
 * <li><code>after<code></li>
 * </ul>
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlSimpleLayoutRenderer
	extends Renderer {

	//
	// Private statics
	//

	private static final String	AFTER_FACET	= "after";

	//
	// Public methods
	//

	@Override
	public void encodeBegin( FacesContext context, UIComponent component )
		throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		// Important to wrap output in something with an id, so that 'label for' can refer to it

		writer.startElement( "div", component );
		writer.writeAttribute( "id", component.getClientId( context ), "id" );

		// Display as 'inline' so as not to affect formatting. However don't use a 'span' because
		// we're not allowed to put some tags (i.e. 'div', 'table') inside a 'span'

		writer.writeAttribute( "style", "display: inline", null );

		super.encodeBegin( context, component );
	}

	@Override
	public void encodeEnd( FacesContext context, UIComponent component )
		throws IOException {

		super.encodeEnd( context, component );
		FacesUtils.render( context, component.getFacet( AFTER_FACET ) );

		ResponseWriter writer = context.getResponseWriter();
		writer.endElement( "div" );
	}
}
