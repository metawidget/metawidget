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
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
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
	// Private members
	//

	private PropertyStyle	mPropertyStyle;

	private ActionStyle		mActionStyle;

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

		// Fluent interface

		return this;
	}

	/**
	 * Gets the style used to recognize properties.
	 */

	PropertyStyle getPropertyStyle()
	{
		return mPropertyStyle;
	}

	/**
	 * Sets the style used to recognize actions.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseObjectInspectorConfig setActionStyle( ActionStyle actionStyle )
	{
		mActionStyle = actionStyle;

		// Fluent interface

		return this;
	}

	/**
	 * Gets the style used to recognize actions.
	 */

	ActionStyle getActionStyle()
	{
		return mActionStyle;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof BaseObjectInspectorConfig ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mPropertyStyle, ((BaseObjectInspectorConfig) that).mPropertyStyle ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mActionStyle, ((BaseObjectInspectorConfig) that).mActionStyle ))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = ObjectUtils.nullSafeHashCode( mPropertyStyle );
		hashCode ^= ObjectUtils.nullSafeHashCode( mActionStyle );

		return hashCode;
	}
}
