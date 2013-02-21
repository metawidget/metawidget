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

package org.metawidget.widgetbuilder.composite;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.AdvancedWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class CompositeWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testStartEndBuild()
		throws Exception {

		final List<String> events = CollectionUtils.newArrayList();

		WidgetBuilder<JComponent, JComponent> simpleWidgetBuilder = new WidgetBuilder<JComponent, JComponent>() {

			public JComponent buildWidget( String elementName, Map<String, String> attributes, JComponent metawidget ) {

				events.add( "simpleWidgetBuilder::buildWidget" );
				return null;
			}
		};

		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilderConfig<JComponent, JComponent> config = new CompositeWidgetBuilderConfig<JComponent, JComponent>().setWidgetBuilders( new MyAdvancedWidgetBuilder( events, 1 ), simpleWidgetBuilder, new MyAdvancedWidgetBuilder( events, 2 ) );
		CompositeWidgetBuilder<JComponent, JComponent> widgetBuilder = new CompositeWidgetBuilder<JComponent, JComponent>( config );

		widgetBuilder.onStartBuild( null );
		widgetBuilder.buildWidget( null, null, null );
		widgetBuilder.onEndBuild( null );

		assertEquals( "advancedWidgetBuilder1::onStartBuild", events.get( 0 ) );
		assertEquals( "advancedWidgetBuilder2::onStartBuild", events.get( 1 ) );
		assertEquals( "advancedWidgetBuilder1::buildWidget", events.get( 2 ) );
		assertEquals( "simpleWidgetBuilder::buildWidget", events.get( 3 ) );
		assertEquals( "advancedWidgetBuilder2::buildWidget", events.get( 4 ) );
		assertEquals( "advancedWidgetBuilder1::onEndBuild", events.get( 5 ) );
		assertEquals( "advancedWidgetBuilder2::onEndBuild", events.get( 6 ) );
		assertEquals( 7, events.size() );
	}

	//
	// Inner class
	//

	private static class MyAdvancedWidgetBuilder
		implements AdvancedWidgetBuilder<JComponent, JComponent> {

		//
		// Private members
		//

		private List<String>	mEvents;

		private int				mNumber;

		//
		// Constructor
		//

		public MyAdvancedWidgetBuilder( List<String> events, int number ) {

			mEvents = events;
			mNumber = number;
		}

		//
		// Public methods
		//

		public void onStartBuild( JComponent metawidget ) {

			mEvents.add( "advancedWidgetBuilder" + mNumber + "::onStartBuild" );
		}

		public JComponent buildWidget( String elementName, Map<String, String> attributes, JComponent metawidget ) {

			mEvents.add( "advancedWidgetBuilder" + mNumber + "::buildWidget" );
			return null;
		}

		public void onEndBuild( JComponent metawidget ) {

			mEvents.add( "advancedWidgetBuilder" + mNumber + "::onEndBuild" );
		}
	}
}
