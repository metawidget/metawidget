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

package org.metawidget.statically;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StaticXmlStub
	extends BaseStaticXmlWidget {

	//
	// Constructor
	//

	public StaticXmlStub() {

		// Null prefix/namespace: stubs should never be output (kind of the point of being static)

		super( null, null, null );
	}
}
