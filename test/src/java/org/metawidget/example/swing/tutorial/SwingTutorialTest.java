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

package org.metawidget.example.swing.tutorial;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayoutConfig;

/**
 * @author Richard Kennard
 */

public class SwingTutorialTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testTutorial()
		throws Exception
	{
		// Check start of tutorial

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new Person() );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( "Retired:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( metawidget.getComponent( 6 ) instanceof JPanel );
		assertTrue( 7 == metawidget.getComponentCount() );

		// Check end of tutorial

		Stub stub = new Stub();
		stub.setName( "retired" );
		metawidget.add( stub );
		metawidget.setConfig( "org/metawidget/example/swing/tutorial/metawidget.xml" );
		metawidget.setMetawidgetLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ) ) );
		metawidget.setToInspect( new PersonAtTutorialEnd() );

		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 2 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( "Gender:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 4 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 5 ) instanceof JComboBox );
		assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 5 ) ).getModel().getSize() );
		assertTrue( "Notes:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 6 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 7 ) instanceof JScrollPane );

		JPanel panel = (JPanel) metawidget.getComponent( 8 );
		assertTrue( "Work".equals( ( (JLabel) panel.getComponent( 0 ) ).getText() ) );
		assertTrue( panel.getComponent( 1 ) instanceof JSeparator );
		SwingMetawidget nestedMetawidget = (SwingMetawidget) panel.getComponent( 2 );
		assertTrue( panel.getComponentCount() == 3 );

		assertTrue( "Employer:".equals( ( (JLabel) nestedMetawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( ((Stub) nestedMetawidget.getComponent( 1 )).getComponent( 0 ) instanceof JTextField );
		assertTrue( "Department:".equals( ( (JLabel) nestedMetawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( ((Stub) nestedMetawidget.getComponent( 3 )).getComponent( 0 ) instanceof JTextField );
		assertTrue( nestedMetawidget.getComponent( 4 ) instanceof JPanel );
		assertTrue( 5 == nestedMetawidget.getComponentCount() );

		assertTrue( 9 == metawidget.getComponentCount() );
	}

	public void testSectionAtEnd()
		throws Exception
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new PersonWithSectionAtEnd() );
		assertTrue( "Age:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( "Name:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );

		assertTrue( "foo".equals( ( (JLabel) ( (JPanel) metawidget.getComponent( 4 ) ).getComponent( 0 ) ).getText() ) );
		assertTrue( ( (JPanel) metawidget.getComponent( 4 ) ).getComponent( 1 ) instanceof JSeparator );
		SwingMetawidget nestedMetawidget = (SwingMetawidget) ( (JPanel) metawidget.getComponent( 4 ) ).getComponent( 2 );

		assertTrue( "Retired:".equals( ( (JLabel) nestedMetawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( ( (Stub) nestedMetawidget.getComponent( 1 ) ).getComponent( 0 ) instanceof JCheckBox );
		assertTrue( nestedMetawidget.getComponent( 2 ) instanceof JPanel );
		assertTrue( 5 == metawidget.getComponentCount() );
	}

	/**
	 * Check JFrame.addNotify bug (only see this if the JFrame actually tries to display)
	 */

	public void testAddNotify()
	{
		// Data model

		Person person = new Person();

		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( person );
		Stub stub = new Stub();
		stub.setName( "retired" );
		metawidget.add( stub );

		// JFrame

		JFrame frame = new JFrame( "Metawidget Tutorial" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 210 );
		frame.addNotify();
	}

	//
	// Inner class
	//

	static class PersonWithSectionAtEnd
	{
		public String	name;

		public int		age;

		@UiSection( "foo" )
		public boolean	retired;
	}

	public static class PersonAtTutorialEnd
	{
		public String	name;

		@UiComesAfter( "name" )
		public int		age;

		@UiComesAfter( "age" )
		public boolean	retired;

		@UiComesAfter( "retired" )
		public Gender	gender;

		public enum Gender
		{
			Male, Female
		}

		@UiComesAfter( "gender" )
		@UiLarge
		public String	notes;

		@UiComesAfter( "notes" )
		@UiSection( "Work" )
		public String	employer;

		@UiComesAfter( "employer" )
		public String	department;
	}
}
