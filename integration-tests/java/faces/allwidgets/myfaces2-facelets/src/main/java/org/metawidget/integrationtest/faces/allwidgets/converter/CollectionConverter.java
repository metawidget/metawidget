// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.integrationtest.faces.allwidgets.converter;

import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.metawidget.util.CollectionUtils;

/**
 * Converts Collections to a String representation.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@FacesConverter( forClass = Collection.class )
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
