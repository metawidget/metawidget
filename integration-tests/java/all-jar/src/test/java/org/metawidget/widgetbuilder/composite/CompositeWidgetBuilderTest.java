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

package org.metawidget.widgetbuilder.composite;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetbuilder.swingx.SwingXWidgetBuilder;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * @author Richard Kennard
 */

public class CompositeWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unused" )
	public void testDefensiveCopy()
		throws Exception {

		SwingXWidgetBuilder widgetBuilder1 = new SwingXWidgetBuilder();
		SwingWidgetBuilder widgetBuilder2 = new SwingWidgetBuilder();
		@SuppressWarnings( "unchecked" )
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = new WidgetBuilder[] { widgetBuilder1, widgetBuilder2 };
		CompositeWidgetBuilderConfig<JComponent, SwingMetawidget> config = new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>();
		config.setWidgetBuilders( widgetBuilders );

		CompositeWidgetBuilder<JComponent, SwingMetawidget> widgetBuilderComposite = new CompositeWidgetBuilder<JComponent, SwingMetawidget>( config );
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuildersCopied = widgetBuilderComposite.mWidgetBuilders;
		assertEquals( widgetBuildersCopied[0], widgetBuilder1 );
		assertEquals( widgetBuildersCopied[1], widgetBuilder2 );
		widgetBuilders[0] = null;
		assertTrue( widgetBuildersCopied[0] != null );

		// Test duplicates

		try {
			@SuppressWarnings( "unchecked" )
			WidgetBuilder<JComponent, SwingMetawidget>[] duplicateWidgetBuilders = new WidgetBuilder[] { widgetBuilder1, widgetBuilder2, widgetBuilder1 };

			new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( duplicateWidgetBuilders ) );
			fail();
		} catch ( WidgetBuilderException e ) {
			assertEquals( "CompositeWidgetBuilder's list of WidgetBuilders contains two of the same org.metawidget.swing.widgetbuilder.swingx.SwingXWidgetBuilder", e.getMessage() );
		}
	}

	@SuppressWarnings( { "unchecked", "unused" } )
	public void testMinimumWidgetBuilders() {

		CompositeWidgetBuilderConfig<JComponent, SwingMetawidget> config = new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>();

		// Null WidgetBuilders

		try {
			new CompositeWidgetBuilder<JComponent, SwingMetawidget>( config );
			fail();
		} catch ( WidgetBuilderException e ) {
			assertEquals( "CompositeWidgetBuilder needs at least two WidgetBuilders", e.getMessage() );
		}

		// 0 WidgetBuilders

		config.setWidgetBuilders( new WidgetBuilder[0] );

		try {
			new CompositeWidgetBuilder<JComponent, SwingMetawidget>( config );
			fail();
		} catch ( WidgetBuilderException e ) {
			assertEquals( "CompositeWidgetBuilder needs at least two WidgetBuilders", e.getMessage() );
		}

		// 1 WidgetBuilder

		config.setWidgetBuilders( new WidgetBuilder[] { new SwingWidgetBuilder() } );

		try {
			new CompositeWidgetBuilder<JComponent, SwingMetawidget>( config );
			fail();
		} catch ( WidgetBuilderException e ) {
			assertEquals( "CompositeWidgetBuilder needs at least two WidgetBuilders", e.getMessage() );
		}

		// 2 WidgetBuilders

		config.setWidgetBuilders( new WidgetBuilder[] { new SwingWidgetBuilder(), new SwingXWidgetBuilder() } );

		try {
			new CompositeWidgetBuilder<JComponent, SwingMetawidget>( config );
			assertTrue( true );
		} catch ( WidgetBuilderException e ) {
			fail();
		}
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( CompositeWidgetBuilderConfig.class, new CompositeWidgetBuilderConfig<Object, Object>() {
			// Subclass
		} );
	}
}
