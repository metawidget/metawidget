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

package org.metawidget.example.jsp.addressbook.controller;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.servlet.http.HttpSession;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.jsp.UiJspAttribute;
import org.metawidget.inspector.jsp.UiJspAttributes;

/**
 * @author Richard Kennard
 */

public class ContactController
{
	//
	//
	// Private members
	//
	//

	private HttpSession	mSession;

	private boolean		mReadOnly;

	//
	//
	// Constructor
	//
	//

	public ContactController( HttpSession session )
	{
		mSession = session;
	}

	//
	//
	// Public methods
	//
	//

	@UiHidden
	public boolean isReadOnly()
	{
		return mReadOnly;
	}

	public void setReadOnly( boolean readOnly )
	{
		mReadOnly = readOnly;
	}

	@UiAction
	@UiJspAttribute( name = HIDDEN, value = "${!contactController.readOnly}" )
	public void edit()
	{
		mReadOnly = false;
	}

	@UiAction
	@UiJspAttribute( name = HIDDEN, value = "${contactController.readOnly}" )
	public void save()
	{
		ContactsController contactsController = (ContactsController) mSession.getServletContext().getAttribute( "contacts" );
		Contact contact = (Contact) mSession.getAttribute( "contact" );

		contactsController.save( contact );
	}

	@UiAction
	@UiJspAttribute( name = HIDDEN, value = "${contactController.readOnly || contact.id == 0}" )
	@UiComesAfter( "save" )
	public void delete()
	{
		ContactsController contactsController = (ContactsController) mSession.getServletContext().getAttribute( "contacts" );
		Contact contact = (Contact) mSession.getAttribute( "contact" );

		contactsController.delete( contact );
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiJspAttributes( { @UiJspAttribute( name = LABEL, value = "Back", condition = "${contactController.readOnly}" ) } )
	public String cancel()
		throws Exception
	{
		return "index";
	}

	public void addCommunication()
	{
		Contact contact = (Contact) mSession.getAttribute( "contact" );
		Communication communication = (Communication) mSession.getAttribute( "communication" );

		contact.addCommunication( communication );
	}

	public void deleteCommunication( long communicationId )
	{
		Contact contact = (Contact) mSession.getAttribute( "contact" );
		contact.removeCommunication( communicationId );
	}
}
