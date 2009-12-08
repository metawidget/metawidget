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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class TitledPanelLayoutDecoratorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testTitledPanelLayoutDecorator()
	{
		SwingMetawidget swingMetawidget = new SwingMetawidget();
		swingMetawidget.setMetawidgetLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent,SwingMetawidget>().setLayout( new GridBagLayout() )));
		swingMetawidget.setToInspect( new Foo() );

		Facet facet = new Facet();
		facet.setName( "buttons" );
		swingMetawidget.add( facet );

		assertTrue( "Bar:".equals( ( (JLabel) swingMetawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( swingMetawidget.getComponent( 1 ) instanceof JTextField );

		// Heading #1

		JPanel panel = (JPanel) swingMetawidget.getComponent( 2 );
		assertTrue( 5 == ((EmptyBorder) ((CompoundBorder) panel.getBorder()).getOutsideBorder()).getBorderInsets().top );
		assertTrue( "heading1".equals( ((TitledBorder) ((CompoundBorder) ((CompoundBorder) panel.getBorder()).getInsideBorder()).getOutsideBorder()).getTitle() ));
		assertTrue( 3 == ((EmptyBorder) ((CompoundBorder) ((CompoundBorder) panel.getBorder()).getInsideBorder()).getInsideBorder()).getBorderInsets().top );
		assertTrue( "Baz:".equals( ( (JLabel) panel.getComponent( 0 ) ).getText() ) );
		assertTrue( panel.getComponent( 1 ) instanceof JCheckBox );
		assertTrue( "Abc:".equals( ( (JLabel) panel.getComponent( 2 ) ).getText() ) );
		assertTrue( panel.getComponent( 3 ) instanceof JTextField );
		assertTrue( panel.getComponent( 4 ) instanceof JPanel );
		assertTrue( 5 == panel.getComponentCount() );

		// Heading  #2

		panel = (JPanel) swingMetawidget.getComponent( 3 );
		assertTrue( "heading2".equals( ((TitledBorder) ((CompoundBorder) ((CompoundBorder) panel.getBorder()).getInsideBorder()).getOutsideBorder()).getTitle() ));
		assertTrue( "Def:".equals( ( (JLabel) panel.getComponent( 0 ) ).getText() ) );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == panel.getComponentCount() );

		// Separate component

		assertTrue( "Ghi:".equals( ( (JLabel) swingMetawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( swingMetawidget.getComponent( 5 ) instanceof JComboBox );

		// Heading #3

		panel = (JPanel) swingMetawidget.getComponent( 6 );
		assertTrue( "heading3".equals( ((TitledBorder) ((CompoundBorder) ((CompoundBorder) panel.getBorder()).getInsideBorder()).getOutsideBorder()).getTitle() ));
		assertTrue( "Jkl:".equals( ( (JLabel) panel.getComponent( 0 ) ).getText() ) );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == panel.getComponentCount() );

		assertTrue( facet == swingMetawidget.getComponent( 7 ));
		assertTrue( 8 == swingMetawidget.getComponentCount() );
	}

	public void testNestedPanels()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent,SwingMetawidget>().setLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent,SwingMetawidget>().setLayout( new GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );

		JPanel outerPanel = (JPanel) metawidget.getComponent( 2 );
		assertTrue( "Foo".equals( ((TitledBorder) ((CompoundBorder) ((CompoundBorder) outerPanel.getBorder()).getInsideBorder()).getOutsideBorder()).getTitle() ));
		assertTrue( 5 == outerPanel.getComponentCount() );

		JPanel innerPanel = (JPanel) outerPanel.getComponent( 0 );
		assertTrue( "Bar".equals( ((TitledBorder) ((CompoundBorder) ((CompoundBorder) innerPanel.getBorder()).getInsideBorder()).getOutsideBorder()).getTitle() ));
		assertTrue( "Def:".equals( ( (JLabel) innerPanel.getComponent( 0 ) ).getText() ) );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JCheckBox );
		assertTrue( "Ghi:".equals( ( (JLabel) innerPanel.getComponent( 2 ) ).getText() ) );
		assertTrue( innerPanel.getComponent( 3 ) instanceof JScrollPane );
		assertTrue( 4 == innerPanel.getComponentCount() );

		innerPanel = (JPanel) outerPanel.getComponent( 1 );
		assertTrue( "Baz".equals( ((TitledBorder) ((CompoundBorder) ((CompoundBorder) innerPanel.getBorder()).getInsideBorder()).getOutsideBorder()).getTitle() ));
		assertTrue( "Jkl:".equals( ( (JLabel) innerPanel.getComponent( 0 ) ).getText() ) );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( innerPanel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == innerPanel.getComponentCount() );

		assertTrue( "Mno:".equals( ( (JLabel) outerPanel.getComponent( 2 ) ).getText() ) );
		assertTrue( outerPanel.getComponent( 3 ) instanceof JCheckBox );

		innerPanel = (JPanel) outerPanel.getComponent( 4 );
		assertTrue( "Moo".equals( ((TitledBorder) ((CompoundBorder) ((CompoundBorder) innerPanel.getBorder()).getInsideBorder()).getOutsideBorder()).getTitle() ));
		assertTrue( "Pqr:".equals( ( (JLabel) innerPanel.getComponent( 0 ) ).getText() ) );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( innerPanel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == innerPanel.getComponentCount() );

		assertTrue( "Stu:".equals( ( (JLabel) metawidget.getComponent( 3 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 4 ) instanceof JTextField );
		assertTrue( 5 == metawidget.getComponentCount() );
	}

	public static void main( String[] args )
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent,SwingMetawidget>().setLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent,SwingMetawidget>().setLayout( new GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		JFrame frame = new JFrame( "Metawidget Tutorial" );
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
		public String	bar;

		@UiComesAfter( "bar" )
		@UiSection( "heading1" )
		public boolean	baz;

		@UiComesAfter( "baz" )
		public String	abc;

		@UiSection( "heading2" )
		public String	def;

		@UiSection( "" )
		@UiLookup( { "foo", "bar" } )
		public String	ghi;

		@UiSection( "heading3" )
		public String	jkl;
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
}
