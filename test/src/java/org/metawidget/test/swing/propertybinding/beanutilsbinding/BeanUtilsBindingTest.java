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

package org.metawidget.test.swing.propertybinding.beanutilsbinding;

import javax.swing.JLabel;
import javax.swing.JSpinner;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.scala.ScalaPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.propertybinding.beanutils.BeanUtilsBinding;

/**
 * @author Richard Kennard
 */

public class BeanUtilsBindingTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public BeanUtilsBindingTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testScalaBinding()
		throws Exception
	{
		// Model

		ScalaFoo scalaFoo = new ScalaFoo();
		scalaFoo.bar_$eq( 42l );
		ScalaFoo scalaFoo2 = new ScalaFoo();
		scalaFoo.nestedFoo = scalaFoo2;
		ScalaFoo scalaFoo3 = new ScalaFoo();
		scalaFoo2.nestedFoo = scalaFoo3;
		scalaFoo3.bar_$eq( 52l );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setPropertyBindingClass( BeanUtilsBinding.class );
		metawidget.setParameter( "propertyStyle", BeanUtilsBinding.PROPERTYSTYLE_SCALA );
		BaseObjectInspectorConfig config = new BaseObjectInspectorConfig();
		config.setPropertyStyle( ScalaPropertyStyle.class );
		metawidget.setInspector( new PropertyTypeInspector( config ) );
		metawidget.setToInspect( scalaFoo );

		// Loading
		//
		// Note: if this test fails with an 'illegal value' from SpinnerNumberModel.setValue, check
		// there isn't a pre-1.8 version of BeanUtils somewhere on the CLASSPATH (eg.
		// gwt-dev-windows.jar)

		JSpinner spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( 42l == (Long) spinner.getValue() );
		JLabel label = (JLabel) metawidget.getComponent( 5 );
		assertTrue( "Not settable".equals( label.getText() ));

		JSpinner nestedSpinner = (JSpinner) ( (SwingMetawidget) metawidget.getComponent( 3 )).getComponent( 1 );
		assertTrue( 0l == (Long) nestedSpinner.getValue() );

		JSpinner nestedNestedSpinner = (JSpinner) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 3 ) ).getComponent( 3 ) ).getComponent( 1 );
		assertTrue( 52l == (Long) nestedNestedSpinner.getValue() );

		// Saving

		spinner.setValue( 43l );
		nestedNestedSpinner.setValue( 53l );
		metawidget.save();

		assertTrue( 43l == scalaFoo.bar() );
		assertTrue( 0l == scalaFoo2.bar() );
		assertTrue( 53l == scalaFoo3.bar() );
	}

	//
	// Inner class
	//

	protected static class ScalaFoo
	{
		//
		// Private members
		//

		private long		bar;

		private String		notSettable = "Not settable";

		protected ScalaFoo	nestedFoo;

		//
		// Public methods
		//

		public long bar()
		{
			return bar;
		}

		public void bar_$eq( long theBar )
		{
			bar = theBar;
		}

		public String notSettable()
		{
			return notSettable;
		}

		public ScalaFoo nestedFoo()
		{
			return nestedFoo;
		}
	}
}
