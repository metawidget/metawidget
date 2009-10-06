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
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class CompositeWidgetBuilderTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testDefensiveCopy()
		throws Exception
	{
		SwingWidgetBuilder widgetBuilder = new SwingWidgetBuilder();
		@SuppressWarnings( "unchecked" )
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = new WidgetBuilder[] { widgetBuilder };
		CompositeWidgetBuilderConfig<JComponent, SwingMetawidget> config = new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>();
		config.setWidgetBuilders( widgetBuilders );

		CompositeWidgetBuilder<JComponent, SwingMetawidget> widgetBuilderComposite = new CompositeWidgetBuilder<JComponent, SwingMetawidget>( config );
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuildersCopied = widgetBuilderComposite.mWidgetBuilders;
		assertTrue( widgetBuildersCopied[0] == widgetBuilder );
		widgetBuilders[0] = null;
		assertTrue( widgetBuildersCopied[0] != null );
	}

	@SuppressWarnings( "unchecked" )
	public void testConfig()
	{
		CompositeWidgetBuilderConfig<JComponent, SwingMetawidget> config1 = new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>();
		CompositeWidgetBuilderConfig<JComponent, SwingMetawidget> config2 = new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// inspectors
		//
		// Note: we're not too worried about non-configurable WidgetBuilders implementing equals
		// just so that two CompositeWidgetBuilderConfigs with the same widget builders will also be
		// equal, because sub-WidgetBuilders of CompositeWidgetBuilderConfig will be cached
		// separately by ConfigReader

		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = new WidgetBuilder[] { new SwingXWidgetBuilder(), new SwingWidgetBuilder() };
		config1.setWidgetBuilders( widgetBuilders );
		assertTrue( config1.getWidgetBuilders()[0] instanceof SwingXWidgetBuilder );
		assertTrue( config1.getWidgetBuilders()[1] instanceof SwingWidgetBuilder );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setWidgetBuilders( new SwingWidgetBuilder(), new SwingXWidgetBuilder() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setWidgetBuilders( widgetBuilders );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );
	}
}
