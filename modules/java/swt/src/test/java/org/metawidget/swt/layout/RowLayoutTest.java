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

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RowLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unused" )
	public void testLayout()
		throws Exception {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		Composite composite = new Composite( metawidget, SWT.NONE );

		// startLayout

		RowLayout rowLayout = new RowLayout();
		rowLayout.startContainerLayout( composite, metawidget );

		assertTrue( composite.getLayout() instanceof org.eclipse.swt.layout.RowLayout );
		assertFalse( ( metawidget.getLayout() instanceof org.eclipse.swt.layout.RowLayout ) );
		composite.dispose();

		// layoutWidget

		assertEquals( 0, metawidget.getChildren().length );

		metawidget.setMetawidgetLayout( rowLayout );
		Stub stub = new Stub( metawidget, SWT.NONE );
		metawidget.getChildren();
		assertTrue( ( (RowData) stub.getLayoutData() ).exclude );
		assertEquals( 1, metawidget.getChildren().length );

		Spinner spinner = new Spinner( stub, SWT.NONE );
		metawidget.setToInspect( null );
		metawidget.getChildren();
		assertEquals( null, stub.getLayoutData() );
		assertEquals( 1, metawidget.getChildren().length );

		rowLayout.layoutWidget( new Text( metawidget, SWT.NONE ), PROPERTY, null, composite, metawidget );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( 2, metawidget.getChildren().length );
	}
}
