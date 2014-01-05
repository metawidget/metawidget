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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	public TabbedPaneLayoutDecoratorConfig setTabPlacement( int tabPlacement ) {

		mTabPlacement = tabPlacement;

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
