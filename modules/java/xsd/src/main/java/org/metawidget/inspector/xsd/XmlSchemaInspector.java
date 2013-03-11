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

package org.metawidget.inspector.xsd;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Inspector to look for metadata in XML Schema files.
 *
 * @author Richard Kennard
 */

public class XmlSchemaInspector
	extends BaseXmlInspector {

	//
	// Constructor
	//

	public XmlSchemaInspector( XmlSchemaInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to search by <code>name=</code>, not <code>type=</code>.
	 */

	@Override
	protected String getTopLevelTypeAttribute() {

		return NAME;
	}

	@Override
	protected String getReferenceAttribute() {

		return "ref";
	}

	@Override
	protected Element traverseFromTopLevelTypeToNamedChildren( Element topLevel ) {

		// Skip over 'complexType'...

		Element complexType;

		if ( "complexType".equals( topLevel.getLocalName() )) {

			complexType = topLevel;

		} else {

			complexType = XmlUtils.getFirstChildElement( topLevel );

			if ( complexType == null ) {

				if ( !topLevel.hasAttribute( "type" )) {
					return null;
				}

				// Top-level elements can be empty and just pointed to a top-level complexType

				complexType = XmlUtils.getChildWithAttributeValue( topLevel.getOwnerDocument().getDocumentElement(), "name", topLevel.getAttribute( "type" ) );

				if ( complexType == null ) {
					return null;
				}
			}

			if ( !"complexType".equals( complexType.getLocalName() ) ) {
				throw InspectorException.newException( "Unexpected child node '" + complexType.getLocalName() + "'" );
			}
		}

		// ...and 'sequence' elements

		Element sequence = XmlUtils.getFirstChildElement( complexType );

		if ( sequence == null ) {
			return null;
		}

		if ( !"sequence".equals( sequence.getLocalName() ) ) {
			throw InspectorException.newException( "Unexpected child node '" + sequence.getLocalName() + "'" );
		}

		return sequence;
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect ) {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Redirect to ref (if any)

		Element toInspectToUse = toInspect;

		if ( toInspectToUse.hasAttribute( "ref" )) {
			String name = toInspectToUse.getAttribute( "ref" );
			attributes.put( NAME, name );
			toInspectToUse = XmlUtils.getChildWithAttributeValue( toInspectToUse.getOwnerDocument().getDocumentElement(), getTopLevelTypeAttribute(), name );
		} else {
			attributes.put( NAME, toInspectToUse.getAttribute( NAME ) );
		}

		// Type

		if ( toInspectToUse.hasAttribute( TYPE ) ) {
			attributes.put( TYPE, toInspectToUse.getAttribute( TYPE ) );
		}

		// Required

		String notNull = toInspectToUse.getAttribute( "minOccurs" );

		if ( !"".equals( notNull ) && Integer.parseInt( notNull ) > 0 ) {
			attributes.put( REQUIRED, TRUE );
		}

		return attributes;
	}
}
