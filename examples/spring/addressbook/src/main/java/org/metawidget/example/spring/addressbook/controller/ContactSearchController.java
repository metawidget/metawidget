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
 * @author Richard Kennard
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