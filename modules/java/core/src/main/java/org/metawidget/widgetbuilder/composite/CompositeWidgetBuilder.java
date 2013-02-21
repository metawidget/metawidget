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
 * @author Richard Kennard
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
