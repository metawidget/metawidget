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

package org.metawidget.swt.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
		compositeConfig.setInspectors( new PropertyTypeInspector(), new MetawidgetAnnotationInspector() );

		// Inspect

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setInspector( new CompositeInspector( compositeConfig ) );
		metawidget.setToInspect( new Foo() );

		Button button = (Button) ( (SwtMetawidget) metawidget.getChildren()[1] ).getChildren()[0];

		assertEquals( mActionFired, 0 );
		button.notifyListeners( SWT.Selection, null );
		assertEquals( mActionFired, 1 );
	}

	public void testNullBinding() {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		// Null object

		Button button = new Button( metawidget, SWT.NONE );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		binding.processWidget( button, ACTION, attributes, null );

		// Null nested object

		Foo foo = new Foo();
		foo.setNestedFoo( null );

		metawidget.setToInspect( foo );
		metawidget.setInspectionPath( "foo/nestedFoo/doAction" );
		binding.processWidget( button, ACTION, attributes, metawidget );
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

		@UiAction
		public void doAction() {

			mActionFired++;
		}
	}
}
