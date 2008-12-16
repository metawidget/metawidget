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

package org.metawidget.swing.layout;

import java.awt.Component;
import java.util.Map;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;

/**
 * Layout to simply output components one after another, with no labels and no structure,
 * using <code>javax.awt.FlowLayout</code>.
 *
 * @author Richard Kennard
 */

public class FlowLayout
	extends LayoutImpl
{
	//
	// Private members
	//

	private java.awt.FlowLayout		mLayout;

	//
	// Constructor
	//

	public FlowLayout( SwingMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	@Override
	public void layoutBegin()
	{
		mLayout = new java.awt.FlowLayout();
		getMetawidget().setLayout( mLayout );
	}

	public void layoutChild( Component component, Map<String, String> attributes )
	{
		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 )
			return;

		// Add to the Metawidget

		getMetawidget().add( component );
	}
}
