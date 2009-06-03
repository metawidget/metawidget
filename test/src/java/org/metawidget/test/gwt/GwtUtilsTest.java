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

package org.metawidget.test.gwt;

import junit.framework.TestCase;

import org.metawidget.gwt.client.ui.GwtUtils;

/**
 * @author Richard Kennard
 */

public class GwtUtilsTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public GwtUtilsTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testGwtUtils()
		throws Exception
	{
		// isPrimitive

		assertTrue( GwtUtils.isPrimitive( "byte" ));
		assertTrue( GwtUtils.isPrimitive( "short" ));
		assertTrue( GwtUtils.isPrimitive( "int" ));
		assertTrue( GwtUtils.isPrimitive( "long" ));
		assertTrue( GwtUtils.isPrimitive( "float" ));
		assertTrue( GwtUtils.isPrimitive( "double" ));
		assertTrue( GwtUtils.isPrimitive( "boolean" ));
		assertTrue( GwtUtils.isPrimitive( "char" ));
		assertTrue( !GwtUtils.isPrimitive( "Byte" ));
		assertTrue( !GwtUtils.isPrimitive( "Short" ));
		assertTrue( !GwtUtils.isPrimitive( "Integer" ));
		assertTrue( !GwtUtils.isPrimitive( "Long" ));
		assertTrue( !GwtUtils.isPrimitive( "Float" ));
		assertTrue( !GwtUtils.isPrimitive( "Double" ));
		assertTrue( !GwtUtils.isPrimitive( "Boolean" ));
		assertTrue( !GwtUtils.isPrimitive( "Character" ));

		// isPrimitiveWrapper

		assertTrue( GwtUtils.isPrimitiveWrapper( "Byte" ));
		assertTrue( GwtUtils.isPrimitiveWrapper( "Short" ));
		assertTrue( GwtUtils.isPrimitiveWrapper( "Integer" ));
		assertTrue( GwtUtils.isPrimitiveWrapper( "Long" ));
		assertTrue( GwtUtils.isPrimitiveWrapper( "Float" ));
		assertTrue( GwtUtils.isPrimitiveWrapper( "Double" ));
		assertTrue( GwtUtils.isPrimitiveWrapper( "Boolean" ));
		assertTrue( GwtUtils.isPrimitiveWrapper( "Character" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "byte" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "short" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "int" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "long" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "float" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "double" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "boolean" ));
		assertTrue( !GwtUtils.isPrimitiveWrapper( "char" ));

		// toString

		assertTrue( "".equals( GwtUtils.toString( (String[]) null, ',' )));
		assertTrue( "foo#bar#baz".equals( GwtUtils.toString( new String[]{ "foo", "bar", "baz" }, '#' ) ));
	}
}
