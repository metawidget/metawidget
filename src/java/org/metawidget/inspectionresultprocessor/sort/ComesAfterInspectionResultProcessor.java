// Metawidget
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

package org.metawidget.inspectionresultprocessor.sort;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.metawidget.inspectionresultprocessor.iface.DomInspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Sorts an inspection result by any <code>comes-after</code> attributes.
 * <p>
 * <code>comes-after</code> attributes can be added using the <code>UiComesAfter</code> annotation,
 * among other ways.
 *
 * @author Richard Kennard
 */

public class ComesAfterInspectionResultProcessor<M>
	implements InspectionResultProcessor<M>, DomInspectionResultProcessor<Element, M> {

	//
	// Public methods
	//

	public String processInspectionResult( String inspectionResult, M metawidget ) {

		Document document = XmlUtils.documentFromString( inspectionResult );
		Element inspectionResultRoot = document.getDocumentElement();

		Element newInspectionResultRoot = processInspectionResultAsDom( inspectionResultRoot, metawidget );
		return XmlUtils.documentToString( newInspectionResultRoot.getOwnerDocument(), false );
	}

	public Element processInspectionResultAsDom( Element inspectionResultRoot, M metawidget ) {

		try {
			// Start a new document
			//
			// (Android 1.1 did not cope well with shuffling the nodes of an existing document)

			Document newDocument = XmlUtils.newDocument();
			Element newInspectionResultRoot = newDocument.createElementNS( NAMESPACE, ROOT );
			XmlUtils.setMapAsAttributes( newInspectionResultRoot, XmlUtils.getAttributesAsMap( inspectionResultRoot ) );
			newDocument.appendChild( newInspectionResultRoot );

			Element entity = (Element) inspectionResultRoot.getChildNodes().item( 0 );
			Element newEntity = newDocument.createElementNS( NAMESPACE, ENTITY );
			XmlUtils.setMapAsAttributes( newEntity, XmlUtils.getAttributesAsMap( entity ) );
			newInspectionResultRoot.appendChild( newEntity );

			// Record all traits (ie. properties/actions) that have comes-after

			Map<Element, String[]> traitsWithComesAfter = new LinkedHashMap<Element, String[]>();
			NodeList traits = entity.getChildNodes();

			for ( int loop = 0, length = traits.getLength(); loop < length; loop++ ) {
				Node node = traits.item( loop );

				if ( !( node instanceof Element ) ) {
					continue;
				}

				Element trait = (Element) node;

				// (if no comes-after, move them across to the new document)

				if ( !hasComesAfter( trait, metawidget ) ) {
					newEntity.appendChild( XmlUtils.importElement( newDocument, trait ) );
					continue;
				}

				traitsWithComesAfter.put( trait, ArrayUtils.fromString( getComesAfter( trait, metawidget ) ) );
			}

			// Next, sort the traits
			//
			// Note: this has high big-O complexity. We could plug-in something smarter for large
			// numbers of traits.

			int infiniteLoop = traitsWithComesAfter.size();
			infiniteLoop *= infiniteLoop;

			while ( !traitsWithComesAfter.isEmpty() ) {
				// Infinite loop? Explain why

				infiniteLoop--;

				if ( infiniteLoop < 0 ) {
					List<String> names = CollectionUtils.newArrayList();

					for ( Map.Entry<Element, String[]> entry : traitsWithComesAfter.entrySet() ) {
						names.add( entry.getKey().getAttribute( NAME ) + " comes after " + ArrayUtils.toString( entry.getValue(), " and " ) );
					}

					// (sort for unit tests)

					Collections.sort( names );

					throw InspectionResultProcessorException.newException( "Infinite loop detected when sorting " + COMES_AFTER + ": " + CollectionUtils.toString( names, ", but " ) );
				}

				// For each entry in the Map...

				outer: for ( Iterator<Map.Entry<Element, String[]>> i = traitsWithComesAfter.entrySet().iterator(); i.hasNext(); ) {
					Map.Entry<Element, String[]> entry = i.next();
					Element traitWithComesAfter = entry.getKey();
					String[] comesAfter = entry.getValue();

					// ...if it 'comesAfter everything', make sure there are only
					// other 'comesAfter everything's left...

					if ( comesAfter.length == 0 ) {
						for ( String[] traitWithComesAfterExisting : traitsWithComesAfter.values() ) {
							if ( traitWithComesAfterExisting.length > 0 ) {
								continue outer;
							}
						}

						newEntity.appendChild( XmlUtils.importElement( newDocument, traitWithComesAfter ) );
					}

					// ...or, if it 'comesAfter' something, make sure none of those
					// somethings are left...

					else {
						String name = traitWithComesAfter.getAttribute( NAME );

						for ( String comeAfter : comesAfter ) {
							if ( name.equals( comeAfter ) ) {
								throw InspectionResultProcessorException.newException( "'" + comeAfter + "' " + COMES_AFTER + " itself" );
							}

							for ( Element traitExisting : traitsWithComesAfter.keySet() ) {
								if ( comeAfter.equals( traitExisting.getAttribute( NAME ) ) ) {
									continue outer;
								}
							}
						}

						// Insert it at the earliest point. This seems most 'natural'

						NodeList newTraits = newEntity.getChildNodes();
						Node insertBefore = newTraits.item( 0 );

						for ( int loop = 0, last = newTraits.getLength() - 1; loop <= last; loop++ ) {
							Node node = newTraits.item( loop );

							if ( !( node instanceof Element ) ) {
								continue;
							}

							Element trait = (Element) node;

							if ( !ArrayUtils.contains( comesAfter, trait.getAttribute( NAME ) ) ) {
								continue;
							}

							// (Android throws an error for the last .getNextSibling)

							if ( loop == last ) {
								insertBefore = null;
							} else {
								insertBefore = trait.getNextSibling();
							}
						}

						if ( insertBefore == null ) {
							newEntity.appendChild( XmlUtils.importElement( newDocument, traitWithComesAfter ) );
						} else {
							newEntity.insertBefore( XmlUtils.importElement( newDocument, traitWithComesAfter ), insertBefore );
						}
					}

					i.remove();
				}
			}

			return newInspectionResultRoot;
		} catch ( Exception e ) {
			throw InspectionResultProcessorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	/**
	 * Hook for subclasses wishing to customize the 'comes-after' indicator.
	 * <p>
	 * This can be useful is, say, you wanted to implement different 'comes-after' values for
	 * different screens. You may have a 'comes-after-summary' attribute and a 'comes-after-detail'
	 * attribute, and choose between them based on some property of the Metawidget. Similar to the
	 * approach discussed here:
	 * http://kennardconsulting.blogspot.com/2010/07/customizing-which-form-fields-are_14.html
	 *
	 * @param metawidget
	 *            Metawidget doing the rendering
	 */

	protected boolean hasComesAfter( Element element, M metawidget ) {

		return element.hasAttribute( COMES_AFTER );
	}

	/**
	 * Hook for subclasses wishing to customize the 'comes-after' indicator.
	 * <p>
	 * This can be useful is, say, you wanted to implement different 'comes-after' values for
	 * different screens. You may have a 'comes-after-summary' attribute and a 'comes-after-detail'
	 * attribute, and choose between them based on some property of the Metawidget. Similar to the
	 * approach discussed here:
	 * http://kennardconsulting.blogspot.com/2010/07/customizing-which-form-fields-are_14.html
	 *
	 * @param metawidget
	 *            Metawidget doing the rendering
	 */

	protected String getComesAfter( Element element, M metawidget ) {

		return element.getAttribute( COMES_AFTER );
	}
}
