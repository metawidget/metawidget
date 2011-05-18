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

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class ClassUtilsTest
	extends TestCase {

	//
	// Constructor
	//

	public ClassUtilsTest( String name ) {

		super( name );
	}

	//
	// Public methods
	//

	public void testObjectPrimitive()
		throws Exception {

		assertTrue( ClassUtils.isPrimitiveWrapper( Long.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Boolean.class ) );
		assertFalse( ClassUtils.isPrimitiveWrapper( String.class ) );
	}

	public void testProperties()
		throws Exception {

		Foo foo = new Foo();
		Baz baz = new Baz();

		ClassUtils.setProperty( foo, "bar", baz );
		assertEquals( ClassUtils.getProperty( foo, "bar" ), baz );

		try {
			ClassUtils.setProperty( foo, "bar1", baz );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "No such method getBar1 (or boolean isBar1) on class org.metawidget.util.ClassUtilsTest$Foo", e.getCause().getMessage() );
		}

		try {
			ClassUtils.getProperty( foo, "bar1" );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "No such method getBar1 (or boolean isBar1) on class org.metawidget.util.ClassUtilsTest$Foo", e.getCause().getMessage() );
		}
	}

	public void testStrictJavaBeanConvention()
		throws Exception {

		assertTrue( ClassUtils.getReadMethod( Foo.class, "bar" ) != null );
		assertTrue( ClassUtils.getReadMethod( Foo.class, "littleBoolean" ) != null );
		assertTrue( ClassUtils.getReadMethod( Foo.class, "bigBoolean2" ) != null );

		try {
			ClassUtils.getReadMethod( Foo.class, "bigBoolean" );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "No such method getBigBoolean (or boolean isBigBoolean) on class org.metawidget.util.ClassUtilsTest$Foo", e.getMessage() );
		}
	}

	public void testUnproxy() {

		assertEquals( Foo.class, ClassUtils.getUnproxiedClass( ProxiedFoo_$$_javassist_1.class ) );
		assertEquals( CannotUnproxyFoo_$$_javassist_1.class, ClassUtils.getUnproxiedClass( CannotUnproxyFoo_$$_javassist_1.class ) );
	}

	public void testForName() {

		// With ClassLoader

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assertTrue( classLoader != null );
		assertEquals( String.class, ClassUtils.niceForName( "java.lang.String" ) );
		assertTrue( ClassUtils.niceForName( "[Ljava.lang.String;", classLoader ).isArray() );
		assertEquals( String.class, ClassUtils.niceForName( "[Ljava.lang.String;", classLoader ).getComponentType() );

		// Without ClassLoader

		assertEquals( String.class, ClassUtils.niceForName( "java.lang.String", null ) );
		assertTrue( ClassUtils.niceForName( "[Ljava.lang.String;", null ).isArray() );
		assertEquals( String.class, ClassUtils.niceForName( "[Ljava.lang.String;", null ).getComponentType() );
	}

	public void testGetOriginalDeclaringClass()
		throws Exception {

		// Overridden

		Method method = String.class.getMethod( "equals", Object.class );
		assertEquals( String.class, method.getDeclaringClass() );
		assertEquals( Object.class, ClassUtils.getOriginalDeclaringClass( method ) );

		// Not overridden

		method = Object.class.getMethod( "equals", Object.class );
		assertEquals( Object.class, method.getDeclaringClass() );
		assertEquals( Object.class, ClassUtils.getOriginalDeclaringClass( method ) );

		// Overridden but skipped in middle class of heirarchy

		method = EqualsSkipped.class.getMethod( "equals", Object.class );
		assertEquals( EqualsSkipped.class, method.getDeclaringClass() );
		assertEquals( Object.class, ClassUtils.getOriginalDeclaringClass( method ) );
	}

	//
	// Inner class
	//

	protected static class Foo {

		//
		// Private members
		//

		private Bar	mBar;

		//
		// Public methods
		//

		public Bar getBar() {

			return mBar;
		}

		public void setBar( Bar bar ) {

			mBar = bar;
		}

		public Boolean isBigBoolean1() {

			return null;
		}

		public boolean isLittleBoolean() {

			return false;
		}

		public Boolean getBigBoolean2() {

			return null;
		}
	}

	static interface Bar {
		// Just a marker interface
	}

	static class Baz
		implements Bar {
		// Just an empty class
	}

	static class ProxiedFoo_$$_javassist_1
		extends Foo {
		// Should unproxy
	}

	static class CannotUnproxyFoo_$$_javassist_1 {
		// Should not unproxy, because extends java.lang.Object directly
	}

	static class SuperEqualsSkipped {

		// Do not override equals
	}

	@SuppressWarnings( "all" )
	static class EqualsSkipped
		extends SuperEqualsSkipped {

		@Override
		public boolean equals( Object obj ) {

			return super.equals( obj );
		}
	}
}
