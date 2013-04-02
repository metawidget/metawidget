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

package org.metawidget.inspectionresultprocessor.type;

import java.util.Map;

import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a TypeMappingInspectionResultProcessor prior to use. Once instantiated,
 * InspectionResultProcessors are immutable.
 *
 * @author Ryan Bradley
 */

public class TypeMappingInspectionResultProcessorConfig {

	//
	// Private members
	//

	private Map<String, String>	mTypeMappings;

	private boolean				mRemoveUnmappedTypes;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public TypeMappingInspectionResultProcessorConfig setTypeMapping( String from, String to ) {

		if ( mTypeMappings == null ) {
			mTypeMappings = CollectionUtils.newHashMap();
		}

		mTypeMappings.put( from, to );

		return this;
	}

	/**
	 * Whether to remove types that are not explicitly mapped. This can be useful if the 'raw' type
	 * contains potentially sensitive information (e.g. fully qualified class names can expose the
	 * internal architecture of a system to a client)
	 *
	 * @return this, as part of a fluent interface
	 */

	public TypeMappingInspectionResultProcessorConfig setRemoveUnmappedTypes( boolean removeUnmappedTypes ) {

		mRemoveUnmappedTypes = removeUnmappedTypes;

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

		if ( !ObjectUtils.nullSafeEquals( mTypeMappings, ( (TypeMappingInspectionResultProcessorConfig) that ).mTypeMappings ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mRemoveUnmappedTypes, ( (TypeMappingInspectionResultProcessorConfig) that ).mRemoveUnmappedTypes ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTypeMappings );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRemoveUnmappedTypes );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected Map<String, String> getTypeMappings() {

		return mTypeMappings;
	}

	protected boolean isRemoveUnmappedTypes() {

		return mRemoveUnmappedTypes;
	}
}
