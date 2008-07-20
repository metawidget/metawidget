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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.struts.addressbook.form.BusinessContactForm;
import org.metawidget.example.struts.addressbook.form.PersonalContactForm;

/**
 * @author Richard Kennard
 */

public class SearchAction
	extends Action
{
	//
	//
	// Public methods
	//
	//

	@Override
	public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
		throws Exception
	{
		// Add Personal

		if ( request.getParameter( "addPersonal" ) != null )
		{
			request.getSession().setAttribute( "contactForm", new PersonalContactForm() );
			return mapping.findForward( "contact" );
		}

		// Add Business

		if ( request.getParameter( "addBusiness" ) != null )
		{
			request.getSession().setAttribute( "contactForm", new BusinessContactForm() );
			return mapping.findForward( "contact" );
		}

		// Search

		ContactSearch contactSearch = new ContactSearch();
		request.setAttribute( "contactSearch", contactSearch );

		DynaActionForm actionForm = (DynaActionForm) form;
		contactSearch.setFirstnames( (String) actionForm.get( "firstnames" ));
		contactSearch.setSurname( (String) actionForm.get( "surname" ));
		contactSearch.setType( (ContactType) actionForm.get( "type" ));

		return mapping.findForward( "results" );
	}
}
