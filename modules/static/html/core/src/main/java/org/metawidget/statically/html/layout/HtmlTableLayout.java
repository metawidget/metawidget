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

package org.metawidget.statically.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.widgetbuilder.HtmlLabel;
import org.metawidget.statically.html.widgetbuilder.HtmlTable;
import org.metawidget.statically.html.widgetbuilder.HtmlTableBody;
import org.metawidget.statically.html.widgetbuilder.HtmlTableCell;
import org.metawidget.statically.html.widgetbuilder.HtmlTableHeader;
import org.metawidget.statically.html.widgetbuilder.HtmlTableRow;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets using an HTML table.
 *
 * @author Richard Kennard
 * @author Ryan Bradley
 */

public class HtmlTableLayout
	implements AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private statics
	//

	private static final String	TABLE_PREFIX	= "table-";

	//
	// Private members
	//

	private String				mTableStyle;

	private String				mTableStyleClass;

	//
	// Constructor
	//

	public HtmlTableLayout() {

		this( new HtmlTableLayoutConfig() );
	}

	//
	// Public methods
	//

	public HtmlTableLayout( HtmlTableLayoutConfig config ) {

		mTableStyle = config.getTableStyle();
		mTableStyleClass = config.getTableStyleClass();
	}

	public void onStartBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
			HtmlTable table = new HtmlTable();

			// Id

			if ( metawidget.getPath() != null ) {
				String path = metawidget.getPath();
				path = path.replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );
				String id = TABLE_PREFIX + StringUtils.camelCase( path, StringUtils.SEPARATOR_DOT_CHAR );
				table.putAttribute( "id", id );
			}

			// Styles

			if ( mTableStyle != null ) {
				table.putAttribute( "style", mTableStyle );
			}

			if ( mTableStyleClass != null ) {
				table.putAttribute( "class", mTableStyleClass );
			}

			table.getChildren().add( new HtmlTableBody() );

			container.getChildren().add( table );
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void layoutWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
			// Ignore stubs

			if ( widget instanceof StaticXmlStub ) {
				return;
			}

			HtmlTableBody body = (HtmlTableBody) container.getChildren().get( 0 ).getChildren().get( 0 );
			HtmlTableRow row = new HtmlTableRow();

			// Label

			layoutLabel( row, widget, elementName, attributes, metawidget );

			// Add widget to layout

			HtmlTableCell cell = new HtmlTableCell();
			cell.getChildren().add( widget );
			row.getChildren().add( cell );

			// Indicate whether the field is required or not.

			HtmlTableCell requiredCell = new HtmlTableCell();

			if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
				requiredCell.setTextContent( "*" );
			}

			row.getChildren().add( requiredCell );
			body.getChildren().add( row );

		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void endContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	//
	// Protected methods
	//

	/**
	 * @param elementName
	 *            can be useful if the Layout needs to call a WidgetProcessor
	 * @return whether a label was written
	 */

	protected boolean layoutLabel( HtmlTableRow row, StaticXmlWidget widgetNeedingLabel, String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		HtmlLabel label = new HtmlLabel();
		String id = getWidgetId( widgetNeedingLabel );

		if ( id != null ) {
			label.putAttribute( "for", id );
		}

		String labelText = metawidget.getLabelString( attributes );
		label.setTextContent( labelText );

		HtmlTableHeader labelCell = new HtmlTableHeader();
		labelCell.getChildren().add( label );
		row.getChildren().add( labelCell );

		return true;
	}

	/**
	 * Gets the id attribute of the given widget, recursing into child widgets if necessary.
	 */

	protected String getWidgetId( StaticXmlWidget widget ) {

		String id = widget.getAttribute( "id" );

		if ( id != null ) {
			return id;
		}

		for ( StaticWidget child : widget.getChildren() ) {

			id = getWidgetId( (StaticXmlWidget) child );

			if ( id != null ) {
				return id;
			}
		}

		return null;
	}
}
