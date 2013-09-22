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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseTraitStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JavaBeanPropertyStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testExcludeReturnType() {

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig().setSupportPublicFields( true );

		// Without excluded type

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( config );
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 10, properties.size() );

		assertEquals( "baz", properties.get( "baz" ).getName() );

		// With excluded type

		config.setExcludeReturnType( String.class );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 4, properties.size() );

		assertEquals( "bar", properties.get( "bar" ).getName() );
		assertEquals( "baz", properties.get( "baz" ).getName() );
		assertEquals( "methodAbc", properties.get( "methodAbc" ).getName() );
		assertEquals( "methodBaz", properties.get( "methodBaz" ).getName() );
	}

	public void testExcludeName() {

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig().setSupportPublicFields( true );

		// Without excluded name

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( config );
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 10, properties.size() );

		assertEquals( "baz", properties.get( "baz" ).getName() );

		// With excluded name

		config.setExcludeName( "bar", "baz" );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof TreeMap<?, ?> );
		assertEquals( 8, properties.size() );

		assertEquals( "foo", properties.get( "foo" ).getName() );
		assertEquals( "methodAbc", properties.get( "methodAbc" ).getName() );
		assertEquals( "methodBar", properties.get( "methodBar" ).getName() );
		assertEquals( "methodBaz", properties.get( "methodBaz" ).getName() );
		assertEquals( "methodCovariant", properties.get( "methodCovariant" ).getName() );
		assertEquals( "methodFoo", properties.get( "methodFoo" ).getName() );
		assertEquals( "methodGetterInSuper", properties.get( "methodGetterInSuper" ).getName() );
		assertEquals( "methodSetterInSuper", properties.get( "methodSetterInSuper" ).getName() );
	}

	public void testPublicFieldAndGetter() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

		try {
			propertyStyle.getProperties( ErrorFoo.class.getName() );
		} catch ( InspectorException e ) {
			assertEquals( "JavaBeanProperty 'public java.lang.String org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleTest$Foo.foo' has both a public member variable and a public setter method. Should be one or the other", e.getMessage() );
		}

		try {
			propertyStyle.getProperties( ErrorFoo2.class.getName() );
		} catch ( InspectorException e ) {
			assertEquals( "JavaBeanProperty 'public java.lang.String org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleTest$Foo.foo' has both a public member variable and a public getter method. Should be one or the other", e.getMessage() );
		}
	}

	public void testClearCache()
		throws Exception {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

		Field propertiesCacheField = BaseTraitStyle.class.getDeclaredField( "mCache" );
		propertiesCacheField.setAccessible( true );
		assertEquals( 0, ( (Map<?, ?>) propertiesCacheField.get( propertyStyle ) ).size() );

		propertyStyle.getProperties( Foo.class.getName() );
		assertEquals( 1, ( (Map<?, ?>) propertiesCacheField.get( propertyStyle ) ).size() );

		propertyStyle.clearCache();
		assertEquals( 0, ( (Map<?, ?>) propertiesCacheField.get( propertyStyle ) ).size() );
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
		Map<String, Property> properties = propertyStyle.getProperties( ExcludeOverriddenGetterFoo.class.getName() );
		assertTrue( properties.containsKey( "foo" ) );

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeBaseType( Pattern.compile( ".*SuperExcludeOverriddenGetterFoo" ) );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( ExcludeOverriddenGetterFoo.class.getName() );
		assertTrue( !properties.containsKey( "foo" ) );
	}

	public void testExcludeOverriddenSetter() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( ExcludeOverriddenSetterFoo.class.getName() );
		assertTrue( properties.containsKey( "foo" ) );

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeBaseType( Pattern.compile( ".*SuperExcludeOverriddenSetterFoo" ) );
		propertyStyle = new JavaBeanPropertyStyle( config );
		properties = propertyStyle.getProperties( ExcludeOverriddenSetterFoo.class.getName() );
		assertTrue( !properties.containsKey( "foo" ) );
	}

	public void testStaticMethods() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) );
		Map<String, Property> properties = propertyStyle.getProperties( StaticMethodsTest.class.getName() );
		assertTrue( !properties.containsKey( "staticString" ) );
		assertTrue( properties.containsKey( "nonStaticField" ) );
		assertTrue( properties.containsKey( "nonStaticString" ) );
		assertEquals( 2, properties.size() );
	}

	public void testAlphabeticalOrdering() {

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( AlphabeticalOrderingTest.class.getName() );
		List<String> ordered = CollectionUtils.newArrayList( properties.keySet() );

		assertEquals( "abc", ordered.get( 0 ) );
		assertEquals( "bar", ordered.get( 1 ) );
		assertEquals( "foo", ordered.get( 2 ) );
		assertEquals( "WEPKey", ordered.get( 3 ) );
		assertEquals( 4, ordered.size() );
	}

	public void testAgainstIntrospector()
		throws Exception {

		// Introspector

		BeanInfo beanInfo = Introspector.getBeanInfo( AlphabeticalOrderingTest.class );

		Map<String, PropertyDescriptor> propertyDescriptors = CollectionUtils.newHashMap();
		for ( PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors() ) {
			propertyDescriptors.put( propertyDescriptor.getName(), propertyDescriptor );
		}

		assertTrue( propertyDescriptors.containsKey( "WEPKey" ) );
		assertTrue( propertyDescriptors.containsKey( "foo" ) );
		assertTrue( propertyDescriptors.containsKey( "bar" ) );
		assertTrue( propertyDescriptors.containsKey( "abc" ) );
		assertTrue( propertyDescriptors.containsKey( "class" ) );
		assertEquals( 5, propertyDescriptors.size() );

		// JavaBeanPropertyStyle

		Map<String, Property> properties = new JavaBeanPropertyStyle().getProperties( AlphabeticalOrderingTest.class.getName() );

		assertTrue( properties.containsKey( "WEPKey" ) );
		assertTrue( properties.containsKey( "foo" ) );
		assertTrue( properties.containsKey( "bar" ) );
		assertTrue( properties.containsKey( "abc" ) );
		assertEquals( 4, properties.size() );

		// Introspector

		beanInfo = Introspector.getBeanInfo( UppercaseLowerCaseTest.class );

		propertyDescriptors = CollectionUtils.newHashMap();
		for ( PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors() ) {
			propertyDescriptors.put( propertyDescriptor.getName(), propertyDescriptor );
		}

		// See: https://community.jboss.org/message/751099
		//
		// Note: Introspector is broken here, IMHO. The JavaBean spec says "the property name should
		// start with a lower case character, which will be capitalized in the method names"

		assertTrue( propertyDescriptors.containsKey( "aFIELD" ) );
		assertEquals( "getaFIELD", propertyDescriptors.get( "aFIELD" ).getReadMethod().getName() );
		assertTrue( propertyDescriptors.containsKey( "tle" ) );
		assertTrue( propertyDescriptors.containsKey( "olate" ) );
		assertTrue( propertyDescriptors.containsKey( "class" ) );
		assertEquals( 4, propertyDescriptors.size() );

		// JavaBeanPropertyStyle (follow the broken Introspector implementation)

		properties = new JavaBeanPropertyStyle().getProperties( UppercaseLowerCaseTest.class.getName() );
		assertTrue( properties.containsKey( "aFIELD" ) );
		assertTrue( properties.containsKey( "tle" ) );
		assertTrue( properties.containsKey( "olate" ) );
		assertEquals( 3, properties.size() );
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

		public String		foo;

		public List<Date>	bar;

		public String getMethodFoo() {

			return null;
		}

		/**
		 * @param methodBar
		 */

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

		public static String	staticString;

		public static String getStaticString() {

			return null;
		}

		public static void setStaticString( String aStaticString ) {

			staticString = aStaticString;
		}

		public String	nonStaticField;

		public String getNonStaticString() {

			return null;
		}

		public void setNonStaticString( String aNonStaticString ) {

			nonStaticField = aNonStaticString;
		}
	}

	static class AlphabeticalOrderingTest {

		//
		// Public methods
		//

		public String getWEPKey() {

			return null;
		}

		public String getFoo() {

			return null;
		}

		public String getBar() {

			return null;
		}

		public String getAbc() {

			return null;
		}
	}

	static class UppercaseLowerCaseTest {

		//
		// Public methods
		//

		public String getaFIELD() {

			return null;
		}

		public boolean isolate() {

			return false;
		}

		/**
		 * @param settlementAmount
		 *            ignored
		 */

		public void settle( String settlementAmount ) {

			// Do nothing
		}
	}
}
