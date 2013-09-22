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

package org.metawidget.swt.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Configures a GridLayoutConfig prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GridLayoutConfig {

	//
	// Private members
	//

	private int		mNumberOfColumns	= 1;

	private int		mLabelAlignment		= SWT.LEFT;

	private Color	mLabelForeground;

	private Font	mLabelFont;

	private String	mLabelSuffix		= StringUtils.SEPARATOR_COLON;

	private int		mRequiredAlignment	= SWT.CENTER;

	private String	mRequiredText		= "*";

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

	public GridLayoutConfig setLabelAlignment( int labelAlignment ) {

		mLabelAlignment = labelAlignment;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setLabelForeground( Color labelForeground ) {

		mLabelForeground = labelForeground;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setLabelFont( Font labelFont ) {

		mLabelFont = labelFont;

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

	public GridLayoutConfig setRequiredAlignment( int requiredAlignment ) {

		mRequiredAlignment = requiredAlignment;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public GridLayoutConfig setRequiredText( String requiredText ) {

		mRequiredText = requiredText;

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

		if ( mLabelAlignment != ( (GridLayoutConfig) that ).mLabelAlignment ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelFont, ( (GridLayoutConfig) that ).mLabelFont ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelForeground, ( (GridLayoutConfig) that ).mLabelForeground ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mLabelSuffix, ( (GridLayoutConfig) that ).mLabelSuffix ) ) {
			return false;
		}

		if ( mRequiredAlignment != ( (GridLayoutConfig) that ).mRequiredAlignment ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mRequiredText, ( (GridLayoutConfig) that ).mRequiredText ) ) {
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

	//
	// Protected methods
	//

	protected int getNumberOfColumns() {

		return mNumberOfColumns;
	}

	protected int getLabelAlignment() {

		return mLabelAlignment;
	}

	protected Color getLabelForeground() {

		return mLabelForeground;
	}

	protected Font getLabelFont() {

		return mLabelFont;
	}

	protected String getLabelSuffix() {

		return mLabelSuffix;
	}

	protected int getRequiredAlignment() {

		return mRequiredAlignment;
	}

	protected String getRequiredText() {

		return mRequiredText;
	}
}
