// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.jsp.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.widgetbuilder.HtmlOption;
import org.metawidget.statically.html.widgetbuilder.HtmlSelect;
import org.metawidget.statically.html.widgetbuilder.HtmlTable;
import org.metawidget.statically.html.widgetbuilder.HtmlTableBody;
import org.metawidget.statically.html.widgetbuilder.HtmlTableCell;
import org.metawidget.statically.html.widgetbuilder.HtmlTableHead;
import org.metawidget.statically.html.widgetbuilder.HtmlTableHeader;
import org.metawidget.statically.html.widgetbuilder.HtmlTableRow;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.jsp.StaticJspUtils;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 * @author Ryan Bradley
 */

public class JspWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private members
	//

	private final int			mMaximumColumnsInDataTable;

	//
	// Constructor
	//

	public JspWidgetBuilder() {

		this( new JspWidgetBuilderConfig() );
	}

	public JspWidgetBuilder( JspWidgetBuilderConfig config ) {

		mMaximumColumnsInDataTable = config.getMaximumColumnsInDataTable();
	}

	//
	// Public methods
	//

	public StaticXmlWidget buildWidget( String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return null;
		}

		// JSP Lookup

		String jspLookup = attributes.get( JSP_LOOKUP );

		if ( jspLookup != null && !"".equals( jspLookup ) ) {

			HtmlSelect select = new HtmlSelect();
			addSelectItems( select, jspLookup, attributes );
			return select;
		}

		// Lookup the Class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

		if ( clazz != null ) {

			// Support Collections and Arrays (c:forEach can handle either)

			if ( Collection.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
				return createDataTableComponent( elementName, attributes, metawidget );
			}
		}

		// Not for us

		return null;
	}

	//
	// Protected methods
	//

	/**
	 * @param elementName
	 *            such as ENTITY or PROPERTY. Can be useful in determining how to construct the EL
	 *            for the table.
	 */

	protected StaticXmlWidget createDataTableComponent( String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		HtmlTable table = new HtmlTable();
		CoreForEach forEach = new CoreForEach();

		String items = attributes.get( NAME );

		if ( items != null ) {

			items = StaticJspUtils.wrapExpression( items );
		}

		forEach.putAttribute( "items", items );
		String var = "item";
		forEach.putAttribute( "var", var );

		// Add a section for table headers.

		HtmlTableHead tableHead = new HtmlTableHead();
		table.getChildren().add( tableHead );
		tableHead.getChildren().add( new HtmlTableRow() );

		HtmlTableBody body = new HtmlTableBody();
		body.getChildren().add( forEach );
		table.getChildren().add( body );

		// Inspect the component type.

		String componentType = WidgetBuilderUtils.getComponentType( attributes );
		String inspectedType = null;

		if ( componentType != null ) {
			inspectedType = metawidget.inspect( null, componentType, (String[]) null );
		}

		// If there is no type...

		if ( inspectedType == null ) {
			// ...resort to a single column table...

			HtmlTableRow row = new HtmlTableRow();
			forEach.getChildren().add( row );

			Map<String, String> columnAttributes = CollectionUtils.newHashMap();
			columnAttributes.put( NAME, attributes.get( NAME ) );
			addColumnComponent( row, forEach, attributes, ENTITY, columnAttributes, metawidget );
		}

		// ...otherwise, iterate over the component type and add multiple columns.

		else {
			Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
			NodeList elements = root.getFirstChild().getChildNodes();
			addColumnComponents( table, forEach, attributes, elements, metawidget );
		}

		return table;
	}

	/**
	 * Adds column components to the given table.
	 * <p>
	 * Clients can override this method to add additional columns, such as a 'Delete' button.
	 */

	protected void addColumnComponents( HtmlTable table, CoreForEach forEach, Map<String, String> attributes, NodeList elements, StaticXmlMetawidget metawidget ) {

		// At first, only add columns for the 'required' fields

		boolean onlyRequired = true;

		while ( true ) {

			// Create a new row for the forEach tag to iterate upon.

			HtmlTableRow row = new HtmlTableRow();

			// For each property...

			for ( int i = 0; i < elements.getLength(); i++ ) {

				Node node = elements.item( i );

				if ( !( node instanceof Element ) ) {
					continue;
				}

				Element element = (Element) node;

				// ...(not action)...

				if ( ACTION.equals( element.getNodeName() ) ) {
					continue;
				}

				// ...that is visible...

				if ( TRUE.equals( element.getAttribute( HIDDEN ) ) ) {
					continue;
				}

				// ...and is required...
				//
				// Note: this is a controversial choice. Our logic is that a) we need to limit
				// the number of columns somehow, and b) displaying all the required fields should
				// be enough to uniquely identify the row to the user. However, users may wish
				// to override this default behaviour

				if ( onlyRequired && !TRUE.equals( element.getAttribute( REQUIRED ) ) ) {
					continue;
				}

				// ...add a column...

				addColumnComponent( row, forEach, attributes, PROPERTY, XmlUtils.getAttributesAsMap( element ), metawidget );

				// ...and a header for that column...

				addColumnHeader( table, XmlUtils.getAttributesAsMap( element ), metawidget );

				// ...up to a sensible maximum.

				if ( row.getChildren().size() == mMaximumColumnsInDataTable ) {
					break;
				}

			}

			if ( !row.getChildren().isEmpty() ) {

				forEach.getChildren().add( row );
			}

			// If we couldn't add any 'required' columns, try again for every field.

			if ( !forEach.getChildren().isEmpty() || !onlyRequired ) {
				break;
			}

			onlyRequired = false;
		}
	}

	protected void addColumnHeader( HtmlTable table, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		HtmlTableHeader header = new HtmlTableHeader();
		header.setTextContent( metawidget.getLabelString( attributes ) );

		table.getChildren().get( 0 ).getChildren().get( 0 ).getChildren().add( header );
	}

	/**
	 * Add an HtmlColumn component for the given attributes, to the given HtmlDataTable.
	 * <p>
	 * Clients can override this method to modify the column contents. For example, to place a link
	 * around the text.
	 *
	 * @param tableAttributes
	 *            the metadata attributes used to render the parent table. May be useful for
	 *            determining the overall type of the row
	 */

	protected void addColumnComponent( HtmlTableRow row, CoreForEach forEach, Map<String, String> tableAttributes, String elementName, Map<String, String> columnAttributes, StaticXmlMetawidget metawidget ) {

		// Add a new column to the current row of the table.

		HtmlTableCell cell = new HtmlTableCell();
		row.getChildren().add( cell );

		StaticXmlWidget columnContents;

		// Make the column contents...

		if ( ENTITY.equals( elementName )) {
			columnContents = new CoreOut();
			columnContents.putAttribute( "value", StaticJspUtils.wrapExpression( forEach.getAttribute( "var" ) ) );
		} else {

			// ...using a nested Metawidget if we can

			columnContents = new StaticJspMetawidget();
			String valueExpression = forEach.getAttribute( "var" ) + StringUtils.SEPARATOR_DOT_CHAR + StringUtils.decapitalize( columnAttributes.get( NAME ) );
			columnContents.putAttribute( "value", StaticJspUtils.wrapExpression( valueExpression ) );

			StaticJspMetawidget columnMetawidget = (StaticJspMetawidget) columnContents;
			columnMetawidget.setPath( WidgetBuilderUtils.getComponentType( tableAttributes ) + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + columnAttributes.get( NAME ) );
			metawidget.initNestedMetawidget( columnMetawidget, columnAttributes );
			columnMetawidget.setLayout( new SimpleLayout() );
			columnMetawidget.setReadOnly( true );
		}

		cell.getChildren().add( columnContents );
	}

	protected void addSelectItems( HtmlSelect select, String valueExpression, Map<String, String> attributes ) {

		// Empty option

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			addSelectItem( select, "", null );
		}

		addSelectItem( select, valueExpression, null );
	}

	protected void addSelectItems( HtmlSelect select, List<String> values, List<String> labels, Map<String, String> attributes ) {

		if ( values == null ) {
			return;
		}

		// Empty option.

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			addSelectItem( select, "", null );
		}

		// Add the rest of the select items.

		for ( int i = 0, length = values.size(); i < length; i++ ) {
			String value = values.get( i );
			String label = null;

			if ( labels != null && !labels.isEmpty() ) {
				label = labels.get( i );
			}

			addSelectItem( select, value, label );
		}

		return;
	}

	//
	// Private methods
	//

	private void addSelectItem( HtmlSelect select, String value, String label ) {

		HtmlOption selectItem = new HtmlOption();
		selectItem.putAttribute( "value", value );

		if ( label != null ) {
			selectItem.setTextContent( label );
		}

		select.getChildren().add( selectItem );
		return;
	}

}
