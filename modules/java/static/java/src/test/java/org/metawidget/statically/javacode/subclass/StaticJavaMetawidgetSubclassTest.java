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

package org.metawidget.statically.javacode.subclass;

import junit.framework.TestCase;

import org.metawidget.statically.javacode.StaticJavaMetawidget;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StaticJavaMetawidgetSubclassTest
	extends TestCase {

	//
	// Public methods
	//

	/**
	 * Test default configuration is tied to the superclass, not the subclass.
	 */

	public static void testDefaultConfiguration() {

		assertEquals( "/org/metawidget/statically/javacode/metawidget-static-javacode-default.xml", new StaticJavaMetawidgetSubclass().getDefaultConfiguration() );
	}

	//
	// Inner class
	//

	public static class StaticJavaMetawidgetSubclass
		extends StaticJavaMetawidget {

		@Override
		public String getDefaultConfiguration() {

			return super.getDefaultConfiguration();
		}
	}
}
