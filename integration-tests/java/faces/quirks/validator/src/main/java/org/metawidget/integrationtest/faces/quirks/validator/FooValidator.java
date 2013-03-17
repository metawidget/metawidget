// Metawidget (licensed under LGPL)
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

	public void validate( FacesContext context, UIComponent component, Object value ) {

		if ( !( value instanceof String )) {
			throw new RuntimeException( "Value is not of " + String.class );
		}
		
		if ( "Foo".equals( value )) {
			throw new ValidatorException( new FacesMessage( FacesMessage.SEVERITY_ERROR, "Summary: value was " + value, "Detail: value was " + value ));
		}
	}
}
