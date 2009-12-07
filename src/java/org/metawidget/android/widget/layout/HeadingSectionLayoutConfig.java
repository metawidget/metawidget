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

package org.metawidget.android.widget.layout;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;

import android.view.View;

/**
 * Configures a HeadingSectionLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class HeadingSectionLayoutConfig
	extends LayoutDecoratorConfig<View, AndroidMetawidget>
{
	//
	// Private members
	//

	private int	mStyle;

	//
	// Public methods
	//

	/**
	 * Overridden to use covariant return type.
	 *
	 * @return this, as part of a fluent interface
	 */

	@Override
	public HeadingSectionLayoutConfig setLayout( Layout<View, AndroidMetawidget> layout )
	{
		super.setLayout( layout );

		return this;
	}

	public int getStyle()
	{
		return mStyle;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HeadingSectionLayoutConfig setStyle( int style )
	{
		mStyle = style;
		return this;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof HeadingSectionLayoutConfig ) )
			return false;

		if ( mStyle != ( (HeadingSectionLayoutConfig) that ).mStyle )
			return false;

		return super.equals( that );
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode ^= mStyle;

		return hashCode;
	}
}
