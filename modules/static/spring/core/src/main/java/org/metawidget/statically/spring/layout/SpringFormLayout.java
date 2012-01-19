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

package org.metawidget.statically.spring.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTable;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableBody;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableCell;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableRow;
import org.metawidget.statically.spring.widgetbuilder.FormLabelTag;

/**
 * Layout to arrange widgets using a Spring <form:form> tag and a plain JSP table.
 * 
 * @author Ryan Bradley
 */

public class SpringFormLayout
    implements AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {
    
    //
    // Private statics
    //
    
    private static final String TABLE_PREFIX = "table-";
    
    //
    // Private members
    //
    
    private String mTableStyle;
    
    private String mTableStyleClass;
    
    //
    // Constructors
    //
    
    public SpringFormLayout() {
        
        this( new SpringFormLayoutConfig() );
    }

    public SpringFormLayout( SpringFormLayoutConfig config ) {

        mTableStyle = config.getTableStyle();
        mTableStyleClass = config.getTableStyleClass();
    }
    
    
    //
    // Public methods
    //

    public void onStartBuild( StaticXmlMetawidget metawidget ) {

        // Do nothing
    }

    public void startContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

        try {
            
            HtmlTable table = new HtmlTable();
            table.getChildren().add( new HtmlTableBody() );
            
            if ( mTableStyle != null ) {
                table.putAttribute( "style", mTableStyle );
            }
            
            if ( mTableStyleClass != null ) {
                table.putAttribute( "class", mTableStyleClass );
            }
            
            table.putAttribute( "id", TABLE_PREFIX + metawidget.getPath() );
                        
            container.getChildren().add( table );
        } catch (Exception e) {
            throw LayoutException.newException( e );
        }
    }

    public void layoutWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

        try {
            
            // Ignore stubs
            
            if ( widget instanceof StaticXmlStub ) {
                return;
            }
            
            HtmlTableBody tableBody = (HtmlTableBody) container.getChildren().get( 0 ).getChildren().get( 0 );
            
            HtmlTableRow row = new HtmlTableRow();
            HtmlTableCell labelCell = new HtmlTableCell();
            
            // Add a <form:label>
            
            FormLabelTag label = new FormLabelTag();
            String id = getWidgetId( widget );
            
            if ( id != null ) {
                label.putAttribute( "for", id );
            }
            
            label.putAttribute( "path", widget.getAttribute( "path" ) );            
            String labelText = metawidget.getLabelString( attributes );
            label.setTextContent( labelText );
            
            labelCell.getChildren().add( label );
            row.getChildren().add( labelCell );
            
            // Add the field
            
            HtmlTableCell cell = new HtmlTableCell();
            cell.getChildren().add( widget );
            row.getChildren().add( cell );
            
            // Indicate if the field is required or not.
            
            HtmlTableCell requiredCell = new HtmlTableCell();
            
            if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
                requiredCell.setTextContent( "Required" );
            }
            else {
                requiredCell.setTextContent( "Not Required" );
            }
            
            row.getChildren().add( requiredCell );
            
            tableBody.getChildren().add( row );
            
        } catch (Exception e) {
            throw LayoutException.newException( e );
        }
        
    }

    public void endContainerLayout(StaticXmlWidget container,
            StaticXmlMetawidget metawidget) {

        // Do nothing
    }

    public void onEndBuild(StaticXmlMetawidget metawidget) {

        // Do nothing
    }
    
    //
    // Private methods
    //
    
    private String getWidgetId(StaticXmlWidget widget) {
        
        String id = widget.getAttribute( "id" );

        if ( id != null ) {
            return id;
        }
        
        for( StaticWidget child : widget.getChildren() ) {
            
            id = ( ( StaticXmlWidget ) child ).getAttribute( "id" );
            
            if ( id != null ) {
                return id;
            }
        }
        
        return null;
    }
}
