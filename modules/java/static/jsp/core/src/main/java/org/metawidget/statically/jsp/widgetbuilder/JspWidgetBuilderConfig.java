// Metawidget (licensed under LGPL)
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

package org.metawidget.statically.jsp.widgetbuilder;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a JspWidgetBuilder prior to use. Once instantiated, WidgetBuilders are immutable.
 *
 * @author Ryan Bradley
 */

public class JspWidgetBuilderConfig {

	//
	// Private members
	//

	private int	mMaximumColumnsInDataTable	= 5;

	//
	// Public methods
	//

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

	public JspWidgetBuilderConfig setMaximumColumnsInDataTable( int maximumColumnsInDataTable ) {

		mMaximumColumnsInDataTable = maximumColumnsInDataTable;

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

		if ( mMaximumColumnsInDataTable != ( (JspWidgetBuilderConfig) that ).mMaximumColumnsInDataTable ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + mMaximumColumnsInDataTable;

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getMaximumColumnsInDataTable() {

		return mMaximumColumnsInDataTable;
	}
}
