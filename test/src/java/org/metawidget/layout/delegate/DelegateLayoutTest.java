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

package org.metawidget.layout.delegate;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class DelegateLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testDelegateLayout()
		throws Exception
	{
		try
		{
			new DelegateLayout<JComponent, SwingMetawidget>( new DelegateLayoutConfig<JComponent, SwingMetawidget>() )
			{
				// Just a DelegateLayout
			};
		}
		catch( LayoutException e )
		{
			assertTrue( "org.metawidget.layout.delegate.DelegateLayoutTest$1 needs a Layout to delegate to (use org.metawidget.layout.delegate.DelegateLayoutConfig.setLayout)".equals( e.getMessage() ));
		}
	}
}
