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

package org.metawidget.swt.widgetprocessor.binding.databinding;

import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.swt.layout.GridLayout;
import org.metawidget.swt.layout.TabFolderLayoutDecorator;
import org.metawidget.swt.layout.TabFolderLayoutDecoratorConfig;
import org.metawidget.swt.widgetbuilder.SwtWidgetBuilder;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessor.ConvertFromTo;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DataBindingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( DataBindingProcessorConfig.class, new DataBindingProcessorConfig() {
			// subclass
		} );
		MetawidgetTestUtils.testObjectEqualsAndHashcode( new ConvertFromTo( Integer.class, String.class ), new ConvertFromTo( Integer.class, String.class ), null );
	}

	public void testConvertFromString() {

		DataBindingProcessorConfig config = new DataBindingProcessorConfig();
		config.setConverters( new IConverter() {

			public Object convert( Object value ) {

				return Boolean.valueOf( (String) value );
			}

			public Object getFromType() {

				return String.class;
			}

			public Object getToType() {

				return Boolean.class;
			}
		} );

		assertEquals( false, new DataBindingProcessor( config ).convertFromString( "false", Boolean.class ));
		assertEquals( "false", new DataBindingProcessor( config ).convertFromString( "false", String.class ));
		assertEquals( "no-converter", new DataBindingProcessor( config ).convertFromString( "no-converter", Date.class ));
	}

	@SuppressWarnings( "cast" )
	public void testNestedMetawidget() {

		PersonalContact contact = new PersonalContact();
		Shell shell = new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE );
		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.addWidgetProcessor( new DataBindingProcessor() );
		metawidget.setToInspect( contact );

		// Just GridBagLayout

		assertEquals( null, contact.getFirstname() );
		assertEquals( null, contact.getAddress().getStreet() );
		( (Text) metawidget.getControl( "firstname" ) ).setText( "Foo" );
		( (Text) metawidget.getControl( "address", "street" ) ).setText( "Bar" );
		assertEquals( metawidget, ((Control) metawidget.getControl( "address", "street" )).getParent().getParent() );
		metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo", contact.getFirstname() );
		assertEquals( "Bar", contact.getAddress().getStreet() );

		// With TabbedPaneLayoutDecorator

		metawidget.setMetawidgetLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new GridLayout() ) ) );
		( (Text) metawidget.getControl( "firstname" ) ).setText( "Foo1" );
		( (Text) ((Control) metawidget.getControl( "address", "street" ) )).setText( "Bar1" );
		assertTrue( ((Control) metawidget.getControl( "address", "street" )).getParent().getParent().getParent() instanceof TabFolder );
		metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );
		assertEquals( "Foo1", contact.getFirstname() );
		assertEquals( "Bar1", contact.getAddress().getStreet() );
	}

	public void testMissingReadOnlyWidgetBuilder() {

		Foo foo = new Foo();
		foo.setBar( 35 );

		Shell shell = new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE );
		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.addWidgetProcessor( new DataBindingProcessor() );
		metawidget.setWidgetBuilder( new SwtWidgetBuilder() );
		metawidget.setToInspect( foo );

		assertEquals( 35, ( (Spinner) metawidget.getControl( "bar" ) ).getSelection() );
		( (Spinner) metawidget.getControl( "bar" ) ).setSelection( 36 );
		metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );
		assertEquals( 36l, foo.getBar() );
		metawidget.setReadOnly( true );
		assertEquals( 36l, ( (Spinner) metawidget.getControl( "bar" ) ).getSelection() );
	}

	//
	// Inner class
	//

	public static class Foo {

		//
		// Private members
		//

		private int	mBar;

		//
		// Public methods
		//

		public int getBar() {

			return mBar;
		}

		public void setBar( int bar ) {

			mBar = bar;
		}
	}

	public static class PersonalContact {

		//
		// Private members
		//

		private String	mFirstname;

		private Address	mAddress = new Address();

		//
		// Public methods
		//

		public String getFirstname() {

			return mFirstname;
		}

		public void setFirstname( String firstname ) {

			mFirstname = firstname;
		}

		@UiSection( "Contact Details" )
		public Address getAddress() {

			return mAddress;
		}

		public void setAddress( Address address ) {

			mAddress = address;
		}
	}

	public static class Address {

		//
		// Private members
		//

		private String	mStreet;

		//
		// Public methods
		//

		public String getStreet() {

			return mStreet;
		}

		public void setStreet( String street ) {

			mStreet = street;
		}
	}
}
