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

package org.metawidget.example.spring.addressbook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.spring.addressbook.editor.EnumEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ContactSearchController
	extends SimpleFormController {

	//
	// Protected methods
	//

	@Override
	protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder )
		throws Exception {

		super.initBinder( request, binder );

		binder.registerCustomEditor( ContactType.class, new EnumEditor<ContactType>( ContactType.class ) );
	}

	@Override
	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
		throws Exception {

		// Add Personal

		if ( request.getParameter( "addPersonal" ) != null ) {
			return new ModelAndView( new RedirectView( "contact.html" ) );
		}

		// Add Business

		if ( request.getParameter( "addBusiness" ) != null ) {
			return new ModelAndView( new RedirectView( "contact.html?business" ) );
		}

		// Search

		return showForm( request, response, errors );
	}
}