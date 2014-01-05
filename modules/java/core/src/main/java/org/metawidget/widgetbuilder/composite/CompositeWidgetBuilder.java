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

import java.util.Map;

import org.metawidget.widgetbuilder.iface.AdvancedWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * Delegates widget building to one or more sub-WidgetBuilders.
 * <p>
 * Each sub-WidgetBuilder in the list is invoked, in order, calling its <code>buildWidget</code>
 * method. The first non-null result is returned. If all sub-WidgetBuilders return null, null is
 * returned (the parent Metawidget will generally instantiate a nested Metawidget in this case).
 * <p>
 * Note: the name <em>Composite</em>WidgetBuilder refers to the Composite design pattern.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CompositeWidgetBuilder<W, M extends W>
	implements AdvancedWidgetBuilder<W, M> {

	//
	// Private members
	//

	/* package private */final WidgetBuilder<W, M>[]	mWidgetBuilders;

	//
	// Constructor
	//

	@SuppressWarnings( "unchecked" )
	public CompositeWidgetBuilder( CompositeWidgetBuilderConfig<W, M> config ) {

		WidgetBuilder<W, M>[] widgetBuilders = config.getWidgetBuilders();

		// Must have at least two WidgetBuilders

		if ( widgetBuilders == null || widgetBuilders.length < 2 ) {
			throw WidgetBuilderException.newException( "CompositeWidgetBuilder needs at least two WidgetBuilders" );
		}

		// Defensive copy

		mWidgetBuilders = new WidgetBuilder[widgetBuilders.length];

		for ( int loop = 0, length = widgetBuilders.length; loop < length; loop++ ) {
			WidgetBuilder<W, M> widgetBuilder = widgetBuilders[loop];

			for ( int checkDuplicates = 0; checkDuplicates < loop; checkDuplicates++ ) {
				if ( mWidgetBuilders[checkDuplicates].equals( widgetBuilder ) ) {
					throw WidgetBuilderException.newException( "CompositeWidgetBuilder's list of WidgetBuilders contains two of the same " + widgetBuilder.getClass().getName() );
				}
			}

			mWidgetBuilders[loop] = widgetBuilder;
		}
	}

	//
	// Public methods
	//

	public void onStartBuild( M metawidget ) {

		for ( WidgetBuilder<W, M> widgetBuilder : mWidgetBuilders ) {

			if ( widgetBuilder instanceof AdvancedWidgetBuilder<?, ?> ) {
				((AdvancedWidgetBuilder<W, M>) widgetBuilder).onStartBuild( metawidget );
			}
		}
	}

	public W buildWidget( String elementName, Map<String, String> attributes, M metawidget ) {

		for ( WidgetBuilder<W, M> widgetBuilder : mWidgetBuilders ) {
			W widget = widgetBuilder.buildWidget( elementName, attributes, metawidget );

			if ( widget != null ) {
				return widget;
			}
		}

		return null;
	}

	public void onEndBuild( M metawidget ) {

		for ( WidgetBuilder<W, M> widgetBuilder : mWidgetBuilders ) {

			if ( widgetBuilder instanceof AdvancedWidgetBuilder<?, ?> ) {
				((AdvancedWidgetBuilder<W, M>) widgetBuilder).onEndBuild( metawidget );
			}
		}
	}

	/**
	 * Exposed for <code>getValue</code> calls.
	 */

	public WidgetBuilder<W, M>[] getWidgetBuilders() {

		// Defensive copy

		@SuppressWarnings( "unchecked" )
		WidgetBuilder<W, M>[] widgetBuilders = new WidgetBuilder[mWidgetBuilders.length];
		System.arraycopy( mWidgetBuilders, 0, widgetBuilders, 0, mWidgetBuilders.length );

		return widgetBuilders;
	}
}
