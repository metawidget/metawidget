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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.Binding;
import org.metawidget.swing.binding.beansbinding.BeansBinding;
import org.metawidget.swing.binding.beanutils.BeanUtilsBinding;
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
		assertTrue( "repaint\n".equals( builder.toString() ));

		// Should see another 'repaint' because of remove called .getComponent, which built the widgets

		metawidget.remove( component );
		assertTrue( "repaint\nrepaint\n".equals( builder.toString() ));

		// Should not see another repaint, because widgets already need repainting

		metawidget.add( component );
		assertTrue( "repaint\nrepaint\n".equals( builder.toString() ));

		// Should see another repaint, because .remove will build the widgets

		metawidget.remove( 0 );
		assertTrue( "repaint\nrepaint\nrepaint\n".equals( builder.toString() ));

		// Should not see another repaint, because widgets already need repainting

		metawidget.removeAll();
		assertTrue( "repaint\nrepaint\nrepaint\n".equals( builder.toString() ));
	}

	public void testNestedWithManualInspector()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setBindingClass( BeanUtilsBinding.class );
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
		// ...but because we know the type of the child property, we end up putting an empty Metawidget

		assertTrue( 1 == ((SwingMetawidget) metawidget.getComponent( "foo", "foo", "foo", "foo" )).getComponentCount() );
		assertTrue( 0 == ((JPanel) ((SwingMetawidget) metawidget.getComponent( "foo", "foo", "foo", "foo" )).getComponent( 0 )).getComponentCount() );
	}

	public void testRebind()
	{
		_testRebind( BeansBinding.class, org.jdesktop.beansbinding.Binding.SyncFailureType.SOURCE_UNREADABLE.toString() );
		_testRebind( BeanUtilsBinding.class, "Property 'name' has no getter" );
	}

	public void _testRebind( Class<? extends Binding> bindingClass, String errorMessage )
	{
		// Bind

		Foo foo1 = new Foo();
		foo1.setName( "Charlotte" );
		Foo nestedFoo1 = new Foo();
		foo1.setFoo( nestedFoo1 );
		nestedFoo1.setName( "Philippa" );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setBindingClass( bindingClass );
		metawidget.setToInspect( foo1 );

		JTextField textField = (JTextField) metawidget.getComponent( "name" );
		JTextField nestedTextField = (JTextField) metawidget.getComponent( "foo", "name" );
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
		assertTrue( SwingConstants.RIGHT == ( (JLabel) metawidget.getComponent( 0 )).getHorizontalAlignment() );
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

	public void testGroovy()
		throws Exception
	{
		// Test supporting the enum dropdown for a dynamic type...

		Method method = SwingMetawidget.class.getDeclaredMethod( "buildActiveWidget", String.class, Map.class );
		method.setAccessible( true );

		// ...both nullable (the default)...

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, "dynamic-type-that-cant-be-loaded" );
		attributes.put( LOOKUP, "bar,baz" );

		JComponent component = (JComponent) method.invoke( new SwingMetawidget(), "foo", attributes );
		assertTrue( component instanceof JComboBox );
		assertTrue( 3 == ((JComboBox) component).getItemCount() );

		// ...and not-nullable...

		attributes.put( REQUIRED, TRUE );

		component = (JComponent) method.invoke( new SwingMetawidget(), "foo", attributes );
		assertTrue( component instanceof JComboBox );
		assertTrue( 2 == ((JComboBox) component).getItemCount() );
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
	}
}
