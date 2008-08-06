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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.Contact;

public class LoadServlet
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
		String id = request.getParameter( "id" );

		if ( id != null )
		{
			// Look up the ContactsController...

			ServletContext context = request.getSession().getServletContext();
			ContactsController controller = (ContactsController) context.getAttribute( "contacts" );

			// ...and use it to load the Contact

			Contact contact = controller.load( Long.parseLong( id ) );
			request.getSession().setAttribute( "contact", contact );
		}

		response.sendRedirect( "contact.jsp" );
	}
}
