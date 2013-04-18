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

package org.metawidget.android.widget.widgetprocessor.binding.simple;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.android.widget.layout.LinearLayoutConfig;
import org.metawidget.android.widget.layout.SimpleLayout;
import org.metawidget.android.widget.layout.TabHostLayoutDecorator;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.test.model.annotatedaddressbook.Contact;
import org.metawidget.test.model.annotatedaddressbook.Gender;
import org.metawidget.test.model.annotatedaddressbook.PersonalContact;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * @author Richard Kennard
 */

public class SimpleBindingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testBinding()
		throws Exception {

		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		metawidget.setToInspect( foo );

		// Load and save

		EditText view = metawidget.findViewWithTags( "bar" );
		assertEquals( "42", view.getText() );
		view.setText( "43" );
		assertEquals( 42, foo.getBar() );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( 43, foo.getBar() );
	}

	public void testSingleComponentBinding()
		throws Exception {

		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		metawidget.setToInspect( foo );
		metawidget.setLayout( new SimpleLayout() );
		metawidget.setPath( Foo.class.getName() + "/bar" );

		EditText view = (EditText) metawidget.getChildAt( 0 );
		assertEquals( "42", view.getText() );
		view.setText( "43" );
		assertEquals( 42, foo.getBar() );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( 43, foo.getBar() );
	}

	public void testNoGetterSetterType()
		throws Exception {

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		metawidget.setInspector( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) ) );
		metawidget.setToInspect( new NoGetSetFoo() );

		try {
			metawidget.getChildAt( 0 );
			fail();
		} catch ( Exception e ) {
			assertTrue( e.getCause().getMessage().startsWith( "Unable to get 'bar'" ) );
		}
	}

	public void testUppercase()
		throws Exception {

		AndroidMetawidget metawidget = new AndroidMetawidget( null );

		// Saving

		UppercaseFoo uppercaseFoo = new UppercaseFoo();
		metawidget.setToInspect( uppercaseFoo );

		( (EditText) metawidget.findViewWithTags( "WEPKey" ) ).setText( "1234567890" );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( "1234567890", uppercaseFoo.getWEPKey() );

		// Loading

		uppercaseFoo.setWEPKey( "0987654321" );
		metawidget.setToInspect( uppercaseFoo );

		assertEquals( "0987654321", ( (EditText) metawidget.findViewWithTags( "WEPKey" ) ).getText() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( SimpleBindingProcessorConfig.class, new SimpleBindingProcessorConfig() {
			// Subclass
		} );
	}

	public void testNestedMetawidget() {

		Contact contact = new PersonalContact();
		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		Stub stub = new Stub( null );
		stub.setTag( "dateOfBirth" );
		metawidget.addView( stub );
		metawidget.setToInspect( contact );

		// Just TableLayout

		assertEquals( null, contact.getFirstname() );
		assertEquals( null, contact.getAddress().getStreet() );
		( (EditText) metawidget.findViewWithTags( "firstname" ) ).setText( "Foo" );
		( (EditText) metawidget.findViewWithTags( "address", "street" ) ).setText( "Bar" );
		assertEquals( metawidget, metawidget.findViewWithTags( "address", "street" ).getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent() );
		assertEquals( metawidget, metawidget.findViewWithTags( "address", "postcode" ).getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent() );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo", contact.getFirstname() );
		assertEquals( "Bar", contact.getAddress().getStreet() );

		// With TabSheetLayoutDecorator

		metawidget.setLayout( new TabHostLayoutDecorator( new LayoutDecoratorConfig<View, ViewGroup, AndroidMetawidget>().setLayout( new org.metawidget.android.widget.layout.TableLayout( new LinearLayoutConfig() ) ) ) );
		( (EditText) metawidget.findViewWithTags( "firstname" ) ).setText( "Foo1" );
		( (EditText) metawidget.findViewWithTags( "address", "street" ) ).setText( "Bar1" );
		assertTrue( metawidget.findViewWithTags( "address", "street" ).getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent() instanceof TabHost );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo1", contact.getFirstname() );
		assertEquals( "Bar1", contact.getAddress().getStreet() );
	}

	@SuppressWarnings( "unchecked" )
	public void testEnum()
		throws Exception {

		// As Label

		Contact contact = new PersonalContact();
		contact.setGender( Gender.MALE );

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		SimpleBindingProcessorConfig config = new SimpleBindingProcessorConfig();
		config.setConverter( Gender.class, new Converter<Gender>() {

			public Gender convertFromView( View widget, Object value, Class<?> intoClass ) {

				return Gender.valueOf( (String) value );
			}

			public Object convertForView( View widget, Gender value ) {

				if ( widget instanceof Spinner ) {
					return value;
				}

				return value.name();
			}
		} );
		metawidget.setWidgetProcessors( new SimpleBindingProcessor( config ) );
		metawidget.setToInspect( contact );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		TextView label = new TextView( null );

		attributes.put( NAME, "gender" );
		attributes.put( TYPE, Gender.class.getName() );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).processWidget( label, PROPERTY, attributes, metawidget );
		assertEquals( "MALE", label.getText() );

		// As Spinner

		metawidget.setToInspect( contact );
		Spinner spinner = metawidget.findViewWithTags( "gender" );
		assertEquals( Gender.MALE, spinner.getSelectedItem() );
		assertEquals( null, ( (ArrayAdapter<Gender>) spinner.getAdapter() ).getItem( 0 ) );
		assertEquals( Gender.MALE, ( (ArrayAdapter<Gender>) spinner.getAdapter() ).getItem( 1 ) );
		assertEquals( Gender.FEMALE, ( (ArrayAdapter<Gender>) spinner.getAdapter() ).getItem( 2 ) );
		assertEquals( 3, ( (ArrayAdapter<String>) spinner.getAdapter() ).getCount() );
	}

	//
	// Inner class
	//

	protected static class Foo {

		//
		// Private members
		//

		private long			mBar;

		private int				mBaz	= 4;

		private List<String>	mList	= CollectionUtils.unmodifiableList( "element1", "element2" );

		//
		// Public methods
		//

		public long getBar() {

			return mBar;
		}

		public void setBar( long bar ) {

			mBar = bar;
		}

		public int getBaz() {

			return mBaz;
		}

		public List<String> getList() {

			return mList;
		}

		public void setList( List<String> list ) {

			mList = list;
		}
	}

	protected static class NoGetSetFoo {

		//
		// Public methods
		//

		public float	bar;
	}

	protected static class UppercaseFoo {

		private String	mWEPKey;

		//
		// Public methods
		//

		public String getWEPKey() {

			return mWEPKey;
		}

		public void setWEPKey( String WEPKey ) {

			mWEPKey = WEPKey;
		}
	}
}
