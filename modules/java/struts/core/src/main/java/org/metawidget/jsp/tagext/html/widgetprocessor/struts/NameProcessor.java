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

import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.taglib.html.CheckboxTag;
import org.apache.struts.taglib.html.SelectTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds a 'name' attribute to a Struts Tag.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NameProcessor
	implements WidgetProcessor<Tag, MetawidgetTag> {

	//
	// Public methods
	//

	public Tag processWidget( Tag tag, String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		String name = attributes.get( NAME );
		String pathPrefix = metawidget.getPathPrefix();

		if ( pathPrefix != null ) {
			name = pathPrefix + name;
		}

		if ( tag instanceof BaseInputTag ) {
			( (BaseInputTag) tag ).setProperty( name );
		} else if ( tag instanceof SelectTag ) {
			( (SelectTag) tag ).setProperty( name );
		} else if ( tag instanceof CheckboxTag ) {
			( (CheckboxTag) tag ).setProperty( name );
		}

		return tag;
	}
}
