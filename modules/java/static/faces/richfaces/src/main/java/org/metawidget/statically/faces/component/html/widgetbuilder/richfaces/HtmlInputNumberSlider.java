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

package org.metawidget.statically.faces.component.html.widgetbuilder.richfaces;

import org.metawidget.statically.faces.component.EditableValueHolder;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlInputNumberSlider
	extends RichFacesWidget
	implements EditableValueHolder {

	//
	// Constructor
	//

	public HtmlInputNumberSlider() {

		super( "inputNumberSlider" );
	}

	//
	// Public methods
	//

	public String getValue() {

		return getAttribute( "value" );
	}

	public void setValue( String value ) {

		putAttribute( "value", value );
	}

	public void setConverter( String value ) {

		putAttribute( "converter", value );
	}
}
