package org.metawidget.statically.jsp.html.component;

/**
 * Marks the widget as an editable component.
 * 
 * @author Ryan Bradley
 */

public interface ValueHolder {
    
    //
    // Methods
    //

    void setValue( String value );

    String getValue();

    void setConverter( String converter );
}
