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

import java.awt.GridLayout;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.example.swing.tutorial.Person;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class WidgetBuilderExampleTest
	extends TestCase
{
	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
	public void testWidgetProcessorExample()
		throws Exception
	{
		Person person = new Person();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new JRadioButtonWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.setToInspect( person );

		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 5 ) instanceof JPanel );
		assertTrue( ((JPanel) metawidget.getComponent( 5 )).getComponent( 0 ) instanceof JRadioButton );
		assertTrue( ((JPanel) metawidget.getComponent( 5 )).getComponent( 1 ) instanceof JRadioButton );
	}

	//
	// Inner class
	//

	static class JRadioButtonWidgetBuilder
		implements WidgetBuilder<JComponent, SwingMetawidget>
	{
		public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
		{
			if ( !"boolean".equals( attributes.get( TYPE ) ) )
			{
				return null;
			}

			JRadioButton trueButton = new JRadioButton( "True" );
			JRadioButton falseButton = new JRadioButton( "False" );
			JPanel panel = new JPanel();
			panel.setLayout( new GridLayout( 2, 1 ) );
			panel.add( trueButton );
			panel.add( falseButton );

			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add( trueButton );
			buttonGroup.add( falseButton );

			return panel;
		}
	}
}
