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

package org.metawidget.inspectionresultprocessor.sort;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.inspectionresultprocessor.impl.BaseInspectionResultProcessor;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Sorts an inspection result by any <code>comes-after</code> attributes.
 * <p>
 * <code>comes-after</code> attributes can be added using the <code>UiComesAfter</code> annotation,
 * among other ways.
 *
 * @author Richard Kennard
 */

public class ComesAfterInspectionResultProcessor<M>
	extends BaseInspectionResultProcessor<M> {

	//
	// Public methods
	//

	@Override
	public Element processInspectionResultAsDom( Element inspectionResult, M metawidget, Object toInspect, String type, String... names ) {

		try {
			// Start a new document
			//
			// (Android 1.1 did not cope well with shuffling the nodes of an existing document)

			Document newDocument = XmlUtils.newDocument();
			Element newInspectionResult = newDocument.createElementNS( NAMESPACE, ROOT );
			XmlUtils.setMapAsAttributes( newInspectionResult, XmlUtils.getAttributesAsMap( inspectionResult ) );
			newDocument.appendChild( newInspectionResult );

			Element entity = XmlUtils.getFirstChildElement( inspectionResult );
			Element newEntity = newDocument.createElementNS( NAMESPACE, ENTITY );
			XmlUtils.setMapAsAttributes( newEntity, XmlUtils.getAttributesAsMap( entity ) );
			newInspectionResult.appendChild( newEntity );

			// Record all traits (ie. properties/actions) that have comes-after

			Map<Element, String[]> traitsWithComesAfter = new LinkedHashMap<Element, String[]>();
			Element trait = XmlUtils.getFirstChildElement( entity );

			while( trait != null ) {

				// (if no comes-after, move them across to the new document)

				if ( hasComesAfter( trait, metawidget ) ) {
					traitsWithComesAfter.put( trait, ArrayUtils.fromString( getComesAfter( trait, metawidget ) ) );
				} else {
					newEntity.appendChild( XmlUtils.importElement( newDocument, trait ) );
				}

				trait = XmlUtils.getNextSiblingElement( trait );
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
					List<String> comesAfterNames = CollectionUtils.newArrayList();

					for ( Map.Entry<Element, String[]> entry : traitsWithComesAfter.entrySet() ) {

						String value;

						if ( entry.getValue().length == 0 ) {
							value = "at the end";
						} else {
							value = "after " + ArrayUtils.toString( entry.getValue(), " and " );
						}

						comesAfterNames.add( entry.getKey().getAttribute( NAME ) + " comes " + value );
					}

					// (sort for unit tests)

					Collections.sort( comesAfterNames );

					throw InspectionResultProcessorException.newException( "Infinite loop detected when sorting " + COMES_AFTER + ": " + CollectionUtils.toString( comesAfterNames, ", but " ) );
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

						Element newTrait = XmlUtils.getFirstChildElement( newEntity );
						Element insertBefore = newTrait;

						while( newTrait != null ) {

							if ( ArrayUtils.contains( comesAfter, newTrait.getAttribute( NAME ) ) ) {
								newTrait = XmlUtils.getNextSiblingElement( newTrait );
								insertBefore = newTrait;
								continue;
							}

							newTrait = XmlUtils.getNextSiblingElement( newTrait );
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

			return newInspectionResult;
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
	 * http://blog.kennardconsulting.com/2010/07/customizing-which-form-fields-are_14.html
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
	 * http://blog.kennardconsulting.com/2010/07/customizing-which-form-fields-are_14.html
	 *
	 * @param metawidget
	 *            Metawidget doing the rendering
	 */

	protected String getComesAfter( Element element, M metawidget ) {

		return element.getAttribute( COMES_AFTER );
	}
}
