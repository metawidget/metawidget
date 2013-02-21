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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class ClassUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testObjectPrimitive()
		throws Exception {

		assertTrue( ClassUtils.isPrimitiveWrapper( Byte.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Short.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Integer.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Long.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Float.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Double.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Boolean.class ) );
		assertTrue( ClassUtils.isPrimitiveWrapper( Character.class ) );
		assertFalse( ClassUtils.isPrimitiveWrapper( String.class ) );
	}

	public void testGetWrapperClass()
		throws Exception {

		assertEquals( Byte.class, ClassUtils.getWrapperClass( byte.class ) );
		assertEquals( Short.class, ClassUtils.getWrapperClass( short.class ) );
		assertEquals( Integer.class, ClassUtils.getWrapperClass( int.class ) );
		assertEquals( Long.class, ClassUtils.getWrapperClass( long.class ) );
		assertEquals( Float.class, ClassUtils.getWrapperClass( float.class ) );
		assertEquals( Double.class, ClassUtils.getWrapperClass( double.class ) );
		assertEquals( Boolean.class, ClassUtils.getWrapperClass( boolean.class ) );
		assertEquals( Character.class, ClassUtils.getWrapperClass( char.class ) );

		try {
			ClassUtils.getWrapperClass( String.class );
			fail();
		} catch ( Exception e ) {
			assertEquals( "class java.lang.String is not a primitive type", e.getMessage() );
		}

		assertFalse( ClassUtils.isPrimitiveWrapper( String.class ) );
	}

	public void testParseNumber()
		throws Exception {

		assertEquals( (byte) 2, ClassUtils.parseNumber( byte.class, "2" ) );
		assertEquals( Byte.valueOf( "3" ), ClassUtils.parseNumber( Byte.class, "3" ) );
		assertEquals( (short) 4, ClassUtils.parseNumber( short.class, "4" ) );
		assertEquals( Short.valueOf( "5" ), ClassUtils.parseNumber( Short.class, "5" ) );
		assertEquals( 6, ClassUtils.parseNumber( int.class, "6" ) );
		assertEquals( Integer.valueOf( "7" ), ClassUtils.parseNumber( Integer.class, "7" ) );
		assertEquals( (long) 8, ClassUtils.parseNumber( long.class, "8" ) );
		assertEquals( Long.valueOf( "9" ), ClassUtils.parseNumber( Long.class, "9" ) );
		assertEquals( (float) 10, ClassUtils.parseNumber( float.class, "10" ) );
		assertEquals( Float.valueOf( "11" ), ClassUtils.parseNumber( Float.class, "11" ) );
		assertEquals( (double) 12, ClassUtils.parseNumber( double.class, "12" ) );
		assertEquals( Double.valueOf( "13" ), ClassUtils.parseNumber( Double.class, "13" ) );

		try {
			ClassUtils.parseNumber( String.class, "13" );
			fail();
		} catch ( Exception e ) {
			assertEquals( "class java.lang.String is not a number type", e.getMessage() );
		}
	}

	public void testGetNumberMinValue()
		throws Exception {

		assertEquals( Byte.MIN_VALUE, ClassUtils.getNumberMinValue( byte.class ) );
		assertEquals( Byte.MIN_VALUE, ClassUtils.getNumberMinValue( Byte.class ) );
		assertEquals( Short.MIN_VALUE, ClassUtils.getNumberMinValue( short.class ) );
		assertEquals( Short.MIN_VALUE, ClassUtils.getNumberMinValue( Short.class ) );
		assertEquals( Integer.MIN_VALUE, ClassUtils.getNumberMinValue( int.class ) );
		assertEquals( Integer.MIN_VALUE, ClassUtils.getNumberMinValue( Integer.class ) );
		assertEquals( Long.MIN_VALUE, ClassUtils.getNumberMinValue( long.class ) );
		assertEquals( Long.MIN_VALUE, ClassUtils.getNumberMinValue( Long.class ) );
		assertEquals( -Float.MAX_VALUE, ClassUtils.getNumberMinValue( float.class ) );
		assertEquals( -Float.MAX_VALUE, ClassUtils.getNumberMinValue( Float.class ) );
		assertEquals( -Double.MAX_VALUE, ClassUtils.getNumberMinValue( double.class ) );
		assertEquals( -Double.MAX_VALUE, ClassUtils.getNumberMinValue( Double.class ) );

		try {
			ClassUtils.getNumberMinValue( String.class );
			fail();
		} catch ( Exception e ) {
			assertEquals( "class java.lang.String is not a number type", e.getMessage() );
		}
	}

	public void testGetNumberMaxValue()
		throws Exception {

		assertEquals( Byte.MAX_VALUE, ClassUtils.getNumberMaxValue( byte.class ) );
		assertEquals( Byte.MAX_VALUE, ClassUtils.getNumberMaxValue( Byte.class ) );
		assertEquals( Short.MAX_VALUE, ClassUtils.getNumberMaxValue( short.class ) );
		assertEquals( Short.MAX_VALUE, ClassUtils.getNumberMaxValue( Short.class ) );
		assertEquals( Integer.MAX_VALUE, ClassUtils.getNumberMaxValue( int.class ) );
		assertEquals( Integer.MAX_VALUE, ClassUtils.getNumberMaxValue( Integer.class ) );
		assertEquals( Long.MAX_VALUE, ClassUtils.getNumberMaxValue( long.class ) );
		assertEquals( Long.MAX_VALUE, ClassUtils.getNumberMaxValue( Long.class ) );
		assertEquals( Float.MAX_VALUE, ClassUtils.getNumberMaxValue( float.class ) );
		assertEquals( Float.MAX_VALUE, ClassUtils.getNumberMaxValue( Float.class ) );
		assertEquals( Double.MAX_VALUE, ClassUtils.getNumberMaxValue( double.class ) );
		assertEquals( Double.MAX_VALUE, ClassUtils.getNumberMaxValue( Double.class ) );

		try {
			ClassUtils.getNumberMaxValue( String.class );
			fail();
		} catch ( Exception e ) {
			assertEquals( "class java.lang.String is not a number type", e.getMessage() );
		}
	}

	public void testProperties()
		throws Exception {

		Foo foo = new Foo();
		Baz baz = new Baz();

		ClassUtils.setProperty( foo, "bar", baz );
		assertEquals( ClassUtils.getProperty( foo, "bar" ), baz );

		try {
			ClassUtils.setProperty( foo, "bar1", baz );
			fail();
		} catch ( Exception e ) {
			assertEquals( "No such method getBar1 (or boolean isBar1) on class org.metawidget.util.ClassUtilsTest$Foo", e.getCause().getMessage() );
		}

		try {
			ClassUtils.getProperty( foo, "bar1" );
			fail();
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
			fail();
		} catch ( Exception e ) {
			assertEquals( "No such method getBigBoolean (or boolean isBigBoolean) on class org.metawidget.util.ClassUtilsTest$Foo", e.getMessage() );
		}
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

	public void testAlienClassLoader()
		throws Exception {

		ClassLoader alienClassLoader = new AlienClassLoader();
		alienClassLoader.loadClass( "org.metawidget.util.AlienSet" );
		ClassUtilsTest.unregisterAllAlienClassLoaders();

		try {
			assertEquals( null, ClassUtils.niceForName( "org.metawidget.util.AlienSet" ) );
			ClassUtils.registerAlienClassLoader( alienClassLoader );
			assertEquals( "org.metawidget.util.AlienSet", ClassUtils.niceForName( "org.metawidget.util.AlienSet" ).getName() );
			assertTrue( Set.class.isAssignableFrom( ClassUtils.niceForName( "org.metawidget.util.AlienSet" ) ) );
		} finally {
			ClassUtilsTest.unregisterAllAlienClassLoaders();
		}

		assertEquals( null, ClassUtils.niceForName( "org.metawidget.util.AlienSet" ) );
	}

	public static void unregisterAllAlienClassLoaders() {

		synchronized ( ClassUtils.ALIEN_CLASSLOADERS ) {
			ClassUtils.ALIEN_CLASSLOADERS.clear();
		}
	}

	public static void testGetPackagesAsFolderNames() {

		assertEquals( "/java/lang", ClassUtils.getPackagesAsFolderNames( String.class ) );
	}

	//
	// Inner class
	//

	public static class AlienClassLoader
		extends ClassLoader {

		@Override
		public Class<?> findClass( String name )
			throws ClassNotFoundException {

			if ( !"org.metawidget.util.AlienSet".equals( name ) ) {
				return super.findClass( name );
			}

			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			IOUtils.streamBetween( getResourceAsStream( "org/metawidget/util/AlienSet.alienclass" ), streamOut );
			byte[] bytes = streamOut.toByteArray();
			return defineClass( name, bytes, 0, bytes.length );
		}
	}

	public static class Foo {

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
