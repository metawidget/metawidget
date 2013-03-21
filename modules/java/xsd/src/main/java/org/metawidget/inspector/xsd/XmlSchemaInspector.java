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

import java.util.List;
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

	private static final String	ENUMERATION		= "enumeration";

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

	/**
	 * Overridden to introduce a reference attribute <code>ref=</code>.
	 */

	@Override
	protected String getReferenceAttribute() {

		return REF;
	}

	/**
	 * Overridden because, with XML Schema, the named children are often nested a couple of levels
	 * deep under the top-level element. For example
	 * <code>xs:element/xs:complexType/xs:sequence</code>.
	 */

	@Override
	protected Element traverseFromTopLevelTypeToNamedChildren( Element topLevel ) {

		// 'simpleType' is a top-level element

		if ( SIMPLE_TYPE.equals( topLevel.getLocalName() ) ) {
			return topLevel;
		}

		// 'complexType' needs to be traversed into

		Element complexType;

		if ( COMPLEX_TYPE.equals( topLevel.getLocalName() ) ) {

			complexType = topLevel;

		} else {

			// The most usual case is an 'element' containing a 'complexType' which needs to be
			// traversed into

			complexType = XmlUtils.getFirstChildElement( topLevel );

			if ( complexType == null ) {

				// If no 'complexType' child, perhaps top-level 'element' has a @type...

				if ( !topLevel.hasAttribute( TYPE ) ) {
					return null;
				}

				// ...if so, start over with this new type

				complexType = XmlUtils.getChildWithAttributeValue( topLevel.getOwnerDocument().getDocumentElement(), NAME, topLevel.getAttribute( TYPE ) );

				if ( complexType == null ) {
					return null;
				}
			}

			// Either way, should be at a 'complexType' by now

			if ( !COMPLEX_TYPE.equals( complexType.getLocalName() ) ) {
				throw InspectorException.newException( "Unexpected child node '" + complexType.getLocalName() + "'" );
			}
		}

		// Within 'complexType', 'sequence' needs to be traversed into

		Element sequence = XmlUtils.getFirstChildElement( complexType );

		if ( sequence == null ) {
			return null;
		}

		// Skip over 'annotation' (if any)

		String sequenceLocalName = sequence.getLocalName();

		if ( "annotation".equals( sequenceLocalName )) {
			sequence = XmlUtils.getNextSiblingElement( sequence );
			sequenceLocalName = sequence.getLocalName();
		}

		if ( !SEQUENCE.equals( sequenceLocalName ) && !ALL.equals( sequenceLocalName ) && !SIMPLE_CONTENT.equals( sequenceLocalName ) && !COMPLEX_CONTENT.equals( sequenceLocalName ) && !"attributeGroup".equals( sequenceLocalName ) ) {
			throw InspectorException.newException( "Unexpected child node '" + sequenceLocalName + "'" );
		}

		// Within 'sequence', 'choice' needs to be traversed into

		Element choice = XmlUtils.getChildNamed( sequence, "choice" );

		if ( choice != null ) {
			return choice;
		}

		return sequence;
	}

	@Override
	protected void inspectTraits( Element toInspect, Element toAddTo ) {

		Element toInspectToUse = toInspect;

		// Simple type (inherit @type attribute)

		if ( SIMPLE_TYPE.equals( toInspectToUse.getLocalName() ) ) {

			Map<String, String> attributes = CollectionUtils.newHashMap();
			inspectRestriction( toInspectToUse, attributes );
			XmlUtils.setMapAsAttributes( toAddTo, attributes );
			return;
		}

		// Simple content (inherit @type attribute)

		if ( SIMPLE_CONTENT.equals( toInspectToUse.getLocalName() ) ) {

			Map<String, String> attributes = CollectionUtils.newHashMap();
			inspectExtension( toInspectToUse, attributes );
			inspectRestriction( toInspectToUse, attributes );
			XmlUtils.setMapAsAttributes( toAddTo, attributes );
			return;
		}

		// Complex content (inherit @type attribute)

		if ( COMPLEX_CONTENT.equals( toInspectToUse.getLocalName() ) ) {

			toInspectToUse = XmlUtils.getChildNamed( toInspectToUse, EXTENSION );

			if ( toInspectToUse == null ) {
				throw InspectorException.newException( "Expected " + COMPLEX_CONTENT + " to have an " + EXTENSION );
			}

			String base = toInspectToUse.getAttribute( BASE );

			if ( base == null ) {
				throw InspectorException.newException( "Expected " + EXTENSION + " to have a " + BASE );
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

		Element toInspectToUse = toInspect;

		while ( true ) {

			if ( toInspectToUse == null ) {
				return null;
			}

			// All bets are off for xs:any

			if ( "any".equals( toInspectToUse.getLocalName() ) ) {
				return null;
			}

			// Redirect to ref (if any)

			if ( toInspectToUse.hasAttribute( REF ) ) {
				String name = toInspectToUse.getAttribute( REF );

				if ( !attributes.containsKey( NAME ) ) {
					attributes.put( NAME, name );
				}
				toInspectToUse = XmlUtils.getChildWithAttributeValue( toInspectToUse.getOwnerDocument().getDocumentElement(), getTopLevelTypeAttribute(), name );
				continue;
			}

			// Redirect to type (if any)

			if ( !toInspectToUse.hasChildNodes() && toInspectToUse.hasAttribute( TYPE ) ) {

				// Current toInspectToUse may have minOccurs, etc.

				inspectElement( toInspectToUse, attributes );

				if ( !attributes.containsKey( NAME ) ) {
					attributes.put( NAME, toInspectToUse.getAttribute( NAME ) );
				}
				Element typeToUse = XmlUtils.getChildWithAttributeValue( toInspectToUse.getOwnerDocument().getDocumentElement(), getTopLevelTypeAttribute(), toInspectToUse.getAttribute( TYPE ) );

				if ( typeToUse == null ) {
					break;
				}

				toInspectToUse = typeToUse;
				continue;
			}

			// Complex type may just contain a simple content

			if ( COMPLEX_TYPE.equals( toInspectToUse.getLocalName() ) ) {

				Element simpleContent = XmlUtils.getChildNamed( toInspectToUse, SIMPLE_CONTENT );

				if ( simpleContent != null ) {
					inspectExtension( simpleContent, attributes );
					inspectRestriction( simpleContent, attributes );
				}

				return attributes;
			}

			// Normal node

			if ( !attributes.containsKey( NAME ) && toInspectToUse.hasAttribute( NAME ) ) {
				attributes.put( NAME, toInspectToUse.getAttribute( NAME ) );
			}
			break;
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

			// Type inferred from nested complexType or simpleType

			Element complexType = XmlUtils.getChildNamed( element, COMPLEX_TYPE );

			if ( complexType != null ) {
				Element simpleContent = XmlUtils.getChildNamed( complexType, SIMPLE_CONTENT );

				if ( simpleContent != null ) {
					inspectExtension( simpleContent, attributes );
					inspectRestriction( simpleContent, attributes );
				}
			} else if ( SIMPLE_TYPE.equals( element.getLocalName() ) ) {

				inspectRestriction( element, attributes );

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

		if ( extension.hasAttribute( BASE ) ) {
			attributes.put( TYPE, extension.getAttribute( BASE ) );
		}
	}

	private void inspectRestriction( Element parent, Map<String, String> attributes ) {

		Element restriction = XmlUtils.getChildNamed( parent, RESTRICTION );

		if ( restriction == null ) {
			return;
		}

		if ( restriction.hasAttribute( BASE ) ) {
			attributes.put( TYPE, restriction.getAttribute( BASE ) );
		}

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

		// Lookup

		Element enumeration = XmlUtils.getChildNamed( restriction, ENUMERATION );

		if ( enumeration != null ) {
			List<String> lookup = CollectionUtils.newArrayList();

			while ( enumeration != null ) {
				lookup.add( enumeration.getAttribute( VALUE ) );
				enumeration = XmlUtils.getSiblingNamed( enumeration, ENUMERATION );
			}

			attributes.put( LOOKUP, CollectionUtils.toString( lookup ) );
		}
	}
}
