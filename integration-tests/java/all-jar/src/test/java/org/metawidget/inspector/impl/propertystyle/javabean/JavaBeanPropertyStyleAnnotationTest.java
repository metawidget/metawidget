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

package org.metawidget.inspector.impl.propertystyle.javabean;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Id;

import junit.framework.TestCase;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.impl.propertystyle.Property;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JavaBeanPropertyStyleAnnotationTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJavaBeanPropertyStyle() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) );
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 10, properties.size() );

		assertEquals( "foo", properties.get( "foo" ).toString() );
		assertFalse( properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertEquals( "bar", properties.get( "bar" ).getName() );
		assertEquals( Date.class.getName(), properties.get( "bar" ).getGenericType() );
		assertTrue( properties.get( "methodFoo" ).isAnnotationPresent( NotNull.class ) );
		assertEquals( 5, properties.get( "methodBar" ).getAnnotation( Length.class ).min() );
		assertEquals( String.class.getName(), properties.get( "methodBaz" ).getGenericType() );
		assertTrue( properties.get( "methodBaz" ).isReadable() );
		assertFalse( properties.get( "methodBaz" ).isWritable() );
		assertEquals( Boolean.class.getName(), properties.get( "methodAbc" ).getGenericType() );
		assertFalse( properties.get( "methodAbc" ).isReadable() );
		assertTrue( properties.get( "methodAbc" ).isWritable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isWritable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isWritable() );
		assertEquals( String.class.getName(), properties.get( "methodCovariant" ).getType() );

		try {
			properties.get( "foo" ).read( new Object() );
			fail();
		} catch ( Exception e ) {
			// Should fail
		}

		// Test excludeBaseTypes

		assertEquals( "baz", properties.get( "baz" ).getName() );

		propertyStyle = new FooPropertyStyle();
		properties = propertyStyle.getProperties( Foo.class.getName() );

		assertEquals( properties.get( "baz" ), null );
		assertEquals( properties.get( "methodGetterInSuper" ), null );
		assertEquals( properties.get( "methodSetterInSuper" ), null );
		assertEquals( properties.get( "methodCovariant" ), null );
		assertEquals( 6, properties.size() );
	}

	public void testSupportPublicFields() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) );
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( properties.size(), 10 );

		assertEquals( "foo", properties.get( "foo" ).toString() );
		assertFalse( properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertEquals( "bar", properties.get( "bar" ).getName() );
		assertEquals( Date.class.getName(), properties.get( "bar" ).getGenericType() );
		assertTrue( properties.get( "methodFoo" ).isAnnotationPresent( NotNull.class ) );
		assertEquals( 5, properties.get( "methodBar" ).getAnnotation( Length.class ).min() );
		assertEquals( String.class.getName(), properties.get( "methodBaz" ).getGenericType() );
		assertTrue( properties.get( "methodBaz" ).isReadable() );
		assertFalse( properties.get( "methodBaz" ).isWritable() );
		assertEquals( Boolean.class.getName(), properties.get( "methodAbc" ).getGenericType() );
		assertFalse( properties.get( "methodAbc" ).isReadable() );
		assertTrue( properties.get( "methodAbc" ).isWritable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isWritable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isWritable() );
		assertEquals( String.class.getName(), properties.get( "methodCovariant" ).getType() );
		assertEquals( "baz", properties.get( "baz" ).getName() );

		propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( false ) );
		properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( properties.size(), 7 );

		assertTrue( !properties.containsKey( "foo" ) );
		assertTrue( !properties.containsKey( "bar" ) );
		assertTrue( properties.get( "methodFoo" ).isAnnotationPresent( NotNull.class ) );
		assertEquals( 5, properties.get( "methodBar" ).getAnnotation( Length.class ).min() );
		assertEquals( String.class.getName(), properties.get( "methodBaz" ).getGenericType() );
		assertTrue( properties.get( "methodBaz" ).isReadable() );
		assertFalse( properties.get( "methodBaz" ).isWritable() );
		assertEquals( Boolean.class.getName(), properties.get( "methodAbc" ).getGenericType() );
		assertFalse( properties.get( "methodAbc" ).isReadable() );
		assertTrue( properties.get( "methodAbc" ).isWritable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isWritable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isWritable() );
		assertEquals( String.class.getName(), properties.get( "methodCovariant" ).getType() );
		assertTrue( !properties.containsKey( "baz" ) );
	}

	public void testInterfaceBasedPropertyStyle() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( Proxied_$$_javassist_.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ) );

		properties = propertyStyle.getProperties( new InterfaceFoo() {

			@Override
			public Object getInterfaceBar() {

				return null;
			}
		}.getClass().getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ) );
	}

	public void testPrivateFieldAndGetter() {

		// No convention

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( PrivateFieldFoo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( properties.size(), 4 );

		assertEquals( "foo", properties.get( "foo" ).toString() );
		assertEquals( "Foo Section", properties.get( "foo" ).getAnnotation( UiSection.class ).value()[0] );
		assertEquals( "bar", properties.get( "bar" ).toString() );
		assertEquals( "foo", properties.get( "bar" ).getAnnotation( UiComesAfter.class ).value()[0] );
		assertFalse( properties.get( "bar" ).isAnnotationPresent( Column.class ) );
		assertEquals( "baz", properties.get( "baz" ).toString() );
		assertEquals( "bar", properties.get( "baz" ).getAnnotation( UiComesAfter.class ).value()[0] );
		assertFalse( properties.get( "baz" ).isAnnotationPresent( Id.class ) );
		assertEquals( "abc", properties.get( "abc" ).toString() );
		assertEquals( "Abc Section", properties.get( "abc" ).getAnnotation( UiSection.class ).value()[0] );
		assertFalse( properties.get( "abc" ).isAnnotationPresent( UiLarge.class ) );

		// Convention with {0}

		propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setPrivateFieldConvention( new MessageFormat( "{0}" ) ) );
		properties = propertyStyle.getProperties( PrivateFieldFoo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( properties.size(), 4 );

		assertEquals( "foo", properties.get( "foo" ).toString() );
		assertEquals( "Foo Section", properties.get( "foo" ).getAnnotation( UiSection.class ).value()[0] );
		assertEquals( "bar", properties.get( "bar" ).toString() );
		assertEquals( "foo", properties.get( "bar" ).getAnnotation( UiComesAfter.class ).value()[0] );
		assertFalse( properties.get( "bar" ).isAnnotationPresent( Column.class ) );
		assertEquals( "baz", properties.get( "baz" ).toString() );
		assertEquals( "bar", properties.get( "baz" ).getAnnotation( UiComesAfter.class ).value()[0] );
		assertFalse( properties.get( "baz" ).isAnnotationPresent( Id.class ) );
		assertEquals( "abc", properties.get( "abc" ).toString() );
		assertEquals( "Abc Section", properties.get( "abc" ).getAnnotation( UiSection.class ).value()[0] );
		assertTrue( properties.get( "abc" ).isAnnotationPresent( UiLarge.class ) );

		// Convention with {1}

		propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setPrivateFieldConvention( new MessageFormat( "'m'{1}" ) ) );
		properties = propertyStyle.getProperties( PrivateFieldFoo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( properties.size(), 4 );

		assertEquals( "foo", properties.get( "foo" ).toString() );
		assertEquals( "Foo Section", properties.get( "foo" ).getAnnotation( UiSection.class ).value()[0] );
		assertEquals( "bar", properties.get( "bar" ).toString() );
		assertEquals( "foo", properties.get( "bar" ).getAnnotation( UiComesAfter.class ).value()[0] );
		assertFalse( properties.get( "bar" ).getAnnotation( Column.class ).nullable() );
		assertEquals( "baz", properties.get( "baz" ).toString() );
		assertEquals( "bar", properties.get( "baz" ).getAnnotation( UiComesAfter.class ).value()[0] );
		assertTrue( properties.get( "baz" ).isAnnotationPresent( Id.class ) );
		assertEquals( "abc", properties.get( "abc" ).toString() );
		assertEquals( "Abc Section", properties.get( "abc" ).getAnnotation( UiSection.class ).value()[0] );
		assertFalse( properties.get( "abc" ).isAnnotationPresent( UiLarge.class ) );
	}

	//
	// Inner class
	//

	static class Foo
		extends SuperFoo {

		@Column( nullable = false )
		public String		foo;

		public List<Date>	bar;

		@NotNull
		public String getMethodFoo() {

			return null;
		}

		/**
		 * @param methodBar
		 */

		@Length( min = 5 )
		public void setMethodBar( String methodBar ) {

			// Do nothing
		}

		public List<String> getMethodBaz() {

			return null;
		}

		/**
		 * @param methodAbc
		 */

		public void setMethodAbc( List<Boolean> methodAbc ) {

			// Do nothing
		}

		@Override
		public void setMethodGetterInSuper( String methodGetterInSuper ) {

			// Do nothing
		}

		public String getMethodSetterInSuper() {

			return null;
		}

		@Override
		public String getMethodCovariant() {

			return null;
		}
	}

	static class SuperFoo {

		public boolean	baz;

		public String getMethodGetterInSuper() {

			return null;
		}

		/**
		 * @param methodSetterInSuper
		 */

		public void setMethodGetterInSuper( String methodSetterInSuper ) {

			// Do nothing
		}

		/**
		 * @param methodSetterInSuper
		 */

		public void setMethodSetterInSuper( String methodSetterInSuper ) {

			// Do nothing
		}

		public Object getMethodCovariant() {

			return null;
		}
	}

	static class FooPropertyStyle
		extends JavaBeanPropertyStyle {

		public FooPropertyStyle() {

			super( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) );
		}

		@Override
		protected boolean isExcludedBaseType( Class<?> classToExclude ) {

			if ( SuperFoo.class.equals( classToExclude ) ) {
				return true;
			}

			return super.isExcludedBaseType( classToExclude );
		}
	}

	static class Proxied_$$_javassist_
		implements InterfaceFoo {

		@Override
		public Object getInterfaceBar() {

			return null;
		}
	}

	static interface InterfaceFoo {

		@UiMasked
		Object getInterfaceBar();
	}

	static class SuperPrivateFieldFoo {

		@Column( nullable = false )
		private String	mBar;

		@Id
		String			mBaz;

		@UiLarge
		private String	abc;

		@UiSection( "Foo Section" )
		public String getFoo() {

			return null;
		}

		/**
		 * @param foo
		 *            not stored (no private member for this property)
		 */

		public void setFoo( String foo ) {

			// Do nothing
		}

		@UiComesAfter( "foo" )
		public String getBar() {

			return mBar;
		}

		public void setBar( String bar ) {

			mBar = bar;
		}

		public String getAbc() {

			return abc;
		}

		@UiSection( "Abc Section" )
		public void setAbc( @SuppressWarnings( "hiding" ) String abc ) {

			this.abc = abc;
		}
	}

	static class PrivateFieldFoo
		extends SuperPrivateFieldFoo {

		// Tests finding overidden getters/setters

		@UiComesAfter( "bar" )
		public String getBaz() {

			return null;
		}

		/**
		 * @param baz
		 *            not stored (private member not accessible to subclass)
		 */

		public void setBaz( String baz ) {

			// Do nothing
		}
	}
}
