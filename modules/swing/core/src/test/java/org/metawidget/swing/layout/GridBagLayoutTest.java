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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import junit.framework.TestCase;

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
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author Richard Kennard
 */

public class GridBagLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLayout()
		throws Exception {

		// Without stub

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		try {
			metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( -1 ) ) );
			fail();
		} catch ( LayoutException e ) {
			assertTrue( "numberOfColumns must be >= 0".equals( e.getMessage() ) );
		}

		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( ( "abc_label" ).equals( metawidget.getComponent( 0 ).getName() ) );
		assertEquals( metawidget.getComponent( "abc_label" ), metawidget.getComponent( 0 ) );
		Insets insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ) ).insets;
		assertEquals( insets.left, 0 );
		assertEquals( insets.right, 3 );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridy );
		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ) ).insets;
		assertEquals( insets.left, 3 );
		assertEquals( insets.right, 3 );

		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( 3, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );

		// JTabbedPane

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 6 );
		assertEquals( 3, tabbedPane.getComponentCount() );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridy );
		assertEquals( GridBagConstraints.BOTH, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).fill );
		assertEquals( GridBagConstraints.REMAINDER, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridwidth );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).weighty );

		assertTrue( "tab1".equals( tabbedPane.getTitleAt( 0 ) ) );
		JPanel tabPanel = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( tabPanel.isOpaque() );
		assertTrue( "Tab 1_jkl:".equals( ( (JLabel) tabPanel.getComponent( 0 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 1 ) instanceof JLabel );
		assertEquals( 1, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 1 ) ).gridx );
		assertTrue( "Tab 1_mno:".equals( ( (JLabel) tabPanel.getComponent( 2 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 3 ) instanceof JComboBox );
		assertEquals( 3, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 3 ) ).gridx );
		assertTrue( "Tab 1_pqr:".equals( ( (JLabel) tabPanel.getComponent( 4 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 5 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 5 ) ).gridx );
		assertTrue( tabPanel.getComponent( 6 ) instanceof JPanel );
		assertTrue( 1.0f == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 6 ) ).weighty );
		assertEquals( 7, tabPanel.getComponentCount() );

		assertTrue( "tab2".equals( tabbedPane.getTitleAt( 1 ) ) );
		tabPanel = (JPanel) tabbedPane.getComponent( 1 );
		assertTrue( tabPanel.getComponent( 0 ) instanceof JScrollPane );
		assertEquals( 0, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridx );
		assertEquals( GridBagConstraints.REMAINDER, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridwidth );
		assertEquals( 1, tabPanel.getComponentCount() );

		assertTrue( "tab3".equals( tabbedPane.getTitleAt( 2 ) ) );
		tabPanel = (JPanel) tabbedPane.getComponent( 2 );
		assertTrue( tabPanel.getComponent( 0 ) instanceof JTextField );
		assertEquals( 0, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridwidth );
		assertTrue( "Tab 3_mno:".equals( ( (JLabel) tabPanel.getComponent( 1 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 2 ) instanceof JTextField );
		assertEquals( 3, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 2 ) ).gridx );
		assertTrue( "Tab 3_pqr:".equals( ( (JLabel) tabPanel.getComponent( 3 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 4 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 4 ) ).gridx );
		assertTrue( tabPanel.getComponent( 5 ) instanceof JPanel );
		assertTrue( 1.0f == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 5 ) ).weighty );
		assertEquals( 6, tabPanel.getComponentCount() );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 7 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 8 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );

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

		// (insets.top == 6 for Nimbus, 2 for Metal - depends on what
		// UIManger.setLookAndFeel has been called by other tests)

		insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ) ).insets;
		assertEquals( insets.top, ( "Nimbus".equals( UIManager.getLookAndFeel().getName() ) ? 6 : 2 ) );
		assertEquals( insets.left, 0 );
		assertEquals( insets.bottom, insets.top );
		assertEquals( insets.right, 3 );

		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ) ).insets;
		assertEquals( insets.top, 0 );
		assertEquals( insets.left, 0 );
		assertEquals( insets.bottom, 3 );
		assertEquals( insets.right, 0 );

		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof Stub );
		assertTrue( ( (Stub) metawidget.getComponent( 3 ) ).getComponent( 0 ) instanceof JSpinner );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertEquals( GridBagConstraints.REMAINDER, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridwidth );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );

		assertTrue( metawidget.getComponent( 6 ) instanceof JTabbedPane );
		assertTrue( arbitrary.equals( metawidget.getComponent( 7 ) ) );
		assertTrue( arbitraryStubWithAttributes.equals( metawidget.getComponent( 8 ) ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertEquals( GridBagConstraints.REMAINDER, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridwidth );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertTrue( "Def:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
	}

	public void testZeroColumns()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 0 ).setLabelSuffix( ":" ) ) ) ) );
		metawidget.setToInspect( new Foo() );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridy );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridy );

		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridy );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertEquals( 3, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridy );

		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridx );
		assertEquals( 4, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridy );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );
		assertEquals( 5, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridy );

		assertTrue( "tab1".equals( ( (JLabel) ( (JPanel) metawidget.getComponent( 6 ) ).getComponent( 0 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridx );
		assertEquals( 6, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridy );
		assertTrue( "Tab 1_jkl:".equals( ( (JLabel) metawidget.getComponent( 7 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridx );
		assertEquals( 7, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridy );
		assertTrue( metawidget.getComponent( 8 ) instanceof JLabel );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertEquals( 8, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridy );

		assertTrue( "Tab 1_mno:".equals( ( (JLabel) metawidget.getComponent( 9 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridx );
		assertEquals( 9, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridy );
		assertTrue( metawidget.getComponent( 10 ) instanceof JComboBox );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 10 ) ).gridx );
		assertEquals( 10, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 10 ) ).gridy );

		assertTrue( "Tab 1_pqr:".equals( ( (JLabel) metawidget.getComponent( 11 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridx );
		assertEquals( 11, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridy );
		assertTrue( metawidget.getComponent( 12 ) instanceof JTextField );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridx );
		assertEquals( 12, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridy );

		assertTrue( "tab2".equals( ( (JLabel) ( (JPanel) metawidget.getComponent( 13 ) ).getComponent( 0 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridx );
		assertEquals( 13, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridy );
		assertTrue( metawidget.getComponent( 14 ) instanceof JScrollPane );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 14 ) ).gridx );
		assertEquals( 14, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 14 ) ).gridy );

		assertTrue( "tab3".equals( ( (JLabel) ( (JPanel) metawidget.getComponent( 15 ) ).getComponent( 0 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 15 ) ).gridx );
		assertEquals( 15, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 15 ) ).gridy );
		assertTrue( metawidget.getComponent( 16 ) instanceof JTextField );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 16 ) ).gridx );
		assertEquals( 16, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 16 ) ).gridy );

		assertTrue( "Tab 3_mno:".equals( ( (JLabel) metawidget.getComponent( 17 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 17 ) ).gridx );
		assertEquals( 17, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 17 ) ).gridy );
		assertTrue( metawidget.getComponent( 18 ) instanceof JTextField );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 18 ) ).gridx );
		assertEquals( 18, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 18 ) ).gridy );

		assertTrue( "Tab 3_pqr:".equals( ( (JLabel) metawidget.getComponent( 19 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 19 ) ).gridx );
		assertEquals( 19, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 19 ) ).gridy );
		assertTrue( metawidget.getComponent( 20 ) instanceof JTextField );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 20 ) ).gridx );
		assertEquals( 20, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 20 ) ).gridy );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 21 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 21 ) ).gridx );
		assertEquals( 21, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 21 ) ).gridy );
		assertTrue( metawidget.getComponent( 22 ) instanceof JTextField );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 22 ) ).gridx );
		assertEquals( 22, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 22 ) ).gridy );

		assertEquals( 23, metawidget.getComponentCount() );
	}

	public void testWide()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ).setLabelSuffix( ":" ) ) );
		metawidget.setToInspect( new WideFoo() );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridy );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridy );

		assertTrue( "Def:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridy );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( 3, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridy );

		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridx );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridy );
		assertTrue( metawidget.getComponent( 5 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridy );
		assertEquals( GridBagConstraints.REMAINDER, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridwidth );

		assertTrue( "Jkl:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridy );
		assertTrue( metawidget.getComponent( 7 ) instanceof JCheckBox );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridy );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridy );
		assertTrue( metawidget.getComponent( 9 ) instanceof JTextField );
		assertEquals( 3, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridy );
	}

	public void testLabelFont() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new RequiredFoo() );

		// Different label foreground

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig() ) );
		assertTrue( "Abc*:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertFalse( Color.yellow.equals( ( (JLabel) metawidget.getComponent( 0 ) ).getForeground() ) );

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setLabelForeground( Color.yellow ) ) );
		assertTrue( Color.yellow.equals( ( (JLabel) metawidget.getComponent( 0 ) ).getForeground() ) );

		// Different label font

		Font newFont = new JPanel().getFont().deriveFont( 64.0f );
		assertFalse( newFont.equals( ( (JLabel) metawidget.getComponent( 0 ) ).getFont() ) );

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setLabelFont( newFont ) ) );
		assertTrue( newFont.equals( ( (JLabel) metawidget.getComponent( 0 ) ).getFont() ) );
	}

	public void testLabelSuffix() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new RequiredFoo() );

		// Different label suffix

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setLabelSuffix( "#" ) ) );
		assertTrue( "Abc*#".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );

		// Align left

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setRequiredAlignment( SwingConstants.LEFT ).setRequiredText( "?" ) ) );
		assertTrue( "?Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );

		// No suffix

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setLabelSuffix( null ).setRequiredText( null ) ) );
		assertTrue( "Abc".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );

		// Align right

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ).setLabelSuffix( "" ).setRequiredText( "<html><font color=\"red\">*</font></html>" ).setRequiredAlignment( SwingConstants.RIGHT ) ) );

		assertTrue( "Abc".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridy );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridy );

		assertTrue( "Def".equals( ( (JLabel) metawidget.getComponent( 3 ) ).getText() ) );
		assertEquals( 3, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridy );
		assertTrue( metawidget.getComponent( 4 ) instanceof JSpinner );
		assertEquals( 4, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 5 ) ).getText() ) );
		assertEquals( 5, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridy );

		assertTrue( "Ghi".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridx );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridy );
		assertTrue( metawidget.getComponent( 7 ) instanceof JScrollPane );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridx );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridy );
		assertEquals( 4, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridwidth );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertEquals( 5, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridy );

		assertTrue( "Jkl".equals( ( (JLabel) metawidget.getComponent( 9 ) ).getText() ) );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridy );
		assertTrue( metawidget.getComponent( 10 ) instanceof JTextField );
		assertEquals( 1, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 10 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 10 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 11 ) ).getText() ) );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridy );

		assertTrue( metawidget.getComponent( 12 ) instanceof JTextField );
		assertEquals( 3, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridwidth );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 13 ) ).getText() ) );
		assertEquals( 5, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridx );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridy );
	}

	public void testMnemonics() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new MnemonicFoo() );

		JLabel label = (JLabel) metawidget.getComponent( 0 );
		assertEquals( "Abc:", label.getText() );
		assertEquals( metawidget.getComponent( 1 ), label.getLabelFor() );
		assertEquals( KeyEvent.VK_A, label.getDisplayedMnemonic() );
		assertEquals( 0, label.getDisplayedMnemonicIndex() );

		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setSupportMnemonics( false ) ) );
		label = (JLabel) metawidget.getComponent( 0 );
		assertEquals( "Abc:", label.getText() );
		assertEquals( metawidget.getComponent( 1 ), label.getLabelFor() );
		assertEquals( 0, label.getDisplayedMnemonic() );
		assertEquals( -1, label.getDisplayedMnemonicIndex() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( GridBagLayoutConfig.class, new GridBagLayoutConfig() {
			// Subclass
		} );
	}

	//
	// Public statics
	//

	public static void main( String[] args ) {

		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );
		metawidget.setToInspect( new Foo() );

		// JFrame

		JFrame frame = new JFrame( "GridBagLayout test" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 210 );
		frame.setVisible( true );
	}

	//
	// Inner class
	//

	public static class Foo {

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

	public static class WideFoo {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String foo ) {

			// Do nothing
		}

		@UiComesAfter( "abc" )
		public int getDef() {

			return 0;
		}

		public void setDef( @SuppressWarnings( "unused" ) int def ) {

			// Do nothing
		}

		@UiWide
		@UiComesAfter( "def" )
		public String getGhi() {

			return null;
		}

		public void setGhi( @SuppressWarnings( "unused" ) String ghi ) {

			// Do nothing
		}

		@UiComesAfter( "ghi" )
		public boolean isJkl() {

			return false;
		}

		public void setJkl( @SuppressWarnings( "unused" ) String jkl ) {

			// Do nothing
		}

		@UiComesAfter( "jkl" )
		public String getMno() {

			return null;
		}

		public void setMno( @SuppressWarnings( "unused" ) String mno ) {

			// Do nothing
		}
	}

	public static class RequiredFoo {

		@UiRequired
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

		public void setDef( @SuppressWarnings( "unused" ) String def ) {

			// Do nothing
		}

		@UiComesAfter( "def" )
		@UiRequired
		@UiLarge
		public String getGhi() {

			return null;
		}

		public void setGhi( @SuppressWarnings( "unused" ) String ghi ) {

			// Do nothing
		}

		@UiComesAfter( "ghi" )
		@UiRequired
		public String getJkl() {

			return null;
		}

		public void setJkl( @SuppressWarnings( "unused" ) String jkl ) {

			// Do nothing
		}

		@UiComesAfter( "jkl" )
		@UiRequired
		@UiLabel( "" )
		public String getMno() {

			return null;
		}

		public void setMno( @SuppressWarnings( "unused" ) String mno ) {

			// Do nothing
		}
	}

	public static class MnemonicFoo {

		@UiLabel( "&Abc" )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}
	}
}
