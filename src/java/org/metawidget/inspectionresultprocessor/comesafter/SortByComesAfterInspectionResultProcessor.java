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

package org.metawidget.inspectionresultprocessor.comesafter;

import static org.metawidget.inspector.InspectionResultConstants.*;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.util.ArrayUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Richard Kennard
 */

public class SortByComesAfterInspectionResultProcessor<M>
	implements InspectionResultProcessor<Element, M>
{
	//
	// Public methods
	//

	public Element processInspectionResult( Element inspectionResult, M metawidget )
	{
		Node entity = inspectionResult.getChildNodes().item( 0 );
		NodeList properties = entity.getChildNodes();

		for ( int loop = 0, length = properties.getLength(); loop < length; )
		{
			Node node = properties.item( loop );

			if ( !( node instanceof Element ))
			{
				loop++;
				continue;
			}

			Element property = (Element) node;

			if ( !property.hasAttribute( "comes-after" ) )
			{
				loop++;
				continue;
			}

			String[] comesAfter = ArrayUtils.fromString( property.getAttribute( COMES_AFTER ) );

			if ( comesAfter.length == 0 )
			{
				// Move all the way to the end

				entity.appendChild( property );
				length--;
				continue;
			}

			// Find the first available slot

			Element firstAvailableSlot = null;

			for ( int match = 0; match < length; match++ )
			{
				Node matchingNode = properties.item( match );

				if ( !( matchingNode instanceof Element ))
					continue;

				Element matchingElement = (Element) matchingNode;
				String name = matchingElement.getAttribute( NAME );

				if ( firstAvailableSlot != null && !ArrayUtils.contains( comesAfter, name ))
					continue;

				firstAvailableSlot = matchingElement;
			}

			// (should never happen)

			if ( firstAvailableSlot == null )
				throw InspectionResultProcessorException.newException( "No available slot" );

			Node insertBefore = firstAvailableSlot.getNextSibling();

			// Already in right place?

			if ( property.equals( insertBefore ))
			{
				loop++;
				continue;
			}

			// Move the node

			entity.insertBefore( property, insertBefore );
		}

		return inspectionResult;
	}
}
