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

package org.metawidget.swing.widgetbuilder;

import java.util.Map;

import javax.swing.JLabel;

/**
 * Label whose values use a lookup.
 *
 * @author Richard Kennard
 */

public class LookupLabel
	extends JLabel
{
	//
	// Private statics
	//

	private final static long	serialVersionUID	= 1l;

	//
	// Private members
	//

	private Map<String, String>	mLookup;

	//
	// Constructor
	//

	public LookupLabel( Map<String, String> lookup )
	{
		if ( lookup == null )
		{
			throw new NullPointerException( "lookup" );
		}

		mLookup = lookup;
	}

	//
	// Public methods
	//

	@Override
	public void setText( String text )
	{
		String lookup = text;

		if ( lookup != null && mLookup != null )
		{
			lookup = mLookup.get( lookup );
		}

		super.setText( lookup );
	}
}

