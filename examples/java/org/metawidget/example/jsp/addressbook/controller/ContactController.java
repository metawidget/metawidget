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

	private boolean			mReadOnly;

	private Contact			mCurrent;

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

	@UiHidden
	public Contact getCurrent()
	{
		return mCurrent;
	}

	public void setCurrent( Contact current )
	{
		mCurrent = current;

		setReadOnly( mCurrent == null || mCurrent.getId() != 0 );
	}

	@UiAction
	@UiJspAttribute( name = HIDDEN, value = "#{!contact.readOnly}" )
	public void edit()
	{
		mReadOnly = false;
	}

	@UiAction
	@UiJspAttribute( name = HIDDEN, value = "${contact.readOnly}" )
	public String save()
		throws Exception
	{
		try
		{
			//getContactsBean().save( mCurrent );
		}
		catch ( Exception e )
		{
			// TODO:FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return null;
		}

		return "index";
	}

	@UiAction
	@UiJspAttribute( name = HIDDEN, value = "${contact.readOnly || contact.current.id == 0}" )
	@UiComesAfter( "save" )
	public String delete()
		throws Exception
	{
		//getContactsBean().delete( getCurrent().getId() );

		return "index";
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiJspAttributes( { @UiJspAttribute( name = LABEL, value = "Back", condition = "#{contact.readOnly}" ) } )
	public String cancel()
		throws Exception
	{
		return "index";
	}

	public void addCommunication()
	{
		//CommunicationBean bean = getCommunicationBean();

		try
		{
			//getCurrent().addCommunication( bean.getCurrent() );
		}
		catch ( Exception e )
		{
			// TODO: FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return;
		}

		//bean.clear();
	}

	public void deleteCommunication()
	{
		//Communication communication = (Communication) mModelCommunications.getRowData();
		//getCurrent().removeCommunication( communication );
	}
}
