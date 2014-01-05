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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
