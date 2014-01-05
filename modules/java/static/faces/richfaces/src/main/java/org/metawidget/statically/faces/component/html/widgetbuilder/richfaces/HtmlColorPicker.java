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

package org.metawidget.statically.faces.component.html.widgetbuilder.richfaces;

import org.metawidget.statically.faces.component.EditableValueHolder;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlColorPicker
	extends RichFacesWidget
	implements EditableValueHolder {

	//
	// Constructor
	//

	public HtmlColorPicker() {

		super( "colorPicker" );
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
