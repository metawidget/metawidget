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

package org.metawidget.inspector.impl.propertystyle.javassist;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;

/**
 * @author Richard Kennard
 */

public class JavassistPropertyStyleDebugOffTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJavassist() {

		JavassistPropertyStyle propertyStyle = new JavassistPropertyStyle();

		try {
			propertyStyle.getProperties( Foo.class );

			// Note: this test will fail unless the code has been compiled with debug off
			// (eg. when running inside Eclipse)

			// TODO: assertTrue( false );
		} catch ( InspectorException e ) {
			String message = "Line number information for " + Foo.class + " not available. Did you compile without debug info?";
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
