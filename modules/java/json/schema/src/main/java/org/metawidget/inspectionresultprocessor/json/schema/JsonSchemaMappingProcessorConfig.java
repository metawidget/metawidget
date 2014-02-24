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

package org.metawidget.inspectionresultprocessor.json.schema;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures an JsonSchemaMappingProcessor prior to use. Once instantiated,
 * InspectionResultProcessors are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonSchemaMappingProcessorConfig {

	//
	// Private members
	//

	/**
	 * Remove attributes that do not apply in JSON Schema:
	 * <ul>
	 * <li>HIDDEN is removed, and also the whole property is hidden if HIDDEN equals TRUE</li>
	 * <li>COMES_AFTER is removed, but is replaced by 'propertyOrder'</li>
	 * <li>PARAMETERIZED_TYPE, ACTUAL_CLASS contain Java type names</li>
	 */

	private String[]	mRemoveAttributes	= new String[] { HIDDEN, COMES_AFTER, PARAMETERIZED_TYPE, ACTUAL_CLASS };

	//
	// Public methods
	//

	/**
	 * Sets attributes to remove from the <tt>inspection-result</tt> entirely. This can be useful if
	 * they contain irrelevant or potentially sensitive information (e.g. fully qualified class
	 * names can expose the internal architecture of a system to a client).
	 */

	public JsonSchemaMappingProcessorConfig setRemoveAttributes( String... removeAttributes ) {

		mRemoveAttributes = removeAttributes;

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

		if ( !ObjectUtils.nullSafeEquals( mRemoveAttributes, ( (JsonSchemaMappingProcessorConfig) that ).mRemoveAttributes ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRemoveAttributes );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected String[] getRemoveAttributes() {

		return mRemoveAttributes;
	}
}
