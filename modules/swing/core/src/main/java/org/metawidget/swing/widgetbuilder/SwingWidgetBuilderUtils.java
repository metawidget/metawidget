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

package org.metawidget.swing.widgetbuilder;

import java.util.List;
import java.util.Map;

import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * Utilities for Swing WidgetBuilders.
 *
 * @author Richard Kennard
 */

public final class SwingWidgetBuilderUtils {

	//
	// Public statics
	//

	public static Map<String, String> getLabelsMap( List<String> values, List<String> labels ) {

		Map<String, String> labelsMap = CollectionUtils.newHashMap();

		if ( labels.size() != values.size() ) {
			throw WidgetBuilderException.newException( "Labels list must be same size as values list" );
		}

		for ( int loop = 0, length = values.size(); loop < length; loop++ ) {
			labelsMap.put( values.get( loop ), labels.get( loop ) );
		}

		return labelsMap;
	}

	//
	// Private constructor
	//

	private SwingWidgetBuilderUtils() {

		// Can never be called
	}
}
