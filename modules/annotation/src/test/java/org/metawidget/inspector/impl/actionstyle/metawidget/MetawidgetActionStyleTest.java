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

package org.metawidget.inspector.impl.actionstyle.metawidget;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseTraitStyle;
import org.metawidget.inspector.impl.actionstyle.Action;

/**
 * @author Richard Kennard
 */

public class MetawidgetActionStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testMetawidgetActionStyle() {

		MetawidgetActionStyle actionStyle = new MetawidgetActionStyle();
		Map<String, Action> actions = actionStyle.getActions( Foo.class );

		assertTrue( actions.size() == 1 );
		assertEquals( "bar", actions.get( "bar" ).toString() );

		try {
			actionStyle.getActions( BadFoo.class );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "@UiAction public abstract void org.metawidget.inspector.impl.actionstyle.metawidget.MetawidgetActionStyleTest$BadFoo.bar(java.lang.String) must not take any parameters", e.getMessage() );
		}
	}

	public void testInterfaceBasedActionStyle() {

		MetawidgetActionStyle actionStyle = new MetawidgetActionStyle();
		Map<String, Action> actions = actionStyle.getActions( Proxied_$$_javassist_.class );

		assertTrue( actions instanceof TreeMap<?, ?> );
		assertTrue( actions.get( "bar1" ).isAnnotationPresent( UiAction.class ) );
		assertTrue( actions.get( "baz" ).isAnnotationPresent( UiAction.class ) );
		assertTrue( actions.size() == 2 );

		actions = actionStyle.getActions( new InterfaceBar() {

			public void baz() {

				// Do nothing
			}

		}.getClass() );

		assertTrue( actions instanceof TreeMap<?, ?> );
		assertTrue( actions.get( "baz" ).isAnnotationPresent( UiAction.class ) );
		assertTrue( actions.size() == 1 );
	}

	public void testClearCache()
		throws Exception {

		MetawidgetActionStyle actionStyle = new MetawidgetActionStyle();

		Field propertiesCacheField = BaseTraitStyle.class.getDeclaredField( "mCache" );
		propertiesCacheField.setAccessible( true );
		assertTrue( 0 == ( (Map<?, ?>) propertiesCacheField.get( actionStyle ) ).size() );

		actionStyle.getActions( Foo.class );
		assertTrue( 1 == ( (Map<?, ?>) propertiesCacheField.get( actionStyle ) ).size() );

		actionStyle.clearCache();
		assertTrue( 0 == ( (Map<?, ?>) propertiesCacheField.get( actionStyle ) ).size() );
	}

	//
	// Inner class
	//

	abstract class Foo {

		@UiAction
		public abstract void bar();

		@UiAction
		protected abstract void shouldntFindMe();

		@SuppressWarnings( "unused" )
		@UiAction
		private void shouldntFindMeEither() {

			// Do nothing
		}
	}

	abstract class BadFoo {

		@UiAction
		public abstract void bar( String baz );
	}

	abstract class Proxied_$$_javassist_
		implements InterfaceFoo, InterfaceBar {
		// Abstract
	}

	interface InterfaceFoo {

		@UiAction
		void bar1();

		void bar2();
	}

	interface InterfaceBar {

		@UiAction
		void baz();
	}
}
