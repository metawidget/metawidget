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

package org.metawidget.faces;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class FacesUtilsTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testWrapping()
		throws Exception
	{
		assertTrue( true == FacesUtils.isExpression( "#{foo}" ) );
		assertTrue( false == FacesUtils.isExpression( "foo" ) );
		assertTrue( true == FacesUtils.isExpression( "#{!empty bar ? '' : '#{foo}'}" ) );
		assertTrue( true == FacesUtils.isExpression( "${!empty bar ? '' : '#{foo}'}" ) );
		assertTrue( false == FacesUtils.isExpression( "#{!empty bar ? '' : '#{foo}'" ) );
		assertTrue( false == FacesUtils.isExpression( "{!empty bar ? '' : '#{foo}'}" ) );
		assertTrue( false == FacesUtils.isExpression( "#!empty bar ? '' : '#{foo}'}" ) );

		assertEquals( "foo", FacesUtils.unwrapExpression( "#{foo}" ));
		assertEquals( "foo", FacesUtils.unwrapExpression( "foo" ));
		assertEquals( "#{foo", FacesUtils.unwrapExpression( "#{foo" ));
		assertEquals( "{foo}", FacesUtils.unwrapExpression( "{foo}" ));
		assertEquals( "foo}", FacesUtils.unwrapExpression( "foo}" ));

		assertEquals( "#{foo}", FacesUtils.wrapExpression( "foo" ));
		assertEquals( "#{foo}", FacesUtils.wrapExpression( "#{foo}" ));
		assertEquals( "#{#{foo}", FacesUtils.wrapExpression( "#{foo" ));
		assertEquals( "#{{foo}}", FacesUtils.wrapExpression( "{foo}" ));
		assertEquals( "#{foo}}", FacesUtils.wrapExpression( "foo}" ));
	}
}
