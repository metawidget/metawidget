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

import java.util.Map;

import org.metawidget.inspectionresultprocessor.impl.BaseInspectionResultProcessor;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * <code>InspectionResultProcessor</code> to map attribute names from
 * <code>inspection-result.xsd</code> to JSON Schema, ready for
 * <code>XmlUtils.elementToJsonSchema</code>.
 * <p>
 * Consider using in conjunction with <code>JsonTypeMappingProcessor</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
		removeAttributes( entity );

		// For each trait...

		Element trait = XmlUtils.getFirstChildElement( entity );

		while ( trait != null ) {

			// ...remove hidden ones...

			if ( shouldRemove( trait ) ) {
				Element toRemove = trait;
				trait = XmlUtils.getNextSiblingElement( trait );
				entity.removeChild( toRemove );
				continue;
			}

			// If the trait has children, modify them too. This is not strictly in keeping with how
			// inspection-result-1.0.xsd is defined, but is very useful for processing embedded
			// schemas before returning them over a remote XML/JSON interface

			Map<String, String> attributes = XmlUtils.getAttributesAsMap( trait );

			if ( trait.getChildNodes().getLength() > 0 && attributes.containsKey( NAME ) ) {
				processTraits( trait, metawidget, toInspect, type, ArrayUtils.add( names, attributes.get( NAME ) ) );
			}

			// ...and map the rest

			mapAttributes( trait );
			removeAttributes( trait );
			trait = XmlUtils.getNextSiblingElement( trait );
		}
	}

	/**
	 * Returns true if the given element should be removed from the returned JSON schema. By
	 * default, returns true if <code>hidden</code> equals <code>true</code>.
	 */

	protected boolean shouldRemove( Element element ) {

		return TRUE.equals( element.getAttribute( HIDDEN ) );
	}

	/**
	 * Map common attribute names from <code>inspection-result.xsd</code> to JSON Schema.
	 */

	protected void mapAttributes( Element element ) {

		mapAttribute( element, LABEL, "title" );
		mapAttribute( element, LOOKUP, "enum" );
		mapAttribute( element, LOOKUP_LABELS, "enumTitles" );
		mapAttribute( element, MINIMUM_VALUE, "minimum" );
		mapAttribute( element, MAXIMUM_VALUE, "maximum" );
		mapAttribute( element, MINIMUM_LENGTH, "minLength" );
		mapAttribute( element, MAXIMUM_LENGTH, "maxLength" );
	}

	/**
	 * Remove specified attributes from the JSON Schema output.
	 */

	protected void removeAttributes( Element element ) {

		if ( mRemoveAttributes == null ) {
			return;
		}

		for ( String removeAttribute : mRemoveAttributes ) {
			element.removeAttribute( removeAttribute );
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
