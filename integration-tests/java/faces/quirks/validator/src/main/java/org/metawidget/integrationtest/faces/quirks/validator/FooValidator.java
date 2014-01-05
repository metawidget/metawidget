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

package org.metawidget.integrationtest.faces.quirks.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@FacesValidator( "fooValidator" )
public class FooValidator
	implements Validator {

	//
	// Public methods
	//

	public void validate( FacesContext context, UIComponent component, Object value ) {

		if ( !( value instanceof String )) {
			throw new RuntimeException( "Value is not of " + String.class );
		}
		
		if ( "Foo".equals( value )) {
			throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, "Summary: value was " + value, "Detail: value was " + value ));
		}
	}
}
