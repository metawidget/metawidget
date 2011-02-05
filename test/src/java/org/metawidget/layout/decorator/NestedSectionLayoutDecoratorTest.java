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

package org.metawidget.layout.decorator;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import junit.framework.TestCase;

import org.metawidget.layout.iface.Layout;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayout;
import org.metawidget.swing.layout.TabbedPaneLayoutDecorator;
import org.metawidget.swing.layout.TabbedPaneLayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class NestedSectionLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testEmptyStub()
		throws Exception {

		final Set<String> triggered = CollectionUtils.newHashSet();

		Layout<JComponent, JComponent, SwingMetawidget> layout = new GridBagLayout() {

			@Override
			public void layoutWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

				triggered.add( "Triggered" );
			}
		};

		NestedSectionLayoutDecorator<JComponent, JComponent, SwingMetawidget> nestedSectionLayoutDecorator = new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( layout ) );

		SwingMetawidget metawidget = new SwingMetawidget();
		JComponent container = new JPanel();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( SECTION, "Bar" );

		// If empty stub, should ignore

		assertTrue( triggered.isEmpty() );
		assertEquals( nestedSectionLayoutDecorator.getState( container, metawidget ).currentSectionWidget, null );
		nestedSectionLayoutDecorator.getState( container, metawidget ).currentSection = "Foo";
		nestedSectionLayoutDecorator.layoutWidget( new Stub(), PROPERTY, attributes, container, metawidget );
		assertEquals( nestedSectionLayoutDecorator.getState( container, metawidget ).currentSection, "Foo" );
		assertTrue( triggered.size() == 1 );
		assertTrue( !attributes.containsKey( SECTION ));

		// Otherwise, should process

		attributes.put( SECTION, "Bar" );
		nestedSectionLayoutDecorator.layoutWidget( new JTextArea(), PROPERTY, attributes, container, metawidget );
		assertEquals( nestedSectionLayoutDecorator.getState( container, metawidget ).currentSection, "Bar" );
		JComponent currentSectionWidget = nestedSectionLayoutDecorator.getState( container, metawidget ).currentSectionWidget;
		assertNotSame( currentSectionWidget, null );

		// Should stay

		attributes.remove( SECTION );
		nestedSectionLayoutDecorator.layoutWidget( new JTextArea(), PROPERTY, attributes, container, metawidget );
		assertEquals( nestedSectionLayoutDecorator.getState( container, metawidget ).currentSection, "Bar" );
		assertEquals( nestedSectionLayoutDecorator.getState( container, metawidget ).currentSectionWidget, currentSectionWidget );

		// Should terminate section

		attributes.put( SECTION, "" );
		nestedSectionLayoutDecorator.layoutWidget( new JTextArea(), PROPERTY, attributes, container, metawidget );
		assertEquals( nestedSectionLayoutDecorator.getState( container, metawidget ).currentSection, "" );
		assertEquals( nestedSectionLayoutDecorator.getState( container, metawidget ).currentSectionWidget, null );
	}
}
