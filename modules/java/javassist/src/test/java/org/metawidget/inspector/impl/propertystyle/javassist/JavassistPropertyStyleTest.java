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

package org.metawidget.inspector.impl.propertystyle.javassist;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JavassistPropertyStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJavassist() {

		JavassistPropertyStyle propertyStyle = new JavassistPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) );
		Map<String, Property> properties;

		try {
			properties = propertyStyle.getProperties( Foo.class.getName() );
		} catch ( InspectorException e ) {
			assertEquals( "Line number information for org.metawidget.inspector.impl.propertystyle.javassist.JavassistPropertyStyleTest$Foo not available. Did you compile without debug info?", e.getMessage() );
			return;
		}

		assertTrue( properties instanceof LinkedHashMap<?, ?> );

		Iterator<Property> i = properties.values().iterator();
		assertEquals( "superBar", i.next().getName() );
		assertEquals( "superFoo", i.next().getName() );
		assertEquals( "methodSuperFoo", i.next().getName() );
		assertEquals( "methodSuperBar", i.next().getName() );
		assertEquals( "bar", i.next().getName() );
		assertEquals( "foo", i.next().getName() );
		assertEquals( "methodFoo", i.next().getName() );
		assertEquals( "methodBar", i.next().getName() );
		assertEquals( "methodBaz", i.next().getName() );
		assertFalse( i.hasNext() );
	}

	public void testSupportPublicFields() {

		JavassistPropertyStyle propertyStyle = new JavassistPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) );
		Map<String, Property> properties;

		try {
			properties = propertyStyle.getProperties( Foo.class.getName() );
		} catch ( InspectorException e ) {
			assertEquals( "Line number information for org.metawidget.inspector.impl.propertystyle.javassist.JavassistPropertyStyleTest$Foo not available. Did you compile without debug info?", e.getMessage() );
			return;
		}

		assertTrue( properties instanceof LinkedHashMap<?, ?> );

		Iterator<Property> i = properties.values().iterator();
		assertEquals( "superBar", i.next().getName() );
		assertEquals( "superFoo", i.next().getName() );
		assertEquals( "methodSuperFoo", i.next().getName() );
		assertEquals( "methodSuperBar", i.next().getName() );
		assertEquals( "bar", i.next().getName() );
		assertEquals( "foo", i.next().getName() );
		assertEquals( "methodFoo", i.next().getName() );
		assertEquals( "methodBar", i.next().getName() );
		assertEquals( "methodBaz", i.next().getName() );
		assertFalse( i.hasNext() );

		propertyStyle = new JavassistPropertyStyle( new JavaBeanPropertyStyleConfig() );
		properties = propertyStyle.getProperties( Foo.class.getName() );

		assertTrue( properties instanceof LinkedHashMap<?, ?> );

		i = properties.values().iterator();
		assertEquals( "methodSuperFoo", i.next().getName() );
		assertEquals( "methodSuperBar", i.next().getName() );
		assertEquals( "methodFoo", i.next().getName() );
		assertEquals( "methodBar", i.next().getName() );
		assertEquals( "methodBaz", i.next().getName() );
		assertFalse( i.hasNext() );
	}

	public void testInterfaceBasedPropertyStyle() {

		JavassistPropertyStyle propertyStyle = new JavassistPropertyStyle();
		Map<String, Property> properties;

		try {
			properties = propertyStyle.getProperties( ProxiedByCGLIB$$.class.getName() );
		} catch ( InspectorException e ) {
			assertEquals( "Line number information for org.metawidget.inspector.impl.propertystyle.javassist.JavassistPropertyStyleTest$ProxiedByCGLIB$$ not available. Did you compile without debug info?", e.getMessage() );
			return;
		}

		assertTrue( properties instanceof LinkedHashMap<?, ?> );
		assertTrue( properties.get( "interfaceBar" ).isAnnotationPresent( UiMasked.class ) );
	}

	//
	// Inner class
	//

	class Foo
		extends SuperFoo {

		@SuppressWarnings( "unused" )
		private boolean	ignoreMe;

		public Date		foo;

		public Long		bar;

		public String getMethodFoo() {

			return null;
		}

		public int getMethodBar() {

			return 0;
		}

		public List<String> getMethodBaz() {

			return null;
		}
	}

	class SuperFoo {

		public Byte		superFoo;

		public Object	superBar;

		public String getMethodSuperFoo() {

			return null;
		}

		/**
		 * @param superBarParam
		 */

		public void setMethodSuperBar( int superBarParam ) {

			// Do nothing
		}
	}

	class ProxiedByCGLIB$$
		implements InterfaceFoo {

		public Object getInterfaceBar() {

			return null;
		}
	}

	interface InterfaceFoo {

		@UiMasked
		Object getInterfaceBar();
	}
}
