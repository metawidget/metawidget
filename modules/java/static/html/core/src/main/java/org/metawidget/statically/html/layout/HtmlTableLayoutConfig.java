// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.html.layout;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures an HtmlTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Ryan Bradley
 */

public class HtmlTableLayoutConfig {

    //
    // Private members
    //

    private String mTableStyle;

    private String mTableStyleClass;

    private String mLabelColumnStyleClass;

    private String mComponentColumnStyleClass;

    private String mRequiredColumnStyleClass;

    //
    // Public methods
    //

    /**
     * CSS style to apply to table.
     * 
     * @return this, as part of a fluent interface
     */

    public HtmlTableLayoutConfig setTableStyle( String tableStyle ) {

        mTableStyle = tableStyle;

        return this;
    }

    /**
     * CSS style class to apply to table.
     * 
     * @return this, as part of a fluent interface
     */

    public HtmlTableLayoutConfig setTableStyleClass( String tableClass ) {

        mTableStyleClass = tableClass;

        return this;
    }

    /**
     * CSS style class to apply to the label table column.
     * 
     * @return this, as part of a fluent interface
     */

    public HtmlTableLayoutConfig setLabelColumnStyleClass( String labelColumnStyleClass ) {

        mLabelColumnStyleClass = labelColumnStyleClass;

        return this;
    }

    /**
     * CSS style class to apply to the component table column.
     * 
     * @return this, as part of a fluent interface
     */

    public HtmlTableLayoutConfig setComponentColumnStyleClass( String componentColumnStyleClass ) {

        mComponentColumnStyleClass = componentColumnStyleClass;

        return this;
    }

    /**
     * CSS style class to apply to the required table column.
     * 
     * @return this, as part of a fluent interface
     */

    public HtmlTableLayoutConfig setRequiredColumnStyleClass( String requiredColumnStyleClass ) {

        mRequiredColumnStyleClass = requiredColumnStyleClass;

        return this;
    }

    @Override
    public boolean equals( Object that ) {

        if ( this == that ) {
            return true;
        }

        if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mTableStyle, ( (HtmlTableLayoutConfig) that ).mTableStyle ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mTableStyleClass, ( (HtmlTableLayoutConfig) that ).mTableStyleClass ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mLabelColumnStyleClass, ( (HtmlTableLayoutConfig) that ).mLabelColumnStyleClass ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mComponentColumnStyleClass,  ( (HtmlTableLayoutConfig) that ).mComponentColumnStyleClass ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mRequiredColumnStyleClass, ( (HtmlTableLayoutConfig) that ).mRequiredColumnStyleClass ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int hashCode = 1;
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTableStyle );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTableStyleClass );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelColumnStyleClass );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mComponentColumnStyleClass );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRequiredColumnStyleClass );

        return hashCode;
    }

    //
    // Protected methods
    //

    protected String getTableStyle() {
        return mTableStyle;
    }

    protected String getTableStyleClass() {
        return mTableStyleClass;
    }

    protected String getLabelColumnStyleClass() {
        return mLabelColumnStyleClass;
    }

    protected String getComponentColumnStyleClass() {
        return mComponentColumnStyleClass;
    }

    protected String getRequiredColumnStyleClass() {
        return mRequiredColumnStyleClass;
    }
}
