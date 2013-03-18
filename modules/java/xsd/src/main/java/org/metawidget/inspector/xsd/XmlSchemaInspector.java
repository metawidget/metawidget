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
 * Inspector to look for metadata in XML Schema (XSD) files.
 * <p>
 * If
 * <code>XmlSchemaInspector</tt> is used for a Java environment, consider using it in conjunction with <code>XmlSchemaToJavaTypeMappingProcessor</code>
 * . For returning results to JavaScript environments, consider
 * <code>JavaToJavaScriptTypeMappingProcessor</code> and <code>XmlUtils.elementToJson</code>.
 * 
 * @author Richard Kennard
 */

public class XmlSchemaInspector
	extends BaseXmlInspector {

	//
	// Private statics
	//

	private static final String	COMPLEX_TYPE	= "complexType";

	private static final String	SIMPLE_TYPE		= "simpleType";

	private static final String	COMPLEX_CONTENT	= "complexContent";

	private static final String	SIMPLE_CONTENT	= "simpleContent";

	private static final String	REF				= "ref";

	private static final String	BASE			= "base";

	private static final String	ALL				= "all";

	private static final String	SEQUENCE		= "sequence";

	private static final String	EXTENSION		= "extension";

	private static final String	RESTRICTION		= "restriction";

	private static final String	VALUE			= "value";

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

		return REF;
	}

	@Override
	protected Element traverseFromTopLevelTypeToNamedChildren( Element topLevel ) {

		// Skip over 'complexType'...

		Element complexType;

		if ( COMPLEX_TYPE.equals( topLevel.getLocalName() ) ) {

			complexType = topLevel;

		} else {

			complexType = XmlUtils.getFirstChildElement( topLevel );

			if ( complexType == null ) {

				if ( !topLevel.hasAttribute( TYPE ) ) {
					return null;
				}

				// Top-level elements can be empty and just pointed to a top-level complexType

				complexType = XmlUtils.getChildWithAttributeValue( topLevel.getOwnerDocument().getDocumentElement(), NAME, topLevel.getAttribute( TYPE ) );

				if ( complexType == null ) {
					return null;
				}
			}

			if ( !COMPLEX_TYPE.equals( complexType.getLocalName() ) ) {
				throw InspectorException.newException( "Unexpected child node '" + complexType.getLocalName() + "'" );
			}
		}

		// ...and 'sequence' elements

		Element sequence = XmlUtils.getFirstChildElement( complexType );

		if ( sequence == null ) {
			return null;
		}

		if ( !SEQUENCE.equals( sequence.getLocalName() ) && !ALL.equals( sequence.getLocalName() ) && !COMPLEX_CONTENT.equals( sequence.getLocalName() ) ) {
			throw InspectorException.newException( "Unexpected child node '" + sequence.getLocalName() + "'" );
		}

		return sequence;
	}

	@Override
	protected void inspectTraits( Element toInspect, Element toAddTo ) {

		Element toInspectToUse = toInspect;

		// Inherited type

		if ( COMPLEX_CONTENT.equals( toInspectToUse.getLocalName() ) ) {

			toInspectToUse = XmlUtils.getChildNamed( toInspectToUse, "extension" );

			if ( toInspectToUse == null ) {
				throw InspectorException.newException( "Expected complexContent to have an extension" );
			}

			String base = toInspectToUse.getAttribute( BASE );

			if ( base == null ) {
				throw InspectorException.newException( "Expected extension to have a base" );
			}

			// Look up the base element...

			Element baseElement = XmlUtils.getChildWithAttributeValue( toInspectToUse.getOwnerDocument().getDocumentElement(), getTopLevelTypeAttribute(), base );
			Element baseSequence = XmlUtils.getFirstChildElement( baseElement );

			if ( !SEQUENCE.equals( baseSequence.getLocalName() ) ) {
				throw InspectorException.newException( "Unexpected child node '" + baseSequence.getLocalName() + "'" );
			}

			inspectTraits( baseSequence, toAddTo );

			// ...then continue with our own sequence

			toInspectToUse = XmlUtils.getFirstChildElement( toInspectToUse );

			if ( !SEQUENCE.equals( toInspectToUse.getLocalName() ) ) {
				throw InspectorException.newException( "Unexpected child node '" + toInspectToUse.getLocalName() + "'" );
			}
		}

		super.inspectTraits( toInspectToUse, toAddTo );
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect ) {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Redirect to ref (if any)

		Element toInspectToUse = toInspect;

		if ( toInspectToUse.hasAttribute( REF ) ) {
			String name = toInspectToUse.getAttribute( REF );
			attributes.put( NAME, name );
			toInspectToUse = XmlUtils.getChildWithAttributeValue( toInspectToUse.getOwnerDocument().getDocumentElement(), getTopLevelTypeAttribute(), name );
		} else {
			attributes.put( NAME, toInspectToUse.getAttribute( NAME ) );
		}

		inspectElement( toInspectToUse, attributes );
		return attributes;
	}

	//
	// Private methods
	//

	private void inspectElement( Element element, Map<String, String> attributes ) {

		// Type

		if ( element.hasAttribute( TYPE ) ) {
			attributes.put( TYPE, element.getAttribute( TYPE ) );
		} else {

			// Type inferred from complexType or simpleType

			Element complexType = XmlUtils.getChildNamed( element, COMPLEX_TYPE );

			if ( complexType != null ) {
				Element simpleContent = XmlUtils.getChildNamed( complexType, SIMPLE_CONTENT );

				if ( simpleContent != null ) {
					inspectExtension( simpleContent, attributes );
					inspectRestriction( simpleContent, attributes );
				}
			} else {

				Element simpleType = XmlUtils.getChildNamed( element, SIMPLE_TYPE );

				if ( simpleType != null ) {
					inspectRestriction( simpleType, attributes );
				}
			}
		}

		// Required

		String notNull = element.getAttribute( "minOccurs" );

		if ( !"".equals( notNull ) && Integer.parseInt( notNull ) > 0 ) {
			attributes.put( REQUIRED, TRUE );
		}
	}

	private void inspectExtension( Element parent, Map<String, String> attributes ) {

		Element extension = XmlUtils.getChildNamed( parent, EXTENSION );

		if ( extension == null ) {
			return;
		}

		attributes.put( TYPE, extension.getAttribute( BASE ) );
	}

	private void inspectRestriction( Element parent, Map<String, String> attributes ) {

		Element restriction = XmlUtils.getChildNamed( parent, RESTRICTION );

		if ( restriction == null ) {
			return;
		}

		attributes.put( TYPE, restriction.getAttribute( BASE ) );

		// Minimum/maximum length

		Element minLength = XmlUtils.getChildNamed( restriction, "minLength" );

		if ( minLength != null ) {
			attributes.put( MINIMUM_LENGTH, minLength.getAttribute( VALUE ) );
		}

		Element maxLength = XmlUtils.getChildNamed( restriction, "maxLength" );

		if ( maxLength != null ) {
			attributes.put( MAXIMUM_LENGTH, maxLength.getAttribute( VALUE ) );
		}

		// Minimum/maximum value

		Element minInclusive = XmlUtils.getChildNamed( restriction, "minInclusive" );

		if ( minInclusive != null ) {
			attributes.put( MINIMUM_VALUE, minInclusive.getAttribute( VALUE ) );
		}

		Element maxInclusive = XmlUtils.getChildNamed( restriction, "maxInclusive" );

		if ( maxInclusive != null ) {
			attributes.put( MAXIMUM_VALUE, maxInclusive.getAttribute( VALUE ) );
		}

		// Fraction digits

		Element fractionDigits = XmlUtils.getChildNamed( restriction, "fractionDigits" );

		if ( fractionDigits != null ) {
			attributes.put( MAXIMUM_FRACTIONAL_DIGITS, fractionDigits.getAttribute( VALUE ) );
		}
	}
}
