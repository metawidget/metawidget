// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

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
