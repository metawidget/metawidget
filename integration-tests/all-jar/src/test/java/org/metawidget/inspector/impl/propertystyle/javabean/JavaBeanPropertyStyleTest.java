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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Id;

import junit.framework.TestCase;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseTraitStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.MetawidgetTestUtils;

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
		assertEquals( 10, properties.size() );

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

		assertEquals( properties.get( "baz" ), null );
		assertEquals( properties.get( "methodGetterInSuper" ), null );
		assertEquals( properties.get( "methodSetterInSuper" ), null );
		assertEquals( properties.get( "methodCovariant" ), null );
		assertEquals( 6, properties.size() );
	}

	public void testExcludeReturnType() {

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();

		// Without excluded type

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( config );
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 10, properties.size()  );

		assertEquals( "baz", properties.get( "baz" ).getName() );

		// With excluded type

		config.setExcludeReturnType( String.class );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 4, properties.size()  );

		assertEquals( "bar", properties.get( "bar" ).getName() );
		assertEquals( "baz", properties.get( "baz" ).getName() );
		assertEquals( "methodAbc", properties.get( "methodAbc" ).getName() );
		assertEquals( "methodBaz", properties.get( "methodBaz" ).getName() );
	}

	public void testExcludeName() {

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();

		// Without excluded name

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( config );
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 10, properties.size()  );

		assertEquals( "baz", properties.get( "baz" ).getName() );

		// With excluded name

		config.setExcludeName( "bar", "baz" );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 8, properties.size()  );

		assertEquals( "foo", properties.get( "foo" ).getName() );
		assertEquals( "methodAbc", properties.get( "methodAbc" ).getName() );
		assertEquals( "methodBar", properties.get( "methodBar" ).getName() );
		assertEquals( "methodBaz", properties.get( "methodBaz" ).getName() );
		assertEquals( "methodCovariant", properties.get( "methodCovariant" ).getName() );
		assertEquals( "methodFoo", properties.get( "methodFoo" ).getName() );
		assertEquals( "methodGetterInSuper", properties.get( "methodGetterInSuper" ).getName() );
		assertEquals( "methodSetterInSuper", properties.get( "methodSetterInSuper" ).getName() );
	}

	public void testSupportPublicFields() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig() );
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
		assertEquals( "baz", properties.get( "baz" ).getName() );

		propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( false ) );
		properties = propertyStyle.getProperties( Foo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.size() == 7 );

		assertTrue( !properties.containsKey( "foo" ) );
		assertTrue( !properties.containsKey( "bar" ) );
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
		assertTrue( !properties.containsKey( "baz" ) );
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
		assertTrue( properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ) );
	}

	public void testPublicFieldAndGetter() {

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

	public void testClearCache()
		throws Exception {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

		Field propertiesCacheField = BaseTraitStyle.class.getDeclaredField( "mCache" );
		propertiesCacheField.setAccessible( true );
		assertTrue( 0 == ( (Map<?, ?>) propertiesCacheField.get( propertyStyle ) ).size() );

		propertyStyle.getProperties( Foo.class );
		assertTrue( 1 == ( (Map<?, ?>) propertiesCacheField.get( propertyStyle ) ).size() );

		propertyStyle.clearCache();
		assertTrue( 0 == ( (Map<?, ?>) propertiesCacheField.get( propertyStyle ) ).size() );
	}

	public void testPrivateFieldAndGetter() {

		// No convention

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( PrivateFieldFoo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.size() == 4 );

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
		properties = propertyStyle.getProperties( PrivateFieldFoo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.size() == 4 );

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
		properties = propertyStyle.getProperties( PrivateFieldFoo.class );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertTrue( properties.size() == 4 );

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

	public void testStrictJavaBeanConvention()
		throws Exception {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		assertEquals( null, propertyStyle.isGetter( StrictJavaBeanConventionFoo.class.getMethod( "isBigBoolean1" ) ) );
		assertEquals( "littleBoolean", propertyStyle.isGetter( StrictJavaBeanConventionFoo.class.getMethod( "isLittleBoolean" ) ) );
		assertEquals( "bigBoolean2", propertyStyle.isGetter( StrictJavaBeanConventionFoo.class.getMethod( "getBigBoolean2" ) ) );
	}

	public void testExcludeOverriddenGetter() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( ExcludeOverriddenGetterFoo.class );
		assertTrue( properties.containsKey( "foo" ));

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeBaseType( Pattern.compile( ".*SuperExcludeOverriddenGetterFoo" ) );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( ExcludeOverriddenGetterFoo.class );
		assertTrue( !properties.containsKey( "foo" ));
	}

	public void testExcludeOverriddenSetter() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( ExcludeOverriddenSetterFoo.class );
		assertTrue( properties.containsKey( "foo" ));

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeBaseType( Pattern.compile( ".*SuperExcludeOverriddenSetterFoo" ) );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( ExcludeOverriddenSetterFoo.class );
		assertTrue( !properties.containsKey( "foo" ));
	}

	public void testStaticMethods() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( StaticMethodsTest.class );
		assertTrue( !properties.containsKey( "staticString" ));
		assertTrue( properties.containsKey( "nonStaticField" ));
		assertTrue( properties.containsKey( "nonStaticString" ));
		assertEquals( 2, properties.size() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JavaBeanPropertyStyleConfig.class, new JavaBeanPropertyStyleConfig() {
			// Subclass
		} );
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

		public String getterIgnoreBecauseLowercase() {

			return null;
		}

		/**
		 * @param ignore
		 */

		public void setterIgnoreBecauseLowercase( String ignore ) {

			// Do nothing
		}
	}

	static class ErrorFoo
		extends Foo {

		/**
		 * @param aFoo
		 */

		public void setFoo( String aFoo ) {

			// Will error
		}
	}

	static class ErrorFoo2
		extends ErrorFoo {

		public String getFoo() {

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

	static class StrictJavaBeanConventionFoo {

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

	static class SuperExcludeOverriddenGetterFoo {

		public String getFoo() {

			return null;
		}
	}

	static class ExcludeOverriddenGetterFoo
		extends SuperExcludeOverriddenGetterFoo {

		@Override
		public String getFoo() {

			return null;
		}

		/**
		 * @param foo
		 *            not stored
		 */

		public void setFoo( String foo ) {

			// Do nothing
		}
	}

	static class SuperExcludeOverriddenSetterFoo {

		/**
		 * @param foo
		 *            not stored
		 */

		public void setFoo( String foo ) {

			// Do nothing
		}
	}

	static class ExcludeOverriddenSetterFoo
		extends SuperExcludeOverriddenSetterFoo {

		public String getFoo() {

			return null;
		}

		/**
		 * @param foo
		 *            not stored
		 */

		@Override
		public void setFoo( String foo ) {

			// Do nothing
		}
	}

	static class StaticMethodsTest {

		public static String staticString;

		public static String getStaticString() {
			return null;
		}

		public static void setStaticString( String aStaticString ) {
			staticString = aStaticString;
		}

		public String nonStaticField;

		public String getNonStaticString() {
			return null;
		}

		public void setNonStaticString( String aNonStaticString ) {
			nonStaticField = aNonStaticString;
		}
	}
}
