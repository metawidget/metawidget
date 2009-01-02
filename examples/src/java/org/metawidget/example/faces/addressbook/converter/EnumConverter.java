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

package org.metawidget.example.faces.addressbook.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Converts Enums to/from a String representation.
 * <p>
 * Unlike the built-in <code>javax.faces.convert.EnumConverter</code>, which <a
 * href="https://javaserverfaces-spec-public.dev.java.net/issues/show_bug.cgi?id=470">may need
 * fixing</a>, converts to String via <code>.name()</code>, which is far more reliable.
 *
 * @author Richard Kennard
 */

public abstract class EnumConverter<T extends Enum<T>>
	implements Converter
{
	//
	// Private members
	//

	private Class<T>	mClass;

	//
	// Constructor
	//

	protected EnumConverter( Class<T> clazz )
	{
		mClass = clazz;
	}

	//
	// Public methods
	//

	public final Object getAsObject( FacesContext context, UIComponent component, String value )
		throws ConverterException
	{
		if ( value == null || "".equals( value ) )
			return null;

		return Enum.valueOf( mClass, value );
	}

	@SuppressWarnings( "unchecked" )
	public final String getAsString( FacesContext context, UIComponent component, Object object )
		throws ConverterException
	{
		if ( object == null )
			return "";

		if ( object instanceof String )
			return (String) object;

		// Convert enums to their .name() form, not their .toString() form, so that we can
		// use .valueOf() in getAsObject.

		return ( (Enum) object ).name();
	}
}
