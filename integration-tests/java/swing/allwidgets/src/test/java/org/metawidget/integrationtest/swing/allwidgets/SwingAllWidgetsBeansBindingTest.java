// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.integrationtest.swing.allwidgets;

import java.util.Date;
import java.util.TimeZone;

import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessorConfig;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
