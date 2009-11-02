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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
		Element entity = (Element) inspectionResult.getFirstChild();

		// First, record all traits (ie. properties/actions) that have comes-after

		Map<Element,String[]> traitsWithComesAfter = new LinkedHashMap<Element, String[]>();
		NodeList properties = entity.getChildNodes();

		for ( int loop = 0, length = properties.getLength(); loop < length; loop++ )
		{
			Node node = properties.item( loop );

			if ( !( node instanceof Element ) )
				continue;

			Element property = (Element) node;

			if ( !property.hasAttribute( COMES_AFTER ) )
				continue;

			traitsWithComesAfter.put( property, ArrayUtils.fromString( property.getAttribute( COMES_AFTER )));
		}

		// Next, sort the traits

		int infiniteLoop = traitsWithComesAfter.size();
		infiniteLoop *= infiniteLoop;

		while ( !traitsWithComesAfter.isEmpty() )
		{
			infiniteLoop--;

			if ( infiniteLoop < 0 )
				throw InspectionResultProcessorException.newException( "Infinite loop detected when sorting " + COMES_AFTER );

			// For each entry in the Map...

			outer: for ( Iterator<Map.Entry<Element, String[]>> i = traitsWithComesAfter.entrySet().iterator(); i.hasNext(); )
			{
				Map.Entry<Element, String[]> entry = i.next();
				Element traitWithComesAfter = entry.getKey();
				String[] comesAfter = entry.getValue();

				// ...if it 'Comes After everything', make sure there are only
				// other 'Comes After everything's left...

				if ( comesAfter.length == 0 )
				{
					for ( String[] traitWithComesAfterExisting : traitsWithComesAfter.values() )
					{
						if ( traitWithComesAfterExisting.length > 0 )
							continue outer;
					}

					entity.appendChild( traitWithComesAfter );
				}

				// ...or, if it 'Comes After' something, make sure none of those
				// somethings are left...

				else
				{
					String name = traitWithComesAfter.getAttribute( NAME );

					for ( String comeAfter : comesAfter )
					{
						if ( name.equals( comeAfter ) )
							throw InspectionResultProcessorException.newException( "'" + comeAfter + "' " + COMES_AFTER + " itself" );

						for( Element traitExisting : traitsWithComesAfter.keySet() )
						{
							if ( comeAfter.equals( traitExisting.getAttribute( NAME )) )
								continue outer;
						}
					}

					// Insert it at the earliest point. This seems most 'natural'

					Node insertBefore = null;

					for ( int loop = 0, length = properties.getLength(); loop < length; loop++ )
					{
						Node node = properties.item( loop );

						if ( !( node instanceof Element ) )
							continue;

						Element property = (Element) node;

						if ( !ArrayUtils.contains( comesAfter, property.getAttribute( NAME )))
							continue;

						insertBefore = property.getNextSibling();
					}

					if ( insertBefore == null )
						entity.appendChild( traitWithComesAfter );
					else
						entity.insertBefore( traitWithComesAfter, insertBefore );
				}

				i.remove();
			}
		}

		return inspectionResult;
	}
}
