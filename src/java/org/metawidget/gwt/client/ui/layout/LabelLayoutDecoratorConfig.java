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

package org.metawidget.gwt.client.ui.layout;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Configures a FlexTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class LabelLayoutDecoratorConfig
	extends LayoutDecoratorConfig<Widget, Panel, GwtMetawidget> {

	//
	// Private members
	//

	private String	mStyleName;

	//
	// Public methods
	//

	/**
	 * Overridden to use covariant return type.
	 *
	 * @return this, as part of a fluent interface
	 */

	@Override
	public LabelLayoutDecoratorConfig setLayout( Layout<Widget, Panel, GwtMetawidget> layout ) {

		super.setLayout( layout );

		return this;
	}

	public String getStyleName() {

		return mStyleName;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public LabelLayoutDecoratorConfig setStyleName( String StyleName ) {

		mStyleName = StyleName;

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

		if ( !ObjectUtils.nullSafeEquals( mStyleName, ( (LabelLayoutDecoratorConfig) that ).mStyleName ) ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStyleName );

		return hashCode;
	}
}
