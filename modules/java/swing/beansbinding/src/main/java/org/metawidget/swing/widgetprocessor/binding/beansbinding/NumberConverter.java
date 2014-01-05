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
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

class NumberConverter<T extends Number>
	extends Converter<T, String> {

	//
	// Private members
	//

	private Class<? extends Number>	mNumberClass;

	//
	// Constructor
	//

	public NumberConverter( Class<? extends T> numberClass ) {

		mNumberClass = numberClass;
	}

	//
	// Public methods
	//

	@Override
	public String convertForward( Number value ) {

		return String.valueOf( value );
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public T convertReverse( String value ) {

		// Empty Strings?
		//
		// See: https://sourceforge.net/projects/metawidget/forums/forum/747624/topic/3438867

		if ( value == null || value.trim().length() == 0 ) {
			return null;
		}

		// Other values

		if ( mNumberClass.equals( Byte.class ) ) {
			return (T) Byte.valueOf( value );
		}

		if ( mNumberClass.equals( Short.class ) ) {
			return (T) Short.valueOf( value );
		}

		if ( mNumberClass.equals( Integer.class ) ) {
			return (T) Integer.valueOf( value );
		}

		if ( mNumberClass.equals( Long.class ) ) {
			return (T) Long.valueOf( value );
		}

		if ( mNumberClass.equals( Float.class ) ) {
			return (T) Float.valueOf( value );
		}

		if ( mNumberClass.equals( Double.class ) ) {
			return (T) Double.valueOf( value );
		}

		throw WidgetProcessorException.newException( "Unknown type " + mNumberClass );
	}
}
