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

package org.metawidget.example.jsp.addressbook.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DateEditor
	extends PropertyEditorSupport {

	//
	// Private statics
	//

	private static final DateFormat	FORMAT	= DateFormat.getDateInstance( DateFormat.SHORT );

	static {
		FORMAT.setLenient( false );
	}

	//
	// Public methods
	//

	@Override
	public String getAsText() {

		Object value = getValue();

		if ( value == null ) {
			return "";
		}

		synchronized ( FORMAT ) {
			return FORMAT.format( value );
		}
	}

	@Override
	public void setAsText( String text ) {

		if ( text == null || "".equals( text ) ) {
			setValue( null );
			return;
		}

		try {
			synchronized ( FORMAT ) {
				setValue( FORMAT.parse( text ) );
			}
		} catch ( ParseException e ) {
			// Do not throw new IllegalArgumentException( e ), as it
			// gives a very ugly stack trace

			setValue( null );
		}
	}
}