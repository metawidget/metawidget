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

package org.metawidget.statically.faces.component.html.widgetbuilder;

import java.io.IOException;
import java.io.Writer;

import org.metawidget.statically.faces.component.ValueHolder;
import org.metawidget.statically.faces.component.html.HtmlWidget;

/**
 * @author Richard Kennard
 */

public class HtmlOutcomeTargetLink
	extends HtmlWidget
	implements ValueHolder {

	//
	// Constructor
	//

	public HtmlOutcomeTargetLink() {

		super( "link" );
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

	//
	// Protected methods
	//

	/**
	 * Overridden to suppress writing 'value' or 'converter' attributes for <tt>h:link</tt>.
	 * <p>
	 * We still want <tt>HtmlOutcomeTargetLink</tt> to implement <tt>ValueHolder</tt> though,
	 * because a) we want it to have an 'id' attribute based on its value binding; b)
	 * <tt>javax.faces.component.html.HtmlOutcomeTargetLink</tt> implements <tt>ValueHolder</tt>.
	 */

	@Override
	protected void writeAttribute( Writer writer, String name, String value )
		throws IOException {

		if ( "value".equals( name ) || "calendar".equals( name )) {
			return;
		}

		super.writeAttribute( writer, name, value );
	}
}
