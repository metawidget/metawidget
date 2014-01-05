// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.swing;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;

/**
 * BeanInfo for SwingMetawidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingMetawidgetBeanInfo
	extends SimpleBeanInfo {

	//
	// Public methods
	//

	@Override
	public BeanDescriptor getBeanDescriptor() {

		BeanDescriptor descriptor = new BeanDescriptor( SwingMetawidget.class );
		descriptor.setDisplayName( "Metawidget" );
		descriptor.setShortDescription( "org.metawidget.swing.SwingMetawidget - http://metawidget.org" );

		return descriptor;
	}

	@Override
	public Image getIcon( int kind ) {

		switch ( kind ) {
			case BeanInfo.ICON_MONO_16x16:
				return loadImage( "icon/mono-16x16.gif" );

			case BeanInfo.ICON_COLOR_16x16:
				return loadImage( "icon/color-16x16.gif" );

			case BeanInfo.ICON_MONO_32x32:
				return loadImage( "icon/mono-32x32.gif" );

			default:
				return loadImage( "icon/color-32x32.gif" );
		}
	}
}
