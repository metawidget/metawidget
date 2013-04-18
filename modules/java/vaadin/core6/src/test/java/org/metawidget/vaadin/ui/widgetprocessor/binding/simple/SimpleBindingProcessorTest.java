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

package org.metawidget.vaadin.ui.widgetprocessor.binding.simple;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

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
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.layout.TabSheetLayoutDecorator;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;

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

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setToInspect( foo );

		// Load and save

		FormLayout layout = metawidget.getContent();

		TextField textField = (TextField) layout.getComponent( 0 );
		assertEquals( "42", textField.getValue() );
		Label label = (Label) layout.getComponent( 1 );
		assertEquals( "4", label.getValue() );

		textField.setValue( "43" );
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

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setToInspect( foo );
		metawidget.setPath( Foo.class.getName() + "/bar" );

		FormLayout layout = metawidget.getContent();

		TextField textField = (TextField) layout.getComponent( 0 );
		assertEquals( "42", textField.getValue() );
		textField.setValue( "43" );
		assertEquals( 42, foo.getBar() );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( 43, foo.getBar() );
	}

	public void testNoGetterSetterType()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setInspector( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) ) );
		metawidget.setToInspect( new NoGetSetFoo() );

		try {
			metawidget.getContent();
			fail();
		} catch ( Exception e ) {
			assertTrue( e.getCause().getMessage().startsWith( "Unable to get 'bar'" ) );
		}
	}

	public void testUppercase()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();

		// Saving

		UppercaseFoo uppercaseFoo = new UppercaseFoo();
		metawidget.setToInspect( uppercaseFoo );

		( (TextField) metawidget.getComponent( "WEPKey" ) ).setValue( "1234567890" );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( "1234567890", uppercaseFoo.getWEPKey() );

		// Loading

		uppercaseFoo.setWEPKey( "0987654321" );
		metawidget.setToInspect( uppercaseFoo );

		FormLayout layout = metawidget.getContent();
		assertEquals( "0987654321", ( (TextField) layout.getComponent( 0 ) ).getValue() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( SimpleBindingProcessorConfig.class, new SimpleBindingProcessorConfig() {
			// Subclass
		} );
	}

	public void testNestedMetawidget() {

		Contact contact = new PersonalContact();
		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.addComponent( new Stub( "dateOfBirth" ) );
		metawidget.setToInspect( contact );

		// Just FormLayout

		assertEquals( null, contact.getFirstname() );
		assertEquals( null, contact.getAddress().getStreet() );
		( (TextField) metawidget.getComponent( "firstname" ) ).setValue( "Foo" );
		( (TextField) metawidget.getComponent( "address", "street" ) ).setValue( "Bar" );
		assertEquals( metawidget, metawidget.getComponent( "address", "street" ).getParent().getParent().getParent().getParent() );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo", contact.getFirstname() );
		assertEquals( "Bar", contact.getAddress().getStreet() );

		// With TabSheetLayoutDecorator

		metawidget.setLayout( new TabSheetLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget>().setLayout( new org.metawidget.vaadin.ui.layout.FormLayout() ) ) );
		( (TextField) metawidget.getComponent( "firstname" ) ).setValue( "Foo1" );
		( (TextField) metawidget.getComponent( "address", "street" ) ).setValue( "Bar1" );
		assertTrue( metawidget.getComponent( "address", "street" ).getParent().getParent().getParent().getParent().getParent().getParent() instanceof TabSheet );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo1", contact.getFirstname() );
		assertEquals( "Bar1", contact.getAddress().getStreet() );
	}

	public void testSlider()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setToInspect( new Foo() );
		SimpleBindingProcessor widgetProcessor = new SimpleBindingProcessor();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		Slider slider = new Slider();

		attributes.put( NAME, "baz" );
		attributes.put( TYPE, int.class.getName() );
		widgetProcessor.processWidget( slider, PROPERTY, attributes, metawidget );

		assertEquals( 4d, slider.getValue() );
	}

	public void testEnum()
		throws Exception {

		// As Label

		Contact contact = new PersonalContact();
		contact.setGender( Gender.MALE );

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setToInspect( contact );
		SimpleBindingProcessor widgetProcessor = new SimpleBindingProcessor();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		Label label = new Label();

		attributes.put( NAME, "gender" );
		attributes.put( TYPE, Gender.class.getName() );
		widgetProcessor.processWidget( label, PROPERTY, attributes, metawidget );

		Field valueField = ObjectProperty.class.getDeclaredField( "value" );
		valueField.setAccessible( true );
		assertEquals( "MALE", label.getValue() );

		// As Select

		metawidget.setToInspect( contact );
		Select select = metawidget.getComponent( "gender" );
		IndexedContainer dataSource = (IndexedContainer) select.getContainerDataSource();
		assertEquals( Gender.MALE, dataSource.getIdByIndex( 0 ) );
		assertEquals( Gender.FEMALE, dataSource.getIdByIndex( 1 ) );
		assertEquals( 2, dataSource.getItemIds().size() );
	}

	public void testConvertFromString() {

		SimpleBindingProcessorConfig config = new SimpleBindingProcessorConfig();
		config.setConverter( String.class, Foo.class, new Converter<String, Foo>() {

			public Foo convert( String value, Class<? extends Foo> actualType ) {

				Foo foo = new Foo();
				foo.setBar( Long.valueOf( value ) );
				return foo;
			}
		} );

		assertEquals( 4, ((Foo) new SimpleBindingProcessor( config ).convertFromString( "4", Foo.class )).getBar());
		assertEquals( "false", new SimpleBindingProcessor( config ).convertFromString( "false", String.class ));
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
