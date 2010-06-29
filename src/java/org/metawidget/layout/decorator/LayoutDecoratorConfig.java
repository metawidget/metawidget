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

package org.metawidget.layout.decorator;

import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a LayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class LayoutDecoratorConfig<W, C extends W, M extends C> {

	//
	// Private members
	//

	private Layout<W, C, M>	mLayout;

	//
	// Public methods
	//

	public Layout<W, C, M> getLayout() {

		return mLayout;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public LayoutDecoratorConfig<W, C, M> setLayout( Layout<W, C, M> layout ) {

		mLayout = layout;

		return this;
	}

	@SuppressWarnings( "unchecked" )
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

		return ( ObjectUtils.nullSafeEquals( mLayout, ( (LayoutDecoratorConfig<W, C, M>) that ).mLayout ) );
	}

	@Override
	public int hashCode() {

		return ObjectUtils.nullSafeHashCode( mLayout );
	}
}
