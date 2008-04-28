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

package org.metawidget.swing;

import java.util.Collections;
import java.util.Map;

import javax.swing.JPanel;

import org.metawidget.util.CollectionUtils;

/**
 * Stub for Swing environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress component
 * creation entirely or to create child components with a different name. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 *
 * @author Richard Kennard
 */

public class Stub
	extends JPanel
{
	//
	//
	// Private statics
	//
	//

	private final static long	serialVersionUID	= -2103016926514513436L;

	//
	//
	// Private members
	//
	//

	private Map<String, String>	mAttributes;

	//
	//
	// Public methods
	//
	//

	public void setAttribute( String name, String value )
	{
		if ( mAttributes == null )
			mAttributes = CollectionUtils.newHashMap();

		mAttributes.put( name, value );
	}

	public Map<String, String> getAttributes()
	{
		if ( mAttributes == null )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, String> empty = Collections.EMPTY_MAP;
			return empty;
		}

		return mAttributes;
	}
}
