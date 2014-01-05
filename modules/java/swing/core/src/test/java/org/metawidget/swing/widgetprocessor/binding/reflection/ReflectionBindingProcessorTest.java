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

package org.metawidget.swing.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

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

		// Configure

		CompositeInspectorConfig compositeConfig = new CompositeInspectorConfig();
		compositeConfig.setInspectors( new PropertyTypeInspector(), new MetawidgetAnnotationInspector() );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector( compositeConfig ) );
		metawidget.setToInspect( new Foo() );

		JButton button = (JButton) ( (SwingMetawidget) metawidget.getComponent( 1 ) ).getComponent( 0 );

		assertEquals( ((Foo) metawidget.getToInspect()).getNestedFoo().getActionFired(), 0 );
		button.doClick();
		assertEquals( ((Foo) metawidget.getToInspect()).getNestedFoo().getActionFired(), 1 );
		assertEquals( "Do Action", button.getText() );

		// Rebind

		metawidget.getWidgetProcessor( ReflectionBindingProcessor.class ).rebind( new Foo(), metawidget );
		assertEquals( ((Foo) metawidget.getToInspect()).getNestedFoo().getActionFired(), 0 );
		button.doClick();
		assertEquals( ((Foo) metawidget.getToInspect()).getNestedFoo().getActionFired(), 1 );
		assertEquals( "Do Action", button.getText() );
	}

	public void testNullBinding() {

		SwingMetawidget metawidget = new SwingMetawidget();
		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		// Null object (should not NPE)

		JButton button = new JButton();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		binding.processWidget( button, ACTION, attributes, metawidget );

		// Null nested object (should not NPE)

		Foo foo = new Foo();
		foo.setNestedFoo( null );

		metawidget.setToInspect( foo );
		metawidget.setPath( "foo/nestedFoo/doAction" );
		binding.processWidget( button, ACTION, attributes, metawidget );
	}

	public void testBadBinding() {

		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		try {
			binding.processWidget( new JTextField(), ACTION, null, null );
		} catch ( WidgetProcessorException e ) {
			assertEquals( "ReflectionBindingProcessor only supports binding actions to AbstractButtons", e.getMessage() );
		}
	}

	//
	// Inner class
	//

	public static class Foo {

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

	public static class NestedFoo {

		//
		// Private members
		//

		private int	mActionFired;

		//
		// Public methods
		//

		@UiHidden
		public int getActionFired() {

			return mActionFired;
		}

		/**
		 * @param event
		 */

		@UiAction
		public void doAction() {

			mActionFired++;
		}
	}
}
