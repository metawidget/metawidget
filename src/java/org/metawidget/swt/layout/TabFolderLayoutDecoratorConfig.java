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

package org.metawidget.swt.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.swt.SwtMetawidget;

/**
 * Configures a TabFolderLayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class TabFolderLayoutDecoratorConfig
	extends LayoutDecoratorConfig<Control, Composite, SwtMetawidget> {

	//
	// Private members
	//

	private int	mTabLocation	= SWT.TOP;

	//
	// Public methods
	//

	/**
	 * Overridden to use covariant return type.
	 *
	 * @return this, as part of a fluent interface
	 */

	@Override
	public TabFolderLayoutDecoratorConfig setLayout( Layout<Control, Composite, SwtMetawidget> layout ) {

		super.setLayout( layout );

		return this;
	}

	/**
	 * One of SWT.TOP or SWT.BOTTOM.
	 *
	 * @return this, as part of a fluent interface
	 */

	public TabFolderLayoutDecoratorConfig setTabLocation( int TabLocation ) {

		mTabLocation = TabLocation;

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

		if ( mTabLocation != ( (TabFolderLayoutDecoratorConfig) that ).mTabLocation ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + mTabLocation;

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getTabLocation() {

		return mTabLocation;
	}
}
