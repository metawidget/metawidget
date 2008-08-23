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

package org.metawidget.test.example.swing.appframework;

import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.example.swing.appframework.CarApplication;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class SwingAppFrameworkTest
	extends TestCase
{
	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public SwingAppFrameworkTest( String name )
	{
		super( name );
	}

	//
	//
	// Public methods
	//
	//

	public void testCarApplication()
		throws Exception
	{
		// Create app

		CarApplication carApplication = new CarApplication();

		// Start app

		Method methodStartupWithoutShow = CarApplication.class.getDeclaredMethod( "startupWithoutShow" );
		methodStartupWithoutShow.setAccessible( true );
		methodStartupWithoutShow.invoke( carApplication );

		// Fetch Metawidget

		JFrame frame = carApplication.getMainFrame();
		SwingMetawidget metawidget = (SwingMetawidget) frame.getContentPane().getComponent( 1 );
		assertTrue( "Ford".equals(((JTextField) metawidget.getComponent( 1 )).getText() ));

		// TODO: Test CarApplication
	}
}
