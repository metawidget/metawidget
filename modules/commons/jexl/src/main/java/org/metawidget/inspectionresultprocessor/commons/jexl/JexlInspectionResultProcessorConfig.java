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

package org.metawidget.inspectionresultprocessor.commons.jexl;

import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a JexlInspectionResultProcessor prior to use. Once instantiated,
 * InspectorResultProcessors are immutable.
 *
 * @author Richard Kennard
 */

public class JexlInspectionResultProcessorConfig {

	//
	// Private statics
	//

	private static PropertyStyle	DEFAULT_PROPERTY_STYLE;

	//
	// Private members
	//

	private PropertyStyle			mInjectThis;

	protected boolean				mNullInjectThis;

	//
	// Public methods
	//

	/**
	 * Sets the PropertyStyle to use to inject a request-level 'this' attribute into JEXL
	 * evaluations.
	 *
	 * @return this, as part of a fluent interface
	 */

	public JexlInspectionResultProcessorConfig setInjectThis( PropertyStyle injectThis ) {

		mInjectThis = injectThis;

		if ( injectThis == null ) {
			mNullInjectThis = true;
		}

		// Fluent interface

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

		if ( !ObjectUtils.nullSafeEquals( mInjectThis, ( (JexlInspectionResultProcessorConfig) that ).mInjectThis ) ) {
			return false;
		}

		if ( mNullInjectThis != ( (JexlInspectionResultProcessorConfig) that ).mNullInjectThis ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInjectThis );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mNullInjectThis );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected PropertyStyle getInjectThis() {

		if ( mInjectThis == null && !mNullInjectThis ) {
			// Do not initialise unless needed, so that we can be shipped without

			if ( DEFAULT_PROPERTY_STYLE == null ) {
				DEFAULT_PROPERTY_STYLE = new JavaBeanPropertyStyle();
			}

			return DEFAULT_PROPERTY_STYLE;
		}

		return mInjectThis;
	}
}