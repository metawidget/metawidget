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

package org.metawidget.statically.html.widgetbuilder;

/**
 * Creates an &lt;output&gt; tag. Output tags are only available in HTML 5,
 * but earlier versions of HTML should degrade to a &lt;div&gt;.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	//
	// Protected methods
	//

	@Override
	protected boolean isSelfClosing() {

		return false;
	}
}
