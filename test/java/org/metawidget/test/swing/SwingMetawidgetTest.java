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
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.beanutils.BeanUtilsBinding;

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

		assertTrue( "Metawidget".equals( info.getBeanDescriptor().getDisplayName() ));
		assertTrue( info.getIcon( BeanInfo.ICON_MONO_32x32 ) != null );
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
		assertTrue( validField.getBoolean( metawidget ));
		metawidget.add( component );
		assertFalse( validField.getBoolean( metawidget ));

		validField.setBoolean( metawidget, true );
		metawidget.remove( component );
		assertFalse( validField.getBoolean( metawidget ));

		validField.setBoolean( metawidget, true );
		metawidget.add( component, 0 );
		assertFalse( validField.getBoolean( metawidget ));

		validField.setBoolean( metawidget, true );
		metawidget.remove( 0 );
		assertFalse( validField.getBoolean( metawidget ));

		validField.setBoolean( metawidget, true );
		metawidget.removeAll();
		assertFalse( validField.getBoolean( metawidget ));
	}

	public void testNestedWithManualInspector()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setBindingClass( BeanUtilsBinding.class );
		metawidget.setToInspect( new Foo() );

		assertTrue( "Baz".equals( metawidget.getValue( "bar", "baz" )));
	}

	public void testRecursion()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		RecursiveFoo foo = new RecursiveFoo();
		foo.foo = foo;
		metawidget.setToInspect( foo );

		assertTrue( null == ((SwingMetawidget) metawidget.getComponent( "foo" )).getComponent( "foo" ));
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
		// Public methods
		//
		//

		public Bar getBar()
		{
			return new Bar();
		}
	}

	public static class Bar
	{
		//
		//
		// Public methods
		//
		//

		public String getBaz()
		{
			return "Baz";
		}
	}

	public static class RecursiveFoo
	{
		public RecursiveFoo foo;
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
