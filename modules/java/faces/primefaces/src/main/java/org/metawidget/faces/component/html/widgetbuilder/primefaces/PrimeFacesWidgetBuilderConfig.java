// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.faces.component.html.widgetbuilder.primefaces;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures an PrimeFacesWidgetBuilder prior to use. Once instantiated, WidgetBuilders are immutable.
 *
 * @author DanilAREFY
 */

public class PrimeFacesWidgetBuilderConfig {

    //
    // Private members
    //

    private String  mDataTableStyleClass;

    private String  mDataTableRowStyleClass;

    private String  mDataTableTableStyleClass;

    private int     mMaximumColumnsInDataTable  =   5;

    //
    // Public methods
    //

    /**
     * @return this, as part of a fluent interface
     */

    public PrimeFacesWidgetBuilderConfig setDataTableStyleClass( String dataTableStyleClass ) {

        mDataTableStyleClass = dataTableStyleClass;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public PrimeFacesWidgetBuilderConfig setDataTableRowStyleClass( String dataTableRowStyleClass ) {
        this.mDataTableRowStyleClass = dataTableRowStyleClass;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public PrimeFacesWidgetBuilderConfig setDataTableTableStyleClass( String dataTableTableStyleClass ) {
        this.mDataTableTableStyleClass = dataTableTableStyleClass;

        return this;
    }

    /**
     * Sets the maximum number of columns to display in a generated data table.
     * <p>
     * By default, Metawidget generates data tables for collections based on their properties.
     * However data tables can become unwieldy if they have too many columns.
     *
     * @param maximumColumnsInDataTable
     *            the maximum number of columns in a data table, or 0 for unlimited
     * @return this, as part of a fluent interface
     */

    public PrimeFacesWidgetBuilderConfig setMaximumColumnsInDataTable( int maximumColumnsInDataTable ) {
        this.mMaximumColumnsInDataTable = maximumColumnsInDataTable;

        return this;
    }

    @Override
    public boolean equals( Object that ) {

        if ( this == that ) {
            return true;
        }

        if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mDataTableStyleClass, ( (PrimeFacesWidgetBuilderConfig) that ).mDataTableStyleClass ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mDataTableRowStyleClass, ( (PrimeFacesWidgetBuilderConfig) that ).mDataTableRowStyleClass ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mDataTableTableStyleClass, ( (PrimeFacesWidgetBuilderConfig) that ).mDataTableTableStyleClass ) ) {
            return false;
        }

        if ( mMaximumColumnsInDataTable != ( (PrimeFacesWidgetBuilderConfig) that ).mMaximumColumnsInDataTable ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int hashCode = 1;
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDataTableStyleClass );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDataTableRowStyleClass );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDataTableTableStyleClass );
        hashCode = 31 * hashCode + mMaximumColumnsInDataTable;

        return hashCode;
    }

    //
    // Protected methods
    //

    protected String getDataTableStyleClass() {

        return mDataTableStyleClass;
    }

    protected String getDataTableRowStyleClass() {

        return mDataTableRowStyleClass;
    }

    protected String getDataTableTableStyleClass() {

        return mDataTableTableStyleClass;
    }

    protected int getMaximumColumnsInDataTable() {

        return mMaximumColumnsInDataTable;
    }
}
