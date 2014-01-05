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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HiddenFieldProcessor
	implements WidgetProcessor<Tag, MetawidgetTag> {

	//
	// Public statics
	//

	/**
	 * Marks a tag as potentially needing a hidden field.
	 * <p>
	 * In order to align tightly with the <code>StubTag</code>s created by the
	 * <code>WidgetBuilder</code>s, and to avoid confusion with manually created
	 * <code>StubTag</code>s (eg. from <code>OverriddenWidgetBuilder</code>), we take a flag-based
	 * approach to hidden field processing. This would be cleaner if JSP had a richer component
	 * model (ie. in JSF we do <code>instanceof UIInput</code>).
	 */

	public static final String	ATTRIBUTE_NEEDS_HIDDEN_FIELD	= "metawidget-needs-hidden-field";

	//
	// Public methods
	//

	public Tag processWidget( Tag tag, String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// Not for us?

		if ( !TRUE.equals( attributes.get( ATTRIBUTE_NEEDS_HIDDEN_FIELD ) ) ) {
			return tag;
		}

		return wrapTag( tag, attributes, metawidget );
	}

	//
	// Protected methods
	//

	protected Tag wrapTag( Tag tag, Map<String, String> attributes, MetawidgetTag metawidget ) {

		try {
			StringBuilder builder = new StringBuilder();

			// Write the tag...

			String value = JspUtils.writeTag( metawidget.getPageContext(), tag, metawidget );
			builder.append( value );

			// ...together with a hidden tag

			Tag hiddenTag = createHiddenTag( attributes, metawidget );
			builder.append( JspUtils.writeTag( metawidget.getPageContext(), hiddenTag, metawidget ) );

			// If value is empty, output a SPAN to stop HtmlTableLayout treating this field as 'just
			// a hidden field' and putting it outside the table

			if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( value ) ) {
				builder.append( "<span></span>" );
			}

			return new LiteralTag( builder.toString() );
		} catch ( JspException e ) {
			throw WidgetBuilderException.newException( e );
		}
	}

	protected Tag createHiddenTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		StringBuilder builder = new StringBuilder();

		builder.append( "<input type=\"hidden\"" );
		builder.append( HtmlWidgetBuilderUtils.writeValueAttribute( attributes, metawidget ) );
		builder.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
		builder.append( "/>" );

		return new LiteralTag( builder.toString() );
	}
}
