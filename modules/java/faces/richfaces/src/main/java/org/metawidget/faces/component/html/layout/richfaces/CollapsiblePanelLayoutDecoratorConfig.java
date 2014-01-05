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

package org.metawidget.faces.component.html.layout.richfaces;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;
import org.richfaces.component.SwitchType;

/**
 * Configures a CollapsiblePanelLayoutDecoratorConfig prior to use. Once instantiated, Layouts are
 * immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CollapsiblePanelLayoutDecoratorConfig
	extends LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> {

	//
	// Private members
	//

	private SwitchType	mSwitchType	= SwitchType.client;

	private boolean		mExpanded	= true;

	//
	// Public methods
	//

	/**
	 * Overridden to use covariant return type.
	 *
	 * @return this, as part of a fluent interface
	 */

	@Override
	public CollapsiblePanelLayoutDecoratorConfig setLayout( Layout<UIComponent, UIComponent, UIMetawidget> layout ) {

		super.setLayout( layout );

		return this;
	}

	/**
	 * Sets <code>CollapsiblePanel.setSwitchType</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public CollapsiblePanelLayoutDecoratorConfig setSwitchType( SwitchType switchType ) {

		mSwitchType = switchType;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public CollapsiblePanelLayoutDecoratorConfig setExpanded( boolean expanded ) {

		mExpanded = expanded;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mSwitchType, ( (CollapsiblePanelLayoutDecoratorConfig) that ).mSwitchType ) ) {
			return false;
		}

		if ( mExpanded != ( (CollapsiblePanelLayoutDecoratorConfig) that ).mExpanded ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSwitchType );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mExpanded );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected SwitchType getSwitchType() {

		return mSwitchType;
	}

	protected boolean isExpanded() {

		return mExpanded;
	}
}
