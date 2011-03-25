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

package org.metawidget.example.jsp.addressbook.servlet;

import java.beans.PropertyEditorManager;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.metawidget.example.jsp.addressbook.editor.ContactTypeEditor;
import org.metawidget.example.jsp.addressbook.editor.DateEditor;
import org.metawidget.example.jsp.addressbook.editor.GenderEditor;
import org.metawidget.example.shared.addressbook.controller.CommunicationsController;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.Gender;

/**
 * @author Richard Kennard
 */

public class AddressBookContextListener
	implements ServletContextListener {

	//
	// Public methods
	//

	@Override
	public void contextInitialized( ServletContextEvent contextEvent ) {

		ServletContext context = contextEvent.getServletContext();

		// Application-wide Controllers

		context.setAttribute( "contacts", new ContactsController() );
		context.setAttribute( "communications", new CommunicationsController() );

		// Register PropertyEditors

		PropertyEditorManager.registerEditor( Gender.class, GenderEditor.class );
		PropertyEditorManager.registerEditor( Date.class, DateEditor.class );
		PropertyEditorManager.registerEditor( ContactType.class, ContactTypeEditor.class );
	}

	@Override
	public void contextDestroyed( ServletContextEvent arg0 ) {

		// Unregister PropertyEditors

		PropertyEditorManager.registerEditor( Gender.class, null );
		PropertyEditorManager.registerEditor( Date.class, null );
		PropertyEditorManager.registerEditor( ContactType.class, null );
	}
}
