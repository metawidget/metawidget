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

package org.metawidget.test.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.test.util.simple.PathUtilsTest;
import org.metawidget.test.util.simple.StringUtilsTest;

/**
 * @author Richard Kennard
 */

public class UtilTests
	extends TestCase
{
	//
	// Public statics
	//

	public static Test suite()
	{
		TestSuite suite = new TestSuite( "Util Tests" );
		suite.addTestSuite( ArrayUtilsTest.class );
		suite.addTestSuite( ClassUtilsTest.class );
		suite.addTestSuite( CollectionUtilsTest.class );
		suite.addTestSuite( JspUtilsTest.class );
		suite.addTestSuite( LogUtilsTest.class );
		suite.addTestSuite( PathUtilsTest.class );
		suite.addTestSuite( StringUtilsTest.class );
		suite.addTestSuite( ThreadUtilsTest.class );
		suite.addTestSuite( WidgetBuilderUtilsTest.class );

		return suite;
	}
}