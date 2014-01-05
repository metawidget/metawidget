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

package org.metawidget.android.widget.layout;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Configures a TextViewLayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TextViewLayoutDecoratorConfig
	extends LayoutDecoratorConfig<View, ViewGroup, AndroidMetawidget> {

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
	public TextViewLayoutDecoratorConfig setLayout( Layout<View, ViewGroup, AndroidMetawidget> layout ) {

		super.setLayout( layout );

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public TextViewLayoutDecoratorConfig setStyle( int style ) {

		mStyle = style;
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

		if ( mStyle != ( (TextViewLayoutDecoratorConfig) that ).mStyle ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + mStyle;

		return hashCode;
	}

	//
	// Protected methods
	//

	protected int getStyle() {

		return mStyle;
	}
}
