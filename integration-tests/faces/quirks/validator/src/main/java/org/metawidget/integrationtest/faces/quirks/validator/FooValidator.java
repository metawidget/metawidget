package org.metawidget.integrationtest.faces.quirks.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator( "fooValidator" )
public class FooValidator
	implements Validator {

	//
	// Public methods
	//

	public void validate( FacesContext context, UIComponent component, Object value )
		throws ValidatorException {

		if ( !( value instanceof String )) {
			throw new RuntimeException( "Value is not of " + String.class );
		}
		
		if ( "Foo".equals( value )) {
			throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, "Summary: value was " + value, "Detail: value was " + value ));
		}
	}
}
