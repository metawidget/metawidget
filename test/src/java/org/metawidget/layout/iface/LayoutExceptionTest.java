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

package org.metawidget.layout.iface;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class LayoutExceptionTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testLayoutException()
		throws Exception
	{
		Throwable throwable = new Throwable();
		assertTrue( throwable == LayoutException.newException( throwable ).getCause() );

		throwable = LayoutException.newException( "Foo" );
		assertEquals( "Foo", throwable.getMessage() );
		assertTrue( throwable == LayoutException.newException( throwable ) );
		assertEquals( "Foo", LayoutException.newException( "Foo", throwable ).getMessage() );
		assertTrue( throwable == LayoutException.newException( "Foo", throwable ).getCause() );
	}
}
