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

package org.metawidget.swt.layout;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FillLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLayout()
		throws Exception {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		Composite composite = new Composite( metawidget, SWT.NONE );

		// startLayout

		FillLayout fillLayout = new FillLayout();
		fillLayout.startContainerLayout( composite, metawidget );

		assertTrue( composite.getLayout() instanceof org.eclipse.swt.layout.FillLayout );
		assertFalse( ( metawidget.getLayout() instanceof org.eclipse.swt.layout.FillLayout ) );
	}
}
