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

package org.metawidget.statically.javacode;

/**
 * Represents a Java statement.
 * <p>
 * Statements will always be terminated by a semicolon. Their children will always be indented and
 * wrapped in braces.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JavaStatement
	extends BaseStaticJavaWidget {

	//
	// Constructor
	//

	public JavaStatement( String textContent ) {

		super( textContent );
	}
}
