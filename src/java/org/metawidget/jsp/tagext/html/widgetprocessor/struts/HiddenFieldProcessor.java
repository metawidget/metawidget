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

package org.metawidget.jsp.tagext.html.widgetprocessor.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.html.HiddenTag;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that turns hidden fields into <code>HiddenTag</code>s, so that they POST back.
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

		// Read-only field with POST-back?

		if ( tag instanceof HiddenTag ) {

			( (HiddenTag) tag ).setDisabled( false );

			// If the read-only output is now just a hidden field, output an empty SPAN to stop the
			// whole thing vanishing under HtmlTableLayout. This is a bit hacky, unfortunately, but
			// it's because we don't want HtmlTableLayout checking whether this tag is a HiddenTag
			// (and introducing a Struts dependency)

			try {
				String literal = JspUtils.writeTag( metawidget.getPageContext(), tag, metawidget, null );

				if ( JspUtils.isJustHiddenFields( literal ) ) {
					return new LiteralTag( literal + "<span></span>" );
				}

				return new LiteralTag( literal );
			} catch ( JspException e ) {
				throw WidgetBuilderException.newException( e );
			}
		}

		// Hidden field?

		HiddenTag hiddenTag = new HiddenTag();
		String name = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null ) {
			name = metawidget.getPathPrefix() + name;
		}

		hiddenTag.setProperty( name );
		return hiddenTag;
	}
}
