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

package org.metawidget.inspector.impl;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.actionstyle.metawidget.MetawidgetActionStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;

/**
 * @author Richard Kennard
 */

public class BaseObjectInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		BaseObjectInspectorConfig config1 = new BaseObjectInspectorConfig();
		assertTrue( config1.getPropertyStyle() instanceof JavaBeanPropertyStyle );
		assertTrue( config1.getActionStyle() instanceof MetawidgetActionStyle );
		config1.setPropertyStyle( null );
		assertTrue( config1.getPropertyStyle() == null );
		config1.setActionStyle( null );
		assertTrue( config1.getActionStyle() == null );

		config1 = new BaseObjectInspectorConfig();
		BaseObjectInspectorConfig config2 = new BaseObjectInspectorConfig();
		assertTrue( config2.getPropertyStyle() == config1.getPropertyStyle() );
		assertTrue( config2.getActionStyle() == config2.getActionStyle() );
	}
}
