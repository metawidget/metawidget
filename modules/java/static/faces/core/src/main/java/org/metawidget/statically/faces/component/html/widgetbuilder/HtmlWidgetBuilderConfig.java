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

package org.metawidget.statically.faces.component.html.widgetbuilder;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a HtmlWidgetBuilder prior to use. Once instantiated, WidgetBuilders are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlWidgetBuilderConfig {

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

	public HtmlWidgetBuilderConfig setMaximumColumnsInDataTable( int maximumColumnsInDataTable ) {

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

		if ( mMaximumColumnsInDataTable != ( (HtmlWidgetBuilderConfig) that ).mMaximumColumnsInDataTable ) {
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
