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

package org.metawidget.example.struts.addressbook.converter;

import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;

/**
 * Converts Objects to a String representation.
 *
 * @author Richard Kennard
 */

public class ToStringConverter
	implements Converter {

	//
	// Public methods
	//

	@SuppressWarnings( "rawtypes" )
	public Object convert( Class clazz, Object value ) {

		if ( value == null || "".equals( value ) ) {
			return null;
		}

		// Convert enums to their .name() form, not their .toString() form, so that we can
		// use .valueOf() in EnumConverter.

		if ( value instanceof Enum ) {
			return ( (Enum) value ).name();
		}

		// Do the Date toString here, as unfortunately it seems
		// org.apache.commons.beanutils.converters.DateConverter.convertToString never
		// gets called?

		if ( value instanceof Date ) {
			return DateFormat.getDateInstance( DateFormat.SHORT ).format( (Date) value );
		}

		return value.toString();
	}
}
