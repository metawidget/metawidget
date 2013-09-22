// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@SuppressWarnings( "deprecation" )
public class ContactSearchBean {

	//
	// Private members
	//

	private ContactSearch	mCurrent;

	//
	// Constructor
	//

	public ContactSearchBean() {

		mCurrent = new ContactSearch();
	}

	//
	// Public methods
	//

	@UiHidden
	public ContactSearch getCurrent() {

		return mCurrent;
	}

	public void setCurrent( ContactSearch current ) {

		mCurrent = current;
	}

	@UiHidden
	public List<Contact> getResults() {

		return getContactsBean().getAllByExample( mCurrent );
	}

	@UiAction
	public void search() {

		// Just refresh the screen
	}

	@UiAction
	@UiComesAfter( "search" )
	public String addPersonal() {

		getContactBean().setCurrent( new PersonalContact() );

		return "addPersonal";
	}

	@UiAction
	@UiComesAfter( "addPersonal" )
	public String addBusiness() {

		getContactBean().setCurrent( new BusinessContact() );

		return "addBusiness";
	}

	//
	// Private methods
	//

	private ContactsBean getContactsBean() {

		FacesContext context = FacesContext.getCurrentInstance();
		VariableResolver variableResolver = context.getApplication().getVariableResolver();

		return (ContactsBean) variableResolver.resolveVariable( context, "contacts" );
	}

	private ContactBean getContactBean() {

		FacesContext context = FacesContext.getCurrentInstance();
		VariableResolver variableResolver = context.getApplication().getVariableResolver();

		return (ContactBean) variableResolver.resolveVariable( context, "contact" );
	}
}
