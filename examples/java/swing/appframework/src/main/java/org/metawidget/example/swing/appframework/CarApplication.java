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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.FlowLayout;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;

/**
 * @author Richard Kennard
 */

public class CarApplication
	extends SingleFrameApplication {

	//
	// Public statics
	//

	public static void main( String[] args ) {

		Application.launch( CarApplication.class, args );
	}

	//
	// Private members
	//

	SwingMetawidget	mMetawidget;

	Car				mCar;

	//
	// Public methods
	//

	@Action
	public void save( ActionEvent event ) {

		mMetawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( mMetawidget );
		JOptionPane.showMessageDialog( getMainFrame(), mCar + " " + event.getActionCommand() + "d" );
	}

	//
	// Protected methods
	//

	@Override
	protected void startup() {

		startupWithoutShow();
		show( getMainFrame() );
	}

	/**
	 * Prepare the Application.
	 * <p>
	 * Separated out from <code>startup</code> for ease of unit testing.
	 */

	protected void startupWithoutShow() {

		// Model

		mCar = new Car();
		mCar.setMake( "Ford" );

		// Description

		JFrame frame = getMainFrame();
		frame.setLayout( new BorderLayout() );

		ResourceBundle bundle = ResourceBundle.getBundle( "org.metawidget.example.swing.appframework.resources.CarApplication" );
		JLabel label = new JLabel();
		label.setBorder( BorderFactory.createEmptyBorder( 5, 5, 10, 5 ) );
		label.setText( "<html>" + bundle.getString( "Application.description" ) + ":</html>" );
		label.setPreferredSize( new Dimension( 400, 50 ) );
		frame.add( label, BorderLayout.NORTH );

		// Metawidget

		mMetawidget = new SwingMetawidget();
		mMetawidget.setConfig( "org/metawidget/example/swing/appframework/metawidget.xml" );
		mMetawidget.setBundle( bundle );
		mMetawidget.setToInspect( mCar );
		mMetawidget.setBorder( BorderFactory.createEmptyBorder( 5, 5, 0, 5 ) );

		// Set our default size. After first run, this will be overridden by the value
		// stored in ${userHome}\Application Data\${vendorId}\${applicationId}\session.xml

		mMetawidget.setPreferredSize( new Dimension( 550, 150 ) );
		frame.add( mMetawidget, BorderLayout.CENTER );

		// Actions

		SwingMetawidget buttonsMetawidget = new SwingMetawidget();
		buttonsMetawidget.setConfig( "org/metawidget/example/swing/appframework/metawidget-action.xml" );
		buttonsMetawidget.setBundle( bundle );
		buttonsMetawidget.setToInspect( this );
		buttonsMetawidget.setMetawidgetLayout( new FlowLayout() );

		Facet facet = new Facet();
		facet.setName( "buttons" );
		facet.add( buttonsMetawidget );
		mMetawidget.add( facet );

		// Trigger rebuild UI after click 'addOwner'

		mCar.addActionListener( new ActionListener() {

			public void actionPerformed( ActionEvent event ) {

				// Save in case changed make/type

				mMetawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( mMetawidget );

				// Re-inspect to create Owner section

				mMetawidget.setToInspect( mCar );
			}
		} );
	}
}
