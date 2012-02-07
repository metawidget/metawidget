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

    //
    // Public methods
    //

    /**
     * @return this, as part of a fluent interface
     */

    public HtmlTableLayoutConfig setTableStyle( String tableStyle ) {

        mTableStyle = tableStyle;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public HtmlTableLayoutConfig setTableStyleClass( String tableClass ) {

        mTableStyleClass = tableClass;

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

        return true;
    }

    @Override
    public int hashCode() {

        int hashCode = 1;
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTableStyle );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTableStyleClass );

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
}
