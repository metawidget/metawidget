// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.example.swing.appframework;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingAppFrameworkTest
	extends TestCase {

	//
	// Public methods
	//

	public void testCarApplication()
		throws Exception {

		// Create app

		CarApplication carApplication = new CarApplication();

		// Start app

		carApplication.startupWithoutShow();

		// Fetch Metawidget

		JFrame frame = carApplication.getMainFrame();
		SwingMetawidget metawidget = (SwingMetawidget) frame.getContentPane().getComponent( 1 );
		Car car = metawidget.getToInspect();

		// Test Car Application

		assertEquals( "Ford", ( (JTextField) metawidget.getComponent( 1 ) ).getText() );
		assertEquals( "Ford", car.toString() );
		assertEquals( null, car.getOwner() );
		assertEquals( metawidget.getComponentCount(), 7 );
		assertTrue( metawidget.getComponent( 5 ) instanceof JPanel );
		assertTrue( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).weighty == 1.0f );
		assertEquals( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getFacet( "buttons" ) ).gridx, -1 );
		assertEquals( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getFacet( "buttons" ) ).gridy, 4 );
		assertEquals( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getFacet( "buttons" ) ).fill, GridBagConstraints.BOTH );
		assertEquals( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getFacet( "buttons" ) ).anchor, GridBagConstraints.WEST );
		assertEquals( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getFacet( "buttons" ) ).gridwidth, GridBagConstraints.REMAINDER );
		assertTrue( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getFacet( "buttons" ) ).weighty == 0 );
		assertTrue( ( (Container) metawidget.getFacet( "buttons" ).getComponent( 0 ) ).getLayout() instanceof FlowLayout );
		assertEquals( "Save", ( (JButton) ( (SwingMetawidget) metawidget.getFacet( "buttons" ).getComponent( 0 ) ).getComponent( 0 ) ).getText() );

		( (JComboBox) metawidget.getComponent( 3 ) ).setSelectedIndex( 1 );
		JButton button = (JButton) metawidget.getComponent( "addOwner" );
		assertEquals( "Add an Owner", button.getText() );
		button.getAction().actionPerformed( null );

		assertTrue( null != car.getOwner() );
		assertEquals( metawidget.getComponentCount(), 8 );
		assertEquals( "Ford Sport, owned by (no name specified)", car.toString() );
		SwingMetawidget metawidgetOwner = (SwingMetawidget) metawidget.getComponent( 5 );
		( (JTextField) metawidgetOwner.getComponent( 1 ) ).setText( "Richard" );
		( (JTextField) metawidgetOwner.getComponent( 3 ) ).setText( "Kennard" );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );

		assertEquals( "Ford Sport, owned by Richard Kennard", car.toString() );
	}
}
