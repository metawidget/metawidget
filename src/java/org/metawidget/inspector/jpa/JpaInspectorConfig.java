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

package org.metawidget.inspector.jpa;

import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a JpaInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author Richard Kennard
 */

public class JpaInspectorConfig
	extends BaseObjectInspectorConfig {

	//
	// Private members
	//

	private boolean	mHideIds	= true;

	//
	// Public methods
	//

	/**
	 * Whether the Inspector returns Id properties as <code>hidden="true"</code>. True by default.
	 * <p>
	 * JPA recommends using synthetic ids, so generally they don't appear in the UI.
	 */

	public boolean isHideIds() {

		return mHideIds;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public JpaInspectorConfig setHideIds( boolean hideIds ) {

		mHideIds = hideIds;

		// Fluent interface

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

		if ( mHideIds != ( (JpaInspectorConfig) that ).mHideIds ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mHideIds );

		return hashCode;
	}
}