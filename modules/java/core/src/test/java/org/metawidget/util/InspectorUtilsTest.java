// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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