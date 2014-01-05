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
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a TabPanelLayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TabPanelLayoutDecoratorConfig
	extends LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> {

	//
	// Private members
	//

	private String	mHeaderAlignment	= "left";

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public TabPanelLayoutDecoratorConfig setHeaderAlignment( String headerAlignment ) {

		mHeaderAlignment = headerAlignment;

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

		if ( !ObjectUtils.nullSafeEquals( mHeaderAlignment, ( (TabPanelLayoutDecoratorConfig) that ).mHeaderAlignment ) ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mHeaderAlignment );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected String getHeaderAlignment() {

		return mHeaderAlignment;
	}
}
