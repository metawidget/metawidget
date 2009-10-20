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

package org.metawidget.inspector.composite;

import org.metawidget.config.ConfigReader;
import org.metawidget.config.NeedsResourceResolver;
import org.metawidget.config.ResourceResolver;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a CompositeInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author Richard Kennard
 */

public class ValidatingCompositeInspectorConfig
	extends CompositeInspectorConfig
	implements NeedsResourceResolver
{
	//
	// Private members
	//

	private ResourceResolver	mResourceResolver;

	//
	// Public methods
	//

	public ResourceResolver getResourceResolver()
	{
		if ( mResourceResolver == null )
			mResourceResolver = new ConfigReader();

		return mResourceResolver;
	}

	@Override
	public void setResourceResolver( ResourceResolver resourceResolver )
	{
		mResourceResolver = resourceResolver;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( !( that instanceof ValidatingCompositeInspectorConfig ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mResourceResolver, ((ValidatingCompositeInspectorConfig) that).mResourceResolver ))
			return false;

		return super.equals( that );
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode ^= ObjectUtils.nullSafeHashCode( mResourceResolver );

		return hashCode;
	}
}
