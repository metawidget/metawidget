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

package org.metawidget.inspector.impl.propertystyle.javassist;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JavassistPropertyStyleDebugOffTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJavassist() {

		JavassistPropertyStyle propertyStyle = new JavassistPropertyStyle();

		try {
			propertyStyle.getProperties( Foo.class.getName() );
		} catch ( InspectorException e ) {

			// Note: this test will not trigger unless the code has been compiled with debug off

			String message = "Line number information for " + Foo.class.getName() + " not available. Did you compile without debug info?";
			assertEquals( message, e.getMessage() );
		}
	}

	//
	// Inner class
	//

	class Foo {

		public String getMethodFoo() {

			return null;
		}
	}
}
