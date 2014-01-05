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

package org.metawidget.integrationtest.faces.allwidgets.converter;

import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.metawidget.util.CollectionUtils;

/**
 * Converts Collections to a String representation.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CollectionConverter
	implements Converter {

	//
	// Public methods
	//

	public Object getAsObject( FacesContext context, UIComponent component, String value ) {

		return CollectionUtils.fromString( value );
	}

	public String getAsString( FacesContext context, UIComponent component, Object value ) {

		return CollectionUtils.toString( (Collection<?>) value );
	}
}
