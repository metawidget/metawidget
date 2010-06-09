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

package org.metawidget.example.swt.tutorial;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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
import org.metawidget.inspector.java5.Java5Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
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
	extends TestCase
{
	//
	// Public statics
	//

	public static void main( String[] args )
	{
		Display display = new Display();
		Shell shell = new Shell( display, SWT.DIALOG_TRIM | SWT.RESIZE );
		shell.setLayout( new FillLayout() );

		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.setConfig( "org/metawidget/example/swt/tutorial/metawidget.xml" );
		metawidget.setToInspect( new PersonAtTutorialEnd() );

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() )
		{
			if ( !display.readAndDispatch() )
			{
				display.sleep();
			}
		}

		display.dispose();
	}

	//
	// Public methods
	//

	public void testTutorial()
		throws Exception
	{
		// Check start of tutorial

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new Person() );
		assertEquals( "Age:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Spinner );
		assertEquals( "Name:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );
		assertEquals( "Retired:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Button );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertTrue( 6 == metawidget.getChildren().length );

		// Check middle of tutorial

		CompositeInspectorConfig inspectorConfig = new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector(), new MetawidgetAnnotationInspector(), new Java5Inspector() );
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
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertEquals( "Gender:", ( (Label) metawidget.getChildren()[6] ).getText() );
		assertTrue( metawidget.getChildren()[7] instanceof Combo );
		assertTrue( 3 == ( (Combo) metawidget.getChildren()[7] ).getItemCount() );
		assertEquals( "Notes:", ( (Label) metawidget.getChildren()[8] ).getText() );
		assertTrue( metawidget.getChildren()[9] instanceof Text );
		assertTrue( ( (GridData) metawidget.getChildren()[9].getLayoutData() ).grabExcessVerticalSpace );
		assertTrue( ( metawidget.getChildren()[9].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( metawidget.getChildren()[9].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( metawidget.getChildren()[9].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( metawidget.getChildren()[9].getStyle() & SWT.WRAP ) == SWT.WRAP );
		assertTrue( ( metawidget.getChildren()[9].getStyle() & SWT.H_SCROLL ) == SWT.NONE );

		Composite separator = (Composite) metawidget.getChildren()[10];
		assertTrue( ((GridLayout) separator.getLayout() ).marginWidth == 0 );
		assertEquals( "Work", ( (Label) separator.getChildren()[0] ).getText() );
		assertTrue( separator.getChildren()[1] instanceof Label );
		assertTrue( ( separator.getChildren()[1].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );

		assertEquals( "Employer:", ( (Label) metawidget.getChildren()[11] ).getText() );
		assertTrue( metawidget.getChildren()[12] instanceof Text );
		assertEquals( "Department:", ( (Label) metawidget.getChildren()[13] ).getText() );
		assertTrue( metawidget.getChildren()[14] instanceof Text );

		assertTrue( 15 == metawidget.getChildren().length );

		// Check end of tutorial

		Stub stub = new Stub( metawidget, SWT.NONE );
		stub.setData( NAME, "retired" );
		metawidget.setConfig( "org/metawidget/example/swt/tutorial/metawidget.xml" );

		assertEquals( "Name:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( "Age:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Spinner );
		assertTrue( metawidget.getChildren()[4] instanceof Stub );
		assertTrue( ( (GridData) metawidget.getChildren()[4].getLayoutData() ).exclude );
		assertEquals( "Gender:", ( (Label) metawidget.getChildren()[5] ).getText() );
		assertTrue( metawidget.getChildren()[6] instanceof Combo );
		assertTrue( 3 == ( (Combo) metawidget.getChildren()[6] ).getItemCount() );
		assertEquals( "Notes:", ( (Label) metawidget.getChildren()[7] ).getText() );
		assertTrue( metawidget.getChildren()[8] instanceof Text );
		assertTrue( ( (GridData) metawidget.getChildren()[8].getLayoutData() ).grabExcessVerticalSpace );
		assertTrue( ( metawidget.getChildren()[8].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( metawidget.getChildren()[8].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( metawidget.getChildren()[8].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( metawidget.getChildren()[8].getStyle() & SWT.WRAP ) == SWT.WRAP );
		assertTrue( ( metawidget.getChildren()[8].getStyle() & SWT.H_SCROLL ) == SWT.NONE );

		TabFolder tabFolder = (TabFolder) metawidget.getChildren()[9];
		assertEquals( "Work", tabFolder.getItem( 0 ).getText() );

		Composite panel = (Composite) tabFolder.getItem( 0 ).getControl();
		assertEquals( "Employer:", ( (Label) panel.getChildren()[0] ).getText() );
		assertTrue( panel.getChildren()[1] instanceof Text );
		assertEquals( "Department:", ( (Label) panel.getChildren()[2] ).getText() );
		assertTrue( panel.getChildren()[3] instanceof Text );

		assertTrue( 10 == metawidget.getChildren().length );
	}

	public void testSectionAtEnd()
		throws Exception
	{
		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new PersonWithSectionAtEnd() );
		assertEquals( "Age:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Spinner );
		assertEquals( "Name:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );

		Composite separator = (Composite) metawidget.getChildren()[4];
		assertTrue( ((GridLayout) separator.getLayout() ).marginWidth == 0 );
		assertEquals( "foo", ( (Label) separator.getChildren()[0] ).getText() );
		assertTrue( separator.getChildren()[1] instanceof Label );
		assertTrue( ( separator.getChildren()[1].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );

		assertEquals( "Retired:", ( (Label) metawidget.getChildren()[5] ).getText() );
		assertTrue( metawidget.getChildren()[6] instanceof Button );
		assertTrue( ( metawidget.getChildren()[6].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertTrue( 7 == metawidget.getChildren().length );
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
