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

package org.metawidget.vaadin.ui.widgetprocessor.binding.simple;

/**
 * Built-in Converter to convert Objects to Strings.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ToStringConverter
	implements Converter<Object, String> {

	//
	// Public methods
	//

	public String convert( Object value, Class<? extends String> actualType ) {

		if ( value == null ) {
			return "";
		}

		// Convert Enums to their name(), not their .toString(). We rely on
		// org.metawidget.vaadin.ui.widgetbuilder.LookupLabel to fix this up for presentation

		if ( value instanceof Enum ) {
			return ( (Enum<?>) value ).name();
		}

		return value.toString();
	}
}
