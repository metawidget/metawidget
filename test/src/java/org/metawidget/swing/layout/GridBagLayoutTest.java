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
import org.metawidget.util.TestUtils;

/**
 * @author Richard Kennard
 */

public class GridBagLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testLayout()
		throws Exception
	{
		// Without stub

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		try
		{
			metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 0 ) ) );
			assertTrue( false );
		}
		catch ( LayoutException e )
		{
			assertTrue( "numberOfColumns must be >= 1".equals( e.getMessage() ) );
		}

		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( ( "abc_label" ).equals( metawidget.getComponent( 0 ).getName() ) );
		assertTrue( metawidget.getComponent( "abc_label" ) == metawidget.getComponent( 0 ) );
		Insets insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ) ).insets;
		assertTrue( insets.left == 0 );
		assertTrue( insets.right == 3 );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridy );
		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ) ).insets;
		assertTrue( insets.left == 3 );
		assertTrue( insets.right == 3 );

		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );

		// JTabbedPane

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 6 );
		assertTrue( 3 == tabbedPane.getComponentCount() );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridy );
		assertTrue( GridBagConstraints.BOTH == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).fill );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridwidth );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).weighty );

		assertTrue( "tab1".equals( tabbedPane.getTitleAt( 0 ) ) );
		JPanel tabPanel = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( tabPanel.isOpaque() );
		assertTrue( "Tab 1_jkl:".equals( ( (JLabel) tabPanel.getComponent( 0 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 1 ) instanceof JLabel );
		assertTrue( 1 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 1 ) ).gridx );
		assertTrue( "Tab 1_mno:".equals( ( (JLabel) tabPanel.getComponent( 2 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 3 ) instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 3 ) ).gridx );
		assertTrue( "Tab 1_pqr:".equals( ( (JLabel) tabPanel.getComponent( 4 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 5 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 5 ) ).gridx );
		assertTrue( tabPanel.getComponent( 6 ) instanceof JPanel );
		assertTrue( 1.0f == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 6 ) ).weighty );
		assertTrue( 7 == tabPanel.getComponentCount() );

		assertTrue( "tab2".equals( tabbedPane.getTitleAt( 1 ) ) );
		tabPanel = (JPanel) tabbedPane.getComponent( 1 );
		assertTrue( tabPanel.getComponent( 0 ) instanceof JScrollPane );
		assertTrue( 0 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridx );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridwidth );
		assertTrue( 1 == tabPanel.getComponentCount() );

		assertTrue( "tab3".equals( tabbedPane.getTitleAt( 2 ) ) );
		tabPanel = (JPanel) tabbedPane.getComponent( 2 );
		assertTrue( tabPanel.getComponent( 0 ) instanceof JTextField );
		assertTrue( 0 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 0 ) ).gridwidth );
		assertTrue( "Tab 3_mno:".equals( ( (JLabel) tabPanel.getComponent( 1 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 2 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 2 ) ).gridx );
		assertTrue( "Tab 3_pqr:".equals( ( (JLabel) tabPanel.getComponent( 3 ) ).getText() ) );
		assertTrue( tabPanel.getComponent( 4 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 4 ) ).gridx );
		assertTrue( tabPanel.getComponent( 5 ) instanceof JPanel );
		assertTrue( 1.0f == ( (GridBagLayout) tabPanel.getLayout() ).getConstraints( tabPanel.getComponent( 5 ) ).weighty );
		assertTrue( 6 == tabPanel.getComponentCount() );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 7 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 8 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );

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

		// (insets.top == 6 for Nimbus, 2 for Metal - depends on what
		// UIManger.setLookAndFeel has been called by other tests)

		insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ) ).insets;
		assertTrue( insets.top == ( "Nimbus".equals( UIManager.getLookAndFeel().getName() ) ? 6 : 2 ) );
		assertTrue( insets.left == 0 );
		assertTrue( insets.bottom == insets.top );
		assertTrue( insets.right == 3 );

		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		insets = ( ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ) ).insets;
		assertTrue( insets.top == 0 );
		assertTrue( insets.left == 0 );
		assertTrue( insets.bottom == 3 );
		assertTrue( insets.right == 0 );

		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof Stub );
		assertTrue( ( (Stub) metawidget.getComponent( 3 ) ).getComponent( 0 ) instanceof JSpinner );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridwidth );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );

		assertTrue( metawidget.getComponent( 6 ) instanceof JTabbedPane );
		assertTrue( arbitrary.equals( metawidget.getComponent( 7 ) ) );
		assertTrue( arbitraryStubWithAttributes.equals( metawidget.getComponent( 8 ) ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridwidth );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertTrue( "Def:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
	}

	public void testWide()
		throws Exception
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ).setLabelSuffix( ":" ) ) );
		metawidget.setToInspect( new WideFoo() );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridy );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridy );

		assertTrue( "Def:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridy );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridy );

		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridx );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridy );
		assertTrue( metawidget.getComponent( 5 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridy );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridwidth );

		assertTrue( "Jkl:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridy );
		assertTrue( metawidget.getComponent( 7 ) instanceof JCheckBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridy );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridy );
		assertTrue( metawidget.getComponent( 9 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridy );
	}

	public void testLabelFont()
	{
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

	public void testLabelSuffix()
	{
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
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridy );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 2 ) ).gridy );

		assertTrue( "Def".equals( ( (JLabel) metawidget.getComponent( 3 ) ).getText() ) );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridy );
		assertTrue( metawidget.getComponent( 4 ) instanceof JSpinner );
		assertTrue( 4 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 4 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 5 ) ).getText() ) );
		assertTrue( 5 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridy );

		assertTrue( "Ghi".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridx );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 6 ) ).gridy );
		assertTrue( metawidget.getComponent( 7 ) instanceof JScrollPane );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridx );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridy );
		assertTrue( 4 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridwidth );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertTrue( 5 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridy );

		assertTrue( "Jkl".equals( ( (JLabel) metawidget.getComponent( 9 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridy );
		assertTrue( metawidget.getComponent( 10 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 10 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 10 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 11 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridy );

		assertTrue( metawidget.getComponent( 12 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridwidth );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 12 ) ).gridy );
		assertTrue( "<html><font color=\"red\">*</font></html>".equals( ( (JLabel) metawidget.getComponent( 13 ) ).getText() ) );
		assertTrue( 5 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridy );
	}

	public void testConfig()
	{
		TestUtils.testEqualsAndHashcode( GridBagLayoutConfig.class, new GridBagLayoutConfig()
		{
			// Subclass
		} );
	}

	//
	// Public statics
	//

	public static void main( String[] args )
	{
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
