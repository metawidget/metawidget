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

package org.metawidget.jsp.tagext.html.widgetbuilder.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.html.HiddenTag;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * ReadOnlyWidgetBuilder for Struts environments.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	extends org.metawidget.jsp.tagext.html.widgetbuilder.ReadOnlyWidgetBuilder {

	//
	// Protected methods
	//

	@Override
	protected Tag writeReadOnlyTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		HiddenTag tag = new HiddenTag();
		String name = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null ) {
			name = metawidget.getPathPrefix() + name;
		}

		tag.setProperty( name );
		tag.setWrite( true );

		// Note: according to STR-1305 we'll get a proper html:label tag
		// with Struts 1.4.0, so we can use it instead of .setDisabled( true )

		if ( !( (BaseHtmlMetawidgetTag) metawidget ).isCreateHiddenFields() || TRUE.equals( attributes.get( NO_SETTER ) ) ) {
			tag.setDisabled( true );
		}

		// If the String is just a hidden field, output a SPAN tag to
		// stop the whole thing vanishing under HtmlTableLayout. This is
		// a bit hacky, unfortunately

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
}
