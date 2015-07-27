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

package org.metawidget.faces.component.html.layout.primefaces;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.ObjectUtils;

import javax.faces.component.UIComponent;

/**
 * Configures a AccordionPanelLayoutDecorator prior to use.
 *
 * @author DanilAREFY
 */

public class AccordionPanelLayoutDecoratorConfig
        extends LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> {

    //
    // Private members
    //

    private String  mActiveIndex    =   "false";

    private boolean mDynamic        =   false;

    private boolean mCache          =   true;

    private boolean mMultiple       =   false;

    private String  mStyle;

    private String  mStyleClass;

    //
    // Public methods
    //

    /**
     * Overridden to use covariant return type.
     *
     * @return this, as part of a fluent interface
     */

    @Override
    public AccordionPanelLayoutDecoratorConfig setLayout( Layout<UIComponent, UIComponent, UIMetawidget> layout ) {

        super.setLayout( layout );

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public AccordionPanelLayoutDecoratorConfig setActiveIndex( String activeIndex ) {

        mActiveIndex = activeIndex;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public AccordionPanelLayoutDecoratorConfig setDynamic( boolean dynamic ) {

        mDynamic = dynamic;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public AccordionPanelLayoutDecoratorConfig setCache( boolean cache ) {

        mCache = cache;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public AccordionPanelLayoutDecoratorConfig setMultiple( boolean multiple ) {

        mMultiple = multiple;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public AccordionPanelLayoutDecoratorConfig setStyle( String style ) {

        mStyle = style;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public AccordionPanelLayoutDecoratorConfig setStyleClass( String styleClass ) {

        mStyleClass = styleClass;

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

        if ( !ObjectUtils.nullSafeEquals(mActiveIndex, ((AccordionPanelLayoutDecoratorConfig) that).mActiveIndex) ) {
            return false;
        }

        if ( mDynamic != ( (AccordionPanelLayoutDecoratorConfig) that ).mDynamic ) {
            return false;
        }

        if ( mCache != ( (AccordionPanelLayoutDecoratorConfig) that ).mCache ) {
            return false;
        }

        if ( mMultiple != ( (AccordionPanelLayoutDecoratorConfig) that ).mMultiple ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mStyle, ( (AccordionPanelLayoutDecoratorConfig) that ).mStyle ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mStyleClass, ( (AccordionPanelLayoutDecoratorConfig) that ).mStyleClass ) ) {
            return false;
        }

        return super.equals( that );
    }

    @Override
    public int hashCode() {

        int hashCode = super.hashCode();
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mActiveIndex );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDynamic );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mCache );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mMultiple );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStyle );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStyleClass );

        return hashCode;
    }

    //
    // Protected methods
    //

    protected String getActiveIndex() {

        return mActiveIndex;
    }

    protected boolean isDynamic() {

        return mDynamic;
    }

    protected boolean isCache() {

        return mCache;
    }

    protected boolean isMultiple() {

        return mMultiple;
    }

    protected String getStyle() {

        return mStyle;
    }

    protected String getStyleClass() {

        return mStyleClass;
    }
}
