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

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;

import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;

/**
 * @author Richard Kennard
 */

public class ContactSearchBean
{
	//
	//
	// Private members
	//
	//

	private ContactSearch	mCurrent;

	//
	//
	// Constructor
	//
	//

	public ContactSearchBean()
	{
		mCurrent = new ContactSearch();
	}

	//
	//
	// Public methods
	//
	//

	@UiHidden
	public ContactSearch getCurrent()
	{
		return mCurrent;
	}

	public void setCurrent( ContactSearch current )
	{
		mCurrent = current;
	}

	@UiHidden
	public List<Contact> getResults()
	{
		return getContactsBean().getAllByExample( mCurrent );
	}

	@UiAction
	public void search()
	{
		// Just refresh the screen
	}

	@UiAction
	@UiComesAfter( "search" )
	public String addPersonal()
	{
		getContactBean().setCurrent( new PersonalContact() );

		return "addPersonal";
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public String addBusiness()
	{
		getContactBean().setCurrent( new BusinessContact() );

		return "addBusiness";
	}

	//
	//
	// Private methods
	//
	//

	private ContactsBean getContactsBean()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		VariableResolver variableResolver = context.getApplication().getVariableResolver();

		return (ContactsBean) variableResolver.resolveVariable( context, "contacts" );
	}

	private ContactBean getContactBean()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		VariableResolver variableResolver = context.getApplication().getVariableResolver();

		return (ContactBean) variableResolver.resolveVariable( context, "contact" );
	}
}
