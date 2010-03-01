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

package org.metawidget.swt;

import java.util.TimeZone;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.shared.allwidgets.model.AllWidgets;
import org.metawidget.shared.allwidgets.proxy.AllWidgets$$EnhancerByCGLIB$$1234;

/**
 * @author Richard Kennard
 */

public class SwtAllWidgetsTest
	extends TestCase
{
	//
	// Public statics
	//

	public static void main( String[] args )
	{
		Display display = new Display();
		Shell shell = new Shell( display, SWT.DIALOG_TRIM | SWT.RESIZE );
		shell.setLayout( new FillLayout() );

		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.setConfig( "org/metawidget/swt/allwidgets/metawidget.xml" );
		metawidget.setToInspect( new AllWidgets() );

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() )
		{
			if ( !display.readAndDispatch() )
				display.sleep();
		}

		display.dispose();
	}

	//
	// Constructor
	//

	public SwtAllWidgetsTest()
	{
		// Default constructor
	}

	/**
	 * JUnit 3.7 constructor (SwtAllWidgetsTest gets run under JDK 1.4 using JUnit 3).
	 */

	public SwtAllWidgetsTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testAllWidgets()
		throws Exception
	{
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );

		// Model

		AllWidgets allWidgets = new AllWidgets$$EnhancerByCGLIB$$1234();

		// App

		Display display = new Display();
		Shell shell = new Shell( display, SWT.NONE );
		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.setConfig( "org/metawidget/swt/allwidgets/metawidget.xml" );

		metawidget.setToInspect( allWidgets );

	}
}
