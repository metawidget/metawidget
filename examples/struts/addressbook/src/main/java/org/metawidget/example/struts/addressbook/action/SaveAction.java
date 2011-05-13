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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.struts.addressbook.form.BusinessContactForm;
import org.metawidget.example.struts.addressbook.form.ContactForm;
import org.metawidget.example.struts.addressbook.form.PersonalContactForm;
import org.metawidget.util.simple.ClassUtils;

/**
 * @author Richard Kennard
 */

public class SaveAction
	extends Action {

	//
	// Public methods
	//

	@Override
	public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
		throws Exception {

		// Sanity check

		if ( form == null ) {
			return mapping.findForward( "home" );
		}

		// Support editing

		ContactForm formContact = (ContactForm) form;

		if ( request.getParameter( "edit" ) != null ) {
			formContact.setReadOnly( false );
			return mapping.findForward( "contact" );
		}

		// Look up the ContactsController...

		ServletContext context = request.getSession().getServletContext();
		ContactsController controller = (ContactsController) context.getAttribute( "contacts" );

		// ...map from our Struts Form (this manual mapping step is an unfortunate artifact
		// of the way Struts 1.x forms must extend ActionForm, not just be regular POJOs).

		Contact contact;

		if ( formContact instanceof PersonalContactForm ) {
			contact = new PersonalContact();

			PersonalContactForm formPersonalContact = (PersonalContactForm) form;
			PersonalContact contactPersonal = (PersonalContact) contact;

			contactPersonal.setDateOfBirth( formPersonalContact.getDateOfBirth() );
		} else {
			contact = new BusinessContact();

			BusinessContactForm formBusinessContact = (BusinessContactForm) form;
			BusinessContact contactBusiness = (BusinessContact) contact;

			contactBusiness.setCompany( formBusinessContact.getCompany() );
			contactBusiness.setNumberOfStaff( formBusinessContact.getNumberOfStaff() );
		}

		contact.setId( formContact.getId() );
		contact.setTitle( formContact.getTitle() );
		contact.setFirstname( formContact.getFirstname() );
		contact.setSurname( formContact.getSurname() );
		contact.setGender( formContact.getGender() );
		contact.setAddress( formContact.getAddress() );
		contact.setCommunications( formContact.getCommunications() );
		contact.setNotes( formContact.getNotes() );

		// Add Communication (if any)

		if ( request.getParameter( "addCommunication" ) != null ) {
			String type = request.getParameter( "communication.type" );
			String value = request.getParameter( "communication.value" );

			try {
				contact.addCommunication( new Communication( type, value ) );
				formContact.setCommunications( contact.getCommunications() );
			} catch ( Exception e ) {
				ActionErrors errors = new ActionErrors();
				errors.add( "save", new ActionMessage( e.getMessage(), false ) );
				addErrors( request, errors );

				return mapping.findForward( "contact" );
			}
		}

		// Delete Communication (if any)

		else if ( request.getParameter( "deleteCommunication" ) != null ) {
			String id = request.getParameter( "deleteCommunicationId" );
			contact.removeCommunication( Long.parseLong( id ) );
			formContact.setCommunications( contact.getCommunications() );
		}

		// Save

		if ( request.getParameter( "save" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null ) {
			try {
				controller.save( contact );
				formContact.setId( contact.getId() );
			} catch ( Exception e ) {
				ActionErrors errors = new ActionErrors();
				String message = e.getMessage();

				if ( message == null ) {
					message = ClassUtils.getSimpleName( e.getClass() );
				}

				errors.add( "save", new ActionMessage( message, false ) );
				addErrors( request, errors );

				return mapping.findForward( "contact" );
			}

			if ( request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null ) {
				return mapping.findForward( "contact" );
			}
		}

		// Delete

		if ( request.getParameter( "delete" ) != null ) {
			controller.delete( contact );
		}

		// Cancel

		return mapping.findForward( "home" );
	}
}
