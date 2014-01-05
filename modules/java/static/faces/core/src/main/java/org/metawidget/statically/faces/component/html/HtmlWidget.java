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

package org.metawidget.statically.faces.component.html;

import org.metawidget.statically.BaseStaticXmlWidget;

/**
 * Widgets within the JSF h: namespace.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class HtmlWidget
	extends BaseStaticXmlWidget {

	//
	// Constructor
	//

	protected HtmlWidget( String tagName ) {

		super( "h", tagName, "http://java.sun.com/jsf/html" );
	}
}
