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

package org.metawidget.example.gwt.addressbook.server.rpc;

import java.util.List;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsService;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Richard Kennard
 */

public class ContactsServiceImpl
	extends RemoteServiceServlet
	implements ContactsService
{
	//
	// Private statics
	//

	private static final long	serialVersionUID	= 4260373927173571355L;

	//
	// Private members
	//

	private ContactsController	mContactsController	= new ContactsController();

	//
	// Public methods
	//

	public List<Contact> getAllByExample( ContactSearch search )
	{
		return mContactsController.getAllByExample( search );
	}

	public Contact load( long id )
	{
		return mContactsController.load( id );
	}

	public void save( Contact contact )
		throws Exception
	{
		mContactsController.save( contact );
	}

	public boolean delete( Contact contact )
	{
		return mContactsController.delete( contact );
	}

	public boolean delete( long id )
	{
		return mContactsController.delete( id );
	}
}
