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

package org.metawidget.jsp.tagext.html.layout;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a HtmlTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlTableLayoutConfig {

	//
	// Private members
	//

	private int			mNumberOfColumns	= 1;

	private String		mTableStyle;

	private String		mTableStyleClass;

	private String[]	mColumnStyleClasses;

	private String		mFooterStyle;

	private String		mFooterStyleClass;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setNumberOfColumns( int numberOfColumns ) {

		if ( numberOfColumns < 0 ) {
			throw LayoutException.newException( "numberOfColumns must be >= 0" );
		}

		mNumberOfColumns = numberOfColumns;

		return this;
	}

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

	public HtmlTableLayoutConfig setTableStyleClass( String tableStyleClass ) {

		mTableStyleClass = tableStyleClass;

		return this;
	}

	/**
	 * Array of CSS style classes to apply to table columns in order of: label column, component
	 * column, required column.
	 *
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setColumnStyleClasses( String... columnStyleClasses ) {

		mColumnStyleClasses = columnStyleClasses;

		return this;
	}

	public void setFooterStyle( String footerStyle ) {

		mFooterStyle = footerStyle;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setFooterStyleClass( String footerStyleClass ) {

		mFooterStyleClass = footerStyleClass;

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

		if ( mNumberOfColumns != ( (HtmlTableLayoutConfig) that ).mNumberOfColumns ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mTableStyle, ( (HtmlTableLayoutConfig) that ).mTableStyle ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mTableStyleClass, ( (HtmlTableLayoutConfig) that ).mTableStyleClass ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mColumnStyleClasses, ( (HtmlTableLayoutConfig) that ).mColumnStyleClasses ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mFooterStyle, ( (HtmlTableLayoutConfig) that ).mFooterStyle ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mFooterStyleClass, ( (HtmlTableLayoutConfig) that ).mFooterStyleClass ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + mNumberOfColumns;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTableStyle );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTableStyleClass );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mColumnStyleClasses );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mFooterStyle );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mFooterStyleClass );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getNumberOfColumns() {

		return mNumberOfColumns;
	}

	protected String getTableStyle() {

		return mTableStyle;
	}

	protected String getTableStyleClass() {

		return mTableStyleClass;
	}

	protected String[] getColumnStyleClasses() {

		return mColumnStyleClasses;
	}

	protected String getFooterStyle() {

		return mFooterStyle;
	}

	protected String getFooterStyleClass() {

		return mFooterStyleClass;
	}
}
