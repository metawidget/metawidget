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
import org.eclipse.swt.widgets.Control;
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
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author Richard Kennard
 */

public class GridLayoutTest
	extends TestCase {

	//
	// Public statics
	//

	public static void main( String[] args ) {

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

		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}

		display.dispose();
	}

	//
	// Constructor
	//

	public GridLayoutTest( String name ) {

		super( name );
	}

	//
	// Public methods
	//

	@SuppressWarnings( "unused" )
	public void testLayout()
		throws Exception {

		// Without stub

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		try {
			metawidget.setMetawidgetLayout( new GridLayout( new GridLayoutConfig().setNumberOfColumns( 0 ) ) );
			assertTrue( false );
		} catch ( LayoutException e ) {
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
		assertEquals( 3, ( (GridData) metawidget.getChildren()[5].getLayoutData() ).horizontalSpan );

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
		assertTrue( "".equals( ( (Label) tabPanel.getChildren()[1] ).getText() ) );
		assertTrue( "Tab 1_mno:".equals( ( (Label) tabPanel.getChildren()[2] ).getText() ) );
		assertTrue( tabPanel.getChildren()[3] instanceof Combo );
		assertTrue( "Tab 1_pqr:".equals( ( (Label) tabPanel.getChildren()[4] ).getText() ) );
		assertTrue( tabPanel.getChildren()[5] instanceof Text );
		assertTrue( 6 == tabPanel.getChildren().length );

		assertTrue( "tab2".equals( tabbedPane.getItem( 1 ).getText() ) );
		tabPanel = (Composite) tabbedPane.getChildren()[1];
		assertTrue( tabPanel.getChildren()[0] instanceof Text );
		assertTrue( ( tabPanel.getChildren()[0].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( 4 == ( (GridData) tabPanel.getChildren()[0].getLayoutData() ).horizontalSpan );
		assertTrue( 1 == tabPanel.getChildren().length );

		assertTrue( "tab3".equals( tabbedPane.getItem( 2 ).getText() ) );
		tabPanel = (Composite) tabbedPane.getChildren()[2];
		assertTrue( tabPanel.getChildren()[0] instanceof Text );
		assertTrue( 2 == ( (GridData) tabPanel.getChildren()[0].getLayoutData() ).horizontalSpan );
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

		// With an arbitrary stub with attributes

		Stub arbitraryStubWithAttributes = new Stub( metawidget, SWT.NONE );
		new Text( arbitraryStubWithAttributes, SWT.NONE );
		arbitraryStubWithAttributes.setAttribute( "label", "" );
		arbitraryStubWithAttributes.setAttribute( "large", "true" );

		metawidget.setToInspect( new Foo() );
		assertTrue( "Abc:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );

		assertTrue( metawidget.getChildren()[1] instanceof Text );

		assertTrue( "Def*:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
		assertTrue( metawidget.getChildren()[3] instanceof Stub );
		assertTrue( ( (Stub) metawidget.getChildren()[3] ).getChildren()[0] instanceof Spinner );
		assertTrue( 3 == ( (GridData) ( (Stub) metawidget.getChildren()[3] ).getLayoutData() ).horizontalSpan );

		assertTrue( "Ghi:".equals( ( (Label) metawidget.getChildren()[4] ).getText() ) );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );

		assertTrue( metawidget.getChildren()[6] instanceof TabFolder );
		assertTrue( stub.equals( metawidget.getChildren()[7] ) );
		assertTrue( arbitrary.equals( metawidget.getChildren()[8] ) );
		assertTrue( arbitraryStubWithAttributes.equals( metawidget.getChildren()[9] ) );
		assertTrue( 4 == ( (GridData) arbitraryStubWithAttributes.getLayoutData() ).horizontalSpan );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertTrue( "Def:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
	}

	public void testWide()
		throws Exception {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setMetawidgetLayout( new GridLayout( new GridLayoutConfig().setNumberOfColumns( 2 ).setLabelSuffix( ":" ) ) );
		metawidget.setToInspect( new WideFoo() );

		assertTrue( "Abc:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );
		assertTrue( metawidget.getChildren()[1] instanceof Text );

		assertTrue( "Def:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
		assertTrue( metawidget.getChildren()[3] instanceof Spinner );

		assertTrue( "Ghi:".equals( ( (Label) metawidget.getChildren()[4] ).getText() ) );
		assertTrue( metawidget.getChildren()[5] instanceof Text );
		assertTrue( 3 == ( (GridData) metawidget.getChildren()[5].getLayoutData() ).horizontalSpan );

		assertTrue( "Jkl:".equals( ( (Label) metawidget.getChildren()[6] ).getText() ) );
		assertTrue( metawidget.getChildren()[7] instanceof Button );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.CHECK ) == SWT.CHECK );

		assertTrue( "Mno:".equals( ( (Label) metawidget.getChildren()[8] ).getText() ) );
		assertTrue( metawidget.getChildren()[9] instanceof Text );
	}

	public void testLabelSuffix() {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new RequiredFoo() );

		// Different label suffix

		metawidget.setMetawidgetLayout( new GridLayout( new GridLayoutConfig().setLabelSuffix( "#" ) ) );
		assertTrue( "Abc*#".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );

		// Align left

		metawidget.setMetawidgetLayout( new GridLayout( new GridLayoutConfig().setRequiredAlignment( SWT.LEFT ).setRequiredText( "?" ) ) );
		assertTrue( "?Abc:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );

		// No suffix

		metawidget.setMetawidgetLayout( new GridLayout( new GridLayoutConfig().setLabelSuffix( null ).setRequiredText( null ) ) );
		assertTrue( "Abc".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );
	}

	public void testNonExcludedChildren() {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		Composite composite = new Composite( metawidget, SWT.NONE );

		GridLayout layout = new GridLayout( new GridLayoutConfig().setNumberOfColumns( 2 ) );
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// First some stubs...

		Control stub1 = new Stub( composite, SWT.BORDER );
		layout.layoutWidget( stub1, PROPERTY, attributes, composite, metawidget );
		assertTrue( ( (GridData) stub1.getLayoutData() ).exclude );

		Control stub2 = new Stub( composite, SWT.BORDER );
		layout.layoutWidget( stub2, PROPERTY, attributes, composite, metawidget );
		assertTrue( ( (GridData) stub2.getLayoutData() ).exclude );

		Control stub3 = new Stub( composite, SWT.BORDER );
		layout.layoutWidget( stub3, PROPERTY, attributes, composite, metawidget );
		assertTrue( ( (GridData) stub3.getLayoutData() ).exclude );

		// ...then a wide control...

		attributes.put( WIDE, TRUE );
		Control wideText1 = new Text( composite, SWT.BORDER );
		layout.layoutWidget( wideText1, PROPERTY, attributes, composite, metawidget );
		assertTrue( !( (GridData) wideText1.getLayoutData() ).exclude );
		assertEquals( 4, ( (GridData) wideText1.getLayoutData() ).horizontalSpan );

		// ...then another wide control

		Control wideText2 = new Text( composite, SWT.BORDER );
		layout.layoutWidget( wideText2, PROPERTY, attributes, composite, metawidget );

		assertTrue( !( (GridData) wideText2.getLayoutData() ).exclude );
		assertEquals( 4, ( (GridData) wideText2.getLayoutData() ).horizontalSpan );

		// ...should not have reset the first wide control back to 1

		assertEquals( 4, ( (GridData) wideText1.getLayoutData() ).horizontalSpan );

		// Then another stub...

		Control stub4 = new Stub( composite, SWT.BORDER );
		layout.layoutWidget( stub4, PROPERTY, attributes, composite, metawidget );
		assertTrue( ( (GridData) stub4.getLayoutData() ).exclude );

		// ...and another wide control...

		Control wideText3 = new Text( composite, SWT.BORDER );
		layout.layoutWidget( wideText3, PROPERTY, attributes, composite, metawidget );

		// ...should not have touched the horizontalSpan of the stubs

		assertTrue( !( (GridData) wideText3.getLayoutData() ).exclude );
		assertEquals( 4, ( (GridData) wideText1.getLayoutData() ).horizontalSpan );
		assertEquals( 4, ( (GridData) wideText2.getLayoutData() ).horizontalSpan );
		assertEquals( 4, ( (GridData) wideText3.getLayoutData() ).horizontalSpan );
		assertEquals( 1, ( (GridData) stub1.getLayoutData() ).horizontalSpan );
		assertEquals( 1, ( (GridData) stub2.getLayoutData() ).horizontalSpan );
		assertEquals( 1, ( (GridData) stub3.getLayoutData() ).horizontalSpan );
		assertEquals( 1, ( (GridData) stub4.getLayoutData() ).horizontalSpan );

		// startContainerLayout should clear all GridData again

		layout.startContainerLayout( composite, metawidget );
		assertTrue( wideText1.getLayoutData() == null );
		assertTrue( wideText2.getLayoutData() == null );
		assertTrue( wideText3.getLayoutData() == null );
		assertTrue( stub1.getLayoutData() == null );
		assertTrue( stub2.getLayoutData() == null );
		assertTrue( stub3.getLayoutData() == null );
		assertTrue( stub4.getLayoutData() == null );

	}

	public void testConfig() {

		Map<Class<?>, Object> dummyTypes = CollectionUtils.newHashMap();
		dummyTypes.put( Font.class, new Font( SwtMetawidgetTests.TEST_DISPLAY, "SansSerif", 12, SWT.NONE ) );
		dummyTypes.put( Color.class, new Color( SwtMetawidgetTests.TEST_DISPLAY, 255, 0, 0 ) );

		MetawidgetTestUtils.testEqualsAndHashcode( GridLayoutConfig.class, new GridLayoutConfig() {
			// Subclass
		}, dummyTypes );
	}

	//
	// Inner class
	//

	public static class Foo {

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

	public static class WideFoo {

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

	public static class RequiredFoo {

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
