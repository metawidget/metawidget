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

	@Override
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

	@Override
	public void destroy() {

		// Do nothing
	}
}