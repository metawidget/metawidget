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

package org.metawidget.example.struts.addressbook.plugin;

import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.metawidget.example.shared.addressbook.controller.CommunicationsController;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.struts.addressbook.converter.EnumConverter;
import org.metawidget.example.struts.addressbook.converter.ToStringConverter;

/**
 * @author Richard Kennard
 */

public class AddressBookPlugIn
	implements PlugIn {

	//
	// Public methods
	//

	public void init( ActionServlet servlet, ModuleConfig config ) {

		ServletContext context = servlet.getServletContext();

		// Application-wide Controllers

		context.setAttribute( "contacts", new ContactsController() );
		context.setAttribute( "communications", new CommunicationsController() );

		// Application-wide Converters

		ConvertUtils.register( new EnumConverter(), ContactType.class );
		ConvertUtils.register( new EnumConverter(), Gender.class );
		ConvertUtils.register( new ToStringConverter(), String.class );

		DateConverter converterDate = new DateConverter();
		converterDate.setLocale( Locale.getDefault() );
		ConvertUtils.register( converterDate, Date.class );
	}

	public void destroy() {

		// Do nothing
	}
}