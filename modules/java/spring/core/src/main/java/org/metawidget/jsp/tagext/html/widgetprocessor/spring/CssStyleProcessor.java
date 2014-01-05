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

package org.metawidget.jsp.tagext.html.widgetprocessor.spring;

import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/**
 * WidgetProcessor that adds CSS styles to a Spring AbstractHtmlElementTag, based on the styles of
 * the parent Metawidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CssStyleProcessor
	implements WidgetProcessor<Tag, MetawidgetTag> {

	//
	// Public methods
	//

	public Tag processWidget( Tag tag, String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		if ( tag instanceof AbstractHtmlElementTag ) {
			AbstractHtmlElementTag tagAbstractHtmlElement = (AbstractHtmlElementTag) tag;
			BaseHtmlMetawidgetTag htmlMetawidgetTag = (BaseHtmlMetawidgetTag) metawidget;
			tagAbstractHtmlElement.setCssStyle( htmlMetawidgetTag.getStyle() );
			tagAbstractHtmlElement.setCssClass( htmlMetawidgetTag.getStyleClass() );
		}

		return tag;
	}
}
