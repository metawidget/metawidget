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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.TestUtils;

/**
 * @author Richard Kennard
 */

public class TabFolderLayoutDecoratorTest
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
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new GridLayout() ) ) ) ) );
		metawidget.setToInspect( new Baz() );

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
		TestUtils.testEqualsAndHashcode( TabFolderLayoutDecoratorConfig.class, new TabFolderLayoutDecoratorConfig()
		{
			// Subclass
		} );
	}

	public void testNestedTabs()
	{
		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setMetawidgetLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new GridLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertEquals( "Abc:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );

		TabFolder outerTabFolder = (TabFolder) metawidget.getChildren()[2];
		assertEquals( "Foo", outerTabFolder.getItem( 0 ).getText() );
		Composite outerPanel = (Composite) outerTabFolder.getChildren()[0];
		assertTrue( 4 == outerPanel.getChildren().length );

		TabFolder innerTabFolder = (TabFolder) outerPanel.getChildren()[0];
		assertEquals( "Bar", innerTabFolder.getItem( 0 ).getText() );
		Composite barComposite = (Composite) innerTabFolder.getChildren()[0];
		assertEquals( "Def:", ( (Label) barComposite.getChildren()[0] ).getText() );
		assertTrue( barComposite.getChildren()[1] instanceof Button );
		assertTrue( ( barComposite.getChildren()[1].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertEquals( "Ghi:", ( (Label) barComposite.getChildren()[2] ).getText() );
		assertTrue( ( barComposite.getChildren()[3].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( barComposite.getChildren()[3].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( barComposite.getChildren()[3].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( barComposite.getChildren()[3].getStyle() & SWT.WRAP ) == SWT.WRAP );

		assertTrue( 4 == barComposite.getChildren().length );

		assertEquals( "Baz", innerTabFolder.getItem( 1 ).getText() );
		Composite bazComposite = (Composite) innerTabFolder.getChildren()[1];
		assertEquals( "Jkl:", ( (Label) bazComposite.getChildren()[0] ).getText() );
		assertTrue( bazComposite.getChildren()[1] instanceof Text );
		assertTrue( 2 == bazComposite.getChildren().length );

		assertEquals( "Mno:", ( (Label) outerPanel.getChildren()[1] ).getText() );
		assertTrue( outerPanel.getChildren()[2] instanceof Button );
		assertTrue( ( outerPanel.getChildren()[2].getStyle() & SWT.CHECK ) == SWT.CHECK );

		innerTabFolder = (TabFolder) outerPanel.getChildren()[3];
		assertEquals( "Moo", innerTabFolder.getItem( 0 ).getText() );
		Composite mooComposite = (Composite) innerTabFolder.getChildren()[0];
		assertEquals( "Pqr:", ( (Label) mooComposite.getChildren()[0] ).getText() );
		assertTrue( mooComposite.getChildren()[1] instanceof Text );
		assertTrue( 2 == mooComposite.getChildren().length );

		assertEquals( "Stu:", ( (Label) metawidget.getChildren()[3] ).getText() );
		assertTrue( metawidget.getChildren()[4] instanceof Text );
		assertTrue( 5 == metawidget.getChildren().length );

		// Test components within nested tabs still accessible by name

		assertTrue( metawidget.getChildren()[1] == metawidget.getControl( "abc" ) );
		assertTrue( barComposite.getChildren()[1] == metawidget.getControl( "def" ) );
		assertTrue( barComposite.getChildren()[3] == metawidget.getControl( "ghi" ) );
		assertTrue( bazComposite.getChildren()[1] == metawidget.getControl( "jkl" ) );
		assertTrue( outerPanel.getChildren()[2] == metawidget.getControl( "mno" ) );
		assertTrue( mooComposite.getChildren()[1] == metawidget.getControl( "pqr" ) );
		assertTrue( metawidget.getChildren()[4] == metawidget.getControl( "stu" ) );
	}

	public void testFlatSectionAroundNestedSectionLayoutDecorator()
	{
		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new GridLayout() ) ) ) ) );
		metawidget.setToInspect( new Baz() );

		Composite composite = (Composite) metawidget.getChildren()[0];
		assertTrue( ((org.eclipse.swt.layout.GridLayout) composite.getLayout() ).marginWidth == 0 );
		assertEquals( "Foo", ( (Label) composite.getChildren()[0] ).getText() );
		assertTrue( ( composite.getChildren()[1].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );

		TabFolder innerTabFolder = (TabFolder) metawidget.getChildren()[1];
		assertEquals( "Bar", innerTabFolder.getItem( 0 ).getText() );
		Composite innerPanel = (Composite) innerTabFolder.getChildren()[0];
		assertEquals( "Abc:", ( (Label) innerPanel.getChildren()[0] ).getText() );
		assertTrue( innerPanel.getChildren()[1] instanceof Text );
		assertTrue( 2 == innerPanel.getChildren().length );

		composite = (Composite) metawidget.getChildren()[2];
		assertTrue( ((org.eclipse.swt.layout.GridLayout) composite.getLayout() ).marginWidth == 0 );
		assertEquals( "Baz", ( (Label) composite.getChildren()[0] ).getText() );
		assertTrue( ( composite.getChildren()[1].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );

		assertEquals( "Def:", ( (Label) metawidget.getChildren()[3] ).getText() );
		assertTrue( metawidget.getChildren()[4] instanceof Button );
		assertTrue( ( metawidget.getChildren()[4].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertTrue( 5 == metawidget.getChildren().length );
	}

	//
	// Inner class
	//

	static class Foo
	{
		@UiSection( "Section" )
		public String	bar;
	}

	static class Bar
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

		@UiSection( "" )
		public String	stu;
	}

	static class Baz
	{
		@UiSection( { "Foo", "Bar" } )
		public String	abc;

		@UiSection( { "Baz" } )
		public boolean	def;
	}
}
