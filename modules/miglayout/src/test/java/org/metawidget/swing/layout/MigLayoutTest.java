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

package org.metawidget.swing.layout;

import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import junit.framework.TestCase;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.MigLayout;

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
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author Richard Kennard
 */

public class MigLayoutTest
	extends TestCase {

	//
	// Private statics
	//

	private static final int	SPAN_ALL	= 2097051;

	private static final int	GROW_ALL	= 100;

	//
	// Public statics
	//

	public static void main( String[] args ) {

		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );
		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );
		metawidget.add( new Stub( "mno" ) );

		// JFrame

		JFrame frame = new JFrame( "Metawidget MigLayout Test" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 210 );
		frame.setVisible( true );
	}

	//
	// Public methods
	//

	public void testTabLayout()
		throws Exception {

		// Without stub

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		try {
			metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 0 ) ) );
			assertTrue( false );
		} catch ( LayoutException e ) {
			assertTrue( "numberOfColumns must be >= 1".equals( e.getMessage() ) );
		}

		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );

		assertEquals( 0, ( (LC) ( (net.miginfocom.swing.MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).getDebugMillis() );
		UnitValue[] insets = ( (LC) ( (MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).getInsets();
		assertTrue( 0 == insets[0].getValue() );
		assertTrue( 0 == insets[1].getValue() );
		assertTrue( 0 == insets[2].getValue() );
		assertTrue( 0 == insets[3].getValue() );
		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( ( "abc_label" ).equals( metawidget.getComponent( 0 ).getName() ) );
		assertEquals( metawidget.getComponent( "abc_label" ), metawidget.getComponent( 0 ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( 1, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getCellX() );
		assertEquals( null, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getVertical().getGrow() );
		assertEquals( ( ConstraintParser.parseUnitValueOrAlign( "top", false, null ) ), ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getVertical().getAlign() );
		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( 3, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 3 ) ) ).getCellX() );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( 1, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 5 ) ) ).getCellX() );

		// JTabbedPane

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 6 );
		assertEquals( 3, tabbedPane.getComponentCount() );
		assertEquals( 0, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( tabbedPane ) ).getCellX() );
		assertEquals( 2, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( tabbedPane ) ).getCellY() );
		assertEquals( SPAN_ALL, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( tabbedPane ) ).getSpanX() );

		assertTrue( "tab1".equals( tabbedPane.getTitleAt( 0 ) ) );
		JPanel tabPanel = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( tabPanel.isOpaque() );
		assertTrue( tabPanel.getComponent( 0 ) instanceof JLabel );
		assertTrue( "Tab 1_jkl:".equals( ( (JLabel) tabPanel.getComponent( 0 ) ).getText() ) );
		assertEquals( false, ( (LC) ( (MigLayout) tabPanel.getLayout() ).getLayoutConstraints() ).isFillY() );
		assertEquals( 1, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 1 ) ) ).getCellX() );
		assertTrue( "Tab 1_mno:".equals( ( (JLabel) tabPanel.getComponent( 2 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 3 ) instanceof JComboBox );
		assertEquals( 3, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 3 ) ) ).getCellX() );
		assertTrue( "Tab 1_pqr:".equals( ( (JLabel) tabPanel.getComponent( 4 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 5 ) instanceof JTextField );
		assertEquals( 1, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 5 ) ) ).getCellX() );
		assertEquals( 6, tabPanel.getComponentCount() );

		assertTrue( "tab2".equals( tabbedPane.getTitleAt( 1 ) ) );
		tabPanel = (JPanel) tabbedPane.getComponent( 1 );
		assertTrue( tabPanel.getComponent( 0 ) instanceof JScrollPane );
		assertEquals( 0, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 0 ) ) ).getCellX() );
		assertEquals( SPAN_ALL, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 0 ) ) ).getSpanX() );
		assertTrue( GROW_ALL == ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 0 ) ) ).getVertical().getGrow() );
		assertEquals( 1f, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 0 ) ) ).getPushY() );
		assertEquals( 1, tabPanel.getComponentCount() );

		assertTrue( "tab3".equals( tabbedPane.getTitleAt( 2 ) ) );
		tabPanel = (JPanel) tabbedPane.getComponent( 2 );
		assertEquals( false, ( (LC) ( (MigLayout) tabPanel.getLayout() ).getLayoutConstraints() ).isFillY() );
		assertTrue( tabPanel.getComponent( 0 ) instanceof JTextField );
		assertEquals( 0, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 0 ) ) ).getCellX() );
		assertEquals( 2, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 0 ) ) ).getSpanX() );
		assertTrue( "Tab 3_mno:".equals( ( (JLabel) tabPanel.getComponent( 1 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 2 ) instanceof JTextField );
		assertEquals( 3, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 2 ) ) ).getCellX() );
		assertTrue( "Tab 3_pqr:".equals( ( (JLabel) tabPanel.getComponent( 3 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 4 ) instanceof JTextField );
		assertEquals( 1, ( (CC) ( (MigLayout) tabPanel.getLayout() ).getComponentConstraints( tabPanel.getComponent( 4 ) ) ).getCellX() );
		assertEquals( 5, tabPanel.getComponentCount() );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 7 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 8 ) instanceof JTextField );
		assertEquals( 1, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 8 ) ) ).getCellX() );

		// With stub

		metawidget.add( new Stub( "mno" ) );

		// With stub attributes

		Stub stubWithAttributes = new Stub();
		stubWithAttributes.setName( "def" );
		stubWithAttributes.add( new JSpinner() );
		stubWithAttributes.setAttribute( "large", "true" );
		metawidget.add( stubWithAttributes );

		// With an arbitrary component

		JSpinner arbitrary = new JSpinner();
		metawidget.add( arbitrary );

		// With an arbitrary stub with attributes

		Stub arbitraryStubWithAttributes = new Stub();
		arbitraryStubWithAttributes.add( new JTextField() );
		arbitraryStubWithAttributes.setAttribute( "label", "" );
		arbitraryStubWithAttributes.setAttribute( "large", "true" );
		metawidget.add( arbitraryStubWithAttributes );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );

		// (padding[0].getValue() == 6 for Nimbus, 2 for Metal - depends on what
		// UIManger.setLookAndFeel has been called by other tests)

		UnitValue[] padding = ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 0 ) ) ).getPadding();
		assertTrue( padding[0].getValue() == ( "Nimbus".equals( UIManager.getLookAndFeel().getName() ) ? 6 : 2 ) );
		assertTrue( padding[1].getValue() == 0 );
		assertTrue( padding[2].getValue() == padding[0].getValue() );
		assertTrue( padding[3].getValue() == 0 );

		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		padding = ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getPadding();
		assertEquals( padding, null );

		assertEquals( 0f, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 0 ) ) ).getVertical().getAlign().getValue() );
		assertEquals( 1, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getCellX() );
		assertEquals( ( ConstraintParser.parseUnitValueOrAlign( "top", false, null ) ), ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getVertical().getAlign() );
		assertEquals( "Def*:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof Stub );
		assertTrue( ( (Stub) metawidget.getComponent( 3 ) ).getComponent( 0 ) instanceof JSpinner );
		assertEquals( 1, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 3 ) ) ).getCellX() );
		assertEquals( SPAN_ALL, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 3 ) ) ).getSpanX() );
		assertEquals( "Ghi:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( 1, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 5 ) ) ).getCellX() );

		assertTrue( metawidget.getComponent( 6 ) instanceof JTabbedPane );
		assertEquals( arbitrary, metawidget.getComponent( 7 ) );
		assertEquals( arbitraryStubWithAttributes, metawidget.getComponent( 8 ) );
		assertEquals( 0, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 8 ) ) ).getCellX() );
		assertEquals( SPAN_ALL, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 8 ) ) ).getSpanX() );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertTrue( "Def:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
	}

	public void testOddColumns()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout( new MigLayoutConfig().setNumberOfColumns( 2 ) ) );
		metawidget.add( new JTextField() );
		Facet buttons = new Facet();
		buttons.setName( "buttons" );
		metawidget.add( buttons );

		// Facet goes at 'row 1', not 'row 0 column 2'

		Facet facet = (Facet) metawidget.getComponent( 1 );
		assertEquals( 0, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getCellX() );
		assertEquals( 1, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getCellY() );
		assertEquals( SPAN_ALL, ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getSpanX() );
		assertTrue( GROW_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getHorizontal().getGrow() );
	}

	public void testMnemonics() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout() );
		metawidget.setToInspect( new MnemonicFoo() );

		JLabel label = (JLabel) metawidget.getComponent( 0 );
		assertEquals( "Abc:", label.getText() );
		assertEquals( metawidget.getComponent( 1 ), label.getLabelFor() );
		assertEquals( KeyEvent.VK_C, label.getDisplayedMnemonic() );
		assertEquals( 2, label.getDisplayedMnemonicIndex() );

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout( new MigLayoutConfig().setSupportMnemonics( false ) ) );
		label = (JLabel) metawidget.getComponent( 0 );
		assertEquals( "Abc:", label.getText() );
		assertEquals( metawidget.getComponent( 1 ), label.getLabelFor() );
		assertEquals( 0, label.getDisplayedMnemonic() );
		assertEquals( -1, label.getDisplayedMnemonicIndex() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( MigLayoutConfig.class, new MigLayoutConfig() {
			// Subclass
		} );
	}

	//
	// Inner class
	//

	static class Foo {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiComesAfter( "abc" )
		@UiRequired
		public int getDef() {

			return 0;
		}

		public void setDef( @SuppressWarnings( "unused" ) int def ) {

			// Do nothing
		}

		@UiComesAfter( "def" )
		public boolean isGhi() {

			return false;
		}

		public void setGhi( @SuppressWarnings( "unused" ) boolean ghi ) {

			// Do nothing
		}

		@UiSection( "tab1" )
		@UiComesAfter( "ghi" )
		@UiAttribute( name = "required", value = "true" )
		@UiReadOnly
		public String getTab1_jkl() {

			return null;
		}

		public void setTab1_jkl( @SuppressWarnings( "unused" ) String tab1_jkl ) {

			// Do nothing
		}

		@UiComesAfter( "tab1_jkl" )
		@UiLookup( { "foo", "bar" } )
		public String getTab1_mno() {

			return null;
		}

		public void setTab1_mno( @SuppressWarnings( "unused" ) String tab1_mno ) {

			// Do nothing
		}

		@UiComesAfter( "tab1_mno" )
		public String getTab1_pqr() {

			return null;
		}

		public void setTab1_pqr( @SuppressWarnings( "unused" ) String tab1_pqr ) {

			// Do nothing
		}

		@UiSection( "tab2" )
		@UiComesAfter( "tab1_pqr" )
		@UiLarge
		@UiLabel( "" )
		public String getTab2_jkl() {

			return null;
		}

		public void setTab2_jkl( @SuppressWarnings( "unused" ) String tab2_jkl ) {

			// Do nothing
		}

		@UiSection( "tab3" )
		@UiComesAfter( "tab2_jkl" )
		@UiLabel( "" )
		public String getTab3_jkl() {

			return null;
		}

		public void setTab3_jkl( @SuppressWarnings( "unused" ) String tab3_jkl ) {

			// Do nothing
		}

		@UiComesAfter( "tab3_jkl" )
		public String getTab3_mno() {

			return null;
		}

		public void setTab3_mno( @SuppressWarnings( "unused" ) String tab3_mno ) {

			// Do nothing
		}

		@UiComesAfter( "tab3_mno" )
		public String getTab3_pqr() {

			return null;
		}

		public void setTab3_pqr( @SuppressWarnings( "unused" ) String tab3_pqr ) {

			// Do nothing
		}

		@UiSection( "" )
		@UiComesAfter( "tab3_pqr" )
		public String getMno() {

			return null;
		}

		public void setMno( @SuppressWarnings( "unused" ) String mno ) {

			// Do nothing
		}
	}

	public static class NastyNestingTop {

		public NastyNestingBottom getNested1() {

			return new NastyNestingBottom();
		}

		public NastyNestingMiddle1 getNested2() {

			return new NastyNestingMiddle1();
		}
	}

	public static class NastyNestingMiddle1 {

		public NastyNestingMiddle2 getNested1() {

			return new NastyNestingMiddle2();
		}

		public NastyNestingBottom getNested2() {

			return new NastyNestingBottom();
		}
	}

	public static class NastyNestingMiddle2 {

		public NastyNestingBottom getNested1() {

			return new NastyNestingBottom();
		}

		public String getString() {

			return null;
		}

		@UiLarge
		public String getLarge() {

			return null;
		}
	}

	public static class NastyNestingBottom {

		public String getString() {

			return null;
		}
	}

	public static class MnemonicFoo {

		@UiLabel( "Ab&c" )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}
	}
}
