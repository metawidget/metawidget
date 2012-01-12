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

package org.metawidget.statically.jsp.html.layout;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTable;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableBody;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableCell;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableRow;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableFooter;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets using an HTML table.
 *
 * @author Richard Kennard
 */

public class HtmlTableLayout
	implements AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {
    
    //
    // Private statics
    //
    
    private static final String TABLE_PREFIX                        = "table-";

    private static final String ROW_SUFFIX                          = "-row";

    private static final String CELL_SUFFIX                         = "-cell";

    private static final int    JUST_COMPONENT_AND_REQUIRED         = 2;

    private static final int    LABEL_AND_COMPONENT_AND_REQUIRED    = 3;
    
    //
    // Private members
    //
    
    private int mNumberOfColumns;
    
    private String mTableStyle;
    
    private String mTableStyleClass;
    
    private String[] mColumnStyleClasses;
    
    private String mFooterStyle;
    
    private String mFooterStyleClass;    
    
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

	    mNumberOfColumns = config.getNumberOfColumns();
	    mTableStyle = config.getTableStyle();
	    mTableStyleClass = config.getTableStyleClass();
	    mColumnStyleClasses = config.getColumnStyleClasses();
	    mFooterStyle = config.getFooterStyle();
	    mFooterStyleClass = config.getFooterStyleClass();
    }

    public void onStartBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
		    HtmlTable table = new HtmlTable();
		    
		    // Id
		    String id = TABLE_PREFIX + StringUtils.camelCase( metawidget.getPath(), StringUtils.SEPARATOR_DOT_CHAR );
		    table.putAttribute( "id", id);
		    
		    // Styles
		    
		    if ( mTableStyle != null ) {
		        table.putAttribute( "style", mTableStyle );
		    }
		    
		    if ( mTableStyleClass != null ) {
		        table.putAttribute( "class", mTableStyleClass );
		    }
		    
		    // Add a table footer
		    
		    HtmlTableFooter footer = new HtmlTableFooter();

            // Footer spans multiples of label/component/required
            
            Integer colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, mNumberOfColumns*LABEL_AND_COMPONENT_AND_REQUIRED );
            footer.putAttribute( "colspan", colspan.toString() );
            
            // CSS Styles
            
            if ( mFooterStyle != null ) {
                footer.putAttribute( "style", mFooterStyle );
            }
            
            if ( mFooterStyleClass != null ) {
                footer.putAttribute( "class", mFooterStyleClass );
            }            
		    
		    HtmlTableRow footerRow = new HtmlTableRow();
		    HtmlTableCell footerCell = new HtmlTableCell();
		    footerRow.getChildren().add( footerCell );
            footer.getChildren().add( footerRow );
		    
		    table.getChildren().add( footer );
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

			HtmlTableBody body = (HtmlTableBody) container.getChildren().get( 0 );
			HtmlTableRow row = new HtmlTableRow();
			HtmlTableCell cell = new HtmlTableCell();
						
			cell.getChildren().add( widget );
			row.getChildren().add( cell );
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
}
