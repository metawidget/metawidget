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

package org.metawidget.example.swing.appframework;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;

/**
 * @author Richard Kennard
 */

public class SwingAppFrameworkTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testCarApplication()
		throws Exception
	{
		// Create app

		CarApplication carApplication = new CarApplication();

		// Start app

		carApplication.startupWithoutShow();

		// Fetch Metawidget

		JFrame frame = carApplication.getMainFrame();
		SwingMetawidget metawidget = (SwingMetawidget) frame.getContentPane().getComponent( 1 );
		Car car = metawidget.getToInspect();

		// Test Car Application

		assertTrue( "Ford".equals( ( (JTextField) metawidget.getComponent( 1 ) ).getText() ) );
		assertTrue( "Ford".equals( car.toString() ) );
		assertTrue( null == car.getOwner() );
		assertTrue( metawidget.getComponentCount() == 6 );
		assertTrue( ((Container) metawidget.getFacet( "buttons" ).getComponent( 0 )).getLayout() instanceof FlowLayout );
		assertTrue( "Save".equals( ((JButton) ((SwingMetawidget) metawidget.getFacet( "buttons" ).getComponent( 0 )).getComponent( 0 )).getText() ));

		( (JComboBox) metawidget.getComponent( 3 ) ).setSelectedIndex( 1 );
		JButton button = (JButton) metawidget.getComponent( "addOwner" );
		assertTrue( "Add an Owner".equals( button.getText() ) );
		button.getAction().actionPerformed( null );

		assertTrue( null != car.getOwner() );
		assertTrue( metawidget.getComponentCount() == 7 );
		assertTrue( "Ford Sport, owned by (no name specified)".equals( car.toString() ) );
		SwingMetawidget metawidgetOwner = (SwingMetawidget) metawidget.getComponent( 5 );
		( (JTextField) metawidgetOwner.getComponent( 1 ) ).setText( "Richard" );
		( (JTextField) metawidgetOwner.getComponent( 3 ) ).setText( "Kennard" );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );

		assertTrue( "Ford Sport, owned by Richard Kennard".equals( car.toString() ) );
	}
}
