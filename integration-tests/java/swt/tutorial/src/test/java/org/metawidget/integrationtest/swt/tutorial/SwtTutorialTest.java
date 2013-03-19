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

package org.metawidget.integrationtest.swt.tutorial;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.layout.GridLayoutConfig;
import org.metawidget.swt.layout.SeparatorLayoutDecorator;
import org.metawidget.swt.layout.SeparatorLayoutDecoratorConfig;

/**
 * Tests doing the Swing tutorial using the <code>SwtMetawidget</code>, even though this is not
 * specifically covered in the Reference Documentation.
 *
 * @author Richard Kennard
 */

public class SwtTutorialTest
	extends TestCase {

	//
	// Private statics
	//

	private static final Display	TEST_DISPLAY	= new Display();

	//
	// Public statics
	//

	public static void main( String[] args ) {

		// Data model

		Person person = new Person();

		// Metawidget

		Display display = new Display();
		Shell shell = new Shell( display );
		shell.setLayout( new MigLayout( new LC().fill().debug( 500 ) ) );

		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.None );
		metawidget.setLayoutData( new CC().height( "200px" ).width( "200px" ) );
		metawidget.setToInspect( person );

		// Shell

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}

		display.dispose();
	}

	//
	// Public methods
	//

	public void testTutorial()
		throws Exception {

		// Check start of tutorial

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new Person() );
		assertEquals( "Age:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Spinner );
		assertEquals( "Name:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );
		assertEquals( "Retired:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertEquals( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ), SWT.CHECK );
		assertEquals( 6, metawidget.getChildren().length );

		// Check middle of tutorial

		CompositeInspectorConfig inspectorConfig = new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector(), new MetawidgetAnnotationInspector() );
		metawidget.setInspector( new CompositeInspector( inspectorConfig ) );
		GridLayoutConfig nestedLayoutConfig = new GridLayoutConfig().setNumberOfColumns( 2 );
		SeparatorLayoutDecoratorConfig layoutConfig = new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swt.layout.GridLayout( nestedLayoutConfig ) );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( layoutConfig ) );
		metawidget.setToInspect( new PersonAtTutorialEnd() );

		assertEquals( "Name:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( "Age:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Spinner );
		assertEquals( "Retired:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertEquals( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ), SWT.CHECK );
		assertEquals( "Gender:", ( (Label) metawidget.getChildren()[6] ).getText() );
		assertTrue( metawidget.getChildren()[7] instanceof Combo );
		assertEquals( 3, ( (Combo) metawidget.getChildren()[7] ).getItemCount() );
		assertEquals( "Notes:", ( (Label) metawidget.getChildren()[8] ).getText() );
		assertTrue( metawidget.getChildren()[9] instanceof Text );
		assertTrue( ( (GridData) metawidget.getChildren()[9].getLayoutData() ).grabExcessVerticalSpace );
		assertEquals( ( metawidget.getChildren()[9].getStyle() & SWT.MULTI ), SWT.MULTI );
		assertEquals( ( metawidget.getChildren()[9].getStyle() & SWT.BORDER ), SWT.BORDER );
		assertEquals( ( metawidget.getChildren()[9].getStyle() & SWT.V_SCROLL ), SWT.V_SCROLL );
		assertEquals( ( metawidget.getChildren()[9].getStyle() & SWT.WRAP ), SWT.WRAP );
		assertEquals( ( metawidget.getChildren()[9].getStyle() & SWT.H_SCROLL ), SWT.NONE );

		Composite separator = (Composite) metawidget.getChildren()[10];
		assertEquals( ( (GridLayout) separator.getLayout() ).marginWidth, 0 );
		assertEquals( "Work", ( (Label) separator.getChildren()[0] ).getText() );
		assertTrue( separator.getChildren()[1] instanceof Label );
		assertEquals( ( separator.getChildren()[1].getStyle() & SWT.SEPARATOR ), SWT.SEPARATOR );

		assertEquals( "Employer:", ( (Label) metawidget.getChildren()[11] ).getText() );
		assertTrue( metawidget.getChildren()[12] instanceof Text );
		assertEquals( "Department:", ( (Label) metawidget.getChildren()[13] ).getText() );
		assertTrue( metawidget.getChildren()[14] instanceof Text );

		assertEquals( 15, metawidget.getChildren().length );

		// Check end of tutorial

		Stub stub = new Stub( metawidget, SWT.NONE );
		stub.setData( NAME, "retired" );
		metawidget.setConfig( "org/metawidget/integrationtest/swt/tutorial/metawidget.xml" );

		assertEquals( "Name:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( "Age:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Spinner );
		assertTrue( metawidget.getChildren()[4] instanceof Stub );
		assertTrue( ( (GridData) metawidget.getChildren()[4].getLayoutData() ).exclude );
		assertEquals( "Gender:", ( (Label) metawidget.getChildren()[5] ).getText() );
		assertTrue( metawidget.getChildren()[6] instanceof Combo );
		assertEquals( 3, ( (Combo) metawidget.getChildren()[6] ).getItemCount() );
		assertEquals( "Notes:", ( (Label) metawidget.getChildren()[7] ).getText() );
		assertTrue( metawidget.getChildren()[8] instanceof Text );
		assertTrue( ( (GridData) metawidget.getChildren()[8].getLayoutData() ).grabExcessVerticalSpace );
		assertEquals( ( metawidget.getChildren()[8].getStyle() & SWT.MULTI ), SWT.MULTI );
		assertEquals( ( metawidget.getChildren()[8].getStyle() & SWT.BORDER ), SWT.BORDER );
		assertEquals( ( metawidget.getChildren()[8].getStyle() & SWT.V_SCROLL ), SWT.V_SCROLL );
		assertEquals( ( metawidget.getChildren()[8].getStyle() & SWT.WRAP ), SWT.WRAP );
		assertEquals( ( metawidget.getChildren()[8].getStyle() & SWT.H_SCROLL ), SWT.NONE );

		TabFolder tabFolder = (TabFolder) metawidget.getChildren()[9];
		assertEquals( "Work", tabFolder.getItem( 0 ).getText() );

		Composite panel = (Composite) tabFolder.getItem( 0 ).getControl();
		assertEquals( "Employer:", ( (Label) panel.getChildren()[0] ).getText() );
		assertTrue( panel.getChildren()[1] instanceof Text );
		assertEquals( "Department:", ( (Label) panel.getChildren()[2] ).getText() );
		assertTrue( panel.getChildren()[3] instanceof Text );

		assertEquals( 10, metawidget.getChildren().length );
	}

	public void testSectionAtEnd()
		throws Exception {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new PersonWithSectionAtEnd() );
		assertEquals( "Age:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Spinner );
		assertEquals( "Name:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );

		Composite separator = (Composite) metawidget.getChildren()[4];
		assertEquals( ( (GridLayout) separator.getLayout() ).marginWidth, 0 );
		assertEquals( "foo", ( (Label) separator.getChildren()[0] ).getText() );
		assertTrue( separator.getChildren()[1] instanceof Label );
		assertEquals( ( separator.getChildren()[1].getStyle() & SWT.SEPARATOR ), SWT.SEPARATOR );

		assertEquals( "Retired:", ( (Label) metawidget.getChildren()[5] ).getText() );
		assertTrue( metawidget.getChildren()[6] instanceof Button );
		assertEquals( ( metawidget.getChildren()[6].getStyle() & SWT.CHECK ), SWT.CHECK );
		assertEquals( 7, metawidget.getChildren().length );
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
