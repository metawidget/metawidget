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

package org.metawidget.inspector.jpa;

import org.metawidget.inspector.impl.AbstractPropertyInspectorConfig;

/**
 * Configures a JpaInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author Richard Kennard
 */

public class JpaInspectorConfig
	extends AbstractPropertyInspectorConfig
{
	//
	//
	// Private members
	//
	//

	private boolean	mHideIds	= true;

	//
	//
	// Public methods
	//
	//

	/**
	 * Whether the Inspector returns Id properties as <code>hidden="true"</code>. True by
	 * default.
	 * <p>
	 * JPA recommends using synthetic ids, so generally they don't appear in the UI.
	 */

	public boolean isHideIds()
	{
		return mHideIds;
	}

	public void setHideIds( boolean hideIds )
	{
		mHideIds = hideIds;
	}
}