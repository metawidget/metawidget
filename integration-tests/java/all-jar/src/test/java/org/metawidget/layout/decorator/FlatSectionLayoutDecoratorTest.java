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

package org.metawidget.layout.decorator;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.layout.iface.Layout;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayout;
import org.metawidget.swing.layout.SeparatorLayoutDecorator;
import org.metawidget.swing.layout.SeparatorLayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class FlatSectionLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testEmptyStub()
		throws Exception {

		final List<String> triggered = CollectionUtils.newArrayList();

		Layout<JComponent, JComponent, SwingMetawidget> layout = new GridBagLayout() {

			@Override
			public void layoutWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

				triggered.add( "layoutWidget " + widget.getClass() );
			}
		};

		FlatSectionLayoutDecorator<JComponent, JComponent, SwingMetawidget> flatSectionLayoutDecoratorTest = new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( layout ) );

		SwingMetawidget metawidget = new SwingMetawidget();
		JComponent container = new JPanel();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( SECTION, "Bar" );

		// If empty stub, should ignore

		assertTrue( triggered.isEmpty() );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections, null );
		flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections = new String[] { "Foo" };
		flatSectionLayoutDecoratorTest.layoutWidget( new Stub(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Foo" );
		assertEquals( triggered.get( 0 ), "layoutWidget class org.metawidget.swing.Stub" );
		assertEquals( triggered.size(), 1 );

		// Otherwise, should process

		flatSectionLayoutDecoratorTest.layoutWidget( new JTextArea(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Bar" );
		assertEquals( triggered.get( 1 ), "layoutWidget class javax.swing.JPanel" );
		assertEquals( triggered.get( 2 ), "layoutWidget class javax.swing.JTextArea" );
		assertEquals( triggered.size(), 3 );

		// Should stay

		attributes.remove( SECTION );
		flatSectionLayoutDecoratorTest.layoutWidget( new JSpinner(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Bar" );
		assertEquals( triggered.get( 3 ), "layoutWidget class javax.swing.JSpinner" );
		assertEquals( triggered.size(), 4 );

		// Should still stay (cannot 'terminate' a flat section)

		attributes.put( SECTION, "" );
		flatSectionLayoutDecoratorTest.layoutWidget( new JTextField(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Bar" );
		assertEquals( triggered.get( 4 ), "layoutWidget class javax.swing.JTextField" );
		assertEquals( triggered.size(), 5 );
	}
}
