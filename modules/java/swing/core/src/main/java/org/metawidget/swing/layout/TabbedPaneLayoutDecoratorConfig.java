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

package org.metawidget.swing.layout;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a TabbedPaneLayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class TabbedPaneLayoutDecoratorConfig
	extends LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget> {

	//
	// Private members
	//

	private int	mTabPlacement	= SwingConstants.TOP;

	//
	// Public methods
	//

	/**
	 * Overridden to use covariant return type.
	 *
	 * @return this, as part of a fluent interface
	 */

	@Override
	public TabbedPaneLayoutDecoratorConfig setLayout( Layout<JComponent, JComponent, SwingMetawidget> layout ) {

		super.setLayout( layout );

		return this;
	}

	/**
	 * One of SwingConstants.TOP, SwingConstants.BOTTOM, SwingConstants.LEFT or SwingConstants.RIGHT
	 * as defined by JTabbedPane.setTabAlignment.
	 *
	 * @return this, as part of a fluent interface
	 */

	public TabbedPaneLayoutDecoratorConfig setTabPlacement( int TabPlacement ) {

		mTabPlacement = TabPlacement;

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

		if ( mTabPlacement != ( (TabbedPaneLayoutDecoratorConfig) that ).mTabPlacement ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + mTabPlacement;

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getTabPlacement() {

		return mTabPlacement;
	}
}
