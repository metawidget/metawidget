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
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.example.swing.tutorial.Person;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
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
		metawidget.setToInspect( new Person() );

		// Check what created

		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( ( "age_label" ), metawidget.getComponent( 0 ).getName() );
		assertTrue( metawidget.getComponent( "age_label" ) == metawidget.getComponent( 0 ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );

		// Check adding a stub

		Stub stub = new Stub();
		stub.setName( "age" );
		metawidget.add( stub );
		metawidget.setToInspect( new Person() );

		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JCheckBox );

		// Check end of tutorial

		metawidget.setConfig( "org/metawidget/example/swing/tutorial/metawidget.xml" );
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
		assertTrue( 1 == label.getDisplayedMnemonicIndex() );
	}

	//
	// Inner class
	//

	static class PersonAtTutorialEnd {

		public String	name;

		@UiComesAfter( "name" )
		public int		age;

		@UiComesAfter( "age" )
		public boolean	retired;

		@UiComesAfter( "retired" )
		public String	hobbies;

		@UiComesAfter( "hobbies" )
		public String	employer;
	}

	static class Foo {

		@UiAttribute( name = "required", value = "true" )
		public String	bar;

		@UiRequired
		@UiReadOnly
		public String	baz;
	}

	public static class MnemonicFoo {

		@UiLabel( "A&bc" )
		public String	abc;
	}
}
