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

package org.metawidget.swing;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Richard Kennard
 */

public class SwingCollectionsTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
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
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>(
				new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders(
						new CollectionWidgetBuilder(),
						new SwingWidgetBuilder() ) ) );
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
		assertEquals( "Street", table.getColumnName( 0 ) );
		assertEquals( "City", table.getColumnName( 1 ) );
		assertEquals( "State", table.getColumnName( 2 ) );
		assertEquals( 3, table.getColumnCount() );
		assertEquals( 2, table.getRowCount() );
	}

	@SuppressWarnings( "unchecked" )
	public void testDirectCollection()
		throws Exception {

		// Metawidget

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector(
				new CompositeInspectorConfig().setInspectors(
						new PropertyTypeInspector(),
						new MetawidgetAnnotationInspector() ) ) );
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>(
				new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders(
						new CollectionWidgetBuilder(),
						new SwingWidgetBuilder() ) ) );
		metawidget.setPath( List.class.getName() + "<" + Address.class.getName() + ">" );

		// Test

		assertTrue( metawidget.getComponent( 0 ) instanceof JScrollPane );
		JTable table = (JTable) ( (JScrollPane) metawidget.getComponent( 0 ) ).getViewport().getView();
		assertEquals( "Street", table.getColumnName( 0 ) );
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

	static class CollectionWidgetBuilder
		implements WidgetBuilder<JComponent, SwingMetawidget> {

		public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

			// Not for us?

			if ( TRUE.equals( attributes.get( HIDDEN ) ) || attributes.containsKey( LOOKUP ) ) {
				return null;
			}

			String type = attributes.get( TYPE );

			if ( type == null || "".equals( type ) ) {
				return null;
			}

			final Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz == null ) {
				return null;
			}

			if ( !List.class.isAssignableFrom( clazz ) ) {
				return null;
			}

			// Inspect type of List

			String componentType = WidgetBuilderUtils.getComponentType( attributes );
			String inspectedType = metawidget.inspect( null, componentType );

			// Determine columns

			List<String> columns = CollectionUtils.newArrayList();
			Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
			NodeList elements = root.getFirstChild().getChildNodes();

			for ( int loop = 0, length = elements.getLength(); loop < length; loop++ ) {

				Node node = elements.item( loop );
				columns.add( metawidget.getLabelString( XmlUtils.getAttributesAsMap( node ) ) );
			}

			// Fetch the data. This part could be improved to use BeansBinding or similar

			List<?> list;

			if ( metawidget.getToInspect() == null) {
				list = CollectionUtils.newArrayList();
			} else {
				list = (List<?>) ClassUtils.getProperty( metawidget.getToInspect(), attributes.get( NAME ) );
			}

			// Return the JTable

			@SuppressWarnings( { "unchecked", "rawtypes" } )
			ListTableModel<?> tableModel = new ListTableModel( list, columns );

			return new JScrollPane( new JTable( tableModel ) );
		}
	}

	static class ListTableModel<T>
		extends AbstractTableModel {

		private List<T>			mList;

		private List<String>	mColumns;

		public ListTableModel( List<T> list, List<String> columns ) {

			mList = list;
			mColumns = columns;
		}

		public int getColumnCount() {

			return mColumns.size();
		}

		@Override
		public String getColumnName( int columnIndex ) {

			if ( columnIndex >= getColumnCount() ) {
				return null;
			}

			return mColumns.get( columnIndex );
		}

		public int getRowCount() {

			return mList.size();
		}

		public T getValueAt( int rowIndex ) {

			if ( rowIndex >= getRowCount() ) {
				return null;
			}

			return mList.get( rowIndex );
		}

		public Object getValueAt( int rowIndex, int columnIndex ) {

			if ( columnIndex >= getColumnCount() ) {
				return null;
			}

			T t = getValueAt( rowIndex );

			if ( t == null ) {
				return null;
			}

			return ClassUtils.getProperty( t, getColumnName( columnIndex ) );
		}
	}
}
