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
import javax.servlet.jsp.tagext.Tag;

import org.displaytag.tags.ColumnTag;
import org.displaytag.tags.TableTag;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * WidgetBuilder for the DisplayTag library.
 * <p>
 * For more details on DisplayTag see <a
 * href="http://displaytag.sourceforge.net">http://displaytag.sourceforge.net</a>.
 *
 * @author Richard Kennard
 */

public class DisplayTagWidgetBuilder
	implements WidgetBuilder<Tag, MetawidgetTag> {

	//
	// Public methods
	//

	public Tag buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		try {
			// Not for us?

			if ( TRUE.equals( attributes.get( HIDDEN ) ) || attributes.containsKey( LOOKUP ) ) {
				return null;
			}

			String type = attributes.get( TYPE );

			if ( type == null || "".equals( type ) ) {
				return null;
			}

			Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz == null ) {
				return null;
			}

			if ( !( Collection.class.isAssignableFrom( clazz ) ) && !clazz.isArray() ) {
				return null;
			}

			// Evaluate the expression
			//
			// Note: we tried using just .setName( "foo.bar" ), but DisplayTag requires
			// you to put 'sessionScope' or 'pageScope' at times, and doesn't seem
			// to have an 'allScope' like JSP does?

			PageContext context = metawidgetTag.getPageContext();
			Object toDisplay = context.getExpressionEvaluator().evaluate( "${" + metawidgetTag.getPath() + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) + "}", Object.class, context.getVariableResolver(), null );

			// Create the TableTag

			TableTag tableTag = new TableTag();
			tableTag.setName( toDisplay );

			// Inspect component type

			String componentType = WidgetBuilderUtils.getComponentType( attributes );
			String inspectedType = null;

			if ( componentType != null ) {
				inspectedType = metawidgetTag.inspect( null, componentType );
			}

			// If there is a type...

			if ( componentType != null ) {
				// ...iterate over it and add columns

				Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
				NodeList elements = root.getFirstChild().getChildNodes();
				addColumnTags( tableTag, attributes, elements, metawidgetTag );

			} else {
				// If there is no type, DisplayTag will make a best guess
			}

			return tableTag;
		} catch ( Exception e ) {
			throw WidgetBuilderException.newException( e );
		}
	}

	//
	// Protected methods
	//

	protected void addColumnTags( TableTag tableTag, Map<String, String> attributes, NodeList elements, MetawidgetTag metawidgetTag ) {

		// For each property...

		int columnsAdded = 0;

		for ( int loop = 0, length = elements.getLength(); loop < length; loop++ ) {
			Node node = elements.item( loop );

			if ( !( node instanceof Element ) ) {
				continue;
			}

			Element element = (Element) node;

			// ...that is visible...

			if ( TRUE.equals( element.getAttribute( HIDDEN ) ) ) {
				continue;
			}

			// ...add a column...

			if ( addColumnTag( tableTag, attributes, XmlUtils.getAttributesAsMap( element ), metawidgetTag ) ) {
				columnsAdded++;
			}

			// ...up to a sensible maximum

			if ( columnsAdded == 5 ) {
				break;
			}
		}
	}

	/**
	 * Add a ColumnTag for the given attributes, to the given TableTag
	 * <p>
	 * Clients can override this method to modify the column contents. For example, to place a link
	 * around the text.
	 *
	 * @param tableAttributes
	 *            the metadata attributes used to render the parent table. May be useful for
	 *            determining the overall type of the row
	 * @return true if a column was added, false otherwise
	 */

	protected boolean addColumnTag( TableTag tableTag, Map<String, String> tableAttributes, Map<String, String> columnAttributes, MetawidgetTag metawidgetTag ) {

		ColumnTag columnTag = new ColumnTag();
		columnTag.setTitle( metawidgetTag.getLabelString( columnAttributes ) );
		columnTag.setProperty( columnAttributes.get( NAME ) );

		JspUtils.addDeferredChild( tableTag, columnTag );
		return true;
	}
}
