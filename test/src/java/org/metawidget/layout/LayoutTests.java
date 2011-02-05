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

package org.metawidget.layout;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.layout.decorator.FlatSectionLayoutDecoratorTest;
import org.metawidget.layout.decorator.LayoutDecoratorTest;
import org.metawidget.layout.decorator.NestedSectionLayoutDecoratorTest;
import org.metawidget.layout.iface.LayoutExceptionTest;
import org.metawidget.layout.impl.LayoutUtilsTest;

/**
 * @author Richard Kennard
 */

public class LayoutTests
	extends TestCase {

	//
	// Public statics
	//

	public static Test suite() {

		TestSuite suite = new TestSuite( "Layout Tests" );
		suite.addTestSuite( FlatSectionLayoutDecoratorTest.class );
		suite.addTestSuite( LayoutDecoratorTest.class );
		suite.addTestSuite( LayoutExceptionTest.class );
		suite.addTestSuite( LayoutUtilsTest.class );
		suite.addTestSuite( NestedSectionLayoutDecoratorTest.class );

		return suite;
	}
}