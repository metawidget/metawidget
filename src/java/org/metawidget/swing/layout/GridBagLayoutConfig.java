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

package org.metawidget.swing.layout;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a GridBagLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class GridBagLayoutConfig {

	//
	// Private members
	//

	private int		mNumberOfColumns	= 1;

	private int		mLabelAlignment		= SwingConstants.LEFT;

	private Color	mLabelForeground;

	private Font	mLabelFont;

	private String	mLabelSuffix		= ":";

	private int		mRequiredAlignment	= SwingConstants.CENTER;

	private String	mRequiredText		= "*";

	//
	// Public methods
	//

	public int getNumberOfColumns() {

		return mNumberOfColumns;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setNumberOfColumns( int numberOfColumns ) {

		if ( numberOfColumns < 1 ) {
			throw LayoutException.newException( "numberOfColumns must be >= 1" );
		}

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	public int getLabelAlignment() {

		return mLabelAlignment;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelAlignment( int labelAlignment ) {

		mLabelAlignment = labelAlignment;

		return this;
	}

	public Color getLabelForeground() {

		return mLabelForeground;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelForeground( Color labelForeground ) {

		mLabelForeground = labelForeground;

		return this;
	}

	public Font getLabelFont() {

		return mLabelFont;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelFont( Font labelFont ) {

		mLabelFont = labelFont;

		return this;
	}

	public String getLabelSuffix() {

		return mLabelSuffix;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setLabelSuffix( String labelSuffix ) {

		mLabelSuffix = labelSuffix;

		return this;
	}

	public int getRequiredAlignment() {

		return mRequiredAlignment;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setRequiredAlignment( int requiredAlignment ) {

		mRequiredAlignment = requiredAlignment;

		return this;
	}

	public String getRequiredText() {

		return mRequiredText;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridBagLayoutConfig setRequiredText( String requiredText ) {

		mRequiredText = requiredText;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( that == null ) {
			return false;
		}

		if ( getClass() != that.getClass() ) {
			return false;
		}

		if ( mNumberOfColumns != ( (GridBagLayoutConfig) that ).mNumberOfColumns ) {
			return false;
		}

		if ( mLabelAlignment != ( (GridBagLayoutConfig) that ).mLabelAlignment ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelFont, ( (GridBagLayoutConfig) that ).mLabelFont ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelForeground, ( (GridBagLayoutConfig) that ).mLabelForeground ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelSuffix, ( (GridBagLayoutConfig) that ).mLabelSuffix ) ) {
			return false;
		}

		if ( mRequiredAlignment != ( (GridBagLayoutConfig) that ).mRequiredAlignment ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mRequiredText, ( (GridBagLayoutConfig) that ).mRequiredText ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + mNumberOfColumns;
		hashCode = 31 * hashCode + mLabelAlignment;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelFont );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelForeground );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelSuffix );
		hashCode = 31 * hashCode + mRequiredAlignment;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRequiredText );

		return hashCode;
	}
}
