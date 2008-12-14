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

import java.util.Date;
import java.util.TimeZone;

import org.metawidget.swing.propertybinding.beansbinding.BeansBinding;
import org.metawidget.test.shared.allwidgets.model.AllWidgets.NestedWidgets;

/**
 * @author Richard Kennard
 */

public class SwingAllWidgetsBeansBindingTest
	extends SwingAllWidgetsTest
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public SwingAllWidgetsBeansBindingTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	@Override
	public void testAllWidgets()
		throws Exception
	{
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );

		// BeansBinding

		BeansBinding.registerConverter( Date.class, String.class, new org.metawidget.test.swing.allwidgets.converter.beansbinding.DateConverter( DATE_FORMAT ) );
		BeansBinding.registerConverter( NestedWidgets.class, String.class, new org.metawidget.test.swing.allwidgets.converter.beansbinding.NestedWidgetsConverter() );
		runTest( BeansBinding.class );
	}
}
