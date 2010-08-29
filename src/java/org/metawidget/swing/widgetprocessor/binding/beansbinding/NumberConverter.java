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

package org.metawidget.swing.widgetprocessor.binding.beansbinding;

import org.jdesktop.beansbinding.Converter;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * @author Richard Kennard
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
