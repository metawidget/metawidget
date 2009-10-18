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

package org.metawidget.example.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.example.swing.tutorial.Person;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * @author Richard Kennard
 */

public class WidgetProcessorExampleTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testWidgetProcessorExample()
		throws Exception
	{
		Person person = new Person();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new TooltipProcessor( new TooltipProcessorConfig().setPrefix("Tip:")));
		metawidget.setToInspect( person );

		assertTrue( "Tip:age".equals( ((JComponent) metawidget.getComponent( 1 )).getToolTipText() ));
		assertTrue( "Tip:name".equals( ((JComponent) metawidget.getComponent( 3 )).getToolTipText() ));
		assertTrue( "Tip:retired".equals( ((JComponent) metawidget.getComponent( 5 )).getToolTipText() ));
	}

	//
	// Inner class
	//

	static class TooltipProcessor
		extends BaseWidgetProcessor<JComponent, SwingMetawidget>
	{
		private String mPrefix;

		public TooltipProcessor( TooltipProcessorConfig config ) {
			mPrefix = config.getPrefix();
		}

		public JComponent processWidget( JComponent widget, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
		{
			widget.setToolTipText( mPrefix + attributes.get( NAME ) );
			return widget;
		}
	}

	static class TooltipProcessorConfig {
		private String mPrefix;

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
