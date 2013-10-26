// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.layout.decorator;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( LayoutDecoratorConfig.class, new LayoutDecoratorConfig<Object, Object, Object>() {
			// Subclass
		} );
	}

	@SuppressWarnings( "unused" )
	public void testLayoutDecorator()
		throws Exception {

		try {
			new LayoutDecorator<JComponent, JComponent, SwingMetawidget>( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>() ) {
				// Just a LayoutDecorator
			};
		} catch ( LayoutException e ) {
			assertEquals( "org.metawidget.layout.decorator.LayoutDecoratorTest$2 needs a Layout to decorate (use org.metawidget.layout.decorator.LayoutDecoratorConfig.setLayout)", e.getMessage() );
		}
	}
}
