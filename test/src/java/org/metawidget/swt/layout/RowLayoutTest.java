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

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;

/**
 * @author Richard Kennard
 */

public class RowLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testLayout()
		throws Exception
	{
		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		Composite composite = new Composite( metawidget, SWT.NONE );

		// startLayout

		RowLayout rowLayout = new RowLayout();
		rowLayout.startContainerLayout( composite, metawidget );

		assertTrue( composite.getLayout() instanceof org.eclipse.swt.layout.RowLayout );
		assertFalse( ( metawidget.getLayout() instanceof org.eclipse.swt.layout.RowLayout ));

		Stub stub = new Stub( composite, SWT.NONE );
		rowLayout.layoutWidget( stub, PROPERTY, null, composite, metawidget );
		assertTrue( ( (RowData) stub.getLayoutData() ).exclude );
	}
}
