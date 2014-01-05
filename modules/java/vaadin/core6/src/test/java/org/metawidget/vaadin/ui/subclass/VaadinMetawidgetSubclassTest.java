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

package org.metawidget.vaadin.ui.subclass;

import junit.framework.TestCase;

import org.metawidget.vaadin.ui.VaadinMetawidget;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class VaadinMetawidgetSubclassTest
	extends TestCase {

	//
	// Public methods
	//

	/**
	 * Test default configuration is tied to the superclass, not the subclass.
	 */

	public static void testDefaultConfiguration() {

		assertEquals( "/org/metawidget/vaadin/ui/metawidget-vaadin-default.xml", new VaadinMetawidgetSubclass().getDefaultConfiguration() );
	}

	//
	// Inner class
	//

	public static class VaadinMetawidgetSubclass
		extends VaadinMetawidget {

		@Override
		public String getDefaultConfiguration() {

			return super.getDefaultConfiguration();
		}
	}
}
