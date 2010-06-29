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

package org.metawidget.swt.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdesktop.application.Action;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.swing.SwingAppFrameworkInspector;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * @author Richard Kennard
 */

public class ReflectionBindingProcessorTest
	extends TestCase {

	//
	// Package-level members
	//

	int	mActionFired;

	//
	// Public methods
	//

	public void testBinding()
		throws Exception {

		// Configure

		CompositeInspectorConfig compositeConfig = new CompositeInspectorConfig();
		compositeConfig.setInspectors( new PropertyTypeInspector(), new SwingAppFrameworkInspector() );

		// Inspect

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setInspector( new CompositeInspector( compositeConfig ) );
		metawidget.setToInspect( new Foo() );

		Button button = (Button) ( (SwtMetawidget) metawidget.getChildren()[1] ).getChildren()[0];

		assertTrue( mActionFired == 0 );
		button.notifyListeners( SWT.Selection, null );
		assertTrue( mActionFired == 1 );
	}

	public void testNullBinding() {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		// Null object

		Button button = new Button( metawidget, SWT.NONE );
		binding.processWidget( button, ACTION, null, null );

		// Null nested object

		Foo foo = new Foo();
		foo.setNestedFoo( null );

		metawidget.setToInspect( foo );
		metawidget.setInspectionPath( "foo/nestedFoo/doAction" );
		binding.processWidget( button, ACTION, null, metawidget );
	}

	public void testBadBinding() {

		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		try {
			binding.processWidget( new Text( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE ), ACTION, null, null );
		} catch ( WidgetProcessorException e ) {
			assertEquals( "ReflectionBindingProcessor only supports binding actions to Buttons", e.getMessage() );
		}
	}

	//
	// Inner class
	//

	public class Foo {

		//
		// Private members
		//

		private NestedFoo	mNestedFoo	= new NestedFoo();

		//
		// Public methods
		//

		public NestedFoo getNestedFoo() {

			return mNestedFoo;
		}

		public void setNestedFoo( NestedFoo nestedFoo ) {

			mNestedFoo = nestedFoo;
		}
	}

	public class NestedFoo {

		//
		// Public methods
		//

		@Action
		public void doAction() {

			mActionFired++;
		}
	}
}
