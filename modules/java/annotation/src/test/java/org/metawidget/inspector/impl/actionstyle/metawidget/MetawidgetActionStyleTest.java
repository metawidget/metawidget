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

package org.metawidget.inspector.impl.actionstyle.metawidget;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseTraitStyle;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class MetawidgetActionStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testMetawidgetActionStyle() {

		MetawidgetActionStyle actionStyle = new MetawidgetActionStyle();
		Map<String, Action> actions = actionStyle.getActions( Foo.class.getName() );

		assertEquals( actions.size(), 1 );
		assertEquals( "bar", actions.get( "bar" ).toString() );

		try {
			actionStyle.getActions( BadFoo.class.getName() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "@UiAction public abstract void org.metawidget.inspector.impl.actionstyle.metawidget.MetawidgetActionStyleTest$BadFoo.bar(java.lang.String) must not take any parameters", e.getMessage() );
		}
	}

	public void testInterfaceBasedActionStyle() {

		MetawidgetActionStyle actionStyle = new MetawidgetActionStyle();
		Map<String, Action> actions = actionStyle.getActions( Proxied_$$_javassist_.class.getName() );

		assertTrue( actions instanceof TreeMap<?, ?> );
		assertTrue( actions.get( "bar1" ).isAnnotationPresent( UiAction.class ) );
		assertTrue( actions.get( "baz" ).isAnnotationPresent( UiAction.class ) );
		assertEquals( actions.size(), 2 );

		actions = actionStyle.getActions( new InterfaceBar() {

			public void baz() {

				// Do nothing
			}

		}.getClass().getName() );

		assertTrue( actions instanceof TreeMap<?, ?> );
		assertTrue( actions.get( "baz" ).isAnnotationPresent( UiAction.class ) );
		assertEquals( actions.size(), 1 );
	}

	public void testIsExcluded() {

		final List<Object> isExcluded = CollectionUtils.newArrayList();

		MetawidgetActionStyle actionStyle = new MetawidgetActionStyle() {

			@Override
			protected boolean isExcludedBaseType( Class<?> classToExclude ) {

				isExcluded.add( classToExclude );
				return  super.isExcludedBaseType( classToExclude );
			}

			@Override
			protected boolean isExcludedReturnType( Class<?> clazz ) {
				isExcluded.add( clazz );
				return super.isExcludedReturnType( clazz );
			}

			@Override
			protected boolean isExcludedName( String name ) {

				isExcluded.add( name );
				return super.isExcludedName( name );
			}
		};

		actionStyle.getActions( Proxied_$$_javassist_.class.getName() );

		assertEquals( InterfaceFoo.class, isExcluded.get( 0 ));
		assertEquals( void.class, isExcluded.get( 1 ));
		assertEquals( "bar1", isExcluded.get( 2 ));
		assertEquals( InterfaceBar.class, isExcluded.get( 3 ));
		assertEquals( void.class, isExcluded.get( 4 ));
		assertEquals( "baz", isExcluded.get( 5 ));
		assertEquals( 6, isExcluded.size() );
	}

	public void testClearCache()
		throws Exception {

		MetawidgetActionStyle actionStyle = new MetawidgetActionStyle();

		Field propertiesCacheField = BaseTraitStyle.class.getDeclaredField( "mCache" );
		propertiesCacheField.setAccessible( true );
		assertEquals( 0, ( (Map<?, ?>) propertiesCacheField.get( actionStyle ) ).size() );

		actionStyle.getActions( Foo.class.getName() );
		assertEquals( 1, ( (Map<?, ?>) propertiesCacheField.get( actionStyle ) ).size() );

		actionStyle.clearCache();
		assertEquals( 0, ( (Map<?, ?>) propertiesCacheField.get( actionStyle ) ).size() );
	}

	//
	// Inner class
	//

	abstract class Foo {

		@UiAction
		public abstract void bar();

		@UiAction
		protected abstract void shouldntFindMe();

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
