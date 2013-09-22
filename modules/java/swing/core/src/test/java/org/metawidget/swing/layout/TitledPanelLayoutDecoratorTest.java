// Metawidget (licensed under LGPL)
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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TitledPanelLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testTitledPanelLayoutDecorator() {

		SwingMetawidget swingMetawidget = new SwingMetawidget();
		swingMetawidget.setMetawidgetLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>().setLayout( new GridBagLayout() ) ) );
		swingMetawidget.setToInspect( new Foo() );

		Facet facet = new Facet();
		facet.setName( "buttons" );
		swingMetawidget.add( facet );

		assertEquals( "Bar:", ( (JLabel) swingMetawidget.getComponent( 0 ) ).getText() );
		assertTrue( swingMetawidget.getComponent( 1 ) instanceof JTextField );

		// Heading #1

		JPanel panel = (JPanel) swingMetawidget.getComponent( 2 );
		assertEquals( 5, ( (EmptyBorder) ( (CompoundBorder) panel.getBorder() ).getOutsideBorder() ).getBorderInsets().top );
		assertEquals( "heading1", ( (TitledBorder) ( (CompoundBorder) ( (CompoundBorder) panel.getBorder() ).getInsideBorder() ).getOutsideBorder() ).getTitle() );
		assertEquals( 3, ( (EmptyBorder) ( (CompoundBorder) ( (CompoundBorder) panel.getBorder() ).getInsideBorder() ).getInsideBorder() ).getBorderInsets().top );
		assertEquals( "Baz:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( panel.getComponent( 1 ) instanceof JCheckBox );
		assertEquals( "Abc:", ( (JLabel) panel.getComponent( 2 ) ).getText() );
		assertTrue( panel.getComponent( 3 ) instanceof JTextField );
		assertTrue( panel.getComponent( 4 ) instanceof JPanel );
		assertEquals( 5, panel.getComponentCount() );

		// Heading #2

		panel = (JPanel) swingMetawidget.getComponent( 3 );
		assertEquals( "heading2", ( (TitledBorder) ( (CompoundBorder) ( (CompoundBorder) panel.getBorder() ).getInsideBorder() ).getOutsideBorder() ).getTitle() );
		assertEquals( "Def:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, panel.getComponentCount() );

		// Separate component

		assertEquals( "Ghi:", ( (JLabel) swingMetawidget.getComponent( 4 ) ).getText() );
		assertTrue( swingMetawidget.getComponent( 5 ) instanceof JComboBox );

		// Heading #3

		panel = (JPanel) swingMetawidget.getComponent( 6 );
		assertEquals( "heading3", ( (TitledBorder) ( (CompoundBorder) ( (CompoundBorder) panel.getBorder() ).getInsideBorder() ).getOutsideBorder() ).getTitle() );
		assertEquals( "Jkl:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, panel.getComponentCount() );

		assertEquals( facet, swingMetawidget.getComponent( 7 ) );
		assertEquals( 8, swingMetawidget.getComponentCount() );
	}

	public void testNestedPanels() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>().setLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>().setLayout( new GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertEquals( "Abc:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );

		JPanel outerPanel = (JPanel) metawidget.getComponent( 2 );
		assertEquals( "Foo", ( (TitledBorder) ( (CompoundBorder) ( (CompoundBorder) outerPanel.getBorder() ).getInsideBorder() ).getOutsideBorder() ).getTitle() );
		assertEquals( 5, outerPanel.getComponentCount() );

		JPanel innerPanel = (JPanel) outerPanel.getComponent( 0 );
		assertEquals( "Bar", ( (TitledBorder) ( (CompoundBorder) ( (CompoundBorder) innerPanel.getBorder() ).getInsideBorder() ).getOutsideBorder() ).getTitle() );
		assertEquals( "Def:", ( (JLabel) innerPanel.getComponent( 0 ) ).getText() );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JCheckBox );
		assertEquals( "Ghi:", ( (JLabel) innerPanel.getComponent( 2 ) ).getText() );
		assertTrue( innerPanel.getComponent( 3 ) instanceof JScrollPane );
		assertEquals( 4, innerPanel.getComponentCount() );

		innerPanel = (JPanel) outerPanel.getComponent( 1 );
		assertEquals( "Baz", ( (TitledBorder) ( (CompoundBorder) ( (CompoundBorder) innerPanel.getBorder() ).getInsideBorder() ).getOutsideBorder() ).getTitle() );
		assertEquals( "Jkl:", ( (JLabel) innerPanel.getComponent( 0 ) ).getText() );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( innerPanel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, innerPanel.getComponentCount() );

		assertEquals( "Mno:", ( (JLabel) outerPanel.getComponent( 2 ) ).getText() );
		assertTrue( outerPanel.getComponent( 3 ) instanceof JCheckBox );

		innerPanel = (JPanel) outerPanel.getComponent( 4 );
		assertEquals( "Moo", ( (TitledBorder) ( (CompoundBorder) ( (CompoundBorder) innerPanel.getBorder() ).getInsideBorder() ).getOutsideBorder() ).getTitle() );
		assertEquals( "Pqr:", ( (JLabel) innerPanel.getComponent( 0 ) ).getText() );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( innerPanel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, innerPanel.getComponentCount() );

		assertEquals( "Stu:", ( (JLabel) metawidget.getComponent( 3 ) ).getText() );
		assertTrue( metawidget.getComponent( 4 ) instanceof JTextField );
		assertEquals( 5, metawidget.getComponentCount() );
	}

	public static void main( String[] args ) {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>().setLayout( new TitledPanelLayoutDecorator( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>().setLayout( new GridBagLayout() ) ) ) ) );
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

	public static class Foo {

		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}

		@UiComesAfter( "bar" )
		@UiSection( "heading1" )
		public boolean isBaz() {

			return false;
		}

		public void setBaz( @SuppressWarnings( "unused" ) boolean baz ) {

			// Do nothing
		}

		@UiComesAfter( "baz" )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiSection( "heading2" )
		public String getDef() {

			return null;
		}

		public void setDef( @SuppressWarnings( "unused" ) String def ) {

			// Do nothing
		}

		@UiSection( "" )
		@UiLookup( { "foo", "bar" } )
		public String getGhi() {

			return null;
		}

		public void setGhi( @SuppressWarnings( "unused" ) String ghi ) {

			// Do nothing
		}

		@UiSection( "heading3" )
		public String getJkl() {

			return null;
		}

		public void setJkl( @SuppressWarnings( "unused" ) String jkl ) {

			// Do nothing
		}
	}

	static class Bar {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Bar" } )
		public boolean isDef() {

			return false;
		}

		public void setDef( @SuppressWarnings( "unused" ) boolean def ) {

			// Do nothing
		}

		@UiLarge
		public String getGhi() {

			return null;
		}

		public void setGhi( @SuppressWarnings( "unused" ) String ghi ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Baz" } )
		public String getJkl() {

			return null;
		}

		public void setJkl( @SuppressWarnings( "unused" ) String jkl ) {

			// Do nothing
		}

		@UiSection( { "Foo", "" } )
		public boolean getMno() {

			return false;
		}

		public void setMno( @SuppressWarnings( "unused" ) boolean mno ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Moo" } )
		public String getPqr() {

			return null;
		}

		public void setPqr( @SuppressWarnings( "unused" ) String pqr ) {

			// Do nothing
		}

		@UiSection( "" )
		public String getStu() {

			return null;
		}

		public void setStu( @SuppressWarnings( "unused" ) String stu ) {

			// Do nothing
		}
	}
}
