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
 * Configures a TabViewLayoutDecorator prior to use. Once instantiated, Layouts are immutable.
 *
 * @author DanilAREFY
 */

public class TabViewLayoutDecoratorConfig
        extends LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> {

    //
    // Private members
    //

    private int     mActiveIndex    =   0;

    private String  mEffect;

    private String  mEffectDuration;

    private boolean mDynamic        =   false;

    private boolean mCache          =   true;

    private String  mOrientation    =   "top";

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
    public TabViewLayoutDecoratorConfig setLayout( Layout<UIComponent, UIComponent, UIMetawidget> layout ) {

        super.setLayout( layout );

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setActiveIndex( int activeIndex ) {

        mActiveIndex = activeIndex;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setDynamic( boolean dynamic ) {

        mDynamic = dynamic;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setEffect( String effect ) {

        mEffect = effect;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setEffectDuration( String effectDuration ) {

        mEffectDuration = effectDuration;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setCache( boolean cache ) {

        mCache = cache;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setOrientation( String orientation ) {

        mOrientation = orientation;

        return this;
    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setStyle( String style ) {

        mStyle = style;

        return this;

    }

    /**
     * @return this, as part of a fluent interface
     */

    public TabViewLayoutDecoratorConfig setStyleClass( String styleClass ) {

        mStyleClass = styleClass;

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

        if ( !ObjectUtils.nullSafeEquals( mActiveIndex, ((TabViewLayoutDecoratorConfig) that ).mActiveIndex) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mEffect, ((TabViewLayoutDecoratorConfig) that ).mEffect ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mEffectDuration, ((TabViewLayoutDecoratorConfig) that ).mEffectDuration ) ) {
            return false;
        }

        if ( mDynamic != ( (TabViewLayoutDecoratorConfig) that ).mDynamic ) {
            return false;
        }

        if ( mCache != ( (TabViewLayoutDecoratorConfig) that ).mCache ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mOrientation, ( (TabViewLayoutDecoratorConfig) that ).mOrientation ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mStyle, ( (TabViewLayoutDecoratorConfig) that ).mStyle ) ) {
            return false;
        }

        if ( !ObjectUtils.nullSafeEquals( mStyleClass, ( (TabViewLayoutDecoratorConfig) that ).mStyleClass ) ) {
            return false;
        }

        return super.equals( that );

    }

    @Override
    public int hashCode() {

        int hashCode = super.hashCode();
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mActiveIndex );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mEffect );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mEffectDuration );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDynamic );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mCache );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mOrientation );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStyle );
        hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStyleClass );

        return hashCode;
    }

    //
    // Protected methods
    //

    protected int getActiveIndex() {

        return mActiveIndex;
    }

    protected String getEffect() {

        return mEffect;
    }

    protected String getEffectDuration() {

        return mEffectDuration;
    }

    protected boolean isDynamic() {

        return mDynamic;
    }

    protected boolean isCache() {

        return mCache;
    }

    protected String getOrientation() {

        return mOrientation;
    }

    protected String getStyle() {

        return mStyle;
    }

    protected String getStyleClass() {

        return mStyleClass;
    }
}