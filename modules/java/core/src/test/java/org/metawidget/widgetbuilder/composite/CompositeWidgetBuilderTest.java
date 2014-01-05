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

package org.metawidget.widgetbuilder.composite;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.AdvancedWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
