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

package org.metawidget.statically.spring.layout;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.layout.HtmlTableLayout;
import org.metawidget.statically.html.layout.HtmlTableLayoutConfig;
import org.metawidget.statically.html.widgetbuilder.HtmlTableHeader;
import org.metawidget.statically.html.widgetbuilder.HtmlTag;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormHiddenTag;
import org.metawidget.statically.spring.widgetbuilder.FormLabelTag;
import org.metawidget.statically.spring.widgetprocessor.PathProcessor;

/**
 * Layout to arrange widgets using an HTML table and Spring &lt;form:label&gt; tags.
 *
 * @author Ryan Bradley
 */

public class SpringTableLayout
	extends HtmlTableLayout {

	//
	// Constructors
	//

	public SpringTableLayout() {

		super();
	}

	public SpringTableLayout( HtmlTableLayoutConfig config ) {

		super( config );
	}

	//
	// Public methods
	//

	/**
	 * Overridden to use Spring's &lt;form:label&gt;.
	 */

	@Override
	protected boolean layoutLabel( HtmlTag row, StaticXmlWidget widgetNeedingLabel, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

	    if ( widgetNeedingLabel instanceof FormHiddenTag ) {
	        row.getChildren().add( new HtmlTableHeader() );
	        return false;
	    }

	    FormLabelTag label = new FormLabelTag();
		metawidget.getWidgetProcessor( PathProcessor.class ).processWidget( label, elementName, attributes, (StaticSpringMetawidget) metawidget );
		String labelText = metawidget.getLabelString( attributes );
		if ( labelText != null && labelText.length() > 0 ) {
			label.setTextContent( labelText + ":" );
		}

		HtmlTableHeader labelCell = new HtmlTableHeader();
		labelCell.getChildren().add( label );
		row.getChildren().add( labelCell );

		return true;
	}
}
