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

import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;

import org.metawidget.faces.component.UIMetawidget;

/**
 * Validator to add standard JSF validators to a widget.
 *
 * @author Richard Kennard
 */

public class StandardValidator
	extends ValidatorImpl
{
	//
	// Constructor
	//

	public StandardValidator( UIMetawidget metawidget )
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
		catch( Exception e )
		{
			// Fail gracefully
		}

		// Required

		if ( TRUE.equals( attributes.get( REQUIRED ) ) )
			editableValueHolder.setRequired( true );

		Application application = context.getApplication();

		// Range

		String minimumValue = attributes.get( MINIMUM_VALUE );
		String maximumValue = attributes.get( MAXIMUM_VALUE );

		if ( minimumValue != null || maximumValue != null )
		{
			String type = attributes.get( TYPE );

			if ( "double".equals( type ) || Double.class.getName().equals( type ) )
			{
				if ( getExistingValidator( editableValueHolder, DoubleRangeValidator.class ) == null )
				{
					DoubleRangeValidator validator = (DoubleRangeValidator) application.createValidator( "javax.faces.DoubleRange" );
					editableValueHolder.addValidator( validator );

					if ( minimumValue != null && !"".equals( minimumValue ))
						validator.setMinimum( Double.parseDouble( minimumValue ) );

					if ( maximumValue != null && !"".equals( maximumValue ))
						validator.setMaximum( Double.parseDouble( maximumValue ) );
				}
			}
			else
			{
				if ( getExistingValidator( editableValueHolder, LongRangeValidator.class ) == null )
				{
					LongRangeValidator validator = (LongRangeValidator) application.createValidator( "javax.faces.LongRange" );
					editableValueHolder.addValidator( validator );

					if ( minimumValue != null && !"".equals( minimumValue ))
						validator.setMinimum( Long.parseLong( minimumValue ) );

					if ( maximumValue != null && !"".equals( maximumValue ))
						validator.setMaximum( Long.parseLong( maximumValue ) );
				}
			}
		}

		// Length

		String minimumLength = attributes.get( MINIMUM_LENGTH );
		String maximumLength = attributes.get( MAXIMUM_LENGTH );

		if ( minimumLength != null || maximumLength != null )
		{
			if ( getExistingValidator( editableValueHolder, LengthValidator.class ) == null )
			{
				LengthValidator validator = (LengthValidator) application.createValidator( "javax.faces.Length" );

				if ( minimumLength != null && !"".equals( minimumLength ))
					validator.setMinimum( Integer.parseInt( minimumLength ) );

				if ( maximumLength != null && !"".equals( maximumLength ))
					validator.setMaximum( Integer.parseInt( maximumLength ) );

				editableValueHolder.addValidator( validator );
			}
		}
	}
}
