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

package org.metawidget.swing.widgetprocessor.binding.beanutils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConvertUtils;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.scala.ScalaPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayout;
import org.metawidget.swing.layout.TabbedPaneLayoutDecorator;
import org.metawidget.swing.layout.TabbedPaneLayoutDecoratorConfig;
import org.metawidget.util.TestUtils;

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

		ConvertUtils.register( new org.metawidget.swing.allwidgets.converter.beanutils.DateConverter( DATE_FORMAT ), Date.class );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( new BeanUtilsBindingProcessor( new BeanUtilsBindingProcessorConfig().setPropertyStyle( BeanUtilsBindingProcessorConfig.PROPERTYSTYLE_SCALA ) ) );
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
		assertTrue( 1976 == calendar.get( Calendar.YEAR ) );
		assertTrue( null == scalaFoo2.bar() );
		calendar.setTime( scalaFoo3.bar() );
		assertTrue( 1977 == calendar.get( Calendar.YEAR ) );

		// Rebinding

		textField = (JTextField) metawidget.getComponent( 1 );
		assertEquals( dateFormat.format( dateSecond ), textField.getText() );

		scalaFoo.bar_$eq( dateFirst );
		metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).rebind( scalaFoo, metawidget );

		textField = (JTextField) metawidget.getComponent( 1 );
		assertEquals( dateFormat.format( dateFirst ), textField.getText() );
	}

	public void testConfig() {

		TestUtils.testEqualsAndHashcode( BeanUtilsBindingProcessorConfig.class, new BeanUtilsBindingProcessorConfig() {
			// Subclass
		} );
	}

	public void testNestedMetawidget() {

		Contact contact = new PersonalContact();
	    SwingMetawidget metawidget = new SwingMetawidget();
	    metawidget.addWidgetProcessor( new BeanUtilsBindingProcessor() );
	    metawidget.add( new Stub( "dateOfBirth" ));
        metawidget.setToInspect( contact );

        // Just GridBagLayout

        assertEquals( null, contact.getFirstname() );
        assertEquals( null, contact.getAddress().getStreet() );
        ((JTextField) metawidget.getComponent( "firstname" )).setText( "Foo" );
        ((JTextField) metawidget.getComponent( "address", "street" )).setText( "Bar" );
        assertEquals( metawidget, metawidget.getComponent( "address", "street" ).getParent().getParent() );
        metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).save( metawidget );
        assertEquals( "Foo", contact.getFirstname() );
        assertEquals( "Bar", contact.getAddress().getStreet() );

        // With TabbedPaneLayoutDecorator

        metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new GridBagLayout() ) ));
        ((JTextField) metawidget.getComponent( "firstname" )).setText( "Foo1" );
        ((JTextField) metawidget.getComponent( "address", "street" )).setText( "Bar1" );
        assertTrue( metawidget.getComponent( "address", "street" ).getParent().getParent().getParent() instanceof JTabbedPane );
        metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).save( metawidget );
        assertEquals( "Foo1", contact.getFirstname() );
        assertEquals( "Bar1", contact.getAddress().getStreet() );

        // Test rebind

        contact = new PersonalContact();
        contact.setFirstname( "Foo2" );
        contact.getAddress().setStreet( "Bar2" );
        metawidget.getWidgetProcessor( BeanUtilsBindingProcessor.class ).rebind( contact, metawidget );
        assertEquals( "Foo2", ((JTextField) metawidget.getComponent( "firstname" )).getText() );
        assertEquals( "Bar2", ((JTextField) metawidget.getComponent( "address", "street" )).getText() );
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
}
