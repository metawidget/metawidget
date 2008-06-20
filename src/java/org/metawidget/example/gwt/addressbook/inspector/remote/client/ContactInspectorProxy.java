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

package org.metawidget.example.gwt.addressbook.inspector.remote.client;

import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.gwt.remote.client.GwtRemoteInspectorProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GwtInspector optimized for inspecting Contacts.
 * <p>
 * Metawidget's separated Inspector architecture presents a number of opportunities for
 * optimization, depending on the requirements of the application. For AJAX-based applications,
 * where network traffic is critical, ContactInspectorProxy is an example of optimizing traffic by
 * reducing the amount of unnecessary data transmitted.
 * <p>
 * Note it is still perfectly possible to just use <code>GwtRemoteInspectorProxy</code> instead.
 *
 * @author Richard Kennard
 */

public class ContactInspectorProxy
	extends GwtRemoteInspectorProxy
{
	//
	//
	// Public methods
	//
	//

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * Overriden to optimize AJAX traffic by only sending blank objects, not populated ones.
	 */

	@Override
	public void inspect( Object toInspect, String type, String[] names, final AsyncCallback<String> callback )
	{
		Object superToInspect = toInspect;

		// Send blank versions of toInspect
		//
		// Note: we could maybe do this generically (in GwtRemoteInspectorProxy) if we
		// could hook into com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter?

		if ( toInspect instanceof PersonalContact )
			superToInspect = new PersonalContact();
		else if ( toInspect instanceof BusinessContact )
			superToInspect = new BusinessContact();

		super.inspect( superToInspect, type, names, callback );
	}
}
