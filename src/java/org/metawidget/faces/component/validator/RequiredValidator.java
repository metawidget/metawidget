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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;

/**
 * Validator that only sets the <code>required</code> attribute.
 * <p>
 * This is useful for Seam applications that use the <code>s:validateAll</code> tag, which
 * controls the validation process by using Hibernate Validator and does not work if other, regular
 * JSF validators are defined.
 *
 * @author Richard Kennard
 */

public class RequiredValidator
	extends ValidatorImpl
{
	//
	// Constructor
	//

	public RequiredValidator( UIMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	public void addValidators( FacesContext context, EditableValueHolder editableValueHolder, Map<String, String> attributes )
	{
		// JSF 1.2 support

		try
		{
			Method method = editableValueHolder.getClass().getMethod( "setLabel", String.class );
			method.invoke( editableValueHolder, getMetawidget().getLabelString( context, attributes ) );
		}
		catch ( Exception e )
		{
			// Fail gracefully
		}

		// Required

		if ( TRUE.equals( attributes.get( REQUIRED ) ) )
			editableValueHolder.setRequired( true );
	}
}
