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

package org.metawidget.swt.layout;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.TestUtils;

/**
 * @author Richard Kennard
 */

public class GridLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		Map<Class<?>, Object> dummyTypes = CollectionUtils.newHashMap();
		dummyTypes.put( Font.class, new Font( SwtMetawidgetTests.TEST_DISPLAY, "SansSerif", 12, SWT.NONE ) );
		dummyTypes.put( Color.class, new Color( SwtMetawidgetTests.TEST_DISPLAY, 255, 0, 0 ) );

		TestUtils.testEqualsAndHashcode( GridLayoutConfig.class, new GridLayoutConfig()
		{
			// Subclass
		}, dummyTypes );
	}
}
