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

package org.metawidget.example.struts.addressbook.form;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PersonalContactForm
	extends ContactForm {

	//
	// Private members
	//

	private String				mDateOfBirthAsString;

	private Date				mOfBirth;

	//
	// Public methods
	//

	/**
	 * Returns the Date of Birth, as a String.
	 * <p>
	 * Ideally, Struts 1.x will one day include the patch at
	 * https://issues.apache.org/struts/browse/STR-1455. Until then, it is necessary to do the Date
	 * conversion in the form.
	 */

	@UiComesAfter( "surname" )
	@UiLabel( "dateOfBirth" )
	public String getDateOfBirthAsString() {

		return mDateOfBirthAsString;
	}

	public void setDateOfBirthAsString( String ofBirthAsString ) {

		mDateOfBirthAsString = ofBirthAsString;
	}

	@UiHidden
	public Date getDateOfBirth() {

		return mOfBirth;
	}

	public void setDateOfBirth( Date ofBirth ) {

		mOfBirth = ofBirth;

		if ( mOfBirth != null ) {
			mDateOfBirthAsString = ConvertUtils.convert( mOfBirth );
		}
	}

	@Override
	public ActionErrors validate( ActionMapping mapping, HttpServletRequest request ) {

		ActionErrors errors = new ActionErrors();

		// Date of Birth

		try {
			if ( mDateOfBirthAsString != null ) {
				mOfBirth = (Date) ConvertUtils.convert( mDateOfBirthAsString, Date.class );
			}
		} catch ( Exception e ) {
			errors.add( "dateOfBirth", new ActionMessage( ConversionException.class.getName(), mDateOfBirthAsString ) );
		}

		// Base class

		errors.add( super.validate( mapping, request ) );

		return errors;
	}
}
