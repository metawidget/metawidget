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

package org.metawidget.inspector.impl.actionstyle.swing;

import java.awt.event.ActionEvent;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.Action;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingAppFrameworkActionStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testSwingAppFrameworkActionStyle() {

		SwingAppFrameworkActionStyle actionStyle = new SwingAppFrameworkActionStyle();
		Map<String, Action> actions = actionStyle.getActions( Foo.class.getName() );

		assertEquals( actions.size(), 2 );
		assertEquals( "bar", actions.get( "bar" ).toString() );
		assertEquals( "baz", actions.get( "baz" ).toString() );

		try {
			actionStyle.getActions( BadFoo.class.getName() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "@Action public abstract void org.metawidget.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyleTest$BadFoo.bar(java.lang.String,java.lang.String) must not have more than one parameter", e.getMessage() );
		}

		try {
			actionStyle.getActions( BadFoo2.class.getName() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "@Action public abstract void org.metawidget.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyleTest$BadFoo2.bar(java.lang.String) parameter must be a java.awt.event.ActionEvent", e.getMessage() );
		}
	}

	//
	// Inner class
	//

	abstract class Foo {

		@org.jdesktop.application.Action
		public abstract void bar();

		@org.jdesktop.application.Action
		public abstract void baz( ActionEvent event );
	}

	abstract class BadFoo {

		@org.jdesktop.application.Action
		public abstract void bar( String baz, String bar );
	}

	abstract class BadFoo2 {

		@org.jdesktop.application.Action
		public abstract void bar( String baz );
	}
}
