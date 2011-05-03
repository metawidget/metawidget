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

package org.metawidget.swt;

import org.eclipse.swt.widgets.Display;

/**
 * @author Richard Kennard
 */

public class SwtMetawidgetTests {

	//
	// Public statics
	//

	/**
	 * Need a single Display within the whole Thread of JUnit tests, else get a
	 * 'org.eclipse.swt.SWTException: Invalid thread access'
	 */

	public static final Display	TEST_DISPLAY	= new Display();
}