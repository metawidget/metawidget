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

package org.metawidget.integrationtest.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.integrationtest.swing.tutorial.Person;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class WidgetProcessorExampleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessorExample()
		throws Exception {

		Person person = new Person();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new TooltipProcessor( new TooltipProcessorConfig().setPrefix( "Tip:" ) ) );
		metawidget.setToInspect( person );

		assertEquals( "Tip:age", ( (JComponent) metawidget.getComponent( 1 ) ).getToolTipText() );
		assertEquals( "Tip:name", ( (JComponent) metawidget.getComponent( 3 ) ).getToolTipText() );
		assertEquals( "Tip:retired", ( (JComponent) metawidget.getComponent( 5 ) ).getToolTipText() );
	}

	//
	// Inner class
	//

	static class TooltipProcessor
		implements WidgetProcessor<JComponent, SwingMetawidget> {

		private String	mPrefix;

		public TooltipProcessor( TooltipProcessorConfig config ) {

			mPrefix = config.getPrefix();
		}

		public JComponent processWidget( JComponent widget, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

			widget.setToolTipText( mPrefix + attributes.get( NAME ) );
			return widget;
		}
	}

	static class TooltipProcessorConfig {

		private String	mPrefix;

		public TooltipProcessorConfig setPrefix( String prefix ) {

			mPrefix = prefix;
			return this;
		}

		public String getPrefix() {

			return mPrefix;
		}

		// ...must override equals and hashCode too...
	}
}
