// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.integrationtest.swing.allwidgets.converter.beanutils;

import org.apache.commons.beanutils.Converter;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NestedWidgetsConverter
	implements Converter {

	//
	// Public methods
	//

	@SuppressWarnings( "rawtypes" )
	public Object convert( Class clazz, Object value ) {

		// To String

		if ( clazz.equals( String.class ) ) {
			return StringUtils.quietValueOf( value );
		}

		// To NestedWidgets

		if ( clazz.equals( NestedWidgets.class ) ) {
			String[] values = ArrayUtils.fromString( (String) value );

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

		throw new UnsupportedOperationException( "Don't know how to convert '" + value + "' to a " + clazz );
	}
}
