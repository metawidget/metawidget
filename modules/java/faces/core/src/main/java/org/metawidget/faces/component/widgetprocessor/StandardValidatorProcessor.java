// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.faces.component.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor to add standard JSF validators to a UIComponent.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardValidatorProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Private statics
	//

	private static final boolean	BEAN_VALIDATION_AVAILABLE;

	static {

		boolean isBeanValidationAvailable;

		try {

			// This is how MyFaces does it (javax.faces.validator._ExternalSpecifications)

			Class.forName( "javax.validation.Validation" );
			isBeanValidationAvailable = true;

		} catch ( ClassNotFoundException e ) {

			isBeanValidationAvailable = false;
		}

		BEAN_VALIDATION_AVAILABLE = isBeanValidationAvailable;
	}

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Only validate EditableValueHolders (ie. no labels, no Stubs)

		if ( !( component instanceof EditableValueHolder ) ) {
			return component;
		}

		EditableValueHolder editableValueHolder = (EditableValueHolder) component;

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		// If Bean Validation is available, just use a BeanValidator. See
		// https://issues.apache.org/jira/browse/MYFACES-3299

		if ( isBeanValidationAvailable() ) {

			BeanValidator validator = (BeanValidator) application.createValidator( BeanValidator.VALIDATOR_ID );
			editableValueHolder.addValidator( validator );

			return component;
		}

		// Range

		String minimumValue = attributes.get( MINIMUM_VALUE );
		String maximumValue = attributes.get( MAXIMUM_VALUE );

		if ( minimumValue != null || maximumValue != null ) {
			String type = attributes.get( ACTUAL_CLASS );

			if ( type == null ) {
				type = attributes.get( TYPE );
			}

			if ( double.class.getName().equals( type ) || Double.class.getName().equals( type ) ) {
				if ( !hasExistingValidator( editableValueHolder, DoubleRangeValidator.class ) ) {
					DoubleRangeValidator validator = (DoubleRangeValidator) application.createValidator( DoubleRangeValidator.VALIDATOR_ID );
					editableValueHolder.addValidator( validator );

					if ( minimumValue != null && !"".equals( minimumValue ) ) {
						validator.setMinimum( Double.parseDouble( minimumValue ) );
					}

					if ( maximumValue != null && !"".equals( maximumValue ) ) {
						validator.setMaximum( Double.parseDouble( maximumValue ) );
					}
				}
			} else {
				if ( !hasExistingValidator( editableValueHolder, LongRangeValidator.class ) ) {
					LongRangeValidator validator = (LongRangeValidator) application.createValidator( LongRangeValidator.VALIDATOR_ID );
					editableValueHolder.addValidator( validator );

					if ( minimumValue != null && !"".equals( minimumValue ) ) {
						validator.setMinimum( Long.parseLong( minimumValue ) );
					}

					if ( maximumValue != null && !"".equals( maximumValue ) ) {
						validator.setMaximum( Long.parseLong( maximumValue ) );
					}
				}
			}
		}

		// Length

		String minimumLength = attributes.get( MINIMUM_LENGTH );
		String maximumLength = attributes.get( MAXIMUM_LENGTH );

		if ( minimumLength != null || maximumLength != null ) {
			if ( !hasExistingValidator( editableValueHolder, LengthValidator.class ) ) {
				LengthValidator validator = (LengthValidator) application.createValidator( LengthValidator.VALIDATOR_ID );

				if ( minimumLength != null && !"".equals( minimumLength ) ) {
					validator.setMinimum( Integer.parseInt( minimumLength ) );
				}

				if ( maximumLength != null && !"".equals( maximumLength ) ) {
					validator.setMaximum( Integer.parseInt( maximumLength ) );
				}

				editableValueHolder.addValidator( validator );
			}
		}

		return component;
	}

	//
	// Protected methods
	//

	/**
	 * Exposed for unit tests.
	 */

	protected boolean isBeanValidationAvailable() {

		return BEAN_VALIDATION_AVAILABLE;
	}

	//
	// Private methods
	//

	private boolean hasExistingValidator( EditableValueHolder editableValueHolder, Class<? extends Validator> validatorClass ) {

		Validator[] validators = editableValueHolder.getValidators();

		if ( validators == null ) {
			return false;
		}

		for ( Validator validator : validators ) {
			if ( validatorClass.isAssignableFrom( validator.getClass() ) ) {
				return true;
			}
		}

		return false;
	}
}
