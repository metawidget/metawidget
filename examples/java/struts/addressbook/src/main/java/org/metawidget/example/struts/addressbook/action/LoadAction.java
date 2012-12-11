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
 * @author Richard Kennard
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
