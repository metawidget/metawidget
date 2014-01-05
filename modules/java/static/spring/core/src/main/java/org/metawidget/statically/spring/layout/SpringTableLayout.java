// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

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
import org.metawidget.util.simple.StringUtils;

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

	    super.layoutLabel(row, widgetNeedingLabel, elementName, attributes, metawidget);
	    HtmlTableHeader labelCell = (HtmlTableHeader) row.getChildren().get( 0 );

	    FormLabelTag label = new FormLabelTag();
		metawidget.getWidgetProcessor( PathProcessor.class ).processWidget( label, elementName, attributes, (StaticSpringMetawidget) metawidget );
		String labelText = metawidget.getLabelString( attributes );
		if ( labelText != null && labelText.length() > 0 ) {
			label.setTextContent( labelText + StringUtils.SEPARATOR_COLON );
		}

		labelCell.getChildren().remove( 0 );
		labelCell.getChildren().add( label );

		return true;
	}
}
