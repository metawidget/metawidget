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

package org.metawidget.swing.widgetprocessor.binding.beansbinding;

import org.jdesktop.beansbinding.Converter;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

class BooleanConverter
	extends Converter<Boolean, String> {

	//
	// Public methods
	//

	@Override
	public String convertForward( Boolean value ) {

		return String.valueOf( value );
	}

	@Override
	public Boolean convertReverse( String value ) {

		return Boolean.valueOf( value );
	}
}
