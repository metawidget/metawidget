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

package org.metawidget.test.swing.actionbinding.reflection;

import javax.swing.JButton;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class ReflectionBindingTest
	extends TestCase
{
	//
	// Package-level members
	//

	int mActionFired;

	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ReflectionBindingTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testBinding()
		throws Exception
	{
		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new PropertyTypeInspector(), new MetawidgetAnnotationInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		JButton button = (JButton) ((SwingMetawidget) metawidget.getComponent( 1 )).getComponent( 0 );

		assertTrue( mActionFired == 0 );
		button.doClick();
		assertTrue( mActionFired == 1 );
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

		public void setNestedFoo( NestedFoo foo )
		{
			throw new UnsupportedOperationException();
		}
	}

	public class NestedFoo
	{
		//
		// Public methods
		//

		@UiAction
		public void doAction()
		{
			mActionFired++;
		}
	}
}
