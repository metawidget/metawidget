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

package org.metawidget.swing.widgetprocessor.binding.beansbinding;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.jdesktop.beansbinding.Converter;
import org.metawidget.config.impl.BaseConfigReader;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.BoxLayout;
import org.metawidget.swing.layout.GridBagLayout;
import org.metawidget.swing.layout.TabbedPaneLayoutDecorator;
import org.metawidget.swing.layout.TabbedPaneLayoutDecoratorConfig;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor.ConvertFromTo;
import org.metawidget.test.model.annotatedaddressbook.Contact;
import org.metawidget.test.model.annotatedaddressbook.PersonalContact;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * @author Richard Kennard
 */

public class BeansBindingProcessorTest
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

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setToInspect( foo );

		// Test UpdateStrategy.READ_ONCE
		//
		// Also test correct mapping of Long in JSpinner

		JSpinner spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( 42 == (Long) spinner.getValue() );
		JLabel label = (JLabel) metawidget.getComponent( 3 );
		assertEquals( "4", label.getText() );

		// Test UpdateStrategy.READ

		metawidget.removeWidgetProcessor( metawidget.getWidgetProcessor( BeansBindingProcessor.class ) );
		metawidget.addWidgetProcessor( new BeansBindingProcessor( new BeansBindingProcessorConfig().setUpdateStrategy( UpdateStrategy.READ ) ) );

		spinner = (JSpinner) metawidget.getComponent( 1 );
		foo.setBar( 43 );
		assertTrue( 43 == (Long) spinner.getValue() );
		spinner.setValue( 44l );
		assertEquals( 43, foo.getBar() );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
		assertEquals( 44, foo.getBar() );

		// Test UpdateStrategy.READ_WRITE

		metawidget.removeWidgetProcessor( metawidget.getWidgetProcessor( BeansBindingProcessor.class ) );
		metawidget.addWidgetProcessor( new BeansBindingProcessor( new BeansBindingProcessorConfig().setUpdateStrategy( UpdateStrategy.READ_WRITE ) ) );

		spinner = (JSpinner) metawidget.getComponent( 1 );
		spinner.setValue( spinner.getModel().getNextValue() );
		assertEquals( 45, foo.getBar() );
	}

	public void testXmlBinding()
		throws Exception {

		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		String config = "<metawidget>\r\n";
		config += "\t<swingMetawidget xmlns=\"java:org.metawidget.swing\">\r\n";
		config += "\t\t<inspector>\r\n";
		config += "\t\t\t<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>\r\n";
		config += "\t\t</inspector>\r\n";
		config += "\t\t<widgetProcessors>\r\n";
		config += "\t\t\t<array>\r\n";
		config += "\t\t\t\t<beansBindingProcessor xmlns=\"java:org.metawidget.swing.widgetprocessor.binding.beansbinding\"/>\r\n";
		config += "\t\t\t</array>\r\n";
		config += "\t\t</widgetProcessors>\r\n";
		config += "\t</swingMetawidget>\r\n";
		config += "</metawidget>";

		SwingMetawidget metawidget = new SwingMetawidget();
		new BaseConfigReader().configure( new ByteArrayInputStream( config.getBytes() ), metawidget );
		metawidget.setToInspect( foo );

		// Test UpdateStrategy.READ_ONCE
		//
		// Also test correct mapping of Long in JSpinner

		JSpinner spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( 42 == (Long) spinner.getValue() );
		JLabel label = (JLabel) metawidget.getComponent( 3 );
		assertEquals( "4", label.getText() );

		// Test UpdateStrategy.READ

		config = "<metawidget>\r\n";
		config += "\t<swingMetawidget xmlns=\"java:org.metawidget.swing\">\r\n";
		config += "\t\t<inspector>\r\n";
		config += "\t\t\t<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>\r\n";
		config += "\t\t</inspector>\r\n";
		config += "\t\t<widgetProcessors>\r\n";
		config += "\t\t\t<array>\r\n";
		config += "\t\t\t\t<beansBindingProcessor xmlns=\"java:org.metawidget.swing.widgetprocessor.binding.beansbinding\" config=\"BeansBindingProcessorConfig\">\r\n";
		config += "\t\t\t\t\t<updateStrategy>\r\n";
		config += "\t\t\t\t\t\t<constant>org.jdesktop.beansbinding.AutoBinding$UpdateStrategy.READ</constant>\r\n";
		config += "\t\t\t\t\t</updateStrategy>\r\n";
		config += "\t\t\t\t</beansBindingProcessor>\r\n";
		config += "\t\t\t</array>\r\n";
		config += "\t\t</widgetProcessors>\r\n";
		config += "\t</swingMetawidget>\r\n";
		config += "</metawidget>";

		metawidget = new SwingMetawidget();
		new BaseConfigReader().configure( new ByteArrayInputStream( config.getBytes() ), metawidget );
		metawidget.setToInspect( foo );

		spinner = (JSpinner) metawidget.getComponent( 1 );
		foo.setBar( 43 );
		assertTrue( 43 == (Long) spinner.getValue() );
		spinner.setValue( 44l );
		assertEquals( 43, foo.getBar() );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
		assertEquals( 44, foo.getBar() );

		// Test UpdateStrategy.READ_WRITE

		config = "<metawidget>\r\n";
		config += "\t<swingMetawidget xmlns=\"java:org.metawidget.swing\">\r\n";
		config += "\t\t<inspector>\r\n";
		config += "\t\t\t<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>\r\n";
		config += "\t\t</inspector>\r\n";
		config += "\t\t<widgetProcessors>\r\n";
		config += "\t\t\t<array>\r\n";
		config += "\t\t\t\t<beansBindingProcessor xmlns=\"java:org.metawidget.swing.widgetprocessor.binding.beansbinding\" config=\"BeansBindingProcessorConfig\">\r\n";
		config += "\t\t\t\t\t<updateStrategy>\r\n";
		config += "\t\t\t\t\t\t<constant>org.jdesktop.beansbinding.AutoBinding$UpdateStrategy.READ_WRITE</constant>\r\n";
		config += "\t\t\t\t\t</updateStrategy>\r\n";
		config += "\t\t\t\t</beansBindingProcessor>\r\n";
		config += "\t\t\t</array>\r\n";
		config += "\t\t</widgetProcessors>\r\n";
		config += "\t</swingMetawidget>\r\n";
		config += "</metawidget>";

		metawidget = new SwingMetawidget();
		new BaseConfigReader().configure( new ByteArrayInputStream( config.getBytes() ), metawidget );
		metawidget.setToInspect( foo );

		spinner = (JSpinner) metawidget.getComponent( 1 );
		spinner.setValue( spinner.getModel().getNextValue() );
		assertEquals( 45, foo.getBar() );
	}

	public void testBindingListener()
		throws Exception {

		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		String metadata = "<inspection-result>";
		metadata += "\t<entity type=\"" + Foo.class.getName() + "\">";
		metadata += "\t\t<property name=\"bar\"/>";
		metadata += "\t</entity>";
		metadata += "</inspection-result>";

		SwingMetawidget metawidget = new SwingMetawidget();
		final XmlInspectorConfig config = new XmlInspectorConfig();
		config.setInputStream( new ByteArrayInputStream( metadata.getBytes() ) );
		metawidget.setInspector( new XmlInspector( config ) );

		final List<Object> syncFailed = CollectionUtils.newArrayList();

		metawidget.addWidgetProcessor( new BeansBindingProcessor( new BeansBindingProcessorConfig().setUpdateStrategy( UpdateStrategy.READ_WRITE ) ) {

			@Override
			protected <SS, SV, TS extends Component, TV> Binding<SS, SV, TS, TV> processBinding( Binding<SS, SV, TS, TV> binding, final SwingMetawidget owner ) {

				binding.addBindingListener( new AbstractBindingListener() {

					@Override
					public void syncFailed( @SuppressWarnings( "rawtypes" ) Binding failedBinding, SyncFailure failure ) {

						syncFailed.add( owner );
						syncFailed.add( failure.getConversionException().toString() );
					}
				} );

				return binding;
			}
		} );
		metawidget.setToInspect( foo );

		JTextField textField = (JTextField) metawidget.getComponent( 1 );
		assertEquals( "42", textField.getText() );

		// Set to erroneous value

		assertEquals( 0, syncFailed.size() );
		textField.setText( "error" );
		assertEquals( 42, foo.getBar() );
		assertEquals( 2, syncFailed.size() );
		assertTrue( metawidget == syncFailed.get( 0 ) );
		assertEquals( "java.lang.NumberFormatException: For input string: \"error\"", syncFailed.get( 1 ) );
	}

	public void testSingleComponentBinding()
		throws Exception {

		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeansBindingProcessor( new BeansBindingProcessorConfig().setUpdateStrategy( UpdateStrategy.READ_WRITE ) ) );
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setMetawidgetLayout( new BoxLayout() );
		metawidget.setToInspect( foo );
		metawidget.setPath( Foo.class.getName() + "/bar" );

		JSpinner spinner = (JSpinner) metawidget.getComponent( 0 );
		assertTrue( 42l == (Long) spinner.getValue() );
		spinner.setValue( 43l );
		assertEquals( 43l, foo.getBar() );
	}

	public void testReadOnlyToStringConverter()
		throws Exception {

		// Model

		ReadOnlyToStringConverter<Boolean> converter = new ReadOnlyToStringConverter<Boolean>();

		assertEquals( "true", converter.convertForward( Boolean.TRUE ) );

		try {
			converter.convertReverse( "true" );
			fail();
		} catch ( UnsupportedOperationException e ) {
			assertTrue( e.getMessage().indexOf( "cannot convertReverse" ) != -1 );
		}
	}

	public void testConvert()
		throws Exception {

		// convertReverse with built in Converter

		BeansBindingProcessor binding = new BeansBindingProcessor();
		assertTrue( 1 == (Integer) binding.convertFromString( "1", int.class ) );

		// convertForward with given Converter

		final StringBuilder builder = new StringBuilder();

		BeansBindingProcessorConfig config = new BeansBindingProcessorConfig();
		config.setConverter( String.class, Integer.class, new Converter<String, Integer>() {

			@Override
			public Integer convertForward( String value ) {

				builder.append( "convertedForward" );
				return Integer.valueOf( value );
			}

			@Override
			public String convertReverse( Integer value ) {

				return String.valueOf( value );
			}
		} );

		// convertFromString

		binding = new BeansBindingProcessor( config );
		assertTrue( 1 == (Integer) binding.convertFromString( "1", int.class ) );
		assertEquals( "convertedForward", builder.toString() );
	}

	public void testNoGetterSetterType()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setInspector( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) ) );
		metawidget.setToInspect( new NoGetSetFoo() );

		try {
			metawidget.getComponent( 0 );
			fail();
		} catch ( WidgetProcessorException e ) {
			assertEquals( "Property 'bar' has no getter and no setter (or parent is null)", e.getMessage() );
		}
	}

	public void testUnknownType()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setInspector( new PropertyTypeInspector() );

		// Saving

		CantLoadSaveFoo cantLoadSaveFoo = new CantLoadSaveFoo();
		metawidget.setToInspect( cantLoadSaveFoo );

		try {
			metawidget.setValue( "1/1/2001", "bar" );
			metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
			fail();
		} catch ( WidgetProcessorException e ) {
			assertEquals( "When saving from class javax.swing.JTextField to org.jdesktop.beansbinding.BeanProperty[bar] (have you used BeansBindingProcessorConfig.setConverter?)", e.getMessage() );
		}

		// Loading

		cantLoadSaveFoo.setBar( new Date() );
		metawidget.setToInspect( cantLoadSaveFoo );

		try {
			metawidget.getComponent( 0 );
			fail();
		} catch ( WidgetProcessorException e ) {
			assertEquals( "When binding org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessorTest$CantLoadSaveFoo/bar to class javax.swing.JTextField.text (have you used BeansBindingProcessorConfig.setConverter?)", e.getMessage() );
		}
	}

	public void testUppercase()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );

		// Saving

		UppercaseFoo uppercaseFoo = new UppercaseFoo();
		metawidget.setToInspect( uppercaseFoo );

		metawidget.setValue( "1234567890", "WEPKey" );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
		assertEquals( "1234567890", uppercaseFoo.getWEPKey() );

		// Loading

		uppercaseFoo.setWEPKey( "0987654321" );
		metawidget.setToInspect( uppercaseFoo );

		assertEquals( "0987654321", ( (JTextField) metawidget.getComponent( 1 ) ).getText() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( BeansBindingProcessorConfig.class, new BeansBindingProcessorConfig() {
			// Subclass
		}, "converters" );
		MetawidgetTestUtils.testObjectEqualsAndHashcode( new ConvertFromTo<Integer, String>( Integer.class, String.class ), new ConvertFromTo<Integer, String>( Integer.class, String.class ), null );
	}

	public void testNumberConverter() {

		NumberConverter<Integer> numberConverter = new NumberConverter<Integer>( Integer.class );

		assertEquals( null, numberConverter.convertReverse( null ) );
		assertEquals( null, numberConverter.convertReverse( "" ) );
		assertEquals( null, numberConverter.convertReverse( "   " ) );
		assertTrue( 3 == numberConverter.convertReverse( "3" ) );
	}

	public void testNestedMetawidget() {

		Contact contact = new PersonalContact();
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.add( new Stub( "dateOfBirth" ) );
		metawidget.setToInspect( contact );

		// Just GridBagLayout

		assertEquals( null, contact.getFirstname() );
		assertEquals( null, contact.getAddress().getStreet() );
		( (JTextField) metawidget.getComponent( "firstname" ) ).setText( "Foo" );
		( (JTextField) metawidget.getComponent( "address", "street" ) ).setText( "Bar" );
		assertEquals( metawidget, metawidget.getComponent( "address", "street" ).getParent().getParent() );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo", contact.getFirstname() );
		assertEquals( "Bar", contact.getAddress().getStreet() );

		// With TabbedPaneLayoutDecorator

		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new GridBagLayout() ) ) );
		( (JTextField) metawidget.getComponent( "firstname" ) ).setText( "Foo1" );
		( (JTextField) metawidget.getComponent( "address", "street" ) ).setText( "Bar1" );
		assertTrue( metawidget.getComponent( "address", "street" ).getParent().getParent().getParent() instanceof JTabbedPane );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo1", contact.getFirstname() );
		assertEquals( "Bar1", contact.getAddress().getStreet() );

		// Test rebind

		contact = new PersonalContact();
		contact.setFirstname( "Foo2" );
		contact.getAddress().setStreet( "Bar2" );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).rebind( contact, metawidget );
		assertEquals( "Foo2", ( (JTextField) metawidget.getComponent( "firstname" ) ).getText() );
		assertEquals( "Bar2", ( (JTextField) metawidget.getComponent( "address", "street" ) ).getText() );
	}

	public void testMissingReadOnlyWidgetBuilder() {

		Foo foo = new Foo();
		foo.setBar( 35 );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setWidgetBuilder( new SwingWidgetBuilder() );
		metawidget.setToInspect( foo );

		assertEquals( 35l, ( (JSpinner) metawidget.getComponent( "bar" ) ).getValue() );
		( (JSpinner) metawidget.getComponent( "bar" ) ).setValue( 36l );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
		assertEquals( 36l, foo.getBar() );
		metawidget.setReadOnly( true );
		assertEquals( 36l, ( (JSpinner) metawidget.getComponent( "bar" ) ).getValue() );
	}

	//
	// Inner class
	//

	protected static class Foo {

		//
		// Private members
		//

		private final PropertyChangeSupport	mPropertyChangeSupport	= new PropertyChangeSupport( this );

		private long						mBar;

		private int							mBaz					= 4;

		private List<String>				mList					= CollectionUtils.unmodifiableList( "element1", "element2" );

		//
		// Public methods
		//

		public long getBar() {

			return mBar;
		}

		public void setBar( long bar ) {

			long oldBar = mBar;
			mBar = bar;
			mPropertyChangeSupport.firePropertyChange( "bar", oldBar, mBar );
		}

		public int getBaz() {

			return mBaz;
		}

		public void addPropertyChangeListener( PropertyChangeListener listener ) {

			mPropertyChangeSupport.addPropertyChangeListener( listener );
		}

		public void removePropertyChangeListener( PropertyChangeListener listener ) {

			mPropertyChangeSupport.removePropertyChangeListener( listener );
		}

		public List<String> getList() {

			return mList;
		}

		public void setList( List<String> list ) {

			mList = list;
		}
	}

	protected static class CantLoadSaveFoo {

		private Date	mBar;

		//
		// Public methods
		//

		public Date getBar() {

			return mBar;
		}

		public void setBar( Date bar ) {

			mBar = bar;
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