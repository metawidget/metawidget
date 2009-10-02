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

package org.metawidget.jsp.tagext.html.layout;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a HtmlTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class HtmlTableLayoutConfig
{
	//
	// Private members
	//

	private int			mNumberOfColumns	= 1;

	private String		mTableStyle;

	private String		mTableStyleClass;

	private String[]	mColumnStyleClasses;

	private String		mSectionStyleClass;

	private String		mFooterStyle;

	private String		mFooterStyleClass;

	//
	// Public methods
	//

	public int getNumberOfColumns()
	{
		return mNumberOfColumns;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setNumberOfColumns( int numberOfColumns )
	{
		if ( numberOfColumns < 0 )
			throw LayoutException.newException( "numberOfColumns must be >= 0" );

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	public String getTableStyle()
	{
		return mTableStyle;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setTableStyle( String tableStyle )
	{
		mTableStyle = tableStyle;

		return this;
	}

	public String getTableStyleClass()
	{
		return mTableStyleClass;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setTableStyleClass( String tableStyleClass )
	{
		mTableStyleClass = tableStyleClass;

		return this;
	}

	public String[] getColumnStyleClasses()
	{
		return mColumnStyleClasses;
	}

	/**
	 * Array of CSS style classes to apply to table columns in order of: label column, component column, required column.
	 *
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setColumnStyleClasses( String... columnStyleClasses )
	{
		mColumnStyleClasses = columnStyleClasses;

		return this;
	}

	public String getSectionStyleClass()
	{
		return mSectionStyleClass;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setSectionStyleClass( String sectionStyleClass )
	{
		mSectionStyleClass = sectionStyleClass;

		return this;
	}

	public String getFooterStyle()
	{
		return mFooterStyle;
	}

	public void setFooterStyle( String footerStyle )
	{
		mFooterStyle = footerStyle;
	}

	public String getFooterStyleClass()
	{
		return mFooterStyleClass;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlTableLayoutConfig setFooterStyleClass( String footerStyleClass )
	{
		mFooterStyleClass = footerStyleClass;

		return this;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof HtmlTableLayoutConfig ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mNumberOfColumns, ((HtmlTableLayoutConfig) that).mNumberOfColumns ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mTableStyle, ((HtmlTableLayoutConfig) that).mTableStyle ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mTableStyleClass, ((HtmlTableLayoutConfig) that).mTableStyleClass ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mColumnStyleClasses, ((HtmlTableLayoutConfig) that).mColumnStyleClasses ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mSectionStyleClass, ((HtmlTableLayoutConfig) that).mSectionStyleClass ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mFooterStyle, ((HtmlTableLayoutConfig) that).mFooterStyle ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mFooterStyleClass, ((HtmlTableLayoutConfig) that).mFooterStyleClass ))
			return false;

		return super.equals( that );
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode ^= ObjectUtils.nullSafeHashCode( mNumberOfColumns );
		hashCode ^= ObjectUtils.nullSafeHashCode( mTableStyle );
		hashCode ^= ObjectUtils.nullSafeHashCode( mTableStyleClass );
		hashCode ^= ObjectUtils.nullSafeHashCode( mColumnStyleClasses );
		hashCode ^= ObjectUtils.nullSafeHashCode( mSectionStyleClass );
		hashCode ^= ObjectUtils.nullSafeHashCode( mFooterStyle );
		hashCode ^= ObjectUtils.nullSafeHashCode( mFooterStyleClass );

		return hashCode;
	}
}
