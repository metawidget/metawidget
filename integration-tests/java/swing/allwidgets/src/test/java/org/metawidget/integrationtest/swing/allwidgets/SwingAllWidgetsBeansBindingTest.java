// Metawidget (licensed under LGPL)
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

package org.metawidget.integrationtest.swing.allwidgets;

import java.util.Date;
import java.util.TimeZone;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessorConfig;

/**
 * @author Richard Kennard
 */

public class SwingAllWidgetsBeansBindingTest
	extends SwingAllWidgetsTest {

	//
	// Public methods
	//

	@Override
	public void testAllWidgets()
		throws Exception {

		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );

		// BeansBinding

		BeansBindingProcessorConfig config = new BeansBindingProcessorConfig();
		config.setConverter( Date.class, String.class, new org.metawidget.integrationtest.swing.allwidgets.converter.beansbinding.DateConverter( DATE_FORMAT ) );
		config.setConverter( NestedWidgets.class, String.class, new org.metawidget.integrationtest.swing.allwidgets.converter.beansbinding.NestedWidgetsConverter() );
		runTest( new BeansBindingProcessor( config ) );
	}
}
