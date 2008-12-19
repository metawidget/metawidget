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

import javax.faces.component.EditableValueHolder;

import org.metawidget.faces.component.UIMetawidget;

/**
 * Convenience implementation.
 *
 * @author Richard Kennard
 */

public abstract class BaseValidator
	implements Validator
{
	//
	// Private members
	//

	private UIMetawidget	mMetawidget;

	//
	// Constructor
	//

	public BaseValidator( UIMetawidget metawidget )
	{
		mMetawidget = metawidget;
	}

	//
	// Protected methods
	//

	protected UIMetawidget getMetawidget()
	{
		return mMetawidget;
	}

	/**
	 * Gets the EditableValueHolder's existing Validator of the given type (if any).
	 * <p>
	 * Subclasses should generally check whether the component is already using the
	 * validator before attempting to add their own.
	 */

	protected javax.faces.validator.Validator getExistingValidator( EditableValueHolder editableValueHolder, Class<? extends javax.faces.validator.Validator> validatorClass )
	{
		javax.faces.validator.Validator[] validators = editableValueHolder.getValidators();

		if ( validators != null )
		{
			for( javax.faces.validator.Validator validator : validators )
			{
				if ( validatorClass.isAssignableFrom( validator.getClass() ))
					return validator;
			}
		}

		return null;
	}
}
