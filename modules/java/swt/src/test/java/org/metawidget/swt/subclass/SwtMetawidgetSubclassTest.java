// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.swt.subclass;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwtMetawidgetSubclassTest
	extends TestCase {

	//
	// Public methods
	//

	/**
	 * Test default configuration is tied to the superclass, not the subclass.
	 */

	public static void testDefaultConfiguration() {

		assertEquals( "/org/metawidget/swt/metawidget-swt-default.xml", new SwtMetawidgetSubclass().getDefaultConfiguration() );
	}

	//
	// Inner class
	//

	public static class SwtMetawidgetSubclass
		extends SwtMetawidget {

		//
		// Constructor
		//

		public SwtMetawidgetSubclass() {

			super( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		}

		@Override
		public String getDefaultConfiguration() {

			return super.getDefaultConfiguration();
		}
	}
}
