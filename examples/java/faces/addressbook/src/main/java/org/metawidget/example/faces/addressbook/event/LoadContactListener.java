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

package org.metawidget.example.faces.addressbook.event;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.metawidget.example.faces.addressbook.managedbean.ContactBean;
import org.metawidget.example.faces.addressbook.managedbean.ContactsBean;
import org.metawidget.example.shared.addressbook.model.Contact;

/**
 * PhaseListener to load a Contact from a Request parameter.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class LoadContactListener
	implements javax.faces.event.PhaseListener {

	//
	// Public methods
	//

	public PhaseId getPhaseId() {

		return PhaseId.RESTORE_VIEW;
	}

	public void beforePhase( PhaseEvent event ) {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();

		Map<String, String[]> parameters = externalContext.getRequestParameterValuesMap();

		String[] id = parameters.get( "contact.load" );

		if ( id != null ) {
			VariableResolver resolver = context.getApplication().getVariableResolver();

			ContactsBean contactsBean = (ContactsBean) resolver.resolveVariable( context, "contacts" );
			Contact contact = contactsBean.load( Long.valueOf( id[0] ) );

			ContactBean contactBean = (ContactBean) resolver.resolveVariable( context, "contact" );
			contactBean.setCurrent( contact );
		}
	}

	public void afterPhase( PhaseEvent event ) {

		// nop
	}
}