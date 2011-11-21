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

					String componentType;

					if ( clazz.isArray() ) {
						componentType = clazz.getComponentType().getName();
					} else {
						componentType = attributes.get( PARAMETERIZED_TYPE );
					}

					String inspectedType = metawidgetTag.inspect( null, componentType, (String[]) null );

					// If there is a type...

					if ( componentType != null ) {
						// ...iterate over it...

						Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
						NodeList elements = root.getFirstChild().getChildNodes();

						// ...and for each property...

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

							addColumnTag( tableTag, componentType, XmlUtils.getAttributesAsMap( element ), metawidgetTag );
						}
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

	/**
	 * Add a ColumnTag for the given attributes, to the given TableTag
	 * <p>
	 * Clients can override this method to modify the column contents. For example, to place a link
	 * around the text.
	 *
	 * @param dataType
	 *            the fully qualified type of the data in the collection. Can be useful for
	 *            determining what controller to link to if placing a link around the text. May be
	 *            null
	 */

	protected void addColumnTag( TableTag tableTag, String dataType, Map<String, String> attributes, MetawidgetTag metawidgetTag )
		throws JspException {

		ColumnTag columnTag = new ColumnTag();
		columnTag.setTitle( metawidgetTag.getLabelString( attributes ) );
		columnTag.setProperty( attributes.get( NAME ) );

		JspUtils.writeTag( metawidgetTag.getPageContext(), columnTag, tableTag, null );
	}
}
