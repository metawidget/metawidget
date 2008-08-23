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

import junit.framework.TestCase;

import org.metawidget.util.ThreadUtils;
import org.metawidget.util.ThreadUtils.ReentrantThreadLocal;

/**
 * @author Richard Kennard
 */

public class ThreadUtilsTest
	extends TestCase
{
	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ThreadUtilsTest( String name )
	{
		super( name );
	}

	//
	//
	// Public methods
	//
	//

	public void testThreadUtils()
		throws Exception
	{
		ReentrantThreadLocal<Integer> reentrant = ThreadUtils.newReentrantThreadLocal();

		assertTrue( reentrant.get() == null );

		reentrant.push();
		assertTrue( reentrant.get() == null );
		reentrant.set( Integer.valueOf( 42 ) );

		reentrant.push();
		assertTrue( reentrant.get() == null );
		reentrant.set( Integer.valueOf( 43 ) );
		assertTrue( Integer.valueOf( 43 ).equals( reentrant.get() ) );
		reentrant.pop();

		assertTrue( Integer.valueOf( 42 ).equals( reentrant.get() ) );
		reentrant.pop();

		assertTrue( reentrant.get() == null );
	}
}
