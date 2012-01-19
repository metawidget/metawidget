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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlLabel;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTable;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableBody;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableCell;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableRow;
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
    
    private static final String TABLE_PREFIX                        = "table-";
    
    //
    // Private members
    //
    
    private String mTableStyle;
    
    private String mTableStyleClass;
    
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
		    
		    String id = TABLE_PREFIX + StringUtils.camelCase( metawidget.getPath(), StringUtils.SEPARATOR_DOT_CHAR );
		    table.putAttribute( "id", id);
		    
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
			HtmlTableCell labelCell = new HtmlTableCell();
			HtmlTableCell cell = new HtmlTableCell();
			HtmlTableCell requiredCell = new HtmlTableCell();

			// Label
			
			HtmlLabel label = new HtmlLabel();
			String id = getWidgetId( widget );
			
			if ( id != null ) {
			    label.putAttribute( "for", id );
			}
			
			String labelText = metawidget.getLabelString( attributes );
			label.setTextContent( labelText );
			labelCell.getChildren().add( label );
			row.getChildren().add( labelCell );
			
			// Add widget to layout
			
			cell.getChildren().add( widget );
			row.getChildren().add( cell );
			
			// Indicate whether the field is required or not.
			
			if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
			    requiredCell.setTextContent( "Required" );
			}
			else {
			    requiredCell.setTextContent( "Not Required" );
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
	// Private methods
	//
	
	/**
	 * Gets the id attribute of the given widget, recursing into child widgets if necessary.
	 */
	
    private String getWidgetId(StaticXmlWidget widget) {
        
        String id = widget.getAttribute( "id" );
        
        if ( id != null ) {
            return id;
        }
        
        for( StaticWidget child : widget.getChildren() ) {
            
            id = getWidgetId( (StaticXmlWidget ) child );
            
            if ( id != null ) {
                return id;
            }
        }

        return null;
    }
}
