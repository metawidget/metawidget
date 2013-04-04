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

package org.metawidget.vaadin.ui;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Field;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.sort.ComesAfterInspectionResultProcessor;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspectorTest.RecursiveFoo;
import org.metawidget.util.CollectionUtils;
import org.metawidget.vaadin.ui.widgetbuilder.VaadinWidgetBuilder;
import org.metawidget.vaadin.ui.widgetprocessor.binding.reflection.ReflectionBindingProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

/**
 * @author Richard Kennard
 */

public class VaadinMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testRecursion() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		RecursiveFoo foo = new RecursiveFoo();
		foo.setFoo( foo );
		metawidget.setToInspect( foo );

		assertEquals( null, ( (VaadinMetawidget) metawidget.getComponent( "foo" ) ).getComponent( "foo" ) );
	}

	/**
	 * Suppress 'cast' warning because using workaround from:
	 * http://www.mail-archive.com/users@maven.apache.org/msg106906.html
	 */

	@SuppressWarnings( "cast" )
	public void testMaximumInspectionDepth() {

		Foo foo1 = new Foo();
		Foo foo2 = new Foo();
		Foo foo3 = new Foo();
		foo1.setFoo( foo2 );
		foo2.setFoo( foo3 );
		foo2.setName( "Bar" );
		foo3.setFoo( new Foo() );

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setToInspect( foo1 );
		metawidget.setMaximumInspectionDepth( 0 );
		assertEquals( metawidget.getComponent( "foo" ), null );

		metawidget.setMaximumInspectionDepth( 1 );
		assertEquals( 1, metawidget.getMaximumInspectionDepth() );
		assertTrue( ( (Component) metawidget.getComponent( "foo" ) ) instanceof VaadinMetawidget );
		assertTrue( ( (Component) metawidget.getComponent( "foo", "name" ) ) instanceof TextField );
		assertEquals( "name", ((AbstractComponent) ( (Component) metawidget.getComponent( "foo", "name" ) )).getData() );
		assertEquals( metawidget.getComponent( "foo", "foo" ), null );

		metawidget.setMaximumInspectionDepth( 2 );
		assertTrue( ( (Component) metawidget.getComponent( "foo", "foo" ) ) instanceof VaadinMetawidget );
		assertEquals( metawidget.getComponent( "foo", "foo", "foo" ), null );

		metawidget.setMaximumInspectionDepth( 3 );
		assertTrue( ( (Component) metawidget.getComponent( "foo", "foo", "foo" ) ) instanceof VaadinMetawidget );
		assertEquals( metawidget.getComponent( "foo", "foo", "foo", "foo" ), null );

		metawidget.setMaximumInspectionDepth( 4 );

		// Goes: root (foo1) -> foo (foo2) -> foo (foo3) -> foo (new Foo) -> foo == null
		//
		// ...but because we know the type of the child property, we end up putting an empty
		// Metawidget

		assertTrue( ( (Component) metawidget.getComponent( "foo", "foo", "foo", "foo" ) ) instanceof VaadinMetawidget );
		assertEquals( 0, ((FormLayout) ( (VaadinMetawidget) metawidget.getComponent( "foo", "foo", "foo", "foo" ) ).getContent()).getComponentCount() );
		assertEquals( metawidget.getComponent( "foo", "foo", "foo", "foo", "foo" ), null );
	}

	public void testGroovy()
		throws Exception {

		// Test supporting the enum dropdown for a dynamic type...

		// ...both nullable (the default)...

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, "dynamic-type-that-cant-be-loaded" );
		attributes.put( LOOKUP, "bar,baz" );

		VaadinMetawidget metawidget = new VaadinMetawidget();
		VaadinWidgetBuilder widgetBuilder = new VaadinWidgetBuilder();

		Component component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof Select );
		assertTrue( ((Select) component).isNullSelectionAllowed() );
		assertEquals( 2, ( (Select) component ).getItemIds().size() );

		// ...and not-nullable...

		attributes.put( REQUIRED, TRUE );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof Select );
		assertFalse( ((Select) component).isNullSelectionAllowed() );
		assertEquals( 2, ( (Select) component ).getItemIds().size() );

		// Also test UiDontExpand on a dynamic type

		attributes.remove( REQUIRED );
		attributes.remove( LOOKUP );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertEquals( null, component );

		attributes.put( DONT_EXPAND, TRUE );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof TextField );
	}

	public void testNestedActionBinding() {

		Foo foo1 = new Foo();
		Foo foo2 = new Foo();
		Foo foo3 = new Foo();
		foo1.setFoo( foo2 );
		foo2.setFoo( foo3 );

		VaadinMetawidget metawidget = new VaadinMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.addWidgetProcessor( new FooActionBindingProcessor() );
		metawidget.setToInspect( foo1 );

		FormLayout layout = (FormLayout) metawidget.getContent();
		clickButton( (Button) layout.getComponent( 0 ) );
		FormLayout nestedLayout = (FormLayout) ( (VaadinMetawidget) layout.getComponent( 1 ) ).getContent();
		FormLayout nestedLayout2 = (FormLayout) ( (VaadinMetawidget) nestedLayout.getComponent( 1 ) ).getContent();
		clickButton( (Button) nestedLayout2.getComponent( 0 ) );

		assertEquals( "FooActionBindingProcessor fired", ( (TextField) layout.getComponent( 2 ) ).getValue() );
		assertEquals( "", ( (TextField) nestedLayout.getComponent( 2 ) ).getValue() );
		assertEquals( "FooActionBindingProcessor fired", ( (TextField) nestedLayout2.getComponent( 2 ) ).getValue() );
	}

	public void testFacet() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.addComponent( new Facet() );

		// Should be no layout

		assertEquals( null, metawidget.getContent() );
		metawidget.setPath( "Foo" );
		assertEquals( ((FormLayout) metawidget.getContent()).getComponentCount(), 0 );

		// Normal component should appear (but still not facet)

		metawidget.addComponent( new TextField() );
		assertTrue( ((FormLayout) metawidget.getContent()).getComponent( 0 ) instanceof TextField );
		assertEquals( metawidget.getComponentCount(), 1 );
	}

	public void testRequiredBoolean() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setToInspect( new FooRequiredBoolean() );

		assertTrue( ((FormLayout) metawidget.getContent()).getComponent( 0 ) instanceof CheckBox );
	}

	public void testLabelString() {

		VaadinMetawidget metawidget = new VaadinMetawidget();

		assertEquals( "", metawidget.getLabelString( null ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertEquals( "", metawidget.getLabelString( attributes ) );

		attributes.put( NAME, "foo" );
		assertEquals( "Foo", metawidget.getLabelString( attributes ) );

		attributes.put( LABEL, "foo" );
		assertEquals( "foo", metawidget.getLabelString( attributes ) );
	}

	public void testGetWidgetProcessor() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setConfig( "org/metawidget/vaadin/ui/metawidget-get-widget-processor.xml" );
		metawidget.setToInspect( new String() );
		assertTrue( null != metawidget.getWidgetProcessor( FooActionBindingProcessor.class ) );
	}

	@SuppressWarnings( "unchecked" )
	public void testBuildWidgets()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();

		Field needToBuildWidgets = VaadinMetawidget.class.getDeclaredField( "mNeedToBuildWidgets" );
		needToBuildWidgets.setAccessible( true );
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setToInspect( "" );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getComponentCount();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.addInspectionResultProcessor( new ComesAfterInspectionResultProcessor<VaadinMetawidget>() );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getComponentIterator();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.removeInspectionResultProcessor( new ComesAfterInspectionResultProcessor<VaadinMetawidget>() );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getContent();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setInspectionResultProcessors( (InspectionResultProcessor[]) null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getContent();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setWidgetBuilder( null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getComponentCount();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.addWidgetProcessor( new ReflectionBindingProcessor() );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getWidgetProcessor( ReflectionBindingProcessor.class );
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.removeWidgetProcessor( new ReflectionBindingProcessor() );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getComponentCount();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setWidgetProcessors( (WidgetProcessor[]) null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getComponentCount();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setLayout( null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getComponentCount();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setBundle( null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getFacet( "foo" );
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setReadOnly( true );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		try {
			metawidget.paintContent( null );
		} catch( NullPointerException e ) {
			// Will NullPointerException because no PaintTarget
		}
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setMaximumInspectionDepth( 0 );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
	}

	//
	// Inner class
	//

	public static class Foo {

		//
		// Private members
		//

		private String	mName;

		private Foo		mFoo;

		//
		// Public methods
		//

		public String getName() {

			return mName;
		}

		public void setName( String name ) {

			mName = name;
		}

		public Foo getFoo() {

			return mFoo;
		}

		public void setFoo( Foo foo ) {

			mFoo = foo;
		}

		@UiAction
		public void doAction() {

			// Do nothing
		}
	}

	public static class FooRequiredBoolean {

		//
		// Public methods
		//

		@UiRequired
		public Boolean getBooleanObject() {

			return Boolean.FALSE;
		}

		/**
		 * @param booleanObject
		 */

		public void setBooleanObject( Boolean booleanObject ) {

			// Do nothing
		}
	}

	public static class FooActionBindingProcessor
		implements WidgetProcessor<Component, VaadinMetawidget> {

		//
		// Public methods
		//

		@SuppressWarnings( "serial" )
		public Component processWidget( Component component, String elementName, Map<String, String> attributes, final VaadinMetawidget metawidget ) {

			if ( !ACTION.equals( elementName ) ) {
				return component;
			}

			Button button = (Button) component;

			button.addListener( new ClickListener() {

				public void buttonClick( ClickEvent event ) {

					((TextField) metawidget.getComponent( "name" )).setValue( "FooActionBindingProcessor fired" );
				}
			} );

			return component;
		}
	}

	//
	// Private methods
	//

	private void clickButton( Button button ) {

		new ClickShortcut( button, "" ).handleAction( null, null );
	}
}
