// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspectionresultprocessor.type;

import java.util.Map;

import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a TypeMappingInspectionResultProcessor prior to use. Once instantiated,
 * InspectionResultProcessors are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
