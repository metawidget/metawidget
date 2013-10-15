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

package org.metawidget.android.widget.widgetprocessor.reflection;

import junit.framework.TestCase;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.layout.SimpleLayout;
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
