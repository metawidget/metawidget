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
