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

package org.metawidget.test.swing;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.actionbinding.BaseActionBinding;
import org.metawidget.swing.propertybinding.PropertyBinding;
import org.metawidget.swing.propertybinding.beansbinding.BeansBinding;
import org.metawidget.swing.propertybinding.beanutils.BeanUtilsBinding;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.test.inspector.propertytype.PropertyTypeInspectorTest.RecursiveFoo;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class SwingMetawidgetTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public SwingMetawidgetTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testBeanInfo()
		throws Exception
	{
		BeanInfo info = Introspector.getBeanInfo( SwingMetawidget.class );

		assertTrue( "Metawidget".equals( info.getBeanDescriptor().getDisplayName() ) );
		assertTrue( info.getIcon( BeanInfo.ICON_MONO_16x16 ) != null );
		assertTrue( info.getIcon( BeanInfo.ICON_COLOR_16x16 ) != null );
		assertTrue( info.getIcon( BeanInfo.ICON_MONO_32x32 ) != null );
		assertTrue( info.getIcon( BeanInfo.ICON_COLOR_32x32 ) != null );
		assertTrue( info.getIcon( 5 ) == null );
	}

	@SuppressWarnings( "serial" )
	public void testRepainting()
	{
		final StringBuilder builder = new StringBuilder();

		SwingMetawidget metawidget = new SwingMetawidget()
		{
			@Override
			public void repaint()
			{
				builder.append( "repaint\n" );
				super.repaint();
			}
		};

		// Should see 'repaint' because of the add

		JTextField component = new JTextField();
		metawidget.add( component );
		assertTrue( "repaint\n".equals( builder.toString() ) );

		// Should see another 'repaint' because of remove called .getComponent, which built the
		// widgets

		metawidget.remove( component );
		assertTrue( "repaint\nrepaint\n".equals( builder.toString() ) );

		// Should not see another repaint, because widgets already need repainting

		metawidget.add( component );
		assertTrue( "repaint\nrepaint\n".equals( builder.toString() ) );

		// Should see another repaint, because .remove will build the widgets

		metawidget.remove( 0 );
		assertTrue( "repaint\nrepaint\nrepaint\n".equals( builder.toString() ) );

		// Should not see another repaint, because widgets already need repainting

		metawidget.removeAll();
		assertTrue( "repaint\nrepaint\nrepaint\n".equals( builder.toString() ) );
	}

	public void testNestedWithManualInspector()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setPropertyBindingClass( BeanUtilsBinding.class );
		Foo foo1 = new Foo();
		Foo foo2 = new Foo();
		foo1.setFoo( foo2 );
		foo2.setName( "Bar" );
		metawidget.setToInspect( foo1 );

		assertTrue( "Bar".equals( metawidget.getValue( "foo", "name" ) ) );
	}

	public void testRecursion()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		RecursiveFoo foo = new RecursiveFoo();
		foo.foo = foo;
		metawidget.setToInspect( foo );

		assertTrue( null == ( (SwingMetawidget) metawidget.getComponent( "foo" ) ).getComponent( "foo" ) );
	}

	public void testMaximumInspectionDepth()
	{
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
		assertTrue( metawidget.getComponent( "foo" ) == null );

		metawidget.setMaximumInspectionDepth( 1 );
		assertTrue( 1 == metawidget.getMaximumInspectionDepth() );
		assertTrue( metawidget.getComponent( "foo" ) instanceof SwingMetawidget );
		assertTrue( metawidget.getComponent( "foo", "name" ) instanceof JTextField );
		assertTrue( "name".equals( metawidget.getComponent( "foo", "name" ).getName() ) );
		assertTrue( metawidget.getComponent( "foo", "foo" ) == null );

		metawidget.setMaximumInspectionDepth( 2 );
		assertTrue( metawidget.getComponent( "foo", "foo" ) instanceof SwingMetawidget );
		assertTrue( metawidget.getComponent( "foo", "foo", "foo" ) == null );

		metawidget.setMaximumInspectionDepth( 3 );
		assertTrue( metawidget.getComponent( "foo", "foo", "foo" ) instanceof SwingMetawidget );
		assertTrue( metawidget.getComponent( "foo", "foo", "foo", "foo" ) == null );

		metawidget.setMaximumInspectionDepth( 4 );

		// Goes: root (foo1) -> foo (foo2) -> foo (foo3) -> foo (new Foo) -> foo == null
		//
		// ...but because we know the type of the child property, we end up putting an empty
		// Metawidget

		assertTrue( metawidget.getComponent( "foo", "foo", "foo", "foo" ) instanceof SwingMetawidget );
		assertTrue( 0 == ((SwingMetawidget) metawidget.getComponent( "foo", "foo", "foo", "foo" )).getComponentCount() );
		assertTrue( metawidget.getComponent( "foo", "foo", "foo", "foo", "foo" ) == null );
	}

	public void testRebind()
	{
		_testRebind( BeansBinding.class, org.jdesktop.beansbinding.Binding.SyncFailureType.SOURCE_UNREADABLE.toString() );
		_testRebind( BeanUtilsBinding.class, "Property 'name' has no getter" );
	}

	public void testGroovy()
		throws Exception
	{
		// Test supporting the enum dropdown for a dynamic type...

		// ...both nullable (the default)...

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, "dynamic-type-that-cant-be-loaded" );
		attributes.put( LOOKUP, "bar,baz" );

		SwingMetawidget metawidget = new SwingMetawidget();
		SwingWidgetBuilder widgetBuilder = new SwingWidgetBuilder();

		JComponent component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof JComboBox );
		assertTrue( 3 == ( (JComboBox) component ).getItemCount() );

		// ...and not-nullable...

		attributes.put( REQUIRED, TRUE );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof JComboBox );
		assertTrue( 2 == ( (JComboBox) component ).getItemCount() );

		// Also test UiDontExpand on a dynamic type

		attributes.remove( REQUIRED );
		attributes.remove( LOOKUP );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( null == component );

		attributes.put( DONT_EXPAND, TRUE );

		component = widgetBuilder.buildWidget( "foo", attributes, metawidget );
		assertTrue( component instanceof JTextField );
	}

	public void testNestedActionBinding()
	{
		Foo foo1 = new Foo();
		Foo foo2 = new Foo();
		Foo foo3 = new Foo();
		foo1.setFoo( foo2 );
		foo2.setFoo( foo3 );

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setActionBindingClass( FooActionBinding.class );
		metawidget.setToInspect( foo1 );

		( (JButton) metawidget.getComponent( 0 ) ).doClick();
		( (JButton) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 2 ) ).getComponent( 2 ) ).getComponent( 0 ) ).doClick();

		assertTrue( "FooActionBinding fired".equals( ( (JTextField) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( "".equals( ( (JTextField) ( (SwingMetawidget) metawidget.getComponent( 2 ) ).getComponent( 4 ) ).getText() ) );
		assertTrue( "FooActionBinding fired".equals( ( (JTextField) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 2 ) ).getComponent( 2 ) ).getComponent( 4 ) ).getText() ) );
	}

	public void testFacet()
	{
		SwingMetawidget metawidget = new SwingMetawidget();

		// Facets shouldn't get added directly...

		metawidget.add( new Facet() );
		assertTrue( metawidget.getComponentCount() == 0 );

		// ...but adding a component will cause a layout

		metawidget.add( new JTextField() );
		assertTrue( metawidget.getComponentCount() == 2 );
	}

	public void testRequiredBoolean()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new FooRequiredBoolean() );

		assertTrue( metawidget.getComponent( 1 ) instanceof JCheckBox );
	}

	//
	// Private methods
	//

	private void _testRebind( Class<? extends PropertyBinding> bindingClass, String errorMessage )
	{
		// Bind

		Foo foo1 = new Foo();
		foo1.setName( "Charlotte" );
		Foo nestedFoo1 = new Foo();
		foo1.setFoo( nestedFoo1 );
		nestedFoo1.setName( "Philippa" );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setPropertyBindingClass( bindingClass );
		metawidget.setToInspect( foo1 );

		JTextField textField = metawidget.getComponent( "name" );
		JTextField nestedTextField = metawidget.getComponent( "foo", "name" );
		assertTrue( "Charlotte".equals( textField.getText() ) );
		assertTrue( "Philippa".equals( nestedTextField.getText() ) );

		// Rebind

		Foo foo2 = new Foo();
		foo2.setName( "Julianne" );
		Foo nestedFoo2 = new Foo();
		foo2.setFoo( nestedFoo2 );
		nestedFoo2.setName( "Richard" );

		metawidget.rebind( foo2 );
		assertTrue( "Julianne".equals( textField.getText() ) );
		assertTrue( "Richard".equals( nestedTextField.getText() ) );

		// Check same component

		assertTrue( textField == metawidget.getComponent( "name" ) );
		assertTrue( nestedTextField == metawidget.getComponent( "foo", "name" ) );

		// Check different component

		metawidget.setToInspect( foo2 );
		metawidget.setParameter( "labelAlignment", SwingConstants.RIGHT );
		assertTrue( textField != metawidget.getComponent( "name" ) );
		assertTrue( nestedTextField != metawidget.getComponent( "foo", "name" ) );
		assertTrue( SwingConstants.RIGHT == ( (JLabel) metawidget.getComponent( 0 ) ).getHorizontalAlignment() );
		assertTrue( "Julianne".equals( ( (JTextField) metawidget.getComponent( "name" ) ).getText() ) );
		assertTrue( "Richard".equals( ( (JTextField) metawidget.getComponent( "foo", "name" ) ).getText() ) );

		// Check error

		try
		{
			metawidget.rebind( new Object() );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertTrue( errorMessage.equals( e.getMessage() ) );
		}
	}

	//
	// Inner class
	//

	public static class Foo
	{
		//
		//
		// Private members
		//
		//

		private String	mName;

		private Foo		mFoo;

		//
		//
		// Public methods
		//
		//

		public String getName()
		{
			return mName;
		}

		public void setName( String name )
		{
			mName = name;
		}

		public Foo getFoo()
		{
			return mFoo;
		}

		public void setFoo( Foo foo )
		{
			mFoo = foo;
		}

		@UiAction
		public void doAction()
		{
			// Do nothing
		}
	}

	public static class FooActionBinding
		extends BaseActionBinding
	{
		//
		// Constructor
		//

		public FooActionBinding( SwingMetawidget metawidget )
		{
			super( metawidget );
		}

		//
		// Public methods
		//

		@SuppressWarnings( "serial" )
		public void bindAction( Component component, Map<String, String> attributes, String path )
		{
			JButton button = (JButton) component;
			final SwingMetawidget metawidget = getMetawidget();

			button.setAction( new AbstractAction( button.getText() )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					metawidget.setValue( "FooActionBinding fired", "name" );
				}
			} );
		}
	}

	public static class FooRequiredBoolean
	{
		//
		// Public methods
		//

		@UiRequired
		public Boolean getBoolean()
		{
			return null;
		}

		public void setBoolean( Boolean aBoolean )
		{
			// Do nothing
		}
	}
}
