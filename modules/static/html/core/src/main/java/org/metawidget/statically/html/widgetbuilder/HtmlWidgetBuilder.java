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

package org.metawidget.statically.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Color;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetprocessor.IdProcessor;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Richard Kennard
 * @author Ryan Bradley
 */

public class HtmlWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticHtmlMetawidget> {

	//
	// Private members
	//

	private final int			mMaximumColumnsInDataTable;

	//
	// Constructor
	//

	public HtmlWidgetBuilder() {

		this( new HtmlWidgetBuilderConfig() );
	}

	public HtmlWidgetBuilder( HtmlWidgetBuilderConfig config ) {

		mMaximumColumnsInDataTable = config.getMaximumColumnsInDataTable();
	}

	//
	// Public methods
	//

	public StaticXmlWidget buildWidget( String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new StaticXmlStub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new StaticXmlStub();
		}

		// Lookup the Class

		Class<?> clazz;
		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null ) {
			clazz = String.class;
		} else {
			clazz = ClassUtils.niceForName( type );
		}

		// Support mandatory Booleans.

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return createHtmlCheckbox();
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			HtmlSelect select = new HtmlSelect();
			addSelectItems( select, CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes );
			return select;
		}

		if ( clazz != null ) {

			String minimumValue = attributes.get( MINIMUM_VALUE );
			String maximumValue = attributes.get( MAXIMUM_VALUE );

			// Primitives

			if ( clazz.isPrimitive() ) {

				if ( boolean.class.equals( clazz ) ) {
					return createHtmlCheckbox();
				}

				if ( char.class.equals( clazz ) ) {
					attributes.put( MAXIMUM_LENGTH, "1" );
					HtmlInput characterInput = createHtmlInputText( attributes );
					return characterInput;
				}

				HtmlInput inputNumber = createHtmlInputNumber();

				// Ranged

				if ( minimumValue != null ) {
					inputNumber.putAttribute( "min", minimumValue );
				}

				if ( maximumValue != null ) {
					inputNumber.putAttribute( "max", maximumValue );
				}

				return inputNumber;
			}

			// String

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return createHtmlTextareaTag( attributes );
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					HtmlInput secret = createHtmlInputText( attributes );
					secret.putAttribute( "type", "secret" );
					return secret;
				}

				if ( TRUE.equals( attributes.get( "validation-email" ) ) ) {
					HtmlInput email = createHtmlInputText( attributes );
					email.putAttribute( "type", "email" );
					return email;
				}

				HtmlInput text = createHtmlInputText( attributes );
				text.putAttribute( "pattern", attributes.get( VALIDATION_PATTERN ) );
				return text;
			}

			// Character

			if ( Character.class.equals( clazz ) ) {
				attributes.put( MAXIMUM_LENGTH, "1" );
				HtmlInput characterInput = createHtmlInputText( attributes );
				return characterInput;
			}

			// Date

			if ( Date.class.equals( clazz ) ) {
				return createHtmlInputDate();
			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) ) {

				HtmlInput inputNumber = createHtmlInputNumber();

				// Ranged

				if ( minimumValue != null ) {
					inputNumber.putAttribute( "min", minimumValue );
				}

				if ( maximumValue != null ) {
					inputNumber.putAttribute( "max", maximumValue );
				}

				return inputNumber;
			}

			// Color

			if ( Color.class.equals( clazz ) ) {
				return createHtmlInputColor();
			}

			// Collections and Arrays

			if ( Collection.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
				return createDataTableComponent( elementName, attributes, metawidget );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createHtmlInputText( attributes );
		}

		// Not simple

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

	protected StaticXmlWidget createDataTableComponent( String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		HtmlTable table = new HtmlTable();

		// Add a section for table headers.

		HtmlTableHead tableHead = new HtmlTableHead();
		table.getChildren().add( tableHead );
		tableHead.getChildren().add( new HtmlTableRow() );

		HtmlTableBody body = new HtmlTableBody();
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

			Map<String, String> columnAttributes = CollectionUtils.newHashMap();
			columnAttributes.put( NAME, attributes.get( NAME ) );
			addColumnHeader( table, columnAttributes, metawidget );
		}

		// ...otherwise, iterate over the component type and add multiple columns.

		else {

			// Assign an id so that it gets propagated to our children

			IdProcessor processor = metawidget.getWidgetProcessor( IdProcessor.class );

			if ( processor != null ) {
				processor.processWidget( table, elementName, attributes, metawidget );
			}

			Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
			NodeList elements = root.getFirstChild().getChildNodes();
			addColumnComponents( table, elements, metawidget );
		}

		return table;
	}

	/**
	 * Adds column components to the given table.
	 * <p>
	 * Clients can override this method to add additional columns, such as a 'Delete' button.
	 */

	protected void addColumnComponents( HtmlTable table, NodeList elements, StaticXmlMetawidget metawidget ) {

		// At first, only add columns for the 'required' fields

		boolean onlyRequired = true;
		HtmlTableRow headerRow = (HtmlTableRow) table.getChildren().get( 0 ).getChildren().get( 0 );

		while ( true ) {

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

				// ...and a header for that column...

				addColumnHeader( table, XmlUtils.getAttributesAsMap( element ), metawidget );

				// ...up to a sensible maximum.

				if ( headerRow.getChildren().size() == mMaximumColumnsInDataTable ) {
					break;
				}

			}

			// If we couldn't add any 'required' columns, try again for every field.

			if ( !headerRow.getChildren().isEmpty() || !onlyRequired ) {
				break;
			}

			onlyRequired = false;
		}
	}

	protected void addColumnHeader( HtmlTable table, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		HtmlTableHeader header = new HtmlTableHeader();

		String id = attributes.get( NAME );
		String tableId = table.getAttribute( "id" );

		if ( tableId != null ) {
			id = tableId + '-' + id;
		}

		header.putAttribute( "id", id );
		header.setTextContent( metawidget.getLabelString( attributes ) );

		table.getChildren().get( 0 ).getChildren().get( 0 ).getChildren().add( header );
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

	private HtmlInput createHtmlInputText( Map<String, String> attributes ) {

		HtmlInput input = new HtmlInput();
		input.putAttribute( "type", "text" );
		input.putAttribute( "maxlength", attributes.get( MAXIMUM_LENGTH ) );

		return input;
	}

	private StaticXmlWidget createHtmlCheckbox() {

		HtmlTag checkbox = new HtmlInput();
		checkbox.putAttribute( "type", "checkbox" );

		return checkbox;
	}

	/**
	 * Creates an HTML &lt;input type=\"date\"&gt;. Date inputs are only available in HTML 5,
	 * but earlier versions of HTML will degrade to a textbox.
	 */

	private HtmlInput createHtmlInputDate() {

		HtmlInput input = new HtmlInput();
		input.putAttribute( "type", "date" );

		return input;
	}

	/**
	 * Creates an HTML &lt;input type=\"number\"&gt;. Number inputs are only available in HTML 5,
	 * but earlier versions of HTML will degrade to a textbox.
	 */

	private HtmlInput createHtmlInputNumber() {

		HtmlInput input = new HtmlInput();
		input.putAttribute( "type", "number" );

		return input;
	}

	/**
	 * Creates an HTML &lt;input type=\"color\"&gt;. Color inputs are only available in HTML 5,
	 * but earlier versions of HTML will degrade to a textbox.
	 */

	private HtmlInput createHtmlInputColor() {

		HtmlInput input = new HtmlInput();
		input.putAttribute( "type", "color" );

		return input;
	}

	private StaticXmlWidget createHtmlTextareaTag( Map<String, String> attributes ) {

		HtmlTextarea textarea = new HtmlTextarea();

		String cols = attributes.get( "cols" );

		if ( cols != null ) {
			textarea.putAttribute( "cols", cols );
		}

		String rows = attributes.get( "rows" );

		if ( rows != null ) {
			textarea.putAttribute( "rows", rows );
		}

		return textarea;
	}

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
