// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.swing.widgetbuilder;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CollectionTableModelTest
	extends TestCase {

	//
	// Public methods
	//

	public void testCollections()
		throws Exception {

		// Model

		Person person = new Person();
		person.getAddresses().add( new Address( "Street 1", "City 1", "State 1" ) );
		person.getAddresses().add( new Address( "Street 2", "City 2", "State 2" ) );

		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector(
				new CompositeInspectorConfig().setInspectors(
						new PropertyTypeInspector(),
						new MetawidgetAnnotationInspector() ) ) );
		metawidget.setToInspect( person );

		// Test

		assertEquals( "Name:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Age:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertEquals( "Retired:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertEquals( "Notes:", ( (JLabel) metawidget.getComponent( 8 ) ).getText() );
		assertTrue( metawidget.getComponent( 9 ) instanceof JScrollPane );
		assertTrue( ( (JScrollPane) metawidget.getComponent( 9 ) ).getViewport().getView() instanceof JTextArea );

		assertEquals( "Addresses:", ( (JLabel) metawidget.getComponent( 6 ) ).getText() );
		assertTrue( metawidget.getComponent( 7 ) instanceof JScrollPane );
		JTable table = (JTable) ( (JScrollPane) metawidget.getComponent( 7 ) ).getViewport().getView();
		assertEquals( "The Street", table.getColumnName( 0 ) );
		assertEquals( "City", table.getColumnName( 1 ) );
		assertEquals( "State", table.getColumnName( 2 ) );
		assertEquals( 3, table.getColumnCount() );
		assertEquals( "Street 1", table.getModel().getValueAt( 0, 0 ) );
		assertEquals( "City 1", table.getModel().getValueAt( 0, 1 ) );
		assertEquals( "State 1", table.getModel().getValueAt( 0, 2 ) );
		assertEquals( "Street 2", table.getModel().getValueAt( 1, 0 ) );
		assertEquals( "City 2", table.getModel().getValueAt( 1, 1 ) );
		assertEquals( "State 2", table.getModel().getValueAt( 1, 2 ) );
		assertEquals( 2, table.getRowCount() );
	}

	public void testDirectCollection()
		throws Exception {

		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector(
				new CompositeInspectorConfig().setInspectors(
						new PropertyTypeInspector(),
						new MetawidgetAnnotationInspector() ) ) );
		metawidget.setPath( List.class.getName() + "<" + Address.class.getName() + ">" );

		// Test

		assertTrue( metawidget.getComponent( 0 ) instanceof JScrollPane );
		JTable table = (JTable) ( (JScrollPane) metawidget.getComponent( 0 ) ).getViewport().getView();
		assertEquals( "The Street", table.getColumnName( 0 ) );
		assertEquals( "City", table.getColumnName( 1 ) );
		assertEquals( "State", table.getColumnName( 2 ) );
		assertEquals( 3, table.getColumnCount() );
		assertEquals( 0, table.getRowCount() );
	}

	//
	// Inner class
	//

	public static class Person {

		private String			mName;

		private int				mAge;

		private boolean			mRetired;

		private List<Address>	mAddresses	= CollectionUtils.newArrayList();

		private String			mNotes;

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
		public List<Address> getAddresses() {

			return mAddresses;
		}

		public void setAddresses( List<Address> addresses ) {

			mAddresses = addresses;
		}

		@UiLarge
		@UiComesAfter( "addresses" )
		public String getNotes() {

			return mNotes;
		}

		public void setNotes( String notes ) {

			mNotes = notes;
		}
	}

	public static class Address {

		private String	mStreet;

		private String	mCity;

		private String	mState;

		public Address( String street, String city, String state ) {

			mStreet = street;
			mCity = city;
			mState = state;
		}

		@UiLabel( "The Street" )
		public String getStreet() {

			return mStreet;
		}

		public void setStreet( String street ) {

			mStreet = street;
		}

		@UiComesAfter( "street" )
		public String getCity() {

			return mCity;
		}

		public void setCity( String city ) {

			mCity = city;
		}

		@UiComesAfter( "city" )
		public String getState() {

			return mState;
		}

		public void setState( String state ) {

			mState = state;
		}
	}
}
