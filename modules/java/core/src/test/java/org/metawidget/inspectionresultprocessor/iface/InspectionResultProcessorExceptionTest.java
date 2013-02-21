// Metawidget (licensed under LGPL)
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

package org.metawidget.inspectionresultprocessor.iface;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class InspectionResultProcessorExceptionTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspectionResultProcessorException()
		throws Exception {

		Throwable throwable = new Throwable();
		assertEquals( throwable, InspectionResultProcessorException.newException( throwable ).getCause() );

		throwable = InspectionResultProcessorException.newException( "Foo" );
		assertEquals( "Foo", throwable.getMessage() );
		assertEquals( throwable, InspectionResultProcessorException.newException( throwable ) );
		assertEquals( "Foo", InspectionResultProcessorException.newException( "Foo", throwable ).getMessage() );
		assertEquals( throwable, InspectionResultProcessorException.newException( "Foo", throwable ).getCause() );
	}
}
