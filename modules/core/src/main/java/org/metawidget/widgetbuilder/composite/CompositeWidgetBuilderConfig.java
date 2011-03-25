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

import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * Configures a CompositeWidgetBuilder prior to use. Once instantiated, WidgetBuilders are
 * immutable.
 *
 * @author Richard Kennard
 */

public class CompositeWidgetBuilderConfig<W, M extends W> {

	//
	// Private members
	//

	private WidgetBuilder<W, M>[]	mWidgetBuilders;

	//
	// Public methods
	//

	/**
	 * Sets the sub-WidgetBuilders the CompositeWidgetBuilder will call.
	 * <p>
	 * WidgetBuilders will be called in order.
	 *
	 * @return this, as part of a fluent interface
	 */

	// Note: in Java 7 we can probably put @SafeVarargs here
	//
	public CompositeWidgetBuilderConfig<W, M> setWidgetBuilders( WidgetBuilder<W, M>... widgetBuilders ) {

		mWidgetBuilders = widgetBuilders;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( that == null ) {
			return false;
		}

		if ( getClass() != that.getClass() ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mWidgetBuilders, ( (CompositeWidgetBuilderConfig<?, ?>) that ).mWidgetBuilders ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		return ObjectUtils.nullSafeHashCode( mWidgetBuilders );
	}

	//
	// Protected methods
	//

	protected WidgetBuilder<W, M>[] getWidgetBuilders() {

		return mWidgetBuilders;
	}
}
