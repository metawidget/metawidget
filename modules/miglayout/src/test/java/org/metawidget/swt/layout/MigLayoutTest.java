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
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.UnitValue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
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
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.swt.Facet;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.TestUtils;

/**
 * @author Richard Kennard
 */

public class MigLayoutTest
	extends TestCase {

	//
	// Private statics
	//

	private static final int	SPAN_ALL		= 2097051;

	private static final int	GROW_ALL		= 100;

	public static final Display	TEST_DISPLAY	= new Display();

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
		metawidget.setToInspect( new Foo() );
		metawidget.setMetawidgetLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new org.metawidget.swt.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );
		Stub stub = new Stub( metawidget, SWT.NONE );
		stub.setData( NAME, "mno" );
		stub.setBackground( new Color( display, 255, 0, 0 ) );

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
	// Public methods
	//

	@SuppressWarnings( "unused" )
	public void testTabLayout()
		throws Exception {

		// Without stub

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		try {
			metawidget.setMetawidgetLayout( new org.metawidget.swt.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 0 ) ) );
			assertTrue( false );
		} catch ( LayoutException e ) {
			assertEquals( "numberOfColumns must be >= 1", e.getMessage() );
		}

		metawidget.setMetawidgetLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new org.metawidget.swt.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );

		assertTrue( 0 == ( (LC) ( (net.miginfocom.swt.MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).getDebugMillis() );
		UnitValue[] insets = ( (LC) ( (net.miginfocom.swt.MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).getInsets();
		assertTrue( 0 == insets[0].getValue() );
		assertTrue( 0 == insets[1].getValue() );
		assertTrue( 0 == insets[2].getValue() );
		assertTrue( 0 == insets[3].getValue() );
		assertEquals( "Abc:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertTrue( 1 == ( (CC) metawidget.getChildren()[1].getLayoutData() ).getCellX() );
		assertTrue( null == ( (CC) metawidget.getChildren()[1].getLayoutData() ).getVertical().getGrow() );
		assertEquals( ( ConstraintParser.parseUnitValueOrAlign( "top", false, null ) ), ( (CC) metawidget.getChildren()[1].getLayoutData() ).getVertical().getAlign() );
		assertEquals( "Def*:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Spinner );
		assertTrue( 3 == ( (CC) metawidget.getChildren()[3].getLayoutData() ).getCellX() );
		assertEquals( "Ghi:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertTrue( 1 == ( (CC) metawidget.getChildren()[5].getLayoutData() ).getCellX() );

		// TabFolder

		TabFolder tabFolder = (TabFolder) metawidget.getChildren()[6];
		assertTrue( 3 == tabFolder.getItemCount() );
		assertTrue( 0 == ( (CC) tabFolder.getLayoutData() ).getCellX() );
		assertTrue( 2 == ( (CC) tabFolder.getLayoutData() ).getCellY() );
		assertTrue( SPAN_ALL == ( (CC) tabFolder.getLayoutData() ).getSpanX() );

		assertEquals( "tab1", tabFolder.getItem( 0 ).getText() );
		Composite tabComposite = (Composite) tabFolder.getChildren()[0];
		assertTrue( tabComposite.getChildren()[0] instanceof Label );
		assertEquals( "Tab 1_jkl:", ( (Label) tabComposite.getChildren()[0] ).getText() );
		assertTrue( false == ( (LC) ( (net.miginfocom.swt.MigLayout) tabComposite.getLayout() ).getLayoutConstraints() ).isFillY() );
		assertTrue( 1 == ( (CC) tabComposite.getChildren()[1].getLayoutData() ).getCellX() );
		assertEquals( "Tab 1_mno:", ( (Label) tabComposite.getChildren()[2] ).getText() );
		assertTrue( tabComposite.getChildren()[3] instanceof Combo );
		assertTrue( 3 == ( (CC) tabComposite.getChildren()[3].getLayoutData() ).getCellX() );
		assertEquals( "Tab 1_pqr:", ( (Label) tabComposite.getChildren()[4] ).getText() );
		assertTrue( tabComposite.getChildren()[5] instanceof Text );
		assertTrue( 1 == ( (CC) tabComposite.getChildren()[5].getLayoutData() ).getCellX() );
		assertTrue( 6 == tabComposite.getChildren().length );

		assertEquals( "tab2", tabFolder.getItem( 1 ).getText() );
		tabComposite = (Composite) tabFolder.getChildren()[1];
		assertTrue( tabComposite.getChildren()[0] instanceof Text );
		assertTrue( 0 == ( (CC) tabComposite.getChildren()[0].getLayoutData() ).getCellX() );
		assertTrue( SPAN_ALL == ( (CC) tabComposite.getChildren()[0].getLayoutData() ).getSpanX() );
		assertTrue( GROW_ALL == ( (CC) tabComposite.getChildren()[0].getLayoutData() ).getVertical().getGrow() );
		assertTrue( 1f == ( (CC) tabComposite.getChildren()[0].getLayoutData() ).getPushY() );
		assertTrue( 1 == tabComposite.getChildren().length );

		assertEquals( "tab3", tabFolder.getItem( 2 ).getText() );
		tabComposite = (Composite) tabFolder.getChildren()[2];
		assertTrue( false == ( (LC) ( (net.miginfocom.swt.MigLayout) tabComposite.getLayout() ).getLayoutConstraints() ).isFillY() );
		assertTrue( tabComposite.getChildren()[0] instanceof Text );
		assertTrue( 0 == ( (CC) tabComposite.getChildren()[0].getLayoutData() ).getCellX() );
		assertTrue( 2 == ( (CC) tabComposite.getChildren()[0].getLayoutData() ).getSpanX() );
		assertEquals( "Tab 3_mno:", ( (Label) tabComposite.getChildren()[1] ).getText() );
		assertTrue( tabComposite.getChildren()[2] instanceof Text );
		assertTrue( 3 == ( (CC) tabComposite.getChildren()[2].getLayoutData() ).getCellX() );
		assertEquals( "Tab 3_pqr:", ( (Label) tabComposite.getChildren()[3] ).getText() );
		assertTrue( tabComposite.getChildren()[4] instanceof Text );
		assertTrue( 1 == ( (CC) tabComposite.getChildren()[4].getLayoutData() ).getCellX() );
		assertTrue( 5 == tabComposite.getChildren().length );

		assertEquals( "Mno:", ( (Label) metawidget.getChildren()[7] ).getText() );
		assertTrue( metawidget.getChildren()[8] instanceof Text );
		assertTrue( 1 == ( (CC) metawidget.getChildren()[8].getLayoutData() ).getCellX() );

		// With stub

		Stub stub = new Stub( metawidget, SWT.NONE );
		stub.setData( NAME, "mno" );

		// With stub attributes

		Stub stubWithAttributes = new Stub( metawidget, SWT.NONE );
		stubWithAttributes.setData( NAME, "def" );
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
		assertEquals( "Abc:", ( (Label) metawidget.getChildren()[0] ).getText() );

		UnitValue[] padding = ( (CC) metawidget.getChildren()[0].getLayoutData() ).getPadding();

		// (padding is different on different OS')

		assertTrue( padding[0].getValue() >= 2 );
		assertTrue( padding[1].getValue() == 0 );
		assertTrue( padding[2].getValue() == padding[0].getValue() );
		assertTrue( padding[3].getValue() == 0 );

		assertTrue( metawidget.getChildren()[1] instanceof Text );
		padding = ( (CC) metawidget.getChildren()[1].getLayoutData() ).getPadding();
		assertEquals( padding, null );

		assertTrue( 0f == ( (CC) metawidget.getChildren()[0].getLayoutData() ).getVertical().getAlign().getValue() );
		assertTrue( 1 == ( (CC) metawidget.getChildren()[1].getLayoutData() ).getCellX() );
		assertEquals( ( ConstraintParser.parseUnitValueOrAlign( "top", false, null ) ), ( (CC) metawidget.getChildren()[1].getLayoutData() ).getVertical().getAlign() );
		assertEquals( "Def*:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Stub );
		assertTrue( ( (Stub) metawidget.getChildren()[3] ).getChildren()[0] instanceof Spinner );
		assertTrue( 1 == ( (CC) metawidget.getChildren()[3].getLayoutData() ).getCellX() );
		assertTrue( SPAN_ALL == ( (CC) metawidget.getChildren()[3].getLayoutData() ).getSpanX() );
		assertEquals( "Ghi:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertTrue( 1 == ( (CC) metawidget.getChildren()[5].getLayoutData() ).getCellX() );

		assertTrue( metawidget.getChildren()[6] instanceof TabFolder );
		assertEquals( stub, metawidget.getChildren()[7] );
		assertTrue( 3 == ( (CC) stub.getLayoutData() ).getHideMode() );
		assertTrue( !stub.getVisible() );
		assertEquals( arbitrary, metawidget.getChildren()[8] );
		assertTrue( arbitrary.getVisible() );
		assertEquals( arbitraryStubWithAttributes, metawidget.getChildren()[9] );
		assertTrue( 0 == ( (CC) metawidget.getChildren()[9].getLayoutData() ).getCellX() );
		assertTrue( SPAN_ALL == ( (CC) metawidget.getChildren()[9].getLayoutData() ).getSpanX() );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertEquals( "Def:", ( (Label) metawidget.getChildren()[2] ).getText() );
	}

	@SuppressWarnings( "unused" )
	public void testOddColumns()
		throws Exception {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setMetawidgetLayout( new org.metawidget.swt.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 2 ) ) );
		new Text( metawidget, SWT.NONE );
		Facet buttons = new Facet( metawidget, SWT.NONE );
		buttons.setData( NAME, "buttons" );

		// Facet goes at 'row 1', not 'row 0 column 2'

		Facet facet = (Facet) metawidget.getChildren()[1];
		assertTrue( 0 == ( (CC) facet.getLayoutData() ).getCellX() );
		assertTrue( 1 == ( (CC) facet.getLayoutData() ).getCellY() );
		assertTrue( SPAN_ALL == ( (CC) facet.getLayoutData() ).getSpanX() );
		assertTrue( GROW_ALL == ( (CC) facet.getLayoutData() ).getHorizontal().getGrow() );
	}

	public void testConfig() {

		TestUtils.testEqualsAndHashcode( MigLayoutConfig.class, new MigLayoutConfig() {
			// Subclass
		} );
	}

	//
	// Inner class
	//

	static class Foo {

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

	public static class NastyNestingTop {

		public NastyNestingBottom	nested1	= new NastyNestingBottom();

		public NastyNestingMiddle1	nested2	= new NastyNestingMiddle1();
	}

	public static class NastyNestingMiddle1 {

		public NastyNestingMiddle2	nested1	= new NastyNestingMiddle2();

		public NastyNestingBottom	nested2	= new NastyNestingBottom();
	}

	public static class NastyNestingMiddle2 {

		public NastyNestingBottom	nested1	= new NastyNestingBottom();

		public String				string;

		@UiLarge
		public String				large;
	}

	public static class NastyNestingBottom {

		public String	string;
	}
}
