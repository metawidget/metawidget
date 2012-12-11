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
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
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

	private boolean	mHideIds		= true;

	private boolean	mHideVersions	= true;

	private boolean	mHideTransients	= false;

	//
	// Public methods
	//

	/**
	 * Sets whether the Inspector returns Id properties as <code>hidden="true"</code>. True by
	 * default.
	 * <p>
	 * JPA recommends using synthetic ids, so generally they don't appear in the UI.
	 *
	 * @return this, as part of a fluent interface
	 */

	public JpaInspectorConfig setHideIds( boolean hideIds ) {

		mHideIds = hideIds;

		// Fluent interface

		return this;
	}

	/**
	 * Sets whether the Inspector returns Version properties as <code>hidden="true"</code>. True by
	 * default.
	 * <p>
	 * JPA uses these as an optimisic locking mechanism, so generally they don't appear in the UI.
	 *
	 * @return this, as part of a fluent interface
	 */

	public JpaInspectorConfig setHideVersions( boolean hideVersions ) {

		mHideVersions = hideVersions;

		// Fluent interface

		return this;
	}

	/**
	 * Sets whether the Inspector returns Transient properties as <code>hidden="true"</code>. False
	 * by default.
	 * <p>
	 * There is not a firm relationship between whether a field should be persisted by JPA, and
	 * whether it should appear in the UI. Some architectures prefer Transient fields to be hidden
	 * in the UI by default, but some prefer them to be visible. The latter applies both to
	 * 'synthetic' fields such as <code>getAge</code> that calculates based off a persisted
	 * <code>getDateOfBirth</code>, and also to overridden JPA fields (which generally must be
	 * marked <code>Transient</code> in the subclass).
	 *
	 * @return this, as part of a fluent interface
	 */

	public JpaInspectorConfig setHideTransients( boolean hideTransients ) {

		mHideTransients = hideTransients;

		// Fluent interface

		return this;
	}

	/**
	 * Overriden to return a JpaInspectorConfig, as part of a fluent interface.
	 */

	@Override
	public JpaInspectorConfig setPropertyStyle( PropertyStyle propertyStyle ) {

		return (JpaInspectorConfig) super.setPropertyStyle( propertyStyle );
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( mHideIds != ( (JpaInspectorConfig) that ).mHideIds ) {
			return false;
		}

		if ( mHideVersions != ( (JpaInspectorConfig) that ).mHideVersions ) {
			return false;
		}

		if ( mHideTransients != ( (JpaInspectorConfig) that ).mHideTransients ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mHideIds );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mHideVersions );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mHideTransients );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected boolean isHideIds() {

		return mHideIds;
	}

	protected boolean isHideVersions() {

		return mHideVersions;
	}

	protected boolean isHideTransients() {

		return mHideTransients;
	}
}