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

package org.metawidget.inspectionresultprocessor.sort;

import static org.metawidget.inspector.InspectionResultConstants.COMES_AFTER;
import static org.metawidget.inspector.InspectionResultConstants.ENTITY;
import static org.metawidget.inspector.InspectionResultConstants.NAME;
import static org.metawidget.inspector.InspectionResultConstants.NAMESPACE;
import static org.metawidget.inspector.InspectionResultConstants.ROOT;

import java.util.Collection;
import java.util.Collections;
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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ComesAfterInspectionResultProcessor<M>
	extends BaseInspectionResultProcessor<M> {

	//
	// Private statics
	//

	private static final int	PERMANENT_MARK	= -1;

	//
	// Public methods
	//

	@Override
	public Element processInspectionResultAsDom( Element inspectionResult, M metawidget, Object toInspect, String type, String... names ) {

		try {
			Element entity = XmlUtils.getFirstChildElement( inspectionResult );

			// Prepare all traits as a topological graph (use LinkedHashMap and List
			// so we get a consistent ordering)

			Map<String, TopologicalElement> topologicalElements = CollectionUtils.newLinkedHashMap();

			Element trait = XmlUtils.getFirstChildElement( entity );

			while ( trait != null ) {
				topologicalElements.put( trait.getAttribute( NAME ), new TopologicalElement( trait ) );
				trait = XmlUtils.getNextSiblingElement( trait );
			}

			Collection<TopologicalElement> unmarkedNodes = CollectionUtils.newArrayList();

			for ( TopologicalElement topologicalElement : topologicalElements.values() ) {

				unmarkedNodes.add( topologicalElement );
				trait = topologicalElement.getElement();

				if ( hasComesAfter( trait, metawidget ) ) {

					String[] comesAfters = ArrayUtils.fromString( getComesAfter( trait, metawidget ) );

					if ( comesAfters.length == 0 ) {
						for ( TopologicalElement comesAfter : topologicalElements.values() ) {

							if ( !comesAfter.equals( topologicalElement ) ) {
								topologicalElement.addComesAfter( comesAfter );
							}
						}
						continue;
					}

					String traitName = trait.getAttribute( NAME );

					for ( String comesAfter : comesAfters ) {

						if ( comesAfter.equals( traitName ) ) {
							throw InspectionResultProcessorException.newException( '\'' + traitName + "' " + COMES_AFTER + " itself" );
						}

						TopologicalElement comesAfterElement = topologicalElements.get( comesAfter );
						
						if ( comesAfterElement == null ) {
							continue;
						}
						
						topologicalElement.addComesAfter( comesAfterElement );
					}
				}
			}

			// Sort the graph

			List<Element> sorted = CollectionUtils.newArrayList();
			topologicalSort( unmarkedNodes, sorted );

			// Reconstruct the DOM. First, start a new document (Android 1.1 did
			// not cope well with shuffling the nodes of an existing document)...

			Document newDocument = XmlUtils.newDocument();
			Element newInspectionResult = newDocument.createElementNS( NAMESPACE, ROOT );
			XmlUtils.setMapAsAttributes( newInspectionResult, XmlUtils.getAttributesAsMap( inspectionResult ) );
			newDocument.appendChild( newInspectionResult );

			Element newEntity = newDocument.createElementNS( NAMESPACE, ENTITY );
			XmlUtils.setMapAsAttributes( newEntity, XmlUtils.getAttributesAsMap( entity ) );
			newInspectionResult.appendChild( newEntity );

			// ...then import our sorted traits into it

			for ( Element sortedTrait : sorted ) {

				newEntity.appendChild( XmlUtils.importElement( newDocument, sortedTrait ) );
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

	//
	// Private methods
	//

	/**
	 * Visit the next unmarked node.
	 */

	private void topologicalSort( Collection<TopologicalElement> unmarkedNodes, List<Element> sorted ) {

		while ( !unmarkedNodes.isEmpty() ) {

			TopologicalElement node = unmarkedNodes.iterator().next();
			topologicalVisit( node, unmarkedNodes, sorted, unmarkedNodes.size() );
		}
	}

	private void topologicalVisit( TopologicalElement node, Collection<TopologicalElement> unmarkedNodes, List<Element> sorted, int temporaryMark ) {

		// If node has a permanent mark then all done

		if ( node.getMark() == PERMANENT_MARK ) {
			return;
		}

		// If node has a temporary mark then error

		if ( node.getMark() == temporaryMark ) {
			List<String> infiniteLoopNames = CollectionUtils.newArrayList();

			for ( TopologicalElement infiniteLoopNode : unmarkedNodes ) {

				String value;
				Element trait = infiniteLoopNode.getElement();
				String comesAfter = getComesAfter( trait, null );

				if ( comesAfter == null ) {
					continue;
				}

				if ( comesAfter.length() == 0 ) {
					value = "at the end";
				} else {
					value = "after " + comesAfter.replace( ",", " and " );
				}

				infiniteLoopNames.add( trait.getAttribute( NAME ) + " comes " + value );
			}

			// (sort for unit tests)

			Collections.sort( infiniteLoopNames );

			throw InspectionResultProcessorException.newException( "Infinite loop detected when sorting " + COMES_AFTER + ": " + CollectionUtils.toString( infiniteLoopNames, ", but " ) );
		}

		// Mark node temporarily

		node.setMark( temporaryMark );

		// Visit each dependent node

		for ( TopologicalElement comesAfter : node.getComesAfter() ) {
			topologicalVisit( comesAfter, unmarkedNodes, sorted, temporaryMark );
		}

		// Mark node permanently

		node.setMark( PERMANENT_MARK );
		unmarkedNodes.remove( node );

		// Add node to sorted list

		sorted.add( node.getElement() );
	}

	//
	// Inner class
	//

	/**
	 * Holds elements during topological sort.
	 */

	private static class TopologicalElement {

		//
		// Private members
		//

		private Element							mElement;

		private int								mMark;

		/**
		 * Use a List, not a Set, so that sorting is stable.
		 */

		private Collection<TopologicalElement>	mComesAfter	= CollectionUtils.newArrayList();

		//
		// Constructor
		//

		public TopologicalElement( Element element ) {

			mElement = element;
		}

		//
		// Public methods
		//

		public Element getElement() {

			return mElement;
		}

		public Collection<TopologicalElement> getComesAfter() {

			return mComesAfter;
		}

		public void addComesAfter( TopologicalElement comesAfter ) {

			mComesAfter.add( comesAfter );
		}

		public void setMark( int mark ) {

			mMark = mark;
		}

		public int getMark() {

			return mMark;
		}
	}
}
