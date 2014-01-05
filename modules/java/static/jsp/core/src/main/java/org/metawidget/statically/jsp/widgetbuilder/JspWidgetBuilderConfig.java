// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

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
