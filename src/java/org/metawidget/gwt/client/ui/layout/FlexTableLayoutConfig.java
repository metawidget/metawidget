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

package org.metawidget.gwt.client.ui.layout;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.ClassUtils;

/**
 * Configures a FlexTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class FlexTableLayoutConfig
{
	//
	// Private members
	//

	private int			mNumberOfColumns	= 1;

	private String		mTableStyleName;

	private String[]	mColumnStyleNames;

	private String		mSectionStyleName;

	private String		mFooterStyleName;

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

	public FlexTableLayoutConfig setNumberOfColumns( int numberOfColumns )
	{
		if ( numberOfColumns < 0 )
			throw LayoutException.newException( "numberOfColumns must be >= 0" );

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	public String getTableStyleName()
	{
		return mTableStyleName;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setTableStyleName( String tableStyleName )
	{
		mTableStyleName = tableStyleName;

		return this;
	}

	public String[] getColumnStyleNames()
	{
		return mColumnStyleNames;
	}

	/**
	 * Array of CSS style classes to apply to table columns in order of: label column, component column, required column.
	 *
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setColumnStyleNames( String... columnStyleNames )
	{
		mColumnStyleNames = columnStyleNames;

		return this;
	}

	public String getSectionStyleName()
	{
		return mSectionStyleName;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setSectionStyleName( String sectionStyleName )
	{
		mSectionStyleName = sectionStyleName;

		return this;
	}

	public String getFooterStyleName()
	{
		return mFooterStyleName;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setFooterStyleName( String footerStyleName )
	{
		mFooterStyleName = footerStyleName;

		return this;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof FlexTableLayoutConfig ))
			return false;

		if ( !ClassUtils.nullSafeEquals( mNumberOfColumns, ((FlexTableLayoutConfig) that).mNumberOfColumns ))
			return false;

		if ( !ClassUtils.nullSafeEquals( mTableStyleName, ((FlexTableLayoutConfig) that).mTableStyleName ))
			return false;

		if ( !ClassUtils.nullSafeEquals( mColumnStyleNames, ((FlexTableLayoutConfig) that).mColumnStyleNames ))
			return false;

		if ( !ClassUtils.nullSafeEquals( mSectionStyleName, ((FlexTableLayoutConfig) that).mSectionStyleName ))
			return false;

		if ( !ClassUtils.nullSafeEquals( mFooterStyleName, ((FlexTableLayoutConfig) that).mFooterStyleName ))
			return false;

		return super.equals( that );
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode ^= ClassUtils.nullSafeHashCode( mNumberOfColumns );
		hashCode ^= ClassUtils.nullSafeHashCode( mTableStyleName );
		hashCode ^= ClassUtils.nullSafeHashCode( mColumnStyleNames );
		hashCode ^= ClassUtils.nullSafeHashCode( mSectionStyleName );
		hashCode ^= ClassUtils.nullSafeHashCode( mFooterStyleName );

		return hashCode;
	}
}
