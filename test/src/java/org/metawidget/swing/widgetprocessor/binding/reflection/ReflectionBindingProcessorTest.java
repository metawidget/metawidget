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

package org.metawidget.swing.widgetprocessor.binding.reflection;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.swing.JButton;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.jdesktop.application.Action;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.swing.SwingAppFrameworkInspector;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class ReflectionBindingProcessorTest
	extends TestCase
{
	//
	// Package-level members
	//

	int mActionFired;

	//
	// Public methods
	//

	public void testBinding()
		throws Exception
	{
		// Configure

		BaseObjectInspectorConfig actionConfig = new BaseObjectInspectorConfig().setActionStyle( SwingAppFrameworkActionStyle.class );
		CompositeInspectorConfig compositeConfig = new CompositeInspectorConfig();
		compositeConfig.setInspectors( new PropertyTypeInspector( actionConfig ), new SwingAppFrameworkInspector( actionConfig ) );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector( compositeConfig ) );
		metawidget.setToInspect( new Foo() );

		JButton button = (JButton) ((SwingMetawidget) metawidget.getComponent( 1 )).getComponent( 0 );

		assertTrue( mActionFired == 0 );
		button.doClick();
		assertTrue( mActionFired == 1 );
	}

	public void testNullBinding()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		// Null object

		JButton button = new JButton();
		binding.onAdd( button, ACTION, null, null );

		assertTrue( button.getAction() == null );

		// Null nested object

		Foo foo = new Foo();
		foo.setNestedFoo( null );

		metawidget.setToInspect( foo );
		metawidget.setPath( "foo/nestedFoo/doAction" );
		binding.onAdd( button, ACTION, null, metawidget );

		assertTrue( button.getAction() == null );
	}

	public void testBadBinding()
	{
		ReflectionBindingProcessor binding = new ReflectionBindingProcessor();

		try
		{
			binding.onAdd( new JTextField(), ACTION, null, null );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "ReflectionBinding only supports binding actions to AbstractButtons".equals( e.getMessage() ));
		}
	}

	//
	// Inner class
	//

	public class Foo
	{
		//
		// Private members
		//

		private NestedFoo mNestedFoo = new NestedFoo();

		//
		// Public methods
		//

		public NestedFoo getNestedFoo()
		{
			return mNestedFoo;
		}

		public void setNestedFoo( NestedFoo nestedFoo )
		{
			mNestedFoo = nestedFoo;
		}
	}

	public class NestedFoo
	{
		//
		// Public methods
		//

		@Action
		public void doAction( java.awt.event.ActionEvent event )
		{
			mActionFired++;
		}
	}
}
