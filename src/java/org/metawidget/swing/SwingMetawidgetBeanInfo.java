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

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;

/**
 * BeanInfo for SwingMetawidget.
 *
 * @author Richard Kennard
 */

public class SwingMetawidgetBeanInfo
	extends SimpleBeanInfo
{
	//
	//
	// Public methods
	//
	//

	@Override
	public BeanDescriptor getBeanDescriptor()
	{
		BeanDescriptor descriptor = new BeanDescriptor( SwingMetawidget.class );
		descriptor.setDisplayName( "Metawidget" );
		descriptor.setShortDescription( "org.metawidget.swing.SwingMetawidget\r\nSee http://www.metawidget.org" );

		return descriptor;
	}

	@Override
	public Image getIcon( int kind )
	{
		switch( kind )
		{
			case BeanInfo.ICON_MONO_16x16:
				return loadImage( "icon/mono-16x16.gif" );

			case BeanInfo.ICON_COLOR_16x16:
				return loadImage( "icon/color-16x16.gif" );

			case BeanInfo.ICON_MONO_32x32:
				return loadImage( "icon/mono-32x32.gif" );

			case BeanInfo.ICON_COLOR_32x32:
				return loadImage( "icon/color-32x32.gif" );
		}

		return null;
	}
}
