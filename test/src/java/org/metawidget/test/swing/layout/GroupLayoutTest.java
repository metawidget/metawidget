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

package org.metawidget.test.swing.layout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.example.swing.tutorial.Person;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GroupLayout;

/**
 * @author Richard Kennard
 */

public class GroupLayoutTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public GroupLayoutTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testLayout()
		throws Exception
	{
		// Start app

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setLayoutClass( GroupLayout.class );
		metawidget.setToInspect( new Person() );

		// Check what created

		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );

		// Check adding a stub

		Stub stub = new Stub();
		stub.setName( "age" );
		metawidget.add( stub );
		metawidget.setToInspect( new Person() );

		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JCheckBox );

		// Check end of tutorial

		metawidget.setConfig( "org/metawidget/example/swing/tutorial/inspector-config.xml" );
		metawidget.setToInspect( new PersonAtTutorialEnd() );
		metawidget.remove( stub );

		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( "Hobbies:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 7 ) instanceof JTextField );
		assertTrue( "Work".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 9 ) instanceof JSeparator );
		assertTrue( "Employer:".equals( ( (JLabel) metawidget.getComponent( 10 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 11 ) instanceof JTextField );

		// Check required fields

		metawidget.setToInspect( new Foo() );
		assertTrue( "Bar*:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( "Baz:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );

		metawidget.setReadOnly( true );
		assertTrue( "Bar:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
	}

	//
	// Inner class
	//

	static class PersonAtTutorialEnd
	{
		public String	name;

		@UiComesAfter( "name" )
		public int		age;

		@UiComesAfter( "age" )
		public boolean	retired;

		@UiComesAfter( "retired" )
		public String	hobbies;

		@UiSection( "Work" )
		@UiComesAfter( "hobbies" )
		public String	employer;
	}

	static class Foo
	{
		@UiAttribute( name = "required", value = "true" )
		public String bar;

		@UiRequired
		@UiReadOnly
		public String baz;
	}
}
