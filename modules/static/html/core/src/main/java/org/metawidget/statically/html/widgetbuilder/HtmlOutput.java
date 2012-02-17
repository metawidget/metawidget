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
 * Pseudo-tag because HTML lacks a tag for outputting a read-only value. &lt;label&gt; isn't quite
 * semantically correct (because <code>for</code> is required), and &lt;input type="text"
 * readonly&gt; looks ugly.
 * <p>
 * Basically what we need is a &lt;div&gt; with an id (so that a &lt;label&gt; can point to it).
 *
 * @author Richard Kennard
 */

public class HtmlOutput
	extends HtmlTag
	implements IdHolder {

	//
	// Constructor
	//

	public HtmlOutput() {

		super( "div" );
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
}
