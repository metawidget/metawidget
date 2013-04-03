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

package org.metawidget.inspectionresultprocessor.json.schema;

import static org.metawidget.inspector.InspectionResultConstants.*;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures an JsonSchemaMappingProcessor prior to use. Once instantiated,
 * InspectionResultProcessors are immutable.
 *
 * @author Richard Kennard
 */

public class JsonSchemaMappingProcessorConfig {

	//
	// Private members
	//

	private String[]	mRemoveAttributes	= new String[] { COMES_AFTER, HIDDEN, PARAMETERIZED_TYPE };

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
