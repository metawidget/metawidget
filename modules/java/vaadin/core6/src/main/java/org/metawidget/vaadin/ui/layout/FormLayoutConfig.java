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

package org.metawidget.vaadin.ui.layout;

import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Configures a FormLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Loghman Barari
 */

public class FormLayoutConfig {

	//
	// Private members
	//

	private String	mLabelSuffix		= StringUtils.SEPARATOR_COLON;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public FormLayoutConfig setLabelSuffix( String labelSuffix ) {

		mLabelSuffix = labelSuffix;

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

		if ( !ObjectUtils.nullSafeEquals( mLabelSuffix, ( (FormLayoutConfig) that ).mLabelSuffix ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLabelSuffix );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected String getLabelSuffix() {

		return mLabelSuffix;
	}
}
