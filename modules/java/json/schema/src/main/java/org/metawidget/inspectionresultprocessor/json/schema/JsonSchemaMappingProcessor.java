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

import org.metawidget.inspectionresultprocessor.impl.BaseInspectionResultProcessor;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * <code>InspectionResultProcessor</code> to map attribute names from
 * <code>inspection-result.xsd</code> to JSON Schema, ready for
 * <code>XmlUtils.elementToJsonSchema</code>.
 *
 * @author Richard Kennard
 */

public class JsonSchemaMappingProcessor<M>
	extends BaseInspectionResultProcessor<M> {

	//
	// Private members
	//

	private String[]	mRemoveAttributes;

	//
	// Constructor
	//

	public JsonSchemaMappingProcessor() {

		this( new JsonSchemaMappingProcessorConfig() );
	}

	public JsonSchemaMappingProcessor( JsonSchemaMappingProcessorConfig config ) {

		mRemoveAttributes = config.getRemoveAttributes();
	}

	//
	// Protected methods
	//

	@Override
	protected void processTraits( Element entity, M metawidget, Object toInspect, String type, String... names ) {

		mapAttributes( entity );

		// For each trait...

		Element trait = XmlUtils.getFirstChildElement( entity );

		while ( trait != null ) {

			// ...remove hidden ones...

			if ( TRUE.equals( trait.getAttribute( HIDDEN ) ) ) {
				Element toRemove = trait;
				trait = XmlUtils.getNextSiblingElement( trait );
				entity.removeChild( toRemove );
				continue;
			}

			// ...and map the rest

			mapAttributes( trait );
			trait = XmlUtils.getNextSiblingElement( trait );
		}
	}

	private void mapAttributes( Element element ) {

		// Map some...

		mapAttribute( element, LABEL, "title" );
		mapAttribute( element, LOOKUP, "enum" );
		mapAttribute( element, LOOKUP_LABELS, "enumTitles" );
		mapAttribute( element, MINIMUM_VALUE, "minimum" );
		mapAttribute( element, MAXIMUM_VALUE, "maximum" );
		mapAttribute( element, MINIMUM_LENGTH, "minLength" );
		mapAttribute( element, MAXIMUM_LENGTH, "maxLength" );

		// ...and remove others

		if ( mRemoveAttributes != null ) {
			for( String removeAttribute : mRemoveAttributes ) {
				element.removeAttribute( removeAttribute );
			}
		}
	}

	//
	// Private methods
	//

	private void mapAttribute( Element element, String in, String out ) {

		if ( !element.hasAttribute( in ) ) {
			return;
		}

		element.setAttribute( out, element.getAttribute( in ) );
		element.removeAttribute( in );
	}
}
