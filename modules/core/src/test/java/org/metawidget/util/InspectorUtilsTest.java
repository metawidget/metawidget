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

import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.simple.Pair;

/**
 * @author Richard Kennard
 */

public class InspectorUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testTraverse() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

		// null with names

		Pair<Object, String> pair = propertyStyle.traverse( null, Foo.class.getName(), false, "bar" );
		assertEquals( null, pair.getLeft() );
		assertEquals( null, pair.getRight() );

		// Return class

		pair = propertyStyle.traverse( null, Foo.class.getName(), false );
		assertEquals( null, pair.getLeft() );
		assertEquals( Foo.class.getName(), pair.getRight() );

		// Not matching

		pair = propertyStyle.traverse( new Date(), Foo.class.getName(), false );
		assertEquals( null, pair.getLeft() );
		assertEquals( null, pair.getRight() );

		// No names, but onlyToParent

		Foo foo = new Foo();
		pair = propertyStyle.traverse( foo, Foo.class.getName(), true );
		assertEquals( null, pair.getLeft() );
		assertEquals( null, pair.getRight() );

		// Basic match

		pair = propertyStyle.traverse( foo, Foo.class.getName(), false );
		assertEquals( foo, pair.getLeft() );
		assertEquals( Foo.class.getName(), pair.getRight() );

		// Bad name

		pair = propertyStyle.traverse( foo, Foo.class.getName(), false, "bad" );
		assertEquals( null, pair.getLeft() );
		assertEquals( null, pair.getRight() );

		// Basic traverse

		pair = propertyStyle.traverse( foo, Foo.class.getName(), false, "bar" );
		assertEquals( null, pair.getLeft() );
		assertEquals( String.class.getName(), pair.getRight() );

		// Long traverse

		Foo foo2 = new Foo();
		foo.foo = foo2;
		Foo foo3 = new Foo();
		foo2.foo = foo3;
		foo3.bar = "Bar";
		pair = propertyStyle.traverse( foo, Foo.class.getName(), false, "foo", "foo", "bar" );
		assertEquals( "Bar", pair.getLeft() );
		assertEquals( String.class.getName(), pair.getRight() );

		pair = propertyStyle.traverse( foo, Foo.class.getName(), false, "foo", "foo", "foo", "bar" );
		assertEquals( null, pair.getLeft() );
		assertEquals( null, pair.getRight() );

		// Traverse to parent

		pair = propertyStyle.traverse( foo, Foo.class.getName(), true, "bar" );
		assertEquals( foo, pair.getLeft() );
		assertEquals( Foo.class.getName(), pair.getRight() );

		// Prevent recursion (silently)

		foo.foo = foo;
		pair = propertyStyle.traverse( foo, Foo.class.getName(), false );
		assertEquals( foo, pair.getLeft() );
		assertEquals( Foo.class.getName(), pair.getRight() );

		pair = propertyStyle.traverse( foo, Foo.class.getName(), false, "foo" );
		assertEquals( null, pair.getLeft() );
		assertEquals( null, pair.getRight() );

		pair = propertyStyle.traverse( foo, Foo.class.getName(), true, "foo" );
		assertEquals( null, pair.getLeft() );
		assertEquals( null, pair.getRight() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String	bar;

		public Foo		foo;
	}
}