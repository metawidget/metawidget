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

import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GroupLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLayout()
		throws Exception {

		// Start app

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setMetawidgetLayout( new GroupLayout() );
		metawidget.setToInspect( new PersonAtTutorialStart() );

		// Check what created

		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( ( "age_label" ), metawidget.getComponent( 0 ).getName() );
		assertEquals( metawidget.getComponent( "age_label" ), metawidget.getComponent( 0 ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );

		// Check adding a stub

		Stub stub = new Stub();
		stub.setName( "age" );
		metawidget.add( stub );
		metawidget.setToInspect( new PersonAtTutorialStart() );

		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JCheckBox );

		// Check end of tutorial

		metawidget.setConfig( "org/metawidget/swing/layout/metawidget-tutorial-grouplayout.xml" );
		metawidget.setToInspect( new PersonAtTutorialEnd() );
		metawidget.remove( stub );

		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( "Hobbies:", ( (JLabel) metawidget.getComponent( 6 ) ).getText() );
		assertTrue( metawidget.getComponent( 7 ) instanceof JTextField );
		assertEquals( "Employer:", ( (JLabel) metawidget.getComponent( 8 ) ).getText() );
		assertTrue( metawidget.getComponent( 9 ) instanceof JTextField );

		// Check required fields

		metawidget.setToInspect( new Foo() );
		assertEquals( "Bar*:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( "Baz:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );

		metawidget.setReadOnly( true );
		assertEquals( "Bar:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
	}

	public void testMnemonics() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new GroupLayout() );
		metawidget.setToInspect( new MnemonicFoo() );

		JLabel label = (JLabel) metawidget.getComponent( 0 );
		assertEquals( "Abc:", label.getText() );
		assertEquals( metawidget.getComponent( 1 ), label.getLabelFor() );
		assertEquals( KeyEvent.VK_B, label.getDisplayedMnemonic() );
		assertEquals( 1, label.getDisplayedMnemonicIndex() );
	}

	public void testNestedTabsWithGroupLayout() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new GroupLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertEquals( "Abc:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );

		JTabbedPane outerTabbedPane = (JTabbedPane) metawidget.getComponent( 2 );
		assertEquals( "Foo", outerTabbedPane.getTitleAt( 0 ) );
		JPanel outerPanel = (JPanel) outerTabbedPane.getComponent( 0 );
		assertEquals( 4, outerPanel.getComponentCount() );

		JTabbedPane innerTabbedPane = (JTabbedPane) outerPanel.getComponent( 0 );
		assertEquals( "Bar", innerTabbedPane.getTitleAt( 0 ) );
		JPanel innerPanel = (JPanel) innerTabbedPane.getComponent( 0 );
		assertEquals( "Def:", ( (JLabel) innerPanel.getComponent( 0 ) ).getText() );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JCheckBox );
		assertEquals( "Ghi:", ( (JLabel) innerPanel.getComponent( 2 ) ).getText() );
		assertTrue( innerPanel.getComponent( 3 ) instanceof JScrollPane );
		assertEquals( 4, innerPanel.getComponentCount() );

		assertEquals( "Baz", innerTabbedPane.getTitleAt( 1 ) );
		innerPanel = (JPanel) innerTabbedPane.getComponent( 1 );
		assertEquals( "Jkl:", ( (JLabel) innerPanel.getComponent( 0 ) ).getText() );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JTextField );
		assertEquals( 2, innerPanel.getComponentCount() );

		assertEquals( "Mno:", ( (JLabel) outerPanel.getComponent( 1 ) ).getText() );
		assertTrue( outerPanel.getComponent( 2 ) instanceof JCheckBox );

		innerTabbedPane = (JTabbedPane) outerPanel.getComponent( 3 );
		assertEquals( "Moo", innerTabbedPane.getTitleAt( 0 ) );
		innerPanel = (JPanel) innerTabbedPane.getComponent( 0 );
		assertEquals( "Pqr:", ( (JLabel) innerPanel.getComponent( 0 ) ).getText() );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JTextField );
		assertEquals( 2, innerPanel.getComponentCount() );

		assertEquals( "Stu:", ( (JLabel) metawidget.getComponent( 3 ) ).getText() );
		assertTrue( metawidget.getComponent( 4 ) instanceof JTextField );
		assertEquals( 5, metawidget.getComponentCount() );
	}

	public void testFlatSectionAroundNestedSectionLayoutDecorator() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new GroupLayout() ) ) ) ) );
		metawidget.setToInspect( new Baz() );

		JPanel panel = (JPanel) metawidget.getComponent( 0 );
		assertEquals( "Foo", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertEquals( 5, ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.right );
		assertTrue( panel.getComponent( 1 ) instanceof JSeparator );

		JTabbedPane innerTabbedPane = (JTabbedPane) metawidget.getComponent( 1 );
		assertEquals( "Bar", innerTabbedPane.getTitleAt( 0 ) );
		JPanel innerPanel = (JPanel) innerTabbedPane.getComponent( 0 );
		assertEquals( "Abc:", ( (JLabel) innerPanel.getComponent( 0 ) ).getText() );
		assertTrue( innerPanel.getComponent( 1 ) instanceof JTextField );
		assertEquals( 2, innerPanel.getComponentCount() );

		panel = (JPanel) metawidget.getComponent( 2 );
		assertEquals( "Baz", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertEquals( 5, ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.right );
		assertTrue( panel.getComponent( 1 ) instanceof JSeparator );

		assertEquals( "Def:", ( (JLabel) metawidget.getComponent( 3 ) ).getText() );
		assertTrue( metawidget.getComponent( 4 ) instanceof JCheckBox );
		assertEquals( 5, metawidget.getComponentCount() );
	}

	//
	// Inner class
	//

	static class PersonAtTutorialStart {

		private String	mName;

		private int		mAge;

		private boolean	mRetired;

		public String getName() {

			return mName;
		}

		public void setName( String name ) {

			mName = name;
		}

		public int getAge() {

			return mAge;
		}

		public void setAge( int age ) {

			mAge = age;
		}

		public boolean isRetired() {

			return mRetired;
		}

		public void setRetired( boolean retired ) {

			mRetired = retired;
		}
	}

	static class PersonAtTutorialEnd {

		private String	mName;

		private int		mAge;

		private boolean	mRetired;

		private String	mHobbies;

		private String	mEmployer;

		public String getName() {

			return mName;
		}

		public void setName( String name ) {

			mName = name;
		}

		@UiComesAfter( "name" )
		public int getAge() {

			return mAge;
		}

		public void setAge( int age ) {

			mAge = age;
		}

		@UiComesAfter( "age" )
		public boolean isRetired() {

			return mRetired;
		}

		public void setRetired( boolean retired ) {

			mRetired = retired;
		}

		@UiComesAfter( "retired" )
		public String getHobbies() {

			return mHobbies;
		}

		public void setHobbies( String hobbies ) {

			mHobbies = hobbies;
		}

		@UiComesAfter( "hobbies" )
		public String getEmployer() {

			return mEmployer;
		}

		public void setEmployer( String employer ) {

			mEmployer = employer;
		}
	}

	static class Foo {

		@UiAttribute( name = "required", value = "true" )
		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}

		@UiRequired
		@UiReadOnly
		public String getBaz() {

			return null;
		}

		public void setBaz( @SuppressWarnings( "unused" ) String baz ) {

			// Do nothing
		}
	}

	public static class MnemonicFoo {

		@UiLabel( "A&bc" )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

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

	static class Baz {

		@UiSection( { "Foo", "Bar" } )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiSection( { "Baz" } )
		public boolean isDef() {

			return false;
		}

		public void setDef( @SuppressWarnings( "unused" ) boolean def ) {

			// Do nothing
		}
	}
}
