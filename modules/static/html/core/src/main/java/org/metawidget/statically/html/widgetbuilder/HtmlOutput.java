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

package org.metawidget.statically.html.widgetbuilder;

/**
 * Creates an &lt;output&gt; tag. Output tags are only available in HTML 5,
 * but earlier versions of HTML should degrade to a &lt;div&gt;.
 */

public class HtmlOutput
	extends HtmlTag
	implements IdHolder, NameHolder {

	//
	// Constructor
	//

	public HtmlOutput() {

		super( "output" );
	}

	//
	// Public methods
	//

	public void setId( String id ) {

		putAttribute( "id", id );
	}

	public String getId() {

		return getAttribute( "id" );
	}

	public void setName( String name ) {

		putAttribute( "name", name );
	}

	public String getName() {

		return getAttribute( "name" );
	}
}
