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
import org.metawidget.swing.propertybinding.beansbinding.BeansBinding;

/**
 * @author Richard Kennard
 */

public class CarApplication
	extends SingleFrameApplication
{
	//
	// Public statics
	//

	public static void main( String[] args )
	{
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
	public void save( ActionEvent event )
	{
		mMetawidget.save();
		JOptionPane.showMessageDialog( getMainFrame(), "Saved " + mCar );
	}

	//
	// Protected methods
	//

	@Override
	protected void startup()
	{
		startupWithoutShow();
		show( getMainFrame() );
	}

	/**
	 * Prepare the Application.
	 * <p>
	 * Separated out from <code>startup</code> for ease of unit testing.
	 */

	protected void startupWithoutShow()
	{
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
		mMetawidget.setInspectorConfig( "org/metawidget/example/swing/appframework/inspector-config.xml" );
		mMetawidget.setBundle( bundle );
		mMetawidget.setToInspect( mCar );
		mMetawidget.setPropertyBindingClass( BeansBinding.class );
		mMetawidget.setBorder( BorderFactory.createEmptyBorder( 5, 5, 0, 5 ) );

		// Set our default size. After first run, this will be overriden by the value
		// stored in ${userHome}\Application Data\${vendorId}\${applicationId}\session.xml

		// TODO: bit of a large space at the bottom here? GridBagLayout.INSETS_BUTTONS

		mMetawidget.setPreferredSize( new Dimension( 400, 150 ) );
		frame.add( mMetawidget, BorderLayout.CENTER );

		// Actions

		SwingMetawidget buttonsMetawidget = new SwingMetawidget();
		buttonsMetawidget.setInspectorConfig( "org/metawidget/example/swing/appframework/action-inspector-config.xml" );
		buttonsMetawidget.setBundle( bundle );
		buttonsMetawidget.setToInspect( this );
		buttonsMetawidget.setLayoutClass( FlowLayout.class );

		Facet facet = new Facet();
		facet.setName( "buttons" );
		facet.add( buttonsMetawidget );
		mMetawidget.add( facet );

		// Trigger rebuild UI after click 'addOwner'

		mCar.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				// Save in case changed make/type

				mMetawidget.save();

				// Re-inspect to create Owner section

				mMetawidget.setToInspect( mCar );
			}
		} );
	}
}
