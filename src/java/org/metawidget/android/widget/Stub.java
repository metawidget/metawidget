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

import java.util.Collections;
import java.util.Map;

import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Stub for Android environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress widget
 * creation entirely or to create child widgets with a different tag. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 * <p>
 * Note: this class extends <code>LinearLayout</code> rather than <code>FrameLayout</code>,
 * because <code>FrameLayout</code> would <em>always</em> need to have another
 * <code>Layout</code> embedded within it, whereas <code>LinearLayout</code> is occasionally useful directly.
 *
 * @author Richard Kennard
 */

public class Stub
	extends LinearLayout
{
	//
	//
	// Private statics
	//
	//

	private final static String						ATTRIBUTE_PREFIX		= "attrib";

	//
	//
	// Private members
	//
	//

	private Map<String, String>	mAttributes;

	//
	//
	// Constructor
	//
	//

	public Stub( Context context )
	{
		super( context );
	}

	@SuppressWarnings( "unchecked" )
	public Stub( Context context, AttributeSet attributes, Map inflateParams )
	{
		super( context, attributes, inflateParams );

		setTag( attributes.getAttributeValue( null, "tag" ) );

		// For each attribute...

		for ( int loop = 0, length = attributes.getAttributeCount(); loop < length; loop++ )
		{
			// ...that looks like a stub attribute...

			String name = attributes.getAttributeName( loop );

			if ( !name.startsWith( ATTRIBUTE_PREFIX ) )
				continue;

			name = name.substring( ATTRIBUTE_PREFIX.length() );

			if ( !StringUtils.isFirstLetterUppercase( name ) )
				continue;

			// ...remember it

			String value = attributes.getAttributeValue( loop );
			setAttribute( StringUtils.lowercaseFirstLetter( name ), value );
		}
	}

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
