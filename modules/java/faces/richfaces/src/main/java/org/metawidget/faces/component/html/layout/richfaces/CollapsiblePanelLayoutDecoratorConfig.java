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
 * @author Richard Kennard
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
