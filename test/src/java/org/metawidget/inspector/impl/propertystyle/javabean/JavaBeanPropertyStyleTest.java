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

package org.metawidget.inspector.impl.propertystyle.javabean;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;

/**
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJavaBeanPropertyStyle() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.size() == 10 );

		assertEquals( "foo", properties.get( "foo" ).toString() );
		assertFalse( properties.get( "foo" ).getAnnotation( Column.class ).nullable() );
		assertEquals( "bar", properties.get( "bar" ).getName() );
		assertEquals( Date.class, ( (ParameterizedType) properties.get( "bar" ).getGenericType() ).getActualTypeArguments()[0] );
		assertTrue( properties.get( "methodFoo" ).isAnnotationPresent( NotNull.class ) );
		assertTrue( 5 == properties.get( "methodBar" ).getAnnotation( Length.class ).min() );
		assertEquals( String.class, ( (ParameterizedType) properties.get( "methodBaz" ).getGenericType() ).getActualTypeArguments()[0] );
		assertTrue( properties.get( "methodBaz" ).isReadable() );
		assertFalse( properties.get( "methodBaz" ).isWritable() );
		assertEquals( Boolean.class, ( (ParameterizedType) properties.get( "methodAbc" ).getGenericType() ).getActualTypeArguments()[0] );
		assertFalse( properties.get( "methodAbc" ).isReadable() );
		assertTrue( properties.get( "methodAbc" ).isWritable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isWritable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isWritable() );
		assertEquals( String.class, properties.get( "methodCovariant" ).getType() );

		try {
			properties.get( "foo" ).read( new Object() );
			assertTrue( false );
		} catch ( Exception e ) {
			// Should fail
		}

		// Test excludeBaseTypes

		assertEquals( "baz", properties.get( "baz" ).getName() );

		propertyStyle = new FooPropertyStyle();
		properties = propertyStyle.getProperties( Foo.class );
		assertTrue( properties.size() == 9 );

		assertEquals( properties.get( "baz" ), null );
		assertFalse( properties.get( "methodGetterInSuper" ).isReadable() );
		assertTrue( properties.get( "methodGetterInSuper" ).isWritable() );
		assertTrue( properties.get( "methodSetterInSuper" ).isReadable() );
		assertFalse( properties.get( "methodSetterInSuper" ).isWritable() );
		assertEquals( String.class, properties.get( "methodCovariant" ).getType() );
	}

	public void testInterfaceBasedPropertyStyle() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( Proxied_$$_javassist_.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ) );

		properties = propertyStyle.getProperties( new InterfaceFoo() {

			@Override
			public Object getInterfaceBar() {

				return null;
			}
		}.getClass() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertFalse( properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ) );
	}

	public void testFieldAndGetter() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

		try {
			propertyStyle.getProperties( ErrorFoo.class );
		} catch ( InspectorException e ) {
			assertEquals( "JavaBeanProperty 'public java.lang.String org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleTest$Foo.foo' has both a public member variable and a public setter method. Should be one or the other", e.getMessage() );
		}

		try {
			propertyStyle.getProperties( ErrorFoo2.class );
		} catch ( InspectorException e ) {
			assertEquals( "JavaBeanProperty 'public java.lang.String org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleTest$Foo.foo' has both a public member variable and a public getter method. Should be one or the other", e.getMessage() );
		}
	}

	//
	// Inner class
	//

	class Foo
		extends SuperFoo {

		@Column( nullable = false )
		public String		foo;

		public List<Date>	bar;

		@NotNull
		public String getMethodFoo() {

			return null;
		}

		@Length( min = 5 )
		public void setMethodBar( String methodBar ) {

			// Do nothing
		}

		public List<String> getMethodBaz() {

			return null;
		}

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

		public String getterIgnoreBecauseLowercase() {

			return null;
		}

		public void setterIgnoreBecauseLowercase( String ignore ) {

			// Do nothing
		}
	}

	class ErrorFoo
		extends Foo {

		public void setFoo( String aFoo ) {

			// Will error
		}
	}

	class ErrorFoo2
		extends ErrorFoo {

		public String getFoo() {

			return null;
		}
	}

	class SuperFoo {

		public boolean	baz;

		public String getMethodGetterInSuper() {

			return null;
		}

		public void setMethodGetterInSuper( String methodSetterInSuper ) {

			// Do nothing
		}

		public void setMethodSetterInSuper( String methodSetterInSuper ) {

			// Do nothing
		}

		public Object getMethodCovariant() {

			return null;
		}
	}

	static class FooPropertyStyle
		extends JavaBeanPropertyStyle {

		@Override
		protected boolean isExcludedBaseType( Class<?> classToExclude ) {

			if ( SuperFoo.class.equals( classToExclude ) ) {
				return true;
			}

			return super.isExcludedBaseType( classToExclude );
		}
	}

	class Proxied_$$_javassist_
		implements InterfaceFoo {

		@Override
		public Object getInterfaceBar() {

			return null;
		}
	}

	interface InterfaceFoo {

		@UiMasked
		Object getInterfaceBar();
	}
}
