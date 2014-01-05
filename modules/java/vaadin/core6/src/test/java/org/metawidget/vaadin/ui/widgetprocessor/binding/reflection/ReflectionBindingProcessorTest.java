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

package org.metawidget.vaadin.ui.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Field;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.widgetprocessor.binding.reflection.ReflectionBindingProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

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

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setInspector( new CompositeInspector( compositeConfig ) );
		metawidget.setToInspect( new Foo() );

		Button button = (Button) ( (FormLayout) ( (VaadinMetawidget) ( (FormLayout) metawidget.getContent() ).getComponent( 0 ) ).getContent() ).getComponent( 0 );

		assertEquals( mActionFired, 0 );
		clickButton( button );
		assertEquals( mActionFired, 1 );
	}

	public void testNullBinding()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		// Null object

		Button button = new Button();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		binding.processWidget( button, ACTION, attributes, null );

		Field eventRouter = AbstractComponent.class.getDeclaredField( "eventRouter" );
		eventRouter.setAccessible( true );
		assertEquals( null, eventRouter.get( button ) );

		// Null nested object

		Foo foo = new Foo();
		foo.setNestedFoo( null );

		metawidget.setToInspect( foo );
		metawidget.setPath( "foo/nestedFoo/doAction" );
		binding.processWidget( button, ACTION, attributes, metawidget );

		assertEquals( null, eventRouter.get( button ) );

		// Normal binding

		metawidget.setToInspect( foo );
		metawidget.setPath( "foo" );
		binding.processWidget( button, ACTION, attributes, metawidget );

		assertTrue( eventRouter.get( button ) != null );
	}

	public void testBadBinding() {

		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		try {
			binding.processWidget( new TextField(), ACTION, null, null );
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

		/**
		 * @param event
		 */

		@UiAction
		public void doAction() {

			mActionFired++;
		}
	}

	//
	// Private methods
	//

	private void clickButton( Button button ) {

		new ClickShortcut( button, "" ).handleAction( null, null );
	}
}
