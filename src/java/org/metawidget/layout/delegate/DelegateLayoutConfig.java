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

package org.metawidget.layout.delegate;

import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a DelegateLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class DelegateLayoutConfig<W, M extends W>
{
	//
	// Private members
	//

	private Layout<W, M>	mLayout;

	//
	// Public methods
	//

	public Layout<W, M> getLayout()
	{
		return mLayout;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public DelegateLayoutConfig<W, M> setLayout( Layout<W, M> layout )
	{
		mLayout = layout;

		return this;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof DelegateLayoutConfig ) )
			return false;

		return ( ObjectUtils.nullSafeEquals( mLayout, ( (DelegateLayoutConfig<W, M>) that ).mLayout ) );
	}

	@Override
	public int hashCode()
	{
		return ObjectUtils.nullSafeHashCode( mLayout );
	}
}
