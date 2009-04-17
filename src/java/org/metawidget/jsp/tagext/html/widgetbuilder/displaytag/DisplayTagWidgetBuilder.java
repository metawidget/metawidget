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

package org.metawidget.jsp.tagext.html.widgetbuilder.displaytag;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.displaytag.tags.TableTag;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for the DisplayTag library.
 * <p>
 * For more details on DisplayTag see <a
 * href="http://displaytag.sourceforge.net">http://displaytag.sourceforge.net</a>.
 *
 * @author Richard Kennard
 */

// TODO: DisplayTagWidgetBuilder

@SuppressWarnings( "deprecation" )
public class DisplayTagWidgetBuilder
	implements WidgetBuilder<String, MetawidgetTag>
{
	//
	// Public methods
	//

	@Override
	public String buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidgetTag )
		throws Exception
	{
		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		String type = attributes.get( TYPE );

		if ( type == null || type.length() == 0 )
			return null;

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null )
			return null;

		// Collections

		if ( !( Collection.class.isAssignableFrom( clazz ) ))
			return null;

		// Evaluate the expression
		//
		// Note: we tried using just .setName( "foo.bar" ), but DisplayTag requires
		// you to put 'sessionScope' or 'pageScope' at times, and doesn't seem
		// to have an 'allScope' like JSP does?

		PageContext context = metawidgetTag.getPageContext();
		Object toDisplay = context.getExpressionEvaluator().evaluate( "${" + metawidgetTag.getPath() + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) + "}", Object.class, context.getVariableResolver(), null );

		// Create the DisplayTag

		TableTag displayTag = new TableTag();
		displayTag.setName( toDisplay );
		return JspUtils.writeTag( metawidgetTag.getPageContext(), displayTag, metawidgetTag, null );
	}
}
