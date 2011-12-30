package org.metawidget.statically.jsp.html.widgetbuilder;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a HtmlWidgetBuilder prior to use. Once instantiated, WidgetBuilders are immutable.
 *
 * @author Ryan Bradley
 */

public class HtmlWidgetBuilderConfig {

    //
    // Private members
    //
    
    private int mMaximumRowsInDataTable = 100;
    
    //
    // Public methods
    //
    
    /**
     * Sets the maximum number of columns to display in a generated data table.
     * <p>
     * By default, Metawidget generates data tables for collections based on their properties.
     * However data tables can become unwieldy if they have too many columns.
     *
     * @param maximumRowsInDataTable
     *            the maximum number of columns in a data table, or 0 for unlimited
     * @return this, as part of a fluent interface
     */
    
    public HtmlWidgetBuilderConfig setMaximumRowsInDataTable( int maximumRowsInDataTable ) {
        
        mMaximumRowsInDataTable = maximumRowsInDataTable;
        
        return this;
    }

    @Override
    public boolean equals(Object that) {
        
        if ( this == that ) {
            return true;
        }
        
        if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
            return false;
        }
        
        if ( mMaximumRowsInDataTable != ( ( HtmlWidgetBuilderConfig ) that ).mMaximumRowsInDataTable ) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        
        int hashCode = 1;
        hashCode = 31 * hashCode + mMaximumRowsInDataTable;
        
        return hashCode;
    }
    
    //
    // Protected methods
    //
    
    protected int getMaximumRowsInDataTable() {
        
        return mMaximumRowsInDataTable;
    }
    
}
