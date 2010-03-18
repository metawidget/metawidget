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

package org.metawidget.inspector.faces;

import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a FacesInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author Richard Kennard
 */

public class FacesInspectorConfig
	extends BaseObjectInspectorConfig
{
	//
	// Private members
	//

	private boolean	mInjectThis;

	//
	// Public methods
	//

	/**
	 * Whether the Inspector injects a request-level 'this' attribute into
	 * <code>UiFacesAttribute</code> evaluations. False by default.
	 */

	public boolean isInjectThis()
	{
		return mInjectThis;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FacesInspectorConfig setInjectThis( boolean injectThis )
	{
		mInjectThis = injectThis;

		// Fluent interface

		return this;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( this == that )
			return true;

		if ( that == null )
			return false;

		if ( getClass() != that.getClass() )
			return false;

		if ( mInjectThis != ( (FacesInspectorConfig) that ).mInjectThis )
			return false;

		return super.equals( that );
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInjectThis );

		return hashCode;
	}
}