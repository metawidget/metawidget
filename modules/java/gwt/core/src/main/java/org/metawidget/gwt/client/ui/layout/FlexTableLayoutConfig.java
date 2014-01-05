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

package org.metawidget.gwt.client.ui.layout;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a FlexTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FlexTableLayoutConfig {

	//
	// Private members
	//

	private int			mNumberOfColumns	= 1;

	private String		mTableStyleName;

	private String[]	mColumnStyleNames;

	private String		mFooterStyleName;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setNumberOfColumns( int numberOfColumns ) {

		if ( numberOfColumns < 0 ) {
			throw LayoutException.newException( "numberOfColumns must be >= 0" );
		}

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setTableStyleName( String tableStyleName ) {

		mTableStyleName = tableStyleName;

		return this;
	}

	/**
	 * Array of CSS style classes to apply to table columns in order of: label column, component
	 * column, required column.
	 *
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setColumnStyleNames( String... columnStyleNames ) {

		mColumnStyleNames = columnStyleNames;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setFooterStyleName( String footerStyleName ) {

		mFooterStyleName = footerStyleName;

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

		if ( mNumberOfColumns != ( (FlexTableLayoutConfig) that ).mNumberOfColumns ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mTableStyleName, ( (FlexTableLayoutConfig) that ).mTableStyleName ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mColumnStyleNames, ( (FlexTableLayoutConfig) that ).mColumnStyleNames ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mFooterStyleName, ( (FlexTableLayoutConfig) that ).mFooterStyleName ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + mNumberOfColumns;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTableStyleName );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mColumnStyleNames );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mFooterStyleName );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getNumberOfColumns() {

		return mNumberOfColumns;
	}

	protected String getTableStyleName() {

		return mTableStyleName;
	}

	protected String[] getColumnStyleNames() {

		return mColumnStyleNames;
	}

	protected String getFooterStyleName() {

		return mFooterStyleName;
	}
}
