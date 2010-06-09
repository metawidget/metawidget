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

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.TestUtils;

/**
 * @author Richard Kennard
 */

public class SeparatorLayoutDecoratorTest
	extends TestCase
{
	//
	// Public statics
	//

	public static void main( String[] args )
	{
		Display display = new Display();
		Shell shell = new Shell( display, SWT.DIALOG_TRIM | SWT.RESIZE );
		shell.setLayout( new FillLayout() );

		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new GridLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() )
		{
			if ( !display.readAndDispatch() )
			{
				display.sleep();
			}
		}

		display.dispose();
	}

	//
	// Public methods
	//

	public void testConfig()
	{
		TestUtils.testEqualsAndHashcode( SeparatorLayoutDecoratorConfig.class, new SeparatorLayoutDecoratorConfig()
		{
			// Subclass
		} );
	}

	public void testAlignment()
	{
		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new Foo() );

		Composite composite = (Composite) metawidget.getChildren()[ 0 ];
		assertTrue( ((org.eclipse.swt.layout.GridLayout) composite.getLayout() ).marginWidth == 0 );
		assertEquals( "Section", ( (Label) composite.getChildren()[ 0 ] ).getText() );
		assertTrue( composite.getChildren()[ 1 ] instanceof Label );
		assertTrue( ( composite.getChildren()[1].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );
		assertEquals( "Bar:", ( (Label) metawidget.getChildren()[ 1 ] ).getText() );
		assertTrue( metawidget.getChildren()[ 2 ] instanceof Text );
		assertTrue( 3 == metawidget.getChildren().length );

		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setAlignment( SWT.RIGHT ).setLayout( new GridLayout() ) ) );
		composite = (Composite) metawidget.getChildren()[ 0 ];
		assertTrue( ((org.eclipse.swt.layout.GridLayout) composite.getLayout() ).marginWidth == 0 );
		assertEquals( "Section", ( (Label) composite.getChildren()[ 1 ] ).getText() );
		assertTrue( composite.getChildren()[ 0 ] instanceof Label );
		assertTrue( ( composite.getChildren()[0].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );
		assertEquals( "Bar:", ( (Label) metawidget.getChildren()[ 1 ] ).getText() );
		assertTrue( metawidget.getChildren()[ 2] instanceof Text );
		assertTrue( 3 == metawidget.getChildren().length );
	}

	public void testNestedSeparators()
	{
		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new GridLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertEquals( "Abc:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );

		Composite outerSeparator = (Composite) metawidget.getChildren()[2];
		assertEquals( "Foo", ( (Label) outerSeparator.getChildren()[0] ).getText() );

		Composite innerSeparator = (Composite) metawidget.getChildren()[3];
		assertEquals( "Bar", ( (Label) innerSeparator.getChildren()[0] ).getText() );
		assertEquals( "Def:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertEquals( "Ghi:", ( (Label) metawidget.getChildren()[6] ).getText() );
		assertTrue( metawidget.getChildren()[7] instanceof Text );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.WRAP ) == SWT.WRAP );

		innerSeparator = (Composite) metawidget.getChildren()[8];
		assertEquals( "Baz", ( (Label) innerSeparator.getChildren()[0] ).getText() );
		assertEquals( "Jkl:", ( (Label) metawidget.getChildren()[9] ).getText() );
		assertTrue( metawidget.getChildren()[10] instanceof Text );

		assertEquals( "Mno:", ( (Label) metawidget.getChildren()[11] ).getText() );
		assertTrue( metawidget.getChildren()[12] instanceof Button );
		assertTrue( ( metawidget.getChildren()[12].getStyle() & SWT.CHECK ) == SWT.CHECK );

		innerSeparator = (Composite) metawidget.getChildren()[13];
		assertEquals( "Moo", ( (Label) innerSeparator.getChildren()[0] ).getText() );
		assertEquals( "Pqr:", ( (Label) metawidget.getChildren()[14] ).getText() );
		assertTrue( metawidget.getChildren()[15] instanceof Text );

		innerSeparator = (Composite) metawidget.getChildren()[16];
		assertEquals( "Zoo", ( (Label) innerSeparator.getChildren()[0] ).getText() );
		assertEquals( "Zoo:", ( (Label) metawidget.getChildren()[17] ).getText() );
		SwtMetawidget nestedMetawidget = (SwtMetawidget) metawidget.getChildren()[18];
		assertEquals( "Name:", ( (Label) nestedMetawidget.getChildren()[0] ).getText() );

		assertEquals( "Stu:", ( (Label) metawidget.getChildren()[19] ).getText() );
		assertTrue( metawidget.getChildren()[20] instanceof Text );
		assertTrue( 21 == metawidget.getChildren().length );
	}

	//
	// Inner class
	//

	static class Foo
	{
		@UiSection( "Section" )
		public String	bar;
	}

	public static class Bar
	{
		public String	abc;

		@UiSection( { "Foo", "Bar" } )
		public boolean	def;

		@UiLarge
		public String	ghi;

		@UiSection( { "Foo", "Baz" } )
		public String	jkl;

		@UiSection( { "Foo", "" } )
		public boolean	mno;

		@UiSection( { "Foo", "Moo" } )
		public String	pqr;

		@UiSection( { "Foo", "Zoo" } )
		public Zoo		zoo	= new Zoo();

		@UiSection( "" )
		@UiComesAfter( "zoo" )
		public String	stu;
	}

	public static class Zoo
	{
		public String	name;
	}
}
