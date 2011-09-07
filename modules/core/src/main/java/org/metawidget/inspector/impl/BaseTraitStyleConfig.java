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

package org.metawidget.inspector.impl;

import java.util.regex.Pattern;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Base class for BaseTraitStyle configurations.
 *
 * @author Richard Kennard
 */

public class BaseTraitStyleConfig {

	//
	// Private statics
	//

	private static Pattern	DEFAULT_EXCLUDE_BASE_TYPE;

	//
	// Private members
	//

	private boolean			mCacheLookups	= true;

	private Pattern			mExcludeBaseType;

	private boolean			mNullExcludeBaseType;

	private Class<?>[]		mExcludeReturnType;

	private String[]		mExcludeName;

	//
	// Public methods
	//

	/**
	 * Sets whether to cache lookups for a class. In general, lookups are expensive and their
	 * results are static, so caching can greatly improve performance. However it may be useful to
	 * disable caching for debugging purposes or for dynamic classes.
	 * <p>
	 * True by default.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseTraitStyleConfig setCacheLookups( boolean cacheLookups ) {

		mCacheLookups = cacheLookups;

		// Fluent interface

		return this;
	}

	/**
	 * Sets the Pattern used to exclude base types when searching up the model inheritance chain.
	 * <p>
	 * This can be useful when the base types define traits that are framework-specific, and should
	 * be filtered out from 'real' business model traits.
	 * <p>
	 * By default, excludes any base types from <code>java.*</code> or <code>javax.*</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseTraitStyleConfig setExcludeBaseType( Pattern excludeBaseType ) {

		mExcludeBaseType = excludeBaseType;
		mNullExcludeBaseType = ( excludeBaseType == null );

		// Fluent interface

		return this;
	}

	/**
	 * Sets a list of return types to exclude when searching for traits.
	 * <p>
	 * This can be useful when the convention or base class define traits that are
	 * framework-specific, and should be filtered out from 'real' business model traits.
	 * <p>
	 * By default, does not exclude any return types.
	 *
	 * @param excludeReturnType
	 *            list of return types to consider for exclusion
	 * @return this, as part of a fluent interface
	 */

	public BaseTraitStyleConfig setExcludeReturnType( Class<?>... excludeReturnType ) {

		mExcludeReturnType = excludeReturnType;

		// Fluent interface

		return this;
	}

	/**
	 * Sets a list of names to exclude when searching for traits.
	 * <p>
	 * This can be useful when the convention or base class define traits that are
	 * framework-specific, and should be filtered out from 'real' business model traits.
	 * <p>
	 * By default, does not exclude any return types.
	 *
	 * @param excludeName
	 *            list of names to consider for exclusion
	 * @return this, as part of a fluent interface
	 */

	public BaseTraitStyleConfig setExcludeName( String... excludeName ) {

		mExcludeName = excludeName;

		// Fluent interface

		return this;
	}

	/**
	 * Whether to exclude the given name when searching for traits.
	 * <p>
	 * This can be useful when the convention defines traits that are framework-specific (eg.
	 * <code>getClass()</code>), and should be filtered out from 'real' business model traits.
	 * <p>
	 * By default, does not exclude any names.
	 *
	 * @param name
	 *            to consider for exclusion
	 * @return true if the trait should be excluded, false otherwise
	 */
	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( mCacheLookups != ( (BaseTraitStyleConfig) that ).mCacheLookups ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mExcludeBaseType, ( (BaseTraitStyleConfig) that ).mExcludeBaseType ) ) {
			return false;
		}

		if ( mNullExcludeBaseType != ( (BaseTraitStyleConfig) that ).mNullExcludeBaseType ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mExcludeReturnType, ( (BaseTraitStyleConfig) that ).mExcludeReturnType ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mExcludeName, ( (BaseTraitStyleConfig) that ).mExcludeName ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mCacheLookups );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mExcludeBaseType );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mNullExcludeBaseType );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mExcludeReturnType );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mExcludeName );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected boolean isCacheLookups() {

		return mCacheLookups;
	}

	protected Pattern getExcludeBaseType() {

		if ( mExcludeBaseType == null && !mNullExcludeBaseType ) {
			if ( DEFAULT_EXCLUDE_BASE_TYPE == null ) {
				DEFAULT_EXCLUDE_BASE_TYPE = Pattern.compile( "^(java|javax)\\..*$" );
			}

			return DEFAULT_EXCLUDE_BASE_TYPE;
		}

		return mExcludeBaseType;
	}

	protected Class<?>[] getExcludeReturnType() {

		return mExcludeReturnType;
	}

	protected String[] getExcludeName() {

		return mExcludeName;
	}
}
