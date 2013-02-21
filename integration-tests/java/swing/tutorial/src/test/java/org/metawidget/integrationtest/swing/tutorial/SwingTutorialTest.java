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

package org.metawidget.integrationtest.swing.tutorial;

import java.awt.GridBagLayout;

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

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayoutConfig;
import org.metawidget.swing.layout.SeparatorLayoutDecorator;
import org.metawidget.swing.layout.SeparatorLayoutDecoratorConfig;

/**
 * @author Richard Kennard
 */

public class SwingTutorialTest
	extends TestCase {

	//
	// Public methods
	//

	public void testTutorial()
		throws Exception {

		// Check start of tutorial

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new Person() );
		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( metawidget.getComponent( 6 ) instanceof JPanel );
		assertEquals( 7, metawidget.getComponentCount() );

		// Check middle of tutorial

		CompositeInspectorConfig inspectorConfig = new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector(), new MetawidgetAnnotationInspector() );
		metawidget.setInspector( new CompositeInspector( inspectorConfig ) );
		GridBagLayoutConfig nestedLayoutConfig = new GridBagLayoutConfig().setNumberOfColumns( 2 );
		SeparatorLayoutDecoratorConfig layoutConfig = new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( nestedLayoutConfig ) );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( layoutConfig ) );
		metawidget.setToInspect( new PersonAtTutorialEnd() );

		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 2 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 4 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( "Gender:", ( (JLabel) metawidget.getComponent( 6 ) ).getText() );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 6 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 7 ) instanceof JComboBox );
		assertEquals( 3, ( (JComboBox) metawidget.getComponent( 7 ) ).getModel().getSize() );
		assertEquals( "Notes:", ( (JLabel) metawidget.getComponent( 8 ) ).getText() );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 8 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 9 ) instanceof JScrollPane );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 9 ) ) ).weighty );

		JPanel separatorPanel = (JPanel) metawidget.getComponent( 10 );
		assertEquals( "Work", ( (JLabel) separatorPanel.getComponent( 0 ) ).getText() );
		assertTrue( separatorPanel.getComponent( 1 ) instanceof JSeparator );

		assertEquals( "Employer:", ( (JLabel) metawidget.getComponent( 11 ) ).getText() );
		assertTrue( metawidget.getComponent( 12 ) instanceof JTextField );
		assertEquals( "Department:", ( (JLabel) metawidget.getComponent( 13 ) ).getText() );
		assertTrue( metawidget.getComponent( 14 ) instanceof JTextField );

		assertEquals( 15, metawidget.getComponentCount() );

		// Check end of tutorial

		Stub stub = new Stub();
		stub.setName( "retired" );
		metawidget.add( stub );
		metawidget.setConfig( "org/metawidget/integrationtest/swing/tutorial/metawidget.xml" );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ) ) ) ) );

		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertEquals( 2, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 2 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( "Gender:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 4 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 5 ) instanceof JComboBox );
		assertEquals( 3, ( (JComboBox) metawidget.getComponent( 5 ) ).getModel().getSize() );
		assertEquals( "Notes:", ( (JLabel) metawidget.getComponent( 6 ) ).getText() );
		assertEquals( 0, ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 6 ) ) ).gridx );
		assertTrue( metawidget.getComponent( 7 ) instanceof JScrollPane );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( ( metawidget.getComponent( 7 ) ) ).weighty );

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 8 );
		assertEquals( "Work", tabbedPane.getTitleAt( 0 ) );

		JPanel panel = (JPanel) tabbedPane.getComponent( 0 );
		assertEquals( "Employer:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertEquals( 0, ( (GridBagLayout) panel.getLayout() ).getConstraints( ( panel.getComponent( 0 ) ) ).gridx );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Department:", ( (JLabel) panel.getComponent( 2 ) ).getText() );
		assertEquals( 2, ( (GridBagLayout) panel.getLayout() ).getConstraints( ( panel.getComponent( 2 ) ) ).gridx );
		assertTrue( panel.getComponent( 3 ) instanceof JTextField );

		assertEquals( 9, metawidget.getComponentCount() );
	}

	public void testSectionAtEnd()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new PersonWithSectionAtEnd() );
		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );

		JPanel separatorPanel = (JPanel) metawidget.getComponent( 4 );
		assertEquals( "foo", ( (JLabel) separatorPanel.getComponent( 0 ) ).getText() );
		assertTrue( separatorPanel.getComponent( 1 ) instanceof JSeparator );

		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 5 ) ).getText() );
		assertTrue( metawidget.getComponent( 6 ) instanceof JCheckBox );
		assertTrue( metawidget.getComponent( 7 ) instanceof JPanel );
		assertEquals( 8, metawidget.getComponentCount() );
	}

	/**
	 * Check JFrame.addNotify bug (only see this if the JFrame actually tries to display)
	 */

	public void testAddNotify() {

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

	static class PersonWithSectionAtEnd {

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

		@UiSection( "foo" )
		public boolean isRetired() {

			return mRetired;
		}

		public void setRetired( boolean retired ) {

			mRetired = retired;
		}
	}

	public static class PersonAtTutorialEnd {

		private String	mName;

		private int		mAge;

		private boolean	mRetired;

		public enum Gender {
			Male, Female
		}

		private Gender	mGender;

		private String	mNotes;

		private String	mEmployer;

		private String	mDepartment;

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
		public Gender getGender() {

			return mGender;
		}

		public void setGender( Gender gender ) {

			mGender = gender;
		}

		@UiComesAfter( "gender" )
		@UiLarge
		public String getNotes() {

			return mNotes;
		}

		public void setNotes( String notes ) {

			mNotes = notes;
		}

		@UiComesAfter( "notes" )
		@UiSection( "Work" )
		public String getEmployer() {

			return mEmployer;
		}

		public void setEmployer( String employer ) {

			mEmployer = employer;
		}

		@UiComesAfter( "employer" )
		public String getDepartment() {

			return mDepartment;
		}

		public void setDepartment( String department ) {

			mDepartment = department;
		}
	}
}
