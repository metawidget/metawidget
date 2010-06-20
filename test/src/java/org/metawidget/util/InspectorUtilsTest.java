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

package org.metawidget.util;

import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class InspectorUtilsTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testPutAttributeValue()
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		InspectorUtils.putAttributeValue( attributes, "foo", null );
		assertEquals( "", attributes.get( "foo" ));

		InspectorUtils.putAttributeValue( attributes, "bar", CollectionUtils.newArrayList( ",a", "bc" ) );
		assertEquals( "\\,a,bc", attributes.get( "bar" ));

		InspectorUtils.putAttributeValue( attributes, "baz", new String[]{ "a,b", "bc", "d," } );
		assertEquals( "a\\,b,bc,d\\,", attributes.get( "baz" ));

		InspectorUtils.putAttributeValue( attributes, "abc", 2 );
		assertEquals( "2", attributes.get( "abc" ));
	}
}
