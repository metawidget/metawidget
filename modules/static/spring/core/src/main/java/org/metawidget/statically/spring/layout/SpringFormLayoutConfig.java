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

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a SpringFormLayout prior to use.  Once instantiated, Layouts are immutable.
 *
 * @author Ryan Bradley
 */

public class SpringFormLayoutConfig {

    //
    // Private members
    //
    
    private String mTableStyleClass;

    private String mTableStyle;
    
    //
    // Public methods
    //
    
    public SpringFormLayoutConfig setTableStyle(String tableStyle) {
        
        this.mTableStyle = tableStyle;
        
        return this;
    }      

    public SpringFormLayoutConfig setTableStyleClass( String tableStyleClass ) {
        
        mTableStyleClass = tableStyleClass;
        
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

        if ( !ObjectUtils.nullSafeEquals( mTableStyle, ( (SpringFormLayoutConfig) that ).mTableStyle ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mTableStyleClass, ( (SpringFormLayoutConfig) that ).mTableStyleClass ) ) {
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

    protected String getTableStyleClass() {
        return mTableStyleClass;
    }

    protected String getTableStyle() {
        return mTableStyle;
    }    
}
