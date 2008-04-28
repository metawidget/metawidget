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

package org.metawidget.faces.component.validator;

import java.util.*;

import javax.faces.component.*;
import javax.faces.context.*;

import org.metawidget.faces.component.*;

/**
 * Base class for all JSF validators.
 * <p>
 * Implementations need not be Thread-safe.
 *
 * @author Richard Kennard
 */

public abstract class Validator
{
	//
	//
	// Private members
	//
	//

	private UIMetawidget	mMetawidget;

	//
	//
	// Constructor
	//
	//

	public Validator( UIMetawidget metawidget )
	{
		mMetawidget = metawidget;
	}

	//
	//
	// Public methods
	//
	//

	public abstract void addValidators( FacesContext context, UIComponent component, Map<String, String> attributes );

	//
	//
	// Protected methods
	//
	//

	protected UIMetawidget getMetawidget()
	{
		return mMetawidget;
	}
}
