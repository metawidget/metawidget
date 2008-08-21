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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.metawidget.inspector.jexl.UiJexlAttribute;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.beansbinding.BeansBinding;

/**
 * @author Richard Kennard
 */

public class CarApplication
	extends SingleFrameApplication
{
	//
	//
	// Public statics
	//
	//

	public static void main( String[] args )
	{
		Application.launch( CarApplication.class, args );
	}

	//
	//
	// Private members
	//
	//

	private SwingMetawidget	mMetawidget;

	private SwingMetawidget	mButtonsMetawidget;

	Car						mCar;

	//
	//
	// Public methods
	//
	//

	public Car getCar()
	{
		return mCar;
	}

	@Action( name = "add" )
	@UiJexlAttribute( name = HIDDEN, value = "${this.car.owner != null}" )
	public void addOwner()
	{
		mCar.setOwner( new Owner() );
		mMetawidget.setToInspect( mCar );
		mButtonsMetawidget.setToInspect( this );
	}

	@Action
	public void save()
	{
		mMetawidget.save();
		JOptionPane.showMessageDialog( getMainFrame(), "Saved " + mCar );
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected void startup()
	{
		// Model

		mCar = new Car();
		mCar.setMake( "Ford" );

		// Metawidget

		mMetawidget = new SwingMetawidget();
		mMetawidget.setInspectorConfig( "org/metawidget/example/swing/appframework/inspector-config.xml" );
		mMetawidget.setBundle( ResourceBundle.getBundle( "org.metawidget.example.swing.appframework.resources.CarApplication" ) );
		mMetawidget.setToInspect( mCar );
		mMetawidget.setBindingClass( BeansBinding.class );
		mMetawidget.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

		// Actions

		mButtonsMetawidget = new SwingMetawidget();
		mButtonsMetawidget.setInspectorConfig( "org/metawidget/example/swing/appframework/action-inspector-config.xml" );
		mButtonsMetawidget.setBundle( ResourceBundle.getBundle( "org.metawidget.example.swing.appframework.resources.CarApplication" ) );
		mButtonsMetawidget.setToInspect( this );
		mButtonsMetawidget.setLayoutClass( null );

		Facet facet = new Facet();
		facet.setName( "buttons" );
		facet.add( mButtonsMetawidget );
		mMetawidget.add( facet );

		// Show

		mMetawidget.setPreferredSize( new Dimension( 400, 150 ) );
		show( mMetawidget );
	}
}
