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

package org.metawidget.integrationtest.swt.converter;

import org.eclipse.core.databinding.conversion.Converter;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.util.ArrayUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StringToNestedWidgetsConverter
	extends Converter {

	//
	// Constructor
	//

	public StringToNestedWidgetsConverter() {

		super( String.class, NestedWidgets.class );
	}

	//
	// Public methods
	//

	public Object convert( Object toConvert ) {

		String[] values = ArrayUtils.fromString( (String) toConvert );

		if ( values.length == 0 ) {
			return null;
		}

		NestedWidgets nestedWidgets = new NestedWidgets();
		nestedWidgets.setNestedTextbox1( values[0] );

		if ( values.length > 1 ) {
			nestedWidgets.setNestedTextbox2( values[1] );
		}

		return nestedWidgets;
	}
}
