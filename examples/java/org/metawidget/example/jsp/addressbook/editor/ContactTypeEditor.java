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

package org.metawidget.example.jsp.addressbook.editor;

import java.beans.PropertyEditorSupport;

import org.metawidget.example.shared.addressbook.model.ContactType;

/**
 * @author Richard Kennard
 */

public class ContactTypeEditor
	extends PropertyEditorSupport
{
	//
	//
	// Public methods
	//
	//

	@Override
	public String getAsText()
	{
		ContactType value = (ContactType) getValue();

		if ( value == null )
			return "";

		// Convert enums to their .name() form, not their .toString() form, so that we can
		// use .valueOf() in asText.

		return value.name();
	}

	@Override
	public void setAsText( String text )
		throws IllegalArgumentException
	{
		if ( text == null || "".equals( text ) )
		{
			setValue( null );
			return;
		}

		setValue( ContactType.valueOf( text ) );
	}
}