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

package org.metawidget.util;

import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.ValueAndDeclaredType;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class InspectorUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testTraverse() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ));

		// null with names

		ValueAndDeclaredType valueAndDeclaredType = propertyStyle.traverse( null, Foo.class.getName(), false, "bar" );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( null, valueAndDeclaredType.getDeclaredType() );

		// Return class

		valueAndDeclaredType = propertyStyle.traverse( null, Foo.class.getName(), false );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( Foo.class.getName(), valueAndDeclaredType.getDeclaredType() );

		// Not matching

		valueAndDeclaredType = propertyStyle.traverse( new Date(), Foo.class.getName(), false );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( null, valueAndDeclaredType.getDeclaredType() );

		// No names, but onlyToParent

		Foo foo = new Foo();
		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), true );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( null, valueAndDeclaredType.getDeclaredType() );

		// Basic match

		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), false );
		assertEquals( foo, valueAndDeclaredType.getValue() );
		assertEquals( Foo.class.getName(), valueAndDeclaredType.getDeclaredType() );

		// Bad name

		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), false, "bad" );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( null, valueAndDeclaredType.getDeclaredType() );

		// Basic traverse

		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), false, "bar" );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( String.class.getName(), valueAndDeclaredType.getDeclaredType() );

		// Long traverse

		Foo foo2 = new Foo();
		foo.foo = foo2;
		Foo foo3 = new Foo();
		foo2.foo = foo3;
		foo3.bar = "Bar";
		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), false, "foo", "foo", "bar" );
		assertEquals( "Bar", valueAndDeclaredType.getValue() );
		assertEquals( String.class.getName(), valueAndDeclaredType.getDeclaredType() );

		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), false, "foo", "foo", "foo", "bar" );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( null, valueAndDeclaredType.getDeclaredType() );

		// Traverse to parent

		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), true, "bar" );
		assertEquals( foo, valueAndDeclaredType.getValue() );
		assertEquals( Foo.class.getName(), valueAndDeclaredType.getDeclaredType() );

		// Prevent recursion (silently)

		foo.foo = foo;
		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), false );
		assertEquals( foo, valueAndDeclaredType.getValue() );
		assertEquals( Foo.class.getName(), valueAndDeclaredType.getDeclaredType() );

		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), false, "foo" );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( null, valueAndDeclaredType.getDeclaredType() );

		valueAndDeclaredType = propertyStyle.traverse( foo, Foo.class.getName(), true, "foo" );
		assertEquals( null, valueAndDeclaredType.getValue() );
		assertEquals( null, valueAndDeclaredType.getDeclaredType() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String	bar;

		public Foo		foo;
	}
}