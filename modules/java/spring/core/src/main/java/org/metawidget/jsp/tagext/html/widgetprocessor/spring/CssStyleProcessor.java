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

	// TODO: required/min/max attribute processor
	
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
