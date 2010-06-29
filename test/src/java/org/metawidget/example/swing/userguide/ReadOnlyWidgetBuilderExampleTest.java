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

package org.metawidget.example.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.swing.tutorial.Person;
import org.metawidget.inspector.annotation.UiDontExpand;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilderExampleTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
	public void testWidgetBuilderExample()
		throws Exception {

		Person person = new Person();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new ReadOnlyTextFieldWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.setReadOnly( true );
		metawidget.setToInspect( person );

		assertTrue( metawidget.getComponent( 0 ) instanceof JLabel );
		assertFalse( ( (JTextField) metawidget.getComponent( 1 ) ).isEditable() );
		assertTrue( metawidget.getComponent( 2 ) instanceof JLabel );
		assertFalse( ( (JTextField) metawidget.getComponent( 3 ) ).isEditable() );
		assertTrue( metawidget.getComponent( 4 ) instanceof JLabel );
		assertFalse( ( (JTextField) metawidget.getComponent( 5 ) ).isEditable() );
		assertTrue( metawidget.getComponent( 6 ) instanceof JPanel );
		assertTrue( 7 == metawidget.getComponentCount() );
	}

	@SuppressWarnings( "unchecked" )
	public void testBoundWidgetBuilderExample()
		throws Exception {

		PersonalContact contact = new PersonalContact();
		contact.setFirstname( "Homer" );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new ReadOnlyTextFieldWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setReadOnly( true );
		metawidget.setToInspect( contact );

		assertEquals( "Title:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( "", ( (JTextField) metawidget.getComponent( 1 ) ).getText() );
		assertFalse( ( (JTextField) metawidget.getComponent( 1 ) ).isEditable() );
		assertEquals( "Firstname:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertEquals( "Homer", ( (JTextField) metawidget.getComponent( 3 ) ).getText() );
		assertFalse( ( (JTextField) metawidget.getComponent( 3 ) ).isEditable() );

		Foo foo = new Foo();
		foo.setContact( contact );
		metawidget.setToInspect( foo );

		assertEquals( "Contact:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( "Homer", ( (JTextField) metawidget.getComponent( 1 ) ).getText() );
		assertFalse( ( (JTextField) metawidget.getComponent( 1 ) ).isEditable() );
		assertTrue( 3 == metawidget.getComponentCount() );
	}

	//
	// Inner class
	//

	static class ReadOnlyTextFieldWidgetBuilder
		implements WidgetBuilder<JComponent, SwingMetawidget> {

		@Override
		public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

			if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
				return null;
			}

			if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
				return null;
			}

			Class<?> clazz = ClassUtils.niceForName( attributes.get( TYPE ) );

			if ( String.class.equals( clazz ) || clazz.isPrimitive() || TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
				JTextField textField = new JTextField();
				textField.setEditable( false );

				return textField;
			}

			return null;
		}
	}

	public static class Foo {

		private Contact	mContact;

		@UiDontExpand
		public Contact getContact() {

			return mContact;
		}

		public void setContact( Contact contact ) {

			mContact = contact;
		}
	}
}
