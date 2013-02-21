// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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
	implements Converter {

	//
	// Private members
	//

	private Class<T>	mClass;

	//
	// Constructor
	//

	protected EnumConverter( Class<T> clazz ) {

		mClass = clazz;
	}

	//
	// Public methods
	//

	public final Object getAsObject( FacesContext context, UIComponent component, String value )
		throws ConverterException {

		if ( value == null || "".equals( value ) ) {
			return null;
		}

		return Enum.valueOf( mClass, value );
	}

	public final String getAsString( FacesContext context, UIComponent component, Object object )
		throws ConverterException {

		if ( object == null ) {
			return "";
		}

		if ( object instanceof String ) {
			return (String) object;
		}

		// Convert enums to their .name() form, not their .toString() form, so that we can
		// use .valueOf() in getAsObject.

		return ( (Enum<?>) object ).name();
	}
}
