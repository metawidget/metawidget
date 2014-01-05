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

package org.metawidget.swing.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FlowLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLayout()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		JComponent container = new JPanel();

		// startLayout

		FlowLayout flowLayout = new FlowLayout();
		flowLayout.startContainerLayout( container, metawidget );

		assertTrue( container.getLayout() instanceof java.awt.FlowLayout );
		assertFalse( ( metawidget.getLayout() instanceof java.awt.FlowLayout ) );

		// layoutWidget

		assertEquals( 0, container.getComponentCount() );

		Stub stub = new Stub();
		flowLayout.layoutWidget( stub, PROPERTY, null, container, metawidget );
		assertEquals( 0, container.getComponentCount() );

		stub.add( new JSpinner() );
		flowLayout.layoutWidget( stub, PROPERTY, null, container, metawidget );
		assertEquals( stub, container.getComponent( 0 ) );
		assertEquals( 1, container.getComponentCount() );

		flowLayout.layoutWidget( new JTextField(), PROPERTY, null, container, metawidget );
		assertTrue( container.getComponent( 1 ) instanceof JTextField );
		assertEquals( 2, container.getComponentCount() );
	}
}
