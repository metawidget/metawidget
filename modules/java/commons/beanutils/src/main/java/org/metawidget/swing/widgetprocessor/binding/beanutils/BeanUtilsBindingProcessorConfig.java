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

package org.metawidget.swing.widgetprocessor.binding.beanutils;

import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a BeanUtilsBindingProcessor prior to use. Once instantiated, WidgetProcessors are
 * immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BeanUtilsBindingProcessorConfig {

	//
	// Private statics
	//

	private static PropertyStyle	DEFAULT_PROPERTY_STYLE;

	//
	// Private members
	//

	private PropertyStyle			mPropertyStyle;

	private boolean					mNullPropertyStyle;

	//
	// Public methods
	//

	/**
	 * Sets the style used to recognize properties.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BeanUtilsBindingProcessorConfig setPropertyStyle( PropertyStyle propertyStyle ) {

		mPropertyStyle = propertyStyle;
		mNullPropertyStyle = ( propertyStyle == null );

		// Fluent interface

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

		if ( !ObjectUtils.nullSafeEquals( mPropertyStyle, ( (BeanUtilsBindingProcessorConfig) that ).mPropertyStyle ) ) {
			return false;
		}

		if ( mNullPropertyStyle != ( (BeanUtilsBindingProcessorConfig) that ).mNullPropertyStyle ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mPropertyStyle );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mNullPropertyStyle );

		return hashCode;
	}

	//
	// Protected methods
	//

	/**
	 * Gets the style used to recognize properties.
	 */

	protected PropertyStyle getPropertyStyle() {

		if ( mPropertyStyle == null && !mNullPropertyStyle ) {
			// Do not initialise unless needed, so that we can be shipped without

			if ( DEFAULT_PROPERTY_STYLE == null ) {
				DEFAULT_PROPERTY_STYLE = new JavaBeanPropertyStyle();
			}

			return DEFAULT_PROPERTY_STYLE;
		}

		return mPropertyStyle;
	}
}
