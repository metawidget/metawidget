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

import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * Configures a CompositeWidgetBuilder prior to use. Once instantiated, WidgetBuilders are
 * immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
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
