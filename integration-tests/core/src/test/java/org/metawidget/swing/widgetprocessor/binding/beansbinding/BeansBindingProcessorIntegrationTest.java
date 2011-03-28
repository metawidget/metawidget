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

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayout;
import org.metawidget.swing.layout.TabbedPaneLayoutDecorator;
import org.metawidget.swing.layout.TabbedPaneLayoutDecoratorConfig;
import org.metawidget.test.model.annotatedaddressbook.Contact;
import org.metawidget.test.model.annotatedaddressbook.PersonalContact;

/**
 * @author Richard Kennard
 */

public class BeansBindingProcessorIntegrationTest
	extends TestCase {

	//
	// Public methods
	//

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
}
