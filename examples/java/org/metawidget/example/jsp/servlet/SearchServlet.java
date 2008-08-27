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

package org.metawidget.example.jsp.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.struts.addressbook.form.BusinessContactForm;
import org.metawidget.example.struts.addressbook.form.PersonalContactForm;

/**
 * @author Richard Kennard
 */

public class SearchServlet
	extends HttpServlet
{
	//
	//
	// Public methods
	//
	//

	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response )
		throws IOException
	{
		// Add Personal

		if ( request.getParameter( "addPersonal" ) != null )
		{
			request.getSession().setAttribute( "contact", new PersonalContactForm() );
			response.sendRedirect( "contact.jsp" );
			return;
		}

		// Add Business

		if ( request.getParameter( "addBusiness" ) != null )
		{
			request.getSession().setAttribute( "contact", new BusinessContactForm() );
			response.sendRedirect( "contact.jsp" );
			return;
		}

		// Search

		ContactSearch contactSearch = new ContactSearch();
		request.setAttribute( "contactSearch", contactSearch );

		contactSearch.setFirstnames( request.getParameter( "firstnames" ));
		contactSearch.setSurname( request.getParameter( "surname" ));

		String contactType = request.getParameter( "type" );

		if ( contactType != null )
			contactSearch.setType( ContactType.valueOf( contactType ));

		response.sendRedirect( "index.jsp" );
	}
}
