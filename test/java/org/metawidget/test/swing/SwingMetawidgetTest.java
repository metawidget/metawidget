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

import java.awt.Component;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.Binding;
import org.metawidget.swing.binding.beansbinding.BeansBinding;
import org.metawidget.swing.binding.beanutils.BeanUtilsBinding;
import org.metawidget.test.inspector.propertytype.PropertyTypeInspectorTest.RecursiveFoo;

/**
 * @author Richard Kennard
 */

public class SwingMetawidgetTest
	extends TestCase
{
	//
	//
	// Public methods
	//
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

	public void testInvalidation()
		throws Exception
	{
		JTextField component = new JTextField();
		SwingMetawidget metawidget = new SwingMetawidget();

		Field validField = Component.class.getDeclaredField( "valid" );
		validField.setAccessible( true );

		validField.setBoolean( metawidget, true );
		metawidget.paint( new JFrame().getGraphics() );
		assertTrue( validField.getBoolean( metawidget ) );
		metawidget.add( component );
		assertFalse( validField.getBoolean( metawidget ) );

		validField.setBoolean( metawidget, true );
		metawidget.remove( component );
		assertFalse( validField.getBoolean( metawidget ) );

		validField.setBoolean( metawidget, true );
		metawidget.add( component, 0 );
		assertFalse( validField.getBoolean( metawidget ) );

		validField.setBoolean( metawidget, true );
		metawidget.remove( 0 );
		assertFalse( validField.getBoolean( metawidget ) );

		validField.setBoolean( metawidget, true );
		metawidget.removeAll();
		assertFalse( validField.getBoolean( metawidget ) );
	}

	public void testNestedWithManualInspector()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setBindingClass( BeanUtilsBinding.class );
		metawidget.setToInspect( new Foo() );

		assertTrue( "Baz".equals( metawidget.getValue( "bar", "baz" ) ) );
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
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setToInspect( new Foo() );
		metawidget.setMaximumInspectionDepth( 0 );
		assertTrue( metawidget.getComponent( "bar" ) == null );

		metawidget.setMaximumInspectionDepth( 1 );
		assertTrue( metawidget.getComponent( "bar", "baz" ) instanceof JLabel );
		assertTrue( 1 == metawidget.getMaximumInspectionDepth() );

		Foo foo1 = new Foo();
		Foo foo2 = new Foo();
		Foo foo3 = new Foo();
		foo1.setFoo( foo2 );
		foo2.setFoo( foo3 );

		metawidget.setToInspect( foo1 );
		assertTrue( metawidget.getComponent( "foo", "foo" ) instanceof SwingMetawidget );
		assertTrue( metawidget.getComponent( "foo", "foo", "foo" ) == null );
	}

	public void testRebind()
	{
		_testRebind( BeansBinding.class );
		_testRebind( BeanUtilsBinding.class );
	}

	public void _testRebind( Class<? extends Binding> bindingClass )
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
		assertTrue( textField != metawidget.getComponent( "name" ) );
		assertTrue( nestedTextField != metawidget.getComponent( "foo", "name" ) );
		assertTrue( "Julianne".equals( ( (JTextField) metawidget.getComponent( "name" ) ).getText() ) );
		assertTrue( "Richard".equals( ( (JTextField) metawidget.getComponent( "foo", "name" ) ).getText() ) );
	}

	//
	//
	// Inner class
	//
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

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public SwingMetawidgetTest( String name )
	{
		super( name );
	}
}
