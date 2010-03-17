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

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.annotation.UiWide;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.TestUtils;

/**
 * @author Richard Kennard
 */

public class GridLayoutTest
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
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setMetawidgetLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new GridLayout( new GridLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );
		metawidget.setToInspect( new Foo() );

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() )
		{
			if ( !display.readAndDispatch() )
				display.sleep();
		}

		display.dispose();
	}

	//
	// Public methods
	//

	public void testConfig()
	{
		Map<Class<?>, Object> dummyTypes = CollectionUtils.newHashMap();
		dummyTypes.put( Font.class, new Font( SwtMetawidgetTests.TEST_DISPLAY, "SansSerif", 12, SWT.NONE ) );
		dummyTypes.put( Color.class, new Color( SwtMetawidgetTests.TEST_DISPLAY, 255, 0, 0 ) );

		TestUtils.testEqualsAndHashcode( GridLayoutConfig.class, new GridLayoutConfig()
		{
			// Subclass
		}, dummyTypes );
	}

	public void testLayout()
		throws Exception
	{
		// Without stub

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		try
		{
			metawidget.setMetawidgetLayout( new GridLayout( new GridLayoutConfig().setNumberOfColumns( 0 ) ) );
			assertTrue( false );
		}
		catch ( LayoutException e )
		{
			assertTrue( "numberOfColumns must be >= 1".equals( e.getMessage() ) );
		}

		metawidget.setMetawidgetLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new GridLayout( new GridLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );

		assertTrue( "Abc:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );
		assertTrue( "abc_label".equals( metawidget.getChildren()[0].getData( "name" ) ) );
		assertTrue( metawidget.getControl( "abc_label" ) == metawidget.getChildren()[0] );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertTrue( "Def*:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );

		assertTrue( metawidget.getChildren()[3] instanceof Spinner );
		assertTrue( "Ghi:".equals( ( (Label) metawidget.getChildren()[4] ).getText() ) );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );

		// TabFolder

		TabFolder tabbedPane = (TabFolder) metawidget.getChildren()[6];
		assertTrue( 3 == tabbedPane.getItemCount() );
		assertTrue( SWT.FILL == ( (GridData) tabbedPane.getLayoutData() ).horizontalAlignment );
		assertTrue( SWT.FILL == ( (GridData) tabbedPane.getLayoutData() ).verticalAlignment );
		assertTrue( ( (GridData) tabbedPane.getLayoutData() ).grabExcessHorizontalSpace );
		assertTrue( ( (GridData) tabbedPane.getLayoutData() ).grabExcessVerticalSpace );

		assertTrue( "tab1".equals( tabbedPane.getItem( 0 ).getText() ) );
		Composite tabPanel = (Composite) tabbedPane.getChildren()[0];
		assertTrue( "Tab 1_jkl:".equals( ( (Label) tabPanel.getChildren()[0] ).getText() ) );
		assertTrue( tabPanel.getChildren()[1] instanceof Label );
		assertTrue( "Tab 1_mno:".equals( ( (Label) tabPanel.getChildren()[2] ).getText() ) );
		assertTrue( tabPanel.getChildren()[3] instanceof Combo );
		assertTrue( "Tab 1_pqr:".equals( ( (Label) tabPanel.getChildren()[4] ).getText() ) );
		assertTrue( tabPanel.getChildren()[5] instanceof Text );
		assertTrue( 6 == tabPanel.getChildren().length );

		assertTrue( "tab2".equals( tabbedPane.getItem( 1 ).getText() ) );
		tabPanel = (Composite) tabbedPane.getChildren()[1];
		assertTrue( tabPanel.getChildren()[0] instanceof Text );
		assertTrue( ( tabPanel.getChildren()[0].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( 2 == ( (GridData) tabbedPane.getChildren()[0].getLayoutData() ).horizontalSpan );
		assertTrue( 1 == tabPanel.getChildren().length );

		assertTrue( "tab3".equals( tabbedPane.getItem( 2 ).getText() ) );
		tabPanel = (Composite) tabbedPane.getChildren()[2];
		assertTrue( tabPanel.getChildren()[0] instanceof Text );
		assertTrue( 2 == ( (GridData) tabbedPane.getChildren()[0].getLayoutData() ).horizontalSpan );
		assertTrue( "Tab 3_mno:".equals( ( (Label) tabPanel.getChildren()[1] ).getText() ) );
		assertTrue( tabPanel.getChildren()[2] instanceof Text );
		assertTrue( "Tab 3_pqr:".equals( ( (Label) tabPanel.getChildren()[3] ).getText() ) );
		assertTrue( tabPanel.getChildren()[4] instanceof Text );
		assertTrue( 5 == tabPanel.getChildren().length );

		assertTrue( "Mno:".equals( ( (Label) metawidget.getChildren()[7] ).getText() ) );
		assertTrue( metawidget.getChildren()[8] instanceof Text );

		// With stub

		Stub stub = new Stub( metawidget, SWT.NONE );
		stub.setData( "name", "mno" );

		// With stub attributes

		Stub stubWithAttributes = new Stub( metawidget, SWT.NONE );
		stubWithAttributes.setData( "name", "def" );
		new Spinner( stubWithAttributes, SWT.NONE );
		stubWithAttributes.setAttribute( "large", "true" );

		// With an arbitrary component

		Spinner arbitrary = new Spinner( metawidget, SWT.NONE );

		// With an arbirary stub with attributes

		Stub arbitraryStubWithAttributes = new Stub( metawidget, SWT.NONE );
		new Text( arbitraryStubWithAttributes, SWT.NONE );
		arbitraryStubWithAttributes.setAttribute( "label", "" );
		arbitraryStubWithAttributes.setAttribute( "large", "true" );

		assertTrue( "Abc:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );

		assertTrue( metawidget.getChildren()[1] instanceof Text );

		assertTrue( "Def*:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
		assertTrue( metawidget.getChildren()[3] instanceof Stub );
		assertTrue( ( (Stub) metawidget.getChildren()[3] ).getChildren()[0] instanceof Spinner );
		assertTrue( 2 == ( (GridData) ( (Stub) metawidget.getChildren()[3] ).getChildren()[0].getLayoutData() ).horizontalSpan );

		assertTrue( "Ghi:".equals( ( (Label) metawidget.getChildren()[4] ).getText() ) );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );

		assertTrue( metawidget.getChildren()[6] instanceof TabFolder );
		assertTrue( arbitrary.equals( metawidget.getChildren()[7] ) );
		assertTrue( arbitraryStubWithAttributes.equals( metawidget.getChildren()[8] ) );
		assertTrue( 2 == ( (GridData) metawidget.getLayoutData() ).horizontalSpan );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertTrue( "Def:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		public String	abc;

		@UiComesAfter( "abc" )
		@UiRequired
		public int		def;

		@UiComesAfter( "def" )
		public boolean	ghi;

		@UiSection( "tab1" )
		@UiComesAfter( "ghi" )
		@UiAttribute( name = "required", value = "true" )
		@UiReadOnly
		public String	tab1_jkl;

		@UiComesAfter( "tab1_jkl" )
		@UiLookup( { "foo", "bar" } )
		public String	tab1_mno;

		@UiComesAfter( "tab1_mno" )
		public String	tab1_pqr;

		@UiSection( "tab2" )
		@UiComesAfter( "tab1_pqr" )
		@UiLarge
		@UiLabel( "" )
		public String	tab2_jkl;

		@UiSection( "tab3" )
		@UiComesAfter( "tab2_jkl" )
		@UiLabel( "" )
		public String	tab3_jkl;

		@UiComesAfter( "tab3_jkl" )
		public String	tab3_mno;

		@UiComesAfter( "tab3_mno" )
		public String	tab3_pqr;

		@UiSection( "" )
		@UiComesAfter( "tab3_pqr" )
		public String	mno;
	}

	public static class WideFoo
	{
		public String	abc;

		@UiComesAfter( "abc" )
		public int		def;

		@UiWide
		@UiComesAfter( "def" )
		public String	ghi;

		@UiComesAfter( "ghi" )
		public boolean	jkl;

		@UiComesAfter( "jkl" )
		public String	mno;
	}

	public static class RequiredFoo
	{
		@UiRequired
		public String	abc;

		@UiComesAfter( "abc" )
		@UiRequired
		public int		def;

		@UiComesAfter( "def" )
		@UiRequired
		@UiLarge
		public String	ghi;

		@UiComesAfter( "ghi" )
		@UiRequired
		public String	jkl;

		@UiComesAfter( "jkl" )
		@UiRequired
		@UiLabel( "" )
		public String	mno;
	}
}
