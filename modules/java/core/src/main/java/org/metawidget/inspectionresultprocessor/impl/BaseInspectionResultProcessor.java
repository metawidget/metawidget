// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.inspectionresultprocessor.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspectionresultprocessor.iface.DomInspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience implementation for InspectionResultProcessors.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BaseInspectionResultProcessor<M>
	implements DomInspectionResultProcessor<Element, M> {

	//
	// Public methods
	//

	/**
	 * Process the given inspection result in context of the given Metawidget.
	 * <p>
	 * This method is marked <code>final</code> because most Metawidget implementations will call
	 * <code>processInspectionResultAsDom</code> directly instead. So subclasses need to override
	 * <code>processInspectionResultAsDom</code>, not <code>processInspectionResult</code>.
	 */

	public final String processInspectionResult( String inspectionResult, M metawidget, Object toInspect, String type, String... names ) {

		Document document = XmlUtils.documentFromString( inspectionResult );
		Element inspectionResultRoot = document.getDocumentElement();

		Element newInspectionResultRoot = processInspectionResultAsDom( inspectionResultRoot, metawidget, toInspect, type, names );
		return XmlUtils.documentToString( newInspectionResultRoot.getOwnerDocument(), false );
	}

	public Element processInspectionResultAsDom( Element inspectionResult, M metawidget, Object toInspect, String type, String... names ) {

		Element entity = XmlUtils.getFirstChildElement( inspectionResult );

		// Sanity check

		String elementName = entity.getNodeName();

		if ( !ENTITY.equals( elementName ) ) {
			throw InspectionResultProcessorException.newException( "Top-level element name should be " + ENTITY + ", not " + elementName );
		}

		Map<String, String> attributes = XmlUtils.getAttributesAsMap( entity );
		processEntity( attributes, metawidget, toInspect, type, names );
		XmlUtils.setMapAsAttributes( entity, attributes );

		processTraits( entity, metawidget, toInspect, type, names );

		return inspectionResult;
	}

	//
	// Protected methods
	//

	/**
	 * Process the traits of the given entity. Subclasses may find it useful to override this method
	 * to perform before/after setup around trait processing (for example, establishing an EL
	 * context).
	 *
	 * @param entity
	 *            the DOM Element representing the entity that contains the traits
	 * @param metawidget
	 *            the parent Metawidget. Never null. May be useful to help processing
	 * @param toInspect
	 *            the Object being inspected. May be useful to help processing
	 * @param type
	 *            the type being inspected. May be useful to help processing
	 * @param names
	 *            the names being inspected. May be useful to help processing
	 */

	protected void processTraits( Element entity, M metawidget, Object toInspect, String type, String... names ) {

		// For each trait...

		Element trait = XmlUtils.getFirstChildElement( entity );

		while ( trait != null ) {

			// ...modify its attributes as appropriate

			Map<String, String> attributes = XmlUtils.getAttributesAsMap( trait );
			processTrait( attributes, metawidget );
			XmlUtils.setMapAsAttributes( trait, attributes );

			// If the trait has children, modify them too. This is not strictly in keeping with how
			// inspection-result-1.0.xsd is defined, but is very useful for
			// TypeMappingInspectionResultProcessor so that it can process embedded schemas before
			// returning them over a remote XML/JSON interface

			if ( trait.getChildNodes().getLength() > 0 && attributes.containsKey( NAME ) ) {
				processTraits( trait, metawidget, toInspect, type, ArrayUtils.add( names, attributes.get( NAME ) ) );
			}

			trait = XmlUtils.getNextSiblingElement( trait );
		}
	}

	/**
	 * Defers to <code>processAttributes</code> by default.
	 *
	 * @param attributes
	 *            attributes of the entity being processed. Subclasses can modify this Map to
	 *            modify the attributes
	 * @param metawidget
	 *            the parent Metawidget. Never null. May be useful to help processing
	 * @param toInspect
	 *            the Object being inspected. May be useful to help processing
	 * @param type
	 *            the type being inspected. May be useful to help processing
	 * @param names
	 *            the names being inspected. May be useful to help processing
	 */

	protected void processEntity( Map<String, String> attributes, M metawidget, Object toInspect, String type, String... names ) {

		processAttributes( attributes, metawidget );
	}

	/**
	 * Defers to <code>processAttributes</code> by default.
	 *
	 * @param attributes
	 *            attributes of the trait being processed. Subclasses can modify this Map to
	 *            modify the attributes
	 * @param metawidget
	 *            the parent Metawidget. Never null. May be useful to help processing
	 * @param toInspect
	 *            the Object being inspected. May be useful to help processing
	 * @param type
	 *            the type being inspected. May be useful to help processing
	 * @param names
	 *            the names being inspected. May be useful to help processing
	 */

	protected void processTrait( Map<String, String> attributes, M metawidget ) {

		processAttributes( attributes, metawidget );
	}

	/**
	 * Process the given attributes (which may belong to either entity, property or action).
	 * <p>
	 * Does nothing by default.
	 *
	 * @param attributes
	 *            attributes of the trait being processed. Subclasses can modify this Map to
	 *            modify the attributes
	 * @param metawidget
	 *            the parent Metawidget. Never null. May be useful to help processing
	 */

	protected void processAttributes( Map<String, String> attributes, M metawidget ) {

		// Do nothing by default
	}
}
