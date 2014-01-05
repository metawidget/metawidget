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

/**
 * WidgetProcessor that turns hidden fields into <code>HiddenTag</code>s, so that they POST back.
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HiddenFieldProcessor
extends org.metawidget.jsp.tagext.html.widgetprocessor.HiddenFieldProcessor {

	//
	// Protected methods
	//

	@Override
	protected Tag wrapTag( Tag tag, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// Hidden field?

		HiddenTag hiddenTag = new HiddenTag();
		String name = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null ) {
			name = metawidget.getPathPrefix() + name;
		}

		hiddenTag.setProperty( name );

		// Read-only field?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return hiddenTag;
		}

		// org.apache.struts.taglib.html.HiddenTag will output a <hidden> tag. If tag.setWrite( true
		// ) doesn't output anything additional (i.e. the field has no value) then it'll look to
		// HtmlTableLayout like this should be placed outside the table along with all the other
		// <hidden> fields. Output a SPAN tag to stop this.

		try {
			StringBuilder builder = new StringBuilder();

			hiddenTag.setWrite( true );
			builder.append( JspUtils.writeTag( metawidget.getPageContext(), hiddenTag, metawidget ));

			if ( JspUtils.isJustHiddenFields( builder ) ) {
				builder.append( "<span></span>" );
			}

			return new LiteralTag( builder.toString() );
		} catch ( JspException e ) {
			throw WidgetBuilderException.newException( e );
		}
	}
}
