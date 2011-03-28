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

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author Richard Kennard
 */

public class SeparatorLayoutDecoratorTest
	extends TestCase {

	//
	// Constructor
	//
	
	public SeparatorLayoutDecoratorTest( String name ) {
		
		super( name );
	}
	
	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( SeparatorLayoutDecoratorConfig.class, new SeparatorLayoutDecoratorConfig() {
			// Subclass
		} );
	}

	public void testAlignment() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new Foo() );

		JPanel panel = (JPanel) metawidget.getComponent( 0 );
		assertEquals( "Section", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( 0 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.left );
		assertTrue( 5 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.right );
		assertTrue( panel.getComponent( 1 ) instanceof JSeparator );
		assertEquals( "Bar:", ( (JLabel) metawidget.getComponent( 1 ) ).getText() );
		assertTrue( metawidget.getComponent( 2 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 3 ) instanceof JPanel );
		assertTrue( 4 == metawidget.getComponentCount() );

		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setAlignment( SwingConstants.RIGHT ).setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) );
		panel = (JPanel) metawidget.getComponent( 0 );
		assertEquals( "Section", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( 1 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).gridx );
		assertTrue( 5 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.left );
		assertTrue( 0 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.right );
		assertTrue( panel.getComponent( 1 ) instanceof JSeparator );
		assertTrue( 0 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 1 ) ).gridx );
		assertEquals( "Bar:", ( (JLabel) metawidget.getComponent( 1 ) ).getText() );
		assertTrue( metawidget.getComponent( 2 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 3 ) instanceof JPanel );
		assertTrue( 4 == metawidget.getComponentCount() );
	}

	public void testNestedSeparators() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertEquals( "Abc:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );

		JPanel outerSeparator = (JPanel) metawidget.getComponent( 2 );
		assertEquals( "Foo", ( (JLabel) outerSeparator.getComponent( 0 ) ).getText() );

		JPanel innerSeparator = (JPanel) metawidget.getComponent( 3 );
		assertEquals( "Bar", ( (JLabel) innerSeparator.getComponent( 0 ) ).getText() );
		assertEquals( "Def:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( "Ghi:", ( (JLabel) metawidget.getComponent( 6 ) ).getText() );
		assertTrue( metawidget.getComponent( 7 ) instanceof JScrollPane );

		innerSeparator = (JPanel) metawidget.getComponent( 8 );
		assertEquals( "Baz", ( (JLabel) innerSeparator.getComponent( 0 ) ).getText() );
		assertEquals( "Jkl:", ( (JLabel) metawidget.getComponent( 9 ) ).getText() );
		assertTrue( metawidget.getComponent( 10 ) instanceof JTextField );

		assertEquals( "Mno:", ( (JLabel) metawidget.getComponent( 11 ) ).getText() );
		assertTrue( metawidget.getComponent( 12 ) instanceof JCheckBox );

		innerSeparator = (JPanel) metawidget.getComponent( 13 );
		assertEquals( "Moo", ( (JLabel) innerSeparator.getComponent( 0 ) ).getText() );
		assertEquals( "Pqr:", ( (JLabel) metawidget.getComponent( 14 ) ).getText() );
		assertTrue( metawidget.getComponent( 15 ) instanceof JTextField );

		innerSeparator = (JPanel) metawidget.getComponent( 16 );
		assertEquals( "Zoo", ( (JLabel) innerSeparator.getComponent( 0 ) ).getText() );
		assertEquals( "Zoo:", ( (JLabel) metawidget.getComponent( 17 ) ).getText() );
		SwingMetawidget nestedMetawidget = (SwingMetawidget) metawidget.getComponent( 18 );
		assertEquals( "Name:", ( (JLabel) nestedMetawidget.getComponent( 0 ) ).getText() );

		assertEquals( "Stu:", ( (JLabel) metawidget.getComponent( 19 ) ).getText() );
		assertTrue( metawidget.getComponent( 20 ) instanceof JTextField );
		assertTrue( 21 == metawidget.getComponentCount() );
	}

	public void testEmptyStub() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) );
		metawidget.setToInspect( new Baz() );

		assertTrue( metawidget.getLayout() instanceof GridBagLayout );
		assertTrue( metawidget.getComponent( 0 ) instanceof JPanel );
		assertTrue( false == metawidget.getComponent( 0 ).isOpaque() );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridy );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).weighty );

		assertTrue( 1 == metawidget.getComponentCount() );

		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Baz() );

		assertTrue( metawidget.getLayout() instanceof GridBagLayout );
		assertTrue( metawidget.getComponent( 0 ) instanceof JPanel );
		assertTrue( false == metawidget.getComponent( 0 ).isOpaque() );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).gridy );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 0 ) ).weighty );

		assertTrue( 1 == metawidget.getComponentCount() );
	}

	public static void main( String[] args ) {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) ) ) );
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

	static class Foo {

		@UiSection( "Section" )
		public String	bar;
	}

	public static class Bar {

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

		@UiSection( { "Foo", "Zoo" } )
		public Zoo		zoo	= new Zoo();

		@UiSection( "" )
		@UiComesAfter( "zoo" )
		public String	stu;
	}

	static class Baz {

		@UiSection( "Section" )
		@UiHidden
		public String	abc;
	}

	public static class Zoo {

		public String	name;
	}
}
