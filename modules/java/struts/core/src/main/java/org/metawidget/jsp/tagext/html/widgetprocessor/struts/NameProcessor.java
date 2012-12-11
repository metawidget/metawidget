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

import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.taglib.html.CheckboxTag;
import org.apache.struts.taglib.html.SelectTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds a 'name' attribute to a Struts Tag.
 *
 * @author Richard Kennard
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
