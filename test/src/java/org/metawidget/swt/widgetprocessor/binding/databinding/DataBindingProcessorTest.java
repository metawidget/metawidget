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

package org.metawidget.swt.widgetprocessor.binding.databinding;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.swt.layout.GridLayout;
import org.metawidget.swt.layout.TabFolderLayoutDecorator;
import org.metawidget.swt.layout.TabFolderLayoutDecoratorConfig;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessor.ConvertFromTo;
import org.metawidget.util.TestUtils;

/**
 * @author Richard Kennard
 */

public class DataBindingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		TestUtils.testEqualsAndHashcode( DataBindingProcessorConfig.class, new DataBindingProcessorConfig() {
			// subclass
		} );
		TestUtils.testEqualsAndHashcode( new ConvertFromTo( Integer.class, String.class ), new ConvertFromTo( Integer.class, String.class ), null );
	}

	public void testNestedMetawidget() {

		Contact contact = new PersonalContact();
		Shell shell = new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE );
		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
	    metawidget.addWidgetProcessor( new DataBindingProcessor() );
        metawidget.setToInspect( contact );

        // Just GridBagLayout

        assertEquals( null, contact.getFirstname() );
        assertEquals( null, contact.getAddress().getStreet() );
        ((Text) metawidget.getControl( "firstname" )).setText( "Foo" );
        ((Text) metawidget.getControl( "address", "street" )).setText( "Bar" );
        assertEquals( metawidget, metawidget.getControl( "address", "street" ).getParent().getParent() );
        metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );
        assertEquals( "Foo", contact.getFirstname() );
        assertEquals( "Bar", contact.getAddress().getStreet() );

        // With TabbedPaneLayoutDecorator

        metawidget.setMetawidgetLayout( new TabFolderLayoutDecorator( new TabFolderLayoutDecoratorConfig().setLayout( new GridLayout() ) ));
        ((Text) metawidget.getControl( "firstname" )).setText( "Foo1" );
        ((Text) metawidget.getControl( "address", "street" )).setText( "Bar1" );
        assertTrue( metawidget.getControl( "address", "street" ).getParent().getParent().getParent() instanceof TabFolder );
        metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );
        assertEquals( "Foo1", contact.getFirstname() );
        assertEquals( "Bar1", contact.getAddress().getStreet() );
	}
}
