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

package org.metawidget.layout.composite;

import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a CompositeLayout prior to use. Once instantiated, Layouts are
 * immutable.
 *
 * @author Richard Kennard
 */

public class CompositeLayoutConfig<W, M extends W>
{
	//
	// Private members
	//

	private Layout<W, M>[]	mLayouts;

	//
	// Public methods
	//

	public Layout<W, M>[] getLayouts()
	{
		return mLayouts;
	}

	/**
	 * Sets the sub-Layouts the CompositeLayout will call.
	 * <p>
	 * Layouts will be called in order.
	 *
	 * @return this, as part of a fluent interface
	 */

	// Note: in Java 7 we can probably put @SuppressWarnings( "unchecked" ) here
	//
	public CompositeLayoutConfig<W, M> setLayouts( Layout<W, M>... Layouts )
	{
		mLayouts = Layouts;

		return this;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof CompositeLayoutConfig ) )
			return false;

		if ( !ObjectUtils.nullSafeEquals( mLayouts, ( (CompositeLayoutConfig<?, ?>) that ).mLayouts ) )
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		return ObjectUtils.nullSafeHashCode( mLayouts );
	}
}
