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

package org.metawidget.vaadin.layout;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a GridBagLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Loghman Barari
 */

public class GridLayoutConfig {

	//
	// Private members
	//

	private int		mNumberOfColumns	= 1;

	private int		mNumberOfRows		= 0;

	private boolean	mSupportMnemonics	= true;

	private String	mLabelSuffix		= ":";

	private boolean mAlignCaptionOnLeft = true;
	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setNumberOfColumns( int numberOfColumns ) {

		if ( numberOfColumns < 1 ) {
			throw LayoutException.newException( "numberOfColumns must be >= 1" );
		}

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setNumberOfRows( int numberOfRows ) {

		if ( numberOfRows < 1 ) {
			throw LayoutException.newException( "numberOfRows must be >= 1" );
		}

		mNumberOfRows = numberOfRows;

		return this;
	}



	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setSupportMnemonics( boolean supportMnemonics ) {

		mSupportMnemonics = supportMnemonics;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setLabelSuffix( String labelSuffix ) {

		mLabelSuffix = labelSuffix;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setAlignCaptionOnLeft( boolean alignCaptionOnLeft ) {

		mAlignCaptionOnLeft = alignCaptionOnLeft;

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

		if ( mNumberOfColumns != ( (GridLayoutConfig) that ).mNumberOfColumns ) {
			return false;
		}


		if ( mNumberOfRows != ( (GridLayoutConfig) that ).mNumberOfRows ) {
			return false;
		}

		if ( mSupportMnemonics != ( (GridLayoutConfig) that ).mSupportMnemonics ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelSuffix, ( (GridLayoutConfig) that ).mLabelSuffix ) ) {
			return false;
		}

		if ( mAlignCaptionOnLeft != ( (GridLayoutConfig) that).mAlignCaptionOnLeft ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + mNumberOfColumns;
		hashCode = 31 * hashCode + mNumberOfRows;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSupportMnemonics );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelSuffix );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mAlignCaptionOnLeft );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getNumberOfColumns() {

		return mNumberOfColumns;
	}

	protected int getNumberOfRows() {
		return mNumberOfRows;
	}

	protected boolean isSupportMnemonics() {

		return mSupportMnemonics;
	}

	protected String getLabelSuffix() {

		return mLabelSuffix;
	}

	protected boolean isAlignCaptionOnLeft() {

		return mAlignCaptionOnLeft;
	}

	public static GridLayoutConfig newHorizentalLayoutConfig() {
		GridLayoutConfig config = new  GridLayoutConfig();
		config.mNumberOfColumns = 0;
		config.mNumberOfRows = 1;

		return config;
	}

}
