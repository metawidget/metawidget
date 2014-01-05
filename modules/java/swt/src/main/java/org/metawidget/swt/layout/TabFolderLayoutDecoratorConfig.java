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

package org.metawidget.swt.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a TabFolderLayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	public TabFolderLayoutDecoratorConfig setTabLocation( int tabLocation ) {

		mTabLocation = tabLocation;

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
