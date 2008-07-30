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

public class LoadContactListener
	implements javax.faces.event.PhaseListener
{
	//
	//
	// Private statics
	//
	//

	private final static long	serialVersionUID	= 1057853553358600595L;

	//
	//
	// Public methods
	//
	//

	public PhaseId getPhaseId()
	{
		return PhaseId.RESTORE_VIEW;
	}

	public void beforePhase( PhaseEvent event )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();

		@SuppressWarnings( "unchecked" )
		Map<String, String[]> parameters = externalContext.getRequestParameterValuesMap();

		String[] id = parameters.get( "contact.load" );

		if ( id != null )
		{
			VariableResolver resolver = context.getApplication().getVariableResolver();

			ContactsBean contactsBean = (ContactsBean) resolver.resolveVariable( context, "contacts" );
			Contact contact = contactsBean.load( Long.valueOf( id[0] ) );

			ContactBean contactBean = (ContactBean) resolver.resolveVariable( context, "contact" );
			contactBean.setCurrent( contact );
		}
	}

	public void afterPhase( PhaseEvent event )
	{
		// nop
	}
}