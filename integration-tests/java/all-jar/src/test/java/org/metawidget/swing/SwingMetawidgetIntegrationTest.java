// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.swing;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.layout.GridBagLayoutConfig;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.swing.widgetprocessor.binding.beanutils.BeanUtilsBindingProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingMetawidgetIntegrationTest
	extends TestCase {

	//
	// Public methods
	//

	public void testRebind()
		throws Exception {

		_testRebind( new BeansBindingProcessor(), org.jdesktop.beansbinding.Binding.SyncFailureType.SOURCE_UNREADABLE.toString() );
		_testRebind( new BeanUtilsBindingProcessor(), "Property 'name' has no getter" );
	}

	//
	// Private methods
	//

	@SuppressWarnings( "cast" )
	private void _testRebind( WidgetProcessor<JComponent, SwingMetawidget> processor, String errorMessage )
		throws Exception {

		// Bind

		Foo foo1 = new Foo();
		foo1.setName( "Charlotte" );
		Foo nestedFoo1 = new Foo();
		foo1.setFoo( nestedFoo1 );
		nestedFoo1.setName( "Philippa" );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.addWidgetProcessor( processor );
		metawidget.setToInspect( foo1 );

		JTextField textField = metawidget.getComponent( "name" );
		JTextField nestedTextField = metawidget.getComponent( "foo", "name" );
		assertEquals( "Charlotte", textField.getText() );
		assertEquals( "Philippa", nestedTextField.getText() );

		// Rebind

		Foo foo2 = new Foo();
		foo2.setName( "Julianne" );
		Foo nestedFoo2 = new Foo();
		foo2.setFoo( nestedFoo2 );
		nestedFoo2.setName( "Richard" );

		processor.getClass().getMethod( "rebind", Object.class, SwingMetawidget.class ).invoke( processor, foo2, metawidget );
		assertEquals( "Julianne", textField.getText() );
		assertEquals( "Richard", nestedTextField.getText() );

		// Check same component

		assertEquals( textField, (Component) metawidget.getComponent( "name" ) );
		assertEquals( nestedTextField, (Component) metawidget.getComponent( "foo", "name" ) );

		// Check saves back to the correct place

		processor.getClass().getMethod( "save", SwingMetawidget.class ).invoke( processor, metawidget );
		assertEquals( "Charlotte", foo1.getName() );
		assertEquals( foo2, (Object) metawidget.getToInspect() );
		assertEquals( "Julianne", foo2.getName() );

		// Check different component

		metawidget.setToInspect( foo2 );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setLabelAlignment( SwingConstants.RIGHT ) ) );
		assertTrue( textField != (Component) metawidget.getComponent( "name" ) );
		assertTrue( nestedTextField != (Component) metawidget.getComponent( "foo", "name" ) );
		assertEquals( SwingConstants.RIGHT, ( (JLabel) metawidget.getComponent( 0 ) ).getHorizontalAlignment() );
		assertEquals( "Julianne", ( (JTextField) metawidget.getComponent( "name" ) ).getText() );
		assertEquals( "Richard", ( (JTextField) metawidget.getComponent( "foo", "name" ) ).getText() );

		// Check error

		try {
			processor.getClass().getMethod( "rebind", Object.class, SwingMetawidget.class ).invoke( processor, new Object(), metawidget );
			fail();
		} catch ( Exception e ) {
			assertEquals( errorMessage, e.getCause().getMessage() );
		}
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
	}
}
