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

package org.metawidget.android.widget.widgetprocessor.reflection;

import junit.framework.TestCase;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.inspector.annotation.UiAction;

import android.widget.Button;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReflectionBindingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testBinding()
		throws Exception {

		// Model

		Foo foo = new Foo();

		// Inspect

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		metawidget.setToInspect( foo );

		// Click

		Button button = metawidget.findViewWithTags( "doAction" );
		assertEquals( ReflectionBindingProcessor.class.getName() + "$BoundAction", button.getOnClickListener().getClass().getName() );

		assertEquals( 0, foo.readClicked() );
		button.getOnClickListener().onClick( null );
		assertEquals( 1, foo.readClicked() );
	}

	//
	// Inner class
	//

	protected static class Foo {

		//
		// Private members
		//

		private int mClicked;

		//
		// Public methods
		//

		@UiAction
		public void doAction() {

			mClicked++;
		}

		public int readClicked() {

			return mClicked;
		}
	}
}
