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

package org.metawidget.jsp.tagext.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.widgetbuilder.HtmlWidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds HTML <code>&lt;input type="hidden"&gt;</code> tags to hidden and
 * read-only values, so that they POST back.
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author Richard Kennard
 */

public class HiddenFieldProcessor
	implements WidgetProcessor<Tag, MetawidgetTag> {

	//
	// Public methods
	//

	@Override
	public Tag processWidget( Tag tag, String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// Not for us?

		if ( !TRUE.equals( attributes.get( MetawidgetTag.ATTRIBUTE_NEEDS_HIDDEN_FIELD ) ) ) {
			return tag;
		}

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		try {
			// Write the tag...

			String value = JspUtils.writeTag( metawidget.getPageContext(), tag, metawidget, null );
			buffer.append( value );

			// ...together with a hidden tag

			buffer.append( "<input type=\"hidden\"" );
			buffer.append( HtmlWidgetBuilderUtils.writeValueAttribute( attributes, metawidget ) );
			buffer.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
			buffer.append( "/>" );

			// If value is empty, output an empty SPAN to stop this field being treated as 'just a
			// hidden field'

			if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( value ) ) {
				buffer.append( "<span></span>" );
			}

		} catch ( JspException e ) {
			throw WidgetBuilderException.newException( e );
		}

		return new LiteralTag( buffer.toString() );
	}
}
