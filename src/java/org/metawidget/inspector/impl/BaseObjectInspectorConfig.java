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

package org.metawidget.inspector.impl;

import org.metawidget.inspector.impl.actionstyle.ActionStyle;
import org.metawidget.inspector.impl.actionstyle.metawidget.MetawidgetActionStyle;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Base class for BaseObjectInspector configurations.
 * <p>
 * Handles configuring the pattern to recognize proxied classes, as well as the convention to use to
 * recognize properties.
 * <p>
 * Note that whilst <code>BaseObjectInspector</code> is an abstract class,
 * <code>BaseObjectInspectorConfig</code> is concrete and instantiable. This is so that subclasses
 * of <code>BaseObjectInspector</code> that don't require additional configuration (eg. most of
 * them) don't need to further define a <code>Config</code> class, whilst still allowing their
 * proxy/property/action patterns to be configured in the (rare) cases they need to. Incidentally,
 * this is why we call this class <code>BaseObjectInspectorConfig</code> as opposed to
 * <code>AbstractPropertyInspectorConfig</code>, because a non-abstract class called
 * <code>AbstractXXX</code> was deemed confusing! All our other base classes are called
 * <code>BaseXXX</code> for consistency.
 *
 * @author Richard Kennard
 */

public class BaseObjectInspectorConfig
{
	//
	// Private statics
	//

	private static PropertyStyle	DEFAULT_PROPERTY_STYLE;

	private static ActionStyle		DEFAULT_ACTION_STYLE;

	//
	// Private members
	//

	protected PropertyStyle			mPropertyStyle;

	protected boolean				mNullPropertyStyle;

	protected ActionStyle			mActionStyle;

	protected boolean				mNullActionStyle;

	//
	// Public methods
	//

	/**
	 * Sets the style used to recognize properties.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseObjectInspectorConfig setPropertyStyle( PropertyStyle propertyStyle )
	{
		mPropertyStyle = propertyStyle;

		if ( propertyStyle == null )
			mNullPropertyStyle = true;

		// Fluent interface

		return this;
	}

	/**
	 * Sets the style used to recognize actions.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseObjectInspectorConfig setActionStyle( ActionStyle actionStyle )
	{
		mActionStyle = actionStyle;

		if ( actionStyle == null )
			mNullActionStyle = true;

		// Fluent interface

		return this;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( this == that )
			return true;

		if ( !( that instanceof BaseObjectInspectorConfig ) )
			return false;

		if ( !ObjectUtils.nullSafeEquals( mPropertyStyle, ( (BaseObjectInspectorConfig) that ).mPropertyStyle ) )
			return false;

		if ( mNullPropertyStyle != ( (BaseObjectInspectorConfig) that ).mNullPropertyStyle )
			return false;

		if ( !ObjectUtils.nullSafeEquals( mActionStyle, ( (BaseObjectInspectorConfig) that ).mActionStyle ) )
			return false;

		if ( mNullActionStyle != ( (BaseObjectInspectorConfig) that ).mNullActionStyle )
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mPropertyStyle );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mActionStyle );

		return hashCode;
	}

	//
	// Protected methods
	//

	/**
	 * Gets the style used to recognize properties.
	 */

	protected PropertyStyle getPropertyStyle()
	{
		if ( mPropertyStyle == null && !mNullPropertyStyle )
		{
			// Do not initialise unless needed, so that we can be shipped without

			if ( DEFAULT_PROPERTY_STYLE == null )
				DEFAULT_PROPERTY_STYLE = new JavaBeanPropertyStyle();

			return DEFAULT_PROPERTY_STYLE;
		}

		return mPropertyStyle;
	}

	/**
	 * Gets the style used to recognize actions.
	 */

	protected ActionStyle getActionStyle()
	{
		if ( mActionStyle == null && !mNullActionStyle )
		{
			// Do not initialise unless needed, so that we can be shipped without

			if ( DEFAULT_ACTION_STYLE == null )
			{
				try
				{
					DEFAULT_ACTION_STYLE = new MetawidgetActionStyle();
				}
				catch ( Throwable t )
				{
					// MetawidgetActionStyle is unsupported on JDK 1.4, as well as
					// some environments might choose to ship without it
				}
			}

			return DEFAULT_ACTION_STYLE;
		}

		return mActionStyle;
	}
}
