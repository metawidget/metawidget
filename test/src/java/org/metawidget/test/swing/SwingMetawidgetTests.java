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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.test.swing.actionbinding.reflection.ReflectionBindingTest;
import org.metawidget.test.swing.layout.GridBagLayoutTest;
import org.metawidget.test.swing.layout.GroupLayoutTest;
import org.metawidget.test.swing.layout.MigLayoutTest;
import org.metawidget.test.swing.propertybinding.beansbinding.BeansBindingTest;
import org.metawidget.test.swing.propertybinding.beanutilsbinding.BeanUtilsBindingTest;
import org.metawidget.test.swing.validator.inputverifier.InputVerifierValidatorTest;
import org.metawidget.test.swing.validator.jgoodies.JGoodiesValidatorTest;
import org.metawidget.test.swing.widgetbuilder.swingx.SwingXWidgetBuilderTest;

/**
 * @author Richard Kennard
 */

public class SwingMetawidgetTests
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public SwingMetawidgetTests( String name )
	{
		super( name );
	}

	//
	// Public statics
	//

	public static Test suite()
	{
		TestSuite suite = new TestSuite( "SwingMetawidget Tests" );
		suite.addTestSuite( BeansBindingTest.class );
		suite.addTestSuite( BeanUtilsBindingTest.class );
		suite.addTestSuite( JGoodiesValidatorTest.class );
		suite.addTestSuite( InputVerifierValidatorTest.class );
		suite.addTestSuite( ReflectionBindingTest.class );
		// Not: suite.addTestSuite( SwingAllWidgetsTest.class ) - run separately as JDK 1.4
		suite.addTestSuite( SwingAllWidgetsBeansBindingTest.class );
		suite.addTestSuite( SwingMetawidgetTest.class );
		suite.addTestSuite( SwingXWidgetBuilderTest.class );
		suite.addTestSuite( GridBagLayoutTest.class );
		suite.addTestSuite( GroupLayoutTest.class );
		suite.addTestSuite( GroupLayoutTest.class );
		suite.addTestSuite( MigLayoutTest.class );

		return suite;
	}
}