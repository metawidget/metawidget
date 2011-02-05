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

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import junit.framework.TestCase;

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

		FlatSectionLayoutDecorator<JComponent, JComponent, SwingMetawidget> flatSectionLayoutDecoratorTest = new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new GridBagLayout() ) );

		SwingMetawidget metawidget = new SwingMetawidget();
		JComponent container = new JPanel();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( SECTION, "Bar" );

		// If empty stub, should ignore

		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections, null );
		flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections = new String[] { "Foo" };
		flatSectionLayoutDecoratorTest.layoutWidget( new Stub(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Foo" );

		// Otherwise, should process

		flatSectionLayoutDecoratorTest.layoutWidget( new JTextArea(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Bar" );

		// Should stay

		attributes.remove( SECTION );
		flatSectionLayoutDecoratorTest.layoutWidget( new JTextArea(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Bar" );

		// Should still stay (cannot 'terminate' a flat section)

		attributes.put( SECTION, "" );
		flatSectionLayoutDecoratorTest.layoutWidget( new JTextArea(), PROPERTY, attributes, container, metawidget );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections.length, 1 );
		assertEquals( flatSectionLayoutDecoratorTest.getState( container, metawidget ).currentSections[0], "Bar" );
	}
}
