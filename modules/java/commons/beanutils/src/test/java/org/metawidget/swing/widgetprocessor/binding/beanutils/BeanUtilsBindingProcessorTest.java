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

package org.metawidget.swing.widgetprocessor.binding.beanutils;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConvertUtils;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.scala.ScalaPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayout;
import org.metawidget.swing.layout.TabbedPaneLayoutDecorator;
import org.metawidget.swing.layout.TabbedPaneLayoutDecoratorConfig;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.test.model.annotatedaddressbook.Contact;
import org.metawidget.test.model.annotatedaddressbook.PersonalContact;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author Richard Kennard
 */

public class BeanUtilsBindingProcessorTest
	extends TestCase {

	//
	// Private statics
	//

	private static final String	DATE_FORMAT	= "E MMM dd HH:mm:ss z yyyy";

	//
	// Public methods
	//

	public void testScalaBinding()
		throws Exception {

		// Model

		Date dateFirst = new GregorianCalendar( 1975, Calendar.APRIL, 9 ).getTime();

		ScalaFoo scalaFoo = new ScalaFoo();
		scalaFoo.bar_$eq( dateFirst );
		ScalaFoo scalaFoo2 = new ScalaFoo();
		scalaFoo.nestedFoo = scalaFoo2;
		ScalaFoo scalaFoo3 = new ScalaFoo();
		scalaFoo2.nestedFoo = scalaFoo3;
		scalaFoo3.bar_$eq( new GregorianCalendar( 1976, Calendar.MAY, 16 ).getTime() );

		// BeanUtilsBinding

		ConvertUtils.register( new DateConverter( DATE_FORMAT ), Date.class );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeanUtilsBindingProcessor( new BeanUtilsBindingProcessorConfig().setPropertyStyle( new ScalaPropertyStyle() ) ) );
		BaseObjectInspectorConfig config = new BaseObjectInspectorConfig().setPropertyStyle( new ScalaPropertyStyle() );
		metawidget.setInspector( new PropertyTypeInspector( config ) );
		metawidget.setToInspect( scalaFoo );

		// Loading

		JTextField textField = (JTextField) metawidget.getComponent( 1 );
		DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
		assertEquals( dateFormat.format( dateFirst ), textField.getText() );
		JLabel label = (JLabel) metawidget.getComponent( 5 );
		assertEquals( "Not settable", label.getText() );

		JTextField nestedTextField = (JTextField) ( (SwingMetawidget) metawidget.getComponent( 3 ) ).getComponent( 1 );
		assertEquals( "", nestedTextField.getText() );

		JTextField nestedNestedTextField = (JTextField) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 3 ) ).getComponent( 3 ) ).getComponent( 1 );
		assertEquals( dateFormat.format( scalaFoo3.bar() ), nestedNestedTextField.getText() );

		// Saving

		Date dateSecond = new GregorianCalendar( 1976, Calendar.MAY, 10 ).getTime();
		textField.setText( dateFormat.format( dateSecond ) );
		nestedNestedTextField.setText( dateFormat.format( new GregorianCalendar( 1977, Calendar.JUNE, 17 ).getTime() ) );
		metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).save( metawidget );

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime( scalaFoo.bar() );
		assertEquals( 1976, calendar.get( Calendar.YEAR ) );
		assertEquals( null, scalaFoo2.bar() );
		calendar.setTime( scalaFoo3.bar() );
		assertEquals( 1977, calendar.get( Calendar.YEAR ) );

		// Rebinding

		textField = (JTextField) metawidget.getComponent( 1 );
		assertEquals( dateFormat.format( dateSecond ), textField.getText() );

		scalaFoo.bar_$eq( dateFirst );
		metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).rebind( scalaFoo, metawidget );

		textField = (JTextField) metawidget.getComponent( 1 );
		assertEquals( dateFormat.format( dateFirst ), textField.getText() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( BeanUtilsBindingProcessorConfig.class, new BeanUtilsBindingProcessorConfig() {
			// Subclass
		} );
	}

	public void testConvertFromString()
		throws Exception {

		BeanUtilsBindingProcessor binding = new BeanUtilsBindingProcessor();
		assertEquals( 1, binding.convertFromString( "1", int.class ) );
		assertEquals( false, binding.convertFromString( "false", Boolean.class ) );
		assertEquals( "false", binding.convertFromString( "false", String.class ) );
		assertEquals( "no-converter", binding.convertFromString( "no-converter", Foo.class ) );
	}

	@SuppressWarnings( "cast" )
	public void testNestedMetawidget() {

		Contact contact = new PersonalContact();
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeanUtilsBindingProcessor() );
		metawidget.add( new Stub( "dateOfBirth" ) );
		metawidget.setToInspect( contact );

		// Just GridBagLayout

		assertEquals( null, contact.getFirstname() );
		assertEquals( null, contact.getAddress().getStreet() );
		( (JTextField) metawidget.getComponent( "firstname" ) ).setText( "Foo" );
		( (JTextField) metawidget.getComponent( "address", "street" ) ).setText( "Bar" );
		assertEquals( metawidget, ( (Component) metawidget.getComponent( "address", "street" ) ).getParent().getParent() );
		metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo", contact.getFirstname() );
		assertEquals( "Bar", contact.getAddress().getStreet() );

		// With TabbedPaneLayoutDecorator

		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new GridBagLayout() ) ) );
		( (JTextField) metawidget.getComponent( "firstname" ) ).setText( "Foo1" );
		( (JTextField) metawidget.getComponent( "address", "street" ) ).setText( "Bar1" );
		assertTrue( ( (Component) metawidget.getComponent( "address", "street" ) ).getParent().getParent().getParent() instanceof JTabbedPane );
		metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo1", contact.getFirstname() );
		assertEquals( "Bar1", contact.getAddress().getStreet() );

		// Test rebind

		contact = new PersonalContact();
		contact.setFirstname( "Foo2" );
		contact.getAddress().setStreet( "Bar2" );
		metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).rebind( contact, metawidget );
		assertEquals( "Foo2", ( (JTextField) metawidget.getComponent( "firstname" ) ).getText() );
		assertEquals( "Bar2", ( (JTextField) metawidget.getComponent( "address", "street" ) ).getText() );
	}

	public void testMissingReadOnlyWidgetBuilder() {

		Foo foo = new Foo();
		foo.setBar( 35 );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeanUtilsBindingProcessor() );
		metawidget.setWidgetBuilder( new SwingWidgetBuilder() );
		metawidget.setToInspect( foo );

		assertEquals( 35l, ( (JSpinner) metawidget.getComponent( "bar" ) ).getValue() );
		( (JSpinner) metawidget.getComponent( "bar" ) ).setValue( 36l );
		metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).save( metawidget );
		assertEquals( 36l, foo.getBar() );
		metawidget.setReadOnly( true );
		assertEquals( 36l, ( (JSpinner) metawidget.getComponent( "bar" ) ).getValue() );
	}

	public void testNestedWithManualInspector() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.addWidgetProcessor( new BeanUtilsBindingProcessor() );
		NestedFoo foo1 = new NestedFoo();
		NestedFoo foo2 = new NestedFoo();
		foo1.setNestedFoo( foo2 );
		foo2.setName( "Bar" );
		metawidget.setToInspect( foo1 );

		assertEquals( "Bar", metawidget.getValue( "nestedFoo", "name" ) );
	}

	public void testNullPropertyStyle()
		throws Exception {

		BeanUtilsBindingProcessorConfig config = new BeanUtilsBindingProcessorConfig();
		assertTrue( config.getPropertyStyle() instanceof JavaBeanPropertyStyle );

		config.setPropertyStyle( null );
		assertEquals( null, config.getPropertyStyle() );

		config.setPropertyStyle( new ScalaPropertyStyle() );
		assertTrue( config.getPropertyStyle() instanceof ScalaPropertyStyle );
	}

	//
	// Inner class
	//

	protected static class ScalaFoo {

		//
		// Protected members
		//

		protected ScalaFoo	nestedFoo;

		//
		// Private members
		//

		private Date		bar;

		private String		notSettable	= "Not settable";

		//
		// Public methods
		//

		public Date bar() {

			return bar;
		}

		public void bar_$eq( Date theBar ) {

			bar = theBar;
		}

		public String notSettable() {

			return notSettable;
		}

		public ScalaFoo nestedFoo() {

			return nestedFoo;
		}
	}

	public static class Foo {

		//
		// Private members
		//

		private long	mBar;

		//
		// Public methods
		//

		public long getBar() {

			return mBar;
		}

		public void setBar( long bar ) {

			mBar = bar;
		}
	}

	public static class NestedFoo {

		//
		// Private members
		//

		private String		mName;

		private NestedFoo	mNestedFoo;

		//
		// Public methods
		//

		public String getName() {

			return mName;
		}

		public void setName( String name ) {

			mName = name;
		}

		public NestedFoo getNestedFoo() {

			return mNestedFoo;
		}

		public void setNestedFoo( NestedFoo nestedFoo ) {

			mNestedFoo = nestedFoo;
		}
	}
}
