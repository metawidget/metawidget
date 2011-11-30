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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.displaytag.tags.ColumnTag;
import org.displaytag.tags.TableTag;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.JspUtils.BodyPreparer;
import org.metawidget.jsp.tagext.LiteralTag;
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

	public Tag buildWidget( String elementName, final Map<String, String> attributes, final MetawidgetTag metawidgetTag ) {

		try {
			// Not for us?

			if ( TRUE.equals( attributes.get( HIDDEN ) ) || attributes.containsKey( LOOKUP ) ) {
				return null;
			}

			String type = attributes.get( TYPE );

			if ( type == null || "".equals( type ) ) {
				return null;
			}

			final Class<?> clazz = ClassUtils.niceForName( type );

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

			final TableTag tableTag = new TableTag();
			tableTag.setName( toDisplay );

			// Write the DisplayTag

			String literal = JspUtils.writeTag( metawidgetTag.getPageContext(), tableTag, metawidgetTag, new BodyPreparer() {

				// After DisplayTag.doStartTag, can add columns

				public void prepareBody( PageContext delgateContext )
					throws JspException {

					// Inspect component type

					String componentType = WidgetBuilderUtils.getComponentType( attributes );
					String inspectedType = null;

					if ( componentType != null ) {
						inspectedType = metawidgetTag.inspect( null, componentType, (String[]) null );
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
				}
			} );

			return new LiteralTag( literal );
		} catch ( Exception e ) {
			throw WidgetBuilderException.newException( e );
		}
	}

	//
	// Protected methods
	//

	protected void addColumnTags( TableTag tableTag, Map<String, String> attributes, NodeList elements, MetawidgetTag metawidgetTag )
		throws JspException {

		// For each property...

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

			// ...add a column

			addColumnTag( tableTag, attributes, XmlUtils.getAttributesAsMap( element ), metawidgetTag );
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
	 */

	protected void addColumnTag( TableTag tableTag, Map<String, String> tableAttributes, Map<String, String> columnAttributes, MetawidgetTag metawidgetTag )
		throws JspException {

		ColumnTag columnTag = new ColumnTag();
		columnTag.setTitle( metawidgetTag.getLabelString( columnAttributes ) );
		columnTag.setProperty( columnAttributes.get( NAME ) );

		JspUtils.writeTag( metawidgetTag.getPageContext(), columnTag, tableTag, null );
	}
}
