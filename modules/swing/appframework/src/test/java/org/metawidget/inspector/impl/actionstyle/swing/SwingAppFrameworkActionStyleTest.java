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

package org.metawidget.inspector.impl.actionstyle.swing;

import java.awt.event.ActionEvent;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyle;

/**
 * @author Richard Kennard
 */

public class SwingAppFrameworkActionStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testSwingAppFrameworkActionStyle() {

		SwingAppFrameworkActionStyle actionStyle = new SwingAppFrameworkActionStyle();
		Map<String, Action> actions = actionStyle.getActions( Foo.class );

		assertTrue( actions.size() == 2 );
		assertEquals( "bar", actions.get( "bar" ).toString() );
		assertEquals( "baz", actions.get( "baz" ).toString() );

		try {
			actionStyle.getActions( BadFoo.class );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "@Action public abstract void org.metawidget.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyleTest$BadFoo.bar(java.lang.String,java.lang.String) must not have more than one parameter", e.getMessage() );
		}

		try {
			actionStyle.getActions( BadFoo2.class );
			assertTrue( false );
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
