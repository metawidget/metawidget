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

package org.metawidget.example.spring.addressbook.controller;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.spring.addressbook.editor.EnumEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Controller
@RequestMapping( value = "/index.html" )
public class ContactSearchController {

	//
	// Private members
	//

	@Autowired
	private ContactsController	mContactsController;

	//
	// Protected methods
	//

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {

		binder.registerCustomEditor( ContactType.class, new EnumEditor<ContactType>( ContactType.class ) );
	}

	@RequestMapping( method = RequestMethod.GET )
	public String initForm( @ModelAttribute( "contactSearch" ) ContactSearch contactSearch, ModelMap model ) {

		model.put( "contactResults", mContactsController.getAllByExample( contactSearch ) );
		return "index";
	}

	@RequestMapping( method = RequestMethod.POST, params={"addPersonal"} )
	public String addPersonal() {

		return "redirect:contact.html";
	}

	@RequestMapping( method = RequestMethod.POST, params={"addBusiness"} )
	public String addBusiness() {

		return "redirect:contact.html?business";
	}

	@RequestMapping( method = RequestMethod.POST )
	public String search( @ModelAttribute( "contactSearch" ) ContactSearch contactSearch, ModelMap model ) {

		model.put( "contactSearch", contactSearch );
		model.put( "contactResults", mContactsController.getAllByExample( contactSearch ) );
		return "index";
	}
}