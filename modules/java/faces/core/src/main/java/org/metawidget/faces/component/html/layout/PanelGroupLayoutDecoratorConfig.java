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

package org.metawidget.faces.component.html.layout;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a PanelGroupLayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PanelGroupLayoutDecoratorConfig
	extends LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> {

	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	private String	mPanelLayout;

	//
	// Public methods
	//

	/**
	 * Overridden to use covariant return type.
	 * 
	 * @return this, as part of a fluent interface
	 */

	@Override
	public PanelGroupLayoutDecoratorConfig setLayout( Layout<UIComponent, UIComponent, UIMetawidget> layout ) {

		super.setLayout( layout );

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public PanelGroupLayoutDecoratorConfig setStyle( String style ) {

		mStyle = style;

		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public PanelGroupLayoutDecoratorConfig setStyleClass( String styleClass ) {

		mStyleClass = styleClass;

		return this;
	}

	/**
	 * Sets the Layout, as defined by
	 * <code>javax.faces.component.html.HtmlPanelGroup.setLayout</code>.
	 * 
	 * @return this, as part of a fluent interface
	 */

	public PanelGroupLayoutDecoratorConfig setPanelLayout( String panelLayout ) {

		mPanelLayout = panelLayout;

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

		if ( !ObjectUtils.nullSafeEquals( mStyle, ( (PanelGroupLayoutDecoratorConfig) that ).mStyle ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mStyleClass, ( (PanelGroupLayoutDecoratorConfig) that ).mStyleClass ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mPanelLayout, ( (PanelGroupLayoutDecoratorConfig) that ).mPanelLayout ) ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStyle );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStyleClass );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mPanelLayout );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected String getStyle() {

		return mStyle;
	}

	protected String getStyleClass() {

		return mStyleClass;
	}

	protected String getPanelLayout() {

		return mPanelLayout;
	}
}
