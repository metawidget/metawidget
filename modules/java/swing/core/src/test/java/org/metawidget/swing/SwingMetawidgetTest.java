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

package org.metawidget.swing;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
import org.metawidget.swing.layout.BoxLayout;
import org.metawidget.swing.layout.TabbedPaneLayoutDecorator;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.reflection.ReflectionBindingProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testBeanInfo()
		throws Exception {

		BeanInfo info = Introspector.getBeanInfo( SwingMetawidget.class );

		assertEquals( "Metawidget", info.getBeanDescriptor().getDisplayName() );
		assertTrue( info.getIcon( BeanInfo.ICON_MONO_16x16 ) != null );
		assertTrue( info.getIcon( BeanInfo.ICON_COLOR_16x16 ) != null );
		assertTrue( info.getIcon( BeanInfo.ICON_MONO_32x32 ) != null );
		assertTrue( info.getIcon( BeanInfo.ICON_COLOR_32x32 ) != null );
		assertTrue( info.getIcon( 5 ) != null );
	}

	@SuppressWarnings( "serial" )
	public void testRepainting() {

		final StringBuilder builder = new StringBuilder();

		SwingMetawidget metawidget = new SwingMetawidget() {

			@Override
			public void repaint() {

				builder.append( "repaint\n" );
				super.repaint();
			}
		};

		// Will see some 'repaint's because of the JPanel

		String panelRepaints = builder.toString();

		// Should see 'repaint' because of the add

		JTextField component = new JTextField();
		metawidget.add( component );
		assertEquals( ( panelRepaints + "repaint\n" ), builder.toString() );

		// Should see another 'repaint' because of remove called .getComponent, which built the
		// widgets

		metawidget.remove( component );
		assertEquals( ( panelRepaints + "repaint\nrepaint\n" ), builder.toString() );

		// Should not see another repaint, because widgets already need repainting

		metawidget.add( component );
		assertEquals( ( panelRepaints + "repaint\nrepaint\n" ), builder.toString() );

		// Should see another repaint, because .remove will build the widgets

		metawidget.remove( 0 );
		assertEquals( ( panelRepaints + "repaint\nrepaint\nrepaint\n" ), builder.toString() );

		// Should not see another repaint, because widgets already need repainting

		metawidget.removeAll();
		assertEquals( ( panelRepaints + "repaint\nrepaint\nrepaint\n" ), builder.toString() );
	}

	public void testRecursion() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		RecursiveFoo foo = new RecursiveFoo();
		foo.setFoo( foo );
		metawidget.setToInspect( foo );

		assertEquals( null, ( (SwingMetawidget) metawidget.getComponent( "foo" ) ).getComponent( "foo" ) );
	}

	/**
	 * Suppress 'cast' warning because using workaround from:
	 * http://www.mail-archive.com/users@maven.apache.org/msg106906.html
	 */

	public void testMaximumInspectionDepth() {

		Foo foo1 = new Foo();
		Foo foo2 = new Foo();
		Foo foo3 = new Foo();
		foo1.setFoo( foo2 );
		foo2.setFoo( foo3 );
		foo2.setName( "Bar" );
		foo3.setFoo( new Foo() );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setToInspect( foo1 );
		metawidget.setMaximumInspectionDepth( 0 );
		assertEquals( metawidget.getComponent( "foo" ), null );

		metawidget.setMaximumInspectionDepth( 1 );
		assertEquals( 1, metawidget.getMaximumInspectionDepth() );
		assertTrue( ( metawidget.getComponent( "foo" ) ) instanceof SwingMetawidget );
		assertTrue( ( metawidget.getComponent( "foo", "name" ) ) instanceof JTextField );
		assertEquals( "name", metawidget.getComponent( "foo", "name" ).getName() );
		assertEquals( metawidget.getComponent( "foo", "foo" ), null );

		metawidget.setMaximumInspectionDepth( 2 );
		assertTrue( ( metawidget.getComponent( "foo", "foo" ) ) instanceof SwingMetawidget );
		assertEquals( metawidget.getComponent( "foo", "foo", "foo" ), null );

		metawidget.setMaximumInspectionDepth( 3 );
		assertTrue( ( metawidget.getComponent( "foo", "foo", "foo" ) ) instanceof SwingMetawidget );
		assertEquals( metawidget.getComponent( "foo", "foo", "foo", "foo" ), null );

		metawidget.setMaximumInspectionDepth( 4 );

		// Goes: root (foo1) -> foo (foo2) -> foo (foo3) -> foo (new Foo) -> foo == null
		//
		// ...but because we know the type of the child property, we end up putting an empty
		// Metawidget

		assertTrue( ( metawidget.getComponent( "foo", "foo", "foo", "foo" ) ) instanceof SwingMetawidget );
		assertEquals( 0, ( (SwingMetawidget) metawidget.getComponent( "foo", "foo", "foo", "foo" ) ).getComponentCount() );
		assertEquals( metawidget.getComponent( "foo", "foo", "foo", "foo", "foo" ), null );
	}

	public void testGroovy()
		throws Exception {

		// Test supporting the enum dropdown for a dynamic type...

		// ...both nullable (the default)...

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, "dynamic-type-that-cant-be-loaded" );
		attributes.put( LOOKUP, "bar,baz" );

		SwingMetawidget metawidget = new SwingMetawidget();
		SwingWidgetBuilder widgetBuilder = new SwingWidgetBuilder();

		JComponent component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof JComboBox );
		assertEquals( 3, ( (JComboBox) component ).getItemCount() );

		// ...and not-nullable...

		attributes.put( REQUIRED, TRUE );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof JComboBox );
		assertEquals( 2, ( (JComboBox) component ).getItemCount() );

		// Also test UiDontExpand on a dynamic type

		attributes.remove( REQUIRED );
		attributes.remove( LOOKUP );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertEquals( null, component );

		attributes.put( DONT_EXPAND, TRUE );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof JTextField );
	}

	public void testNestedActionBinding() {

		Foo foo1 = new Foo();
		Foo foo2 = new Foo();
		Foo foo3 = new Foo();
		foo1.setFoo( foo2 );
		foo2.setFoo( foo3 );

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.addWidgetProcessor( new FooActionBindingProcessor() );
		metawidget.setToInspect( foo1 );

		( (JButton) metawidget.getComponent( 0 ) ).doClick();
		( (JButton) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 2 ) ).getComponent( 2 ) ).getComponent( 0 ) ).doClick();

		assertEquals( "FooActionBindingProcessor fired", ( (JTextField) metawidget.getComponent( 4 ) ).getText() );
		assertEquals( "", ( (JTextField) ( (SwingMetawidget) metawidget.getComponent( 2 ) ).getComponent( 4 ) ).getText() );
		assertEquals( "FooActionBindingProcessor fired", ( (JTextField) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 2 ) ).getComponent( 2 ) ).getComponent( 4 ) ).getText() );
	}

	public void testFacet() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.add( new Facet() );

		// Should be no layout or spacer panel

		assertEquals( metawidget.getComponentCount(), 0 );
		metawidget.setPath( "Foo" );
		assertEquals( metawidget.getComponentCount(), 0 );

		// Normal component should appear (but still not facet)

		metawidget.add( new JTextField() );
		assertTrue( metawidget.getComponent( 0 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 1 ) instanceof JPanel );
		assertEquals( metawidget.getComponentCount(), 2 );
	}

	public void testRequiredBoolean() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new FooRequiredBoolean() );

		assertTrue( metawidget.getComponent( 1 ) instanceof JCheckBox );
	}

	public void testLabelString() {

		SwingMetawidget metawidget = new SwingMetawidget();

		assertEquals( "", metawidget.getLabelString( null ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertEquals( "", metawidget.getLabelString( attributes ) );

		attributes.put( NAME, "foo" );
		assertEquals( "Foo", metawidget.getLabelString( attributes ) );

		attributes.put( LABEL, "foo" );
		assertEquals( "foo", metawidget.getLabelString( attributes ) );
	}

	public void testValidate() {

		final List<String> called = CollectionUtils.newArrayList();

		@SuppressWarnings( "serial" )
		SwingMetawidget metawidget = new SwingMetawidget() {

			@Override
			public void validate() {

				called.add( "validate" );
			}

			@Override
			protected void endBuild() {

				called.add( "endBuild" );
			}
		};

		metawidget.mPipeline.endBuild();

		// validate should be called after super.endBuild in SwingMetawidget

		assertEquals( "endBuild", called.get( 0 ) );
		assertEquals( "validate", called.get( 1 ) );
		assertEquals( 2, called.size() );
	}

	public void testGetWidgetProcessor() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setConfig( "org/metawidget/swing/metawidget-get-widget-processor.xml" );
		metawidget.setToInspect( new String() );
		assertTrue( null != metawidget.getWidgetProcessor( FooActionBindingProcessor.class ) );
	}

	public void testOverrideLayout() {

		// Without override

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setConfig( "org/metawidget/swing/metawidget-override-layout.xml" );
		metawidget.setToInspect( new String() );
		metawidget.getComponents();
		assertTrue( metawidget.getMetawidgetLayout() instanceof TabbedPaneLayoutDecorator );

		// With override

		metawidget = new SwingMetawidget();
		metawidget.setConfig( "org/metawidget/swing/metawidget-override-layout.xml" );
		metawidget.setToInspect( new String() );
		metawidget.getComponents();
		metawidget.setMetawidgetLayout( new BoxLayout() );
		assertTrue( metawidget.getMetawidgetLayout() instanceof BoxLayout );
	}

	@SuppressWarnings( "unchecked" )
	public void testBuildWidgets()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();

		Field needToBuildWidgets = SwingMetawidget.class.getDeclaredField( "mNeedToBuildWidgets" );
		needToBuildWidgets.setAccessible( true );
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setToInspect( "" );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getPreferredSize();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.addInspectionResultProcessor( new ComesAfterInspectionResultProcessor<SwingMetawidget>() );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getPreferredSize();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.removeInspectionResultProcessor( new ComesAfterInspectionResultProcessor<SwingMetawidget>() );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.setBounds( new Rectangle() );
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setInspectionResultProcessors( (InspectionResultProcessor[]) null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getBounds( new Rectangle() );
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

		metawidget.removeWidgetProcessor( new ReflectionBindingProcessor() );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getComponents();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setWidgetProcessors( (WidgetProcessor[]) null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getLayout();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setMetawidgetLayout( null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.addNotify();
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setBundle( null );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.getFacet( "foo" );
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setReadOnly( true );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
		metawidget.paintComponent( metawidget.getGraphics() );
		assertTrue( !needToBuildWidgets.getBoolean( metawidget ) );

		metawidget.setMaximumInspectionDepth( 0 );
		assertTrue( needToBuildWidgets.getBoolean( metawidget ) );
	}

	/**
	 * Tests <code>buildWidgets</code> uses <code>configureOnce</code>.
	 */

	public void testConfigureOnce() {

		List<String> configured = new ArrayList<String>();
		SwingMetawidget metawidget = new ConfiguredSwingMetawidget( configured );
		metawidget.setToInspect( new Foo() );
		metawidget.getComponentCount();

		metawidget.setToInspect( new Foo() );
		metawidget.getComponentCount();
		assertEquals( 2, configured.size() );
	}

	//
	// Inner class
	//

	protected static class ConfiguredSwingMetawidget
		extends SwingMetawidget {

		//
		// Private members
		//

		private List<String>	mConfigured;

		//
		// Constructor
		//

		public ConfiguredSwingMetawidget( List<String> configured ) {

			mConfigured = configured;
		}

		public ConfiguredSwingMetawidget() {

		}

		//
		// Protected methods
		//

		@Override
		protected String getDefaultConfiguration() {

			if ( mConfigured != null ) {
				mConfigured.add( "called" );
			}
			return super.getDefaultConfiguration();
		}
	}

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
		implements WidgetProcessor<JComponent, SwingMetawidget> {

		//
		// Public methods
		//

		@SuppressWarnings( "serial" )
		public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, final SwingMetawidget metawidget ) {

			if ( !ACTION.equals( elementName ) ) {
				return component;
			}

			JButton button = (JButton) component;

			button.setAction( new AbstractAction( button.getText() ) {

				public void actionPerformed( ActionEvent e ) {

					metawidget.setValue( "FooActionBindingProcessor fired", "name" );
				}
			} );

			return component;
		}
	}
}
