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

public class HtmlInput
	extends HtmlTag
	implements IdHolder, NameHolder, ValueHolder {

	//
	// Constructor
	//

	public HtmlInput() {

		super( "input" );
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

	public void setValue( String value ) {

		putAttribute( "value", value );
	}

	public String getValue() {

		return getAttribute( "value" );
	}
}
