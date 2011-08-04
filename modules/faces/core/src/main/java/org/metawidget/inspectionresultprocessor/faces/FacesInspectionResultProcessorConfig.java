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

package org.metawidget.inspectionresultprocessor.faces;

import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a FacesInspectionResultProcessor prior to use. Once instantiated,
 * InspectorResultProcessors are immutable.
 *
 * @author Richard Kennard
 */

public class FacesInspectionResultProcessorConfig {

	//
	// Private members
	//

	private PropertyStyle	mInjectThis;

	//
	// Public methods
	//

	/**
	 * Sets the PropertyStyle to use to inject a request-level '_this' attribute (the underscore is
	 * needed because 'this' is a reserved word in EL) into JSF evaluations.
	 * <p>
	 * JSF EL expressions rely on the JSF context being properly initialized with certain managed
	 * bean names. This is rather brittle. Instead, injecting '_this' allows the EL to refer to the
	 * originating object (i.e. <code>#{_this.name}</code>) regardless of how the JSF
	 * context is configured.
	 * <p>
	 * Note: <code>injectThis</code> cannot be used within attributes such as
	 * <code>faces-lookup</code>. Those attributes map to well-defined places within the JSF
	 * framework (i.e. <code>f:selectItems</code>) and are evaluated at a different phase of the JSF
	 * lifecycle. In some cases they will skip invoking <code>FacesInspectionResultProcessor</code>.
	 * For example if a <code>h:selectOneMenu</code> fails to validate during POSTback, its
	 * <code>f:selectItems</code> will be redisplayed without a new inspection and with no chance to
	 * <code>injectThis</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public FacesInspectionResultProcessorConfig setInjectThis( PropertyStyle injectThis ) {

		mInjectThis = injectThis;

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

		if ( !ObjectUtils.nullSafeEquals( mInjectThis, ( (FacesInspectionResultProcessorConfig) that ).mInjectThis ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInjectThis );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected PropertyStyle getInjectThis() {

		return mInjectThis;
	}
}