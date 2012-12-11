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

package org.metawidget.integrationtest.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.integrationtest.swing.tutorial.Person;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.util.ArrayUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class ExcludingWidgetBuilderExampleTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
	public void testWidgetBuilderExample()
		throws Exception {

		Person person = new Person();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new ExcludingWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.putClientProperty( "exclude", new String[] { "age", "retired" } );
		metawidget.setToInspect( person );

		assertTrue( metawidget.getComponent( 0 ) instanceof JLabel );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, metawidget.getComponentCount() );
	}

	//
	// Inner class
	//

	static class ExcludingWidgetBuilder
		implements WidgetBuilder<JComponent, SwingMetawidget> {

		public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

			String[] exclude = (String[]) metawidget.getClientProperty( "exclude" );

			if ( ArrayUtils.contains( exclude, attributes.get( NAME ) ) ) {
				return new Stub();
			}

			return null;
		}
	}
}
