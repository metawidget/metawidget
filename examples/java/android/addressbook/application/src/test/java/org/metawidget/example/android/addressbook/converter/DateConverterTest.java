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
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
// be used to endorse or promote products derived from this software without
// specific prior written permission.
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

package org.metawidget.example.android.addressbook.converter;

import java.text.DateFormat;
import java.util.Date;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class DateConverterTest
	extends TestCase {

	//
	// Private statics
	//

	private static final DateFormat	FORMAT	= DateFormat.getDateInstance( DateFormat.SHORT );

	//
	// Public methods
	//

	public void testConvertFromView()
		throws Exception {

		DateConverter converter = new DateConverter();
		assertEquals( null, converter.convertFromView( null, "", Date.class ) );

		synchronized ( FORMAT ) {
			Date date = new Date();
			date = FORMAT.parse( FORMAT.format( date ) );
			assertEquals( date, converter.convertFromView( null, FORMAT.format( date ), Date.class ) );
		}
	}

	public void testConvertForView()
		throws Exception {

		DateConverter converter = new DateConverter();
		assertEquals( null, converter.convertForView( null, null ) );

		synchronized ( FORMAT ) {
			Date date = new Date();
			assertEquals( FORMAT.format( date ), converter.convertForView( null, date ) );
		}
	}
}
