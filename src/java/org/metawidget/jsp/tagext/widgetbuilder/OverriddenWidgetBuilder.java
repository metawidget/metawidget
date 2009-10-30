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

package org.metawidget.jsp.tagext.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for overridden widgets in JSP environments.
 * <p>
 * Locates overridden widgets based on <code>StubTag</code>.
 *
 * @author Richard Kennard
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<Tag, MetawidgetTag>
{
	//
	// Public methods
	//

	@Override
	public Tag buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		return metawidgetTag.getStub( attributes.get( NAME ) );
	}
}
