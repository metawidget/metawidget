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

package org.metawidget.inspector.impl.propertystyle.javassist;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.inspector.impl.propertystyle.Property;

/**
 * @author Richard Kennard
 */

public class JavassistPropertyStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJavassist() {

		JavassistPropertyStyle propertyStyle = new JavassistPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( Foo.class );

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

	public void testInterfaceBasedPropertyStyle() {

		JavassistPropertyStyle propertyStyle = new JavassistPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( ProxiedByCGLIB$$.class );

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

		public void setMethodSuperBar( int superBarParam ) {

			// Do nothing
		}
	}

	class ProxiedByCGLIB$$
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
