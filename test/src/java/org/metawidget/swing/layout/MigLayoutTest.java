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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import junit.framework.TestCase;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.MigLayout;

import org.metawidget.iface.MetawidgetException;
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
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class MigLayoutTest
	extends TestCase
{
	//
	// Private statics
	//

	private final static int	SPAN_ALL	= 2097051;

	private final static int	GROW_ALL	= 100;

	//
	// Public methods
	//

	public void testTabLayout()
		throws Exception
	{
		// Without stub

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setParameter( "numberOfColumns", 0 );
		metawidget.setParameter( "sectionStyle", org.metawidget.swing.layout.MigLayout.SECTION_AS_TAB );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout() );
		metawidget.setToInspect( new Foo() );

		try
		{
			metawidget.getComponent( 0 );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "numberOfColumns must be >= 1".equals( e.getCause().getCause().getMessage() ) );
		}

		metawidget.setParameter( "numberOfColumns", 2 );

		UnitValue[] insets = ( (LC) ( (MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).getInsets();
		assertTrue( 0 == insets[0].getValue() );
		assertTrue( 0 == insets[1].getValue() );
		assertTrue( 0 == insets[2].getValue() );
		assertTrue( 0 == insets[3].getValue() );
		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( 1 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getCellX() );
		assertTrue( null == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getVertical().getGrow() );
		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( 3 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 3 ) ) ).getCellX() );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( 1 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 5 ) ) ).getCellX() );

		// JTabbedPane

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 6 );
		assertTrue( 3 == tabbedPane.getComponentCount() );
		assertTrue( 0 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( tabbedPane ) ).getCellX() );
		assertTrue( 2 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( tabbedPane ) ).getCellY() );
		assertTrue( SPAN_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( tabbedPane ) ).getSpanX() );
		assertTrue( GROW_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( tabbedPane ) ).getVertical().getGrow() );
		assertTrue( true == ( (LC) ( (MigLayout) metawidget.getLayout() ).getLayoutConstraints() ).isFillY() );

		JPanel panelTab = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( panelTab.isOpaque() );
		assertTrue( "tab1".equals( panelTab.getName() ) );
		assertTrue( panelTab.getComponent( 1 ) instanceof JLabel );
		assertTrue( "Tab 1_jkl:".equals( ( (JLabel) panelTab.getComponent( 0 ) ).getText() ) );
		assertTrue( false == ( (LC) ( (MigLayout) panelTab.getLayout() ).getLayoutConstraints() ).isFillY() );
		assertTrue( 1 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 1 ) ) ).getCellX() );
		assertTrue( "Tab 1_mno:".equals( ( (JLabel) panelTab.getComponent( 2 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 3 ) instanceof JComboBox );
		assertTrue( 3 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 3 ) ) ).getCellX() );
		assertTrue( "Tab 1_pqr:".equals( ( (JLabel) panelTab.getComponent( 4 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 5 ) instanceof JTextField );
		assertTrue( 1 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 5 ) ) ).getCellX() );
		assertTrue( 6 == panelTab.getComponentCount() );

		panelTab = (JPanel) tabbedPane.getComponent( 1 );
		assertTrue( "tab2".equals( panelTab.getName() ) );
		assertTrue( true == ( (LC) ( (MigLayout) panelTab.getLayout() ).getLayoutConstraints() ).isFillY() );
		assertTrue( panelTab.getComponent( 0 ) instanceof JScrollPane );
		assertTrue( 0 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 0 ) ) ).getCellX() );
		assertTrue( SPAN_ALL == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 0 ) ) ).getSpanX() );
		assertTrue( GROW_ALL == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 0 ) ) ).getVertical().getGrow() );
		assertTrue( 1 == panelTab.getComponentCount() );

		panelTab = (JPanel) tabbedPane.getComponent( 2 );
		assertTrue( "tab3".equals( panelTab.getName() ) );
		assertTrue( false == ( (LC) ( (MigLayout) panelTab.getLayout() ).getLayoutConstraints() ).isFillY() );
		assertTrue( panelTab.getComponent( 0 ) instanceof JTextField );
		assertTrue( 0 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 0 ) ) ).getCellX() );
		assertTrue( 2 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 0 ) ) ).getSpanX() );
		assertTrue( "Tab 3_mno:".equals( ( (JLabel) panelTab.getComponent( 1 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 2 ) instanceof JTextField );
		assertTrue( 3 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 2 ) ) ).getCellX() );
		assertTrue( "Tab 3_pqr:".equals( ( (JLabel) panelTab.getComponent( 3 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 4 ) instanceof JTextField );
		assertTrue( 1 == ( (CC) ( (MigLayout) panelTab.getLayout() ).getComponentConstraints( panelTab.getComponent( 4 ) ) ).getCellX() );
		assertTrue( 5 == panelTab.getComponentCount() );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 7 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 8 ) instanceof JTextField );
		assertTrue( 1 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 8 ) ) ).getCellX() );

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

		// With an arbirary stub with attributes

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
		assertTrue( padding == null );

		assertTrue( 0f == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 0 ) ) ).getVertical().getAlign().getValue() );
		assertTrue( 1 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getCellX() );
		assertTrue( null == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 1 ) ) ).getVertical().getAlign() );
		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof Stub );
		assertTrue( ( (Stub) metawidget.getComponent( 3 ) ).getComponent( 0 ) instanceof JSpinner );
		assertTrue( 1 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 3 ) ) ).getCellX() );
		assertTrue( SPAN_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 3 ) ) ).getSpanX() );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( 1 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 5 ) ) ).getCellX() );

		assertTrue( metawidget.getComponent( 6 ) instanceof JTabbedPane );
		assertTrue( arbitrary.equals( metawidget.getComponent( 7 ) ) );
		assertTrue( arbitraryStubWithAttributes.equals( metawidget.getComponent( 8 ) ) );
		assertTrue( 0 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 8 ) ) ).getCellX() );
		assertTrue( SPAN_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( metawidget.getComponent( 8 ) ) ).getSpanX() );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertTrue( "Def:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
	}

	public void testHeadingLayout()
		throws Exception
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() ) ) );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout() );
		metawidget.setToInspect( new Foo() );

		// Heading

		JLabel label = (JLabel) metawidget.getComponent( 6 );
		assertTrue( "tab1".equals( label.getText() ) );
		assertTrue( 0 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( label ) ).getCellX() );
		assertTrue( 3 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( label ) ).getCellY() );
		assertTrue( SPAN_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( label ) ).getSpanX() );

		JSeparator separator = (JSeparator) metawidget.getComponent( 7 );
		assertTrue( 0 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( separator ) ).getCellX() );
		assertTrue( 3 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( separator ) ).getCellY() );
		assertTrue( GROW_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( separator ) ).getHorizontal().getGrow() );
	}

	public void testOddColumns()
		throws Exception
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setParameter( "numberOfColumns", 2 );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout() );
		metawidget.add( new JTextField() );
		Facet buttons = new Facet();
		buttons.setName( "buttons" );
		metawidget.add( buttons );

		// Facet goes at 'row 1', not 'row 0 column 2'

		Facet facet = (Facet) metawidget.getComponent( 1 );
		assertTrue( 0 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getCellX() );
		assertTrue( 1 == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getCellY() );
		assertTrue( SPAN_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getSpanX() );
		assertTrue( GROW_ALL == ( (CC) ( (MigLayout) metawidget.getLayout() ).getComponentConstraints( facet ) ).getHorizontal().getGrow() );
	}

	public static void main( String[] args )
	{
		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setParameter( "numberOfColumns", 2 );
		metawidget.setParameter( "sectionStyle", org.metawidget.swing.layout.MigLayout.SECTION_AS_TAB );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.MigLayout() );
		metawidget.setToInspect( new NastyNestingTop() );

		// JFrame

		JFrame frame = new JFrame( "MigLayout test" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 210 );
		frame.setVisible( true );
	}

	//
	// Inner class
	//

	static class Foo
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

	public static class NastyNestingTop
	{
		public NastyNestingBottom	nested1	= new NastyNestingBottom();

		public NastyNestingMiddle1	nested2	= new NastyNestingMiddle1();
	}

	public static class NastyNestingMiddle1
	{
		public NastyNestingMiddle2	nested1	= new NastyNestingMiddle2();

		public NastyNestingBottom	nested2	= new NastyNestingBottom();
	}

	public static class NastyNestingMiddle2
	{
		public NastyNestingBottom	nested1	= new NastyNestingBottom();

		public String				string;

		@UiLarge
		public String				large;
	}

	public static class NastyNestingBottom
	{
		public String	string;
	}
}
