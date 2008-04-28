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

package org.metawidget.example.faces.addressbook.managedbean;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.faces.model.ListDataModel;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.Address;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class ContactBean
{
	//
	//
	// Private members
	//
	//

	private boolean			mReadOnly;

	private Contact			mContactCurrent;

	private ContactSearch	mContactSearch;

	private ListDataModel	mModelCommunications;

	//
	//
	// Constructor
	//
	//

	public ContactBean()
	{
		mContactSearch = new ContactSearch();
	}

	//
	//
	// Public methods
	//
	//

	public boolean isReadOnly()
	{
		return mReadOnly;
	}

	public void setReadOnly( boolean readOnly )
	{
		mReadOnly = readOnly;
	}

	public Contact getCurrent()
	{
		return mContactCurrent;
	}

	public void setCurrent( Contact contactCurrent )
	{
		mContactCurrent = contactCurrent;
		mModelCommunications = null;

		setReadOnly( mContactCurrent == null || mContactCurrent.getId() != 0 );
	}

	public ListDataModel getCurrentCommunications()
	{
		if ( mModelCommunications == null )
		{
			Contact contact = getCurrent();

			if ( contact == null )
			{
				mModelCommunications = new ListDataModel();
			}
			else
			{
				Set<Communication> communications = contact.getCommunications();

				if ( communications == null )
				{
					mModelCommunications = new ListDataModel();
				}
				else
				{
					// (sort for consistency in unit tests)

					List<Communication> sortedCommunications = CollectionUtils.newArrayList( communications );
					Collections.sort( sortedCommunications );

					mModelCommunications = new ListDataModel( sortedCommunications );
				}
			}
		}

		return mModelCommunications;
	}

	public ContactSearch getSearch()
	{
		return mContactSearch;
	}

	public void setSearch( ContactSearch search )
	{
		mContactSearch = search;
	}

	public void runSearch()
	{
		// Just refresh the screen
	}

	public List<Contact> getResults()
	{
		return getContactsController().getAllByExample( getSearch() );
	}

	public void load( long id )
	{
		Contact contact = getContactsController().load( id );

		if ( contact != null )
			setCurrent( contact );
	}

	public void edit()
	{
		mReadOnly = false;
	}

	public String addPersonal()
	{
		PersonalContact contact = new PersonalContact();
		contact.setAddress( new Address() );

		setCurrent( contact );

		return "addPersonal";
	}

	public String addBusiness()
	{
		BusinessContact contact = new BusinessContact();
		contact.setAddress( new Address() );

		setCurrent( contact );

		return "addBusiness";
	}

	public String save()
		throws Exception
	{
		try
		{
			getContactsController().save( mContactCurrent );
		}
		catch( Exception e )
		{
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return null;
		}

		return "index";
	}

	public String delete()
		throws Exception
	{
		getContactsController().delete( getCurrent().getId() );

		return "index";
	}

	public String cancel()
		throws Exception
	{
		return "index";
	}

	public void addCommunication()
	{
		CommunicationBean bean = getCommunicationManagedBean();

		try
		{
			getCurrent().addCommunication( bean.getCurrent() );
		}
		catch( Exception e )
		{
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return;
		}

		bean.clear();
		mModelCommunications = null;
	}

	public void deleteCommunication()
	{
		Communication communication = (Communication) mModelCommunications.getRowData();
		getCurrent().removeCommunication( communication );

		mModelCommunications = null;
	}

	//
	//
	// Private methods
	//
	//

	private ContactsController getContactsController()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		VariableResolver variableResolver = context.getApplication().getVariableResolver();

		return (ContactsController) variableResolver.resolveVariable( context, "contacts" );
	}

	private CommunicationBean getCommunicationManagedBean()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		VariableResolver variableResolver = context.getApplication().getVariableResolver();

		return (CommunicationBean) variableResolver.resolveVariable( context, "communication" );
	}
}
