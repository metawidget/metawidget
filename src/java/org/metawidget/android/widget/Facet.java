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

package org.metawidget.android.widget;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Facet for Android environments.
 * <p>
 * Facets differ from Stubs in that Stubs override widget creation, whereas Facets are
 * 'decorations' (such as button bars) to be recognized and arranged at the discretion of the
 * Layout.
 * <p>
 * Note: this class extends <code>LinearLayout</code> rather than <code>FrameLayout</code>,
 * because <code>FrameLayout</code> would <em>always</em> need to have another
 * <code>Layout</code> embedded within it, whereas <code>LinearLayout</code> is occasionally useful directly.
 *
 * @author Richard Kennard
 */

public class Facet
	extends LinearLayout
{
	//
	//
	// Private members
	//
	//

	private String	mName;

	//
	//
	// Constructor
	//
	//

	public Facet( Context context )
	{
		super( context );
	}

	@SuppressWarnings( "unchecked" )
	public Facet( Context context, AttributeSet attributes, Map inflateParams )
	{
		super( context, attributes, inflateParams );

		mName = attributes.getAttributeValue( null, "name" );
	}

	//
	//
	// Public methods
	//
	//

	public String getName()
	{
		return mName;
	}

	public void setName( String name )
	{
		mName = name;
	}
}
