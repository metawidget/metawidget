package org.metawidget.statically.jsp.html.widgetbuilder;

import org.metawidget.statically.jsp.html.component.EditableValueHolder;

/**
 * 
 * @author Ryan Bradley
 *
 */

public class SelectItem extends HtmlTag implements EditableValueHolder {
    
    public SelectItem() {
        super("option");
    }

    public void setValue(String value) {
        putAttribute( "value" , value);        
    }

    public String getValue() {
        return getAttribute( "value" );
    }

    public void setConverter(String converter) {
        putAttribute( "converter" , converter);       
    }
}
