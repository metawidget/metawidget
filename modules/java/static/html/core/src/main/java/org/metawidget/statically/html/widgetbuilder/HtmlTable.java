// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.html.widgetbuilder;

/**
 * @author Ryan Bradley
 */

public class HtmlTable
	extends HtmlTag
	implements IdHolder {

	//
	// Constructor
	//

	public HtmlTable() {

		super( "table" );
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
