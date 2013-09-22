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

package org.metawidget.example.struts.addressbook.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.struts.addressbook.form.BusinessContactForm;
import org.metawidget.example.struts.addressbook.form.ContactForm;
import org.metawidget.example.struts.addressbook.form.PersonalContactForm;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LoadAction
	extends Action {

	//
	// Public methods
	//

	@Override
	public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
		throws Exception {

		String id = request.getParameter( "id" );

		if ( id != null ) {
			// Look up the ContactsController...

			ServletContext context = request.getSession().getServletContext();
			ContactsController controller = (ContactsController) context.getAttribute( "contacts" );

			// ...use it to load the Contact...

			Contact contact = controller.load( Long.parseLong( id ) );

			// ...and map it to our Struts Form (this manual mapping step is an unfortunate artifact
			// of the way Struts 1.x forms must extend ActionForm, not just be regular POJOs).

			ContactForm formContact;

			if ( contact instanceof PersonalContact ) {
				formContact = new PersonalContactForm();
				PersonalContactForm formPersonalContact = (PersonalContactForm) formContact;
				PersonalContact contactPersonal = (PersonalContact) contact;

				formPersonalContact.setDateOfBirth( contactPersonal.getDateOfBirth() );
			} else {
				formContact = new BusinessContactForm();
				BusinessContactForm formBusinessContact = (BusinessContactForm) formContact;
				BusinessContact contactBusiness = (BusinessContact) contact;

				formBusinessContact.setCompany( contactBusiness.getCompany() );
				formBusinessContact.setNumberOfStaff( contactBusiness.getNumberOfStaff() );
			}

			formContact.setId( contact.getId() );
			formContact.setTitle( contact.getTitle() );
			formContact.setFirstname( contact.getFirstname() );
			formContact.setSurname( contact.getSurname() );
			formContact.setGender( contact.getGender() );
			formContact.setAddress( contact.getAddress() );
			formContact.setCommunications( contact.getCommunications() );
			formContact.setNotes( contact.getNotes() );
			formContact.setReadOnly( true );

			request.getSession().setAttribute( "contactForm", formContact );
		}

		return mapping.findForward( "contact" );
	}
}
