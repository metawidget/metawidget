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

package org.metawidget.jsp.tagext.html.widgetbuilder.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.html.HiddenTag;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * ReadOnlyWidgetBuilder for Struts environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyWidgetBuilder
	extends org.metawidget.jsp.tagext.html.widgetbuilder.ReadOnlyWidgetBuilder {

	//
	// Protected methods
	//

	@Override
	protected Tag createReadOnlyLabelTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		HiddenTag tag = new HiddenTag();
		String name = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null ) {
			name = metawidget.getPathPrefix() + name;
		}

		tag.setProperty( name );
		tag.setWrite( true );

		// Note: according to STR-1305 we'll get a proper html:label tag with Struts 1.4.0, so we
		// can use it instead of a HiddenTag with .setDisabled( true )

		tag.setDisabled( true );

		// org.apache.struts.taglib.html.HiddenTag will output a disabled <hidden> tag. If
		// tag.setWrite( true ) doesn't output anything additional (i.e. the field has no value)
		// then it'll look to HtmlTableLayout like this should be placed outside the table along
		// with all the other <hidden> fields. Output a SPAN tag to stop this.

		try {
			String literal = JspUtils.writeTag( metawidget.getPageContext(), tag, metawidget );

			if ( JspUtils.isJustHiddenFields( literal ) ) {
				return new LiteralTag( literal + "<span></span>" );
			}

			return new LiteralTag( literal );
		} catch ( JspException e ) {
			throw WidgetBuilderException.newException( e );
		}
	}

	@Override
	protected Tag createReadOnlyButton( Map<String, String> attributes, MetawidgetTag metawidget ) {

		return new HtmlStubTag();
	}
}
