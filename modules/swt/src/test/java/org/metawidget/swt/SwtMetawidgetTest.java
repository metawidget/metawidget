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

package org.metawidget.swt;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Richard Kennard
 */

public class SwtMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testControlMoved()
		throws Exception {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setConfig( "org/metawidget/swt/metawidget.xml" );
		metawidget.setToInspect( new Foo() );
		assertEquals( "Bar:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( "Baz:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );
		assertEquals( 4, metawidget.getChildren().length );

		// Fire controlMoved

		metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new Foo() );
		metawidget.setConfig( "org/metawidget/swt/metawidget.xml" );
		//metawidget.setBounds( 10, 10, metawidget.getBounds().width, metawidget.getBounds().height );

		assertEquals( "Bar:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( "Baz:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );
		assertEquals( 4, metawidget.getChildren().length );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String	bar;

		public String	baz;
	}
}
