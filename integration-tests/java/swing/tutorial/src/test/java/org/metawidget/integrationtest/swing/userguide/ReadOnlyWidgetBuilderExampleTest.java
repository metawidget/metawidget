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

package org.metawidget.integrationtest.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiDontExpand;
import org.metawidget.integrationtest.swing.tutorial.Person;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
		assertEquals( 7, metawidget.getComponentCount() );
	}

	@SuppressWarnings( "unchecked" )
	public void testBoundWidgetBuilderExample()
		throws Exception {

		BoundWidgetBuilderExampleNestedModel nestedModel = new BoundWidgetBuilderExampleNestedModel();
		nestedModel.setFirstname( "Homer" );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new ReadOnlyTextFieldWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setReadOnly( true );
		metawidget.setToInspect( nestedModel );

		assertEquals( "Title:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( "", ( (JTextField) metawidget.getComponent( 1 ) ).getText() );
		assertFalse( ( (JTextField) metawidget.getComponent( 1 ) ).isEditable() );
		assertEquals( "Firstname:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertEquals( "Homer", ( (JTextField) metawidget.getComponent( 3 ) ).getText() );
		assertFalse( ( (JTextField) metawidget.getComponent( 3 ) ).isEditable() );

		BoundWidgetBuilderExampleModel model = new BoundWidgetBuilderExampleModel();
		model.setNested( nestedModel );
		metawidget.setToInspect( model );

		assertEquals( "Nested:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( "Homer", ( (JTextField) metawidget.getComponent( 1 ) ).getText() );
		assertFalse( ( (JTextField) metawidget.getComponent( 1 ) ).isEditable() );
		assertEquals( 3, metawidget.getComponentCount() );
	}

	//
	// Inner class
	//

	static class ReadOnlyTextFieldWidgetBuilder
		implements WidgetBuilder<JComponent, SwingMetawidget> {

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

	public static class BoundWidgetBuilderExampleModel {

		private BoundWidgetBuilderExampleNestedModel	mNested;

		@UiDontExpand
		public BoundWidgetBuilderExampleNestedModel getNested() {

			return mNested;
		}

		public void setNested( BoundWidgetBuilderExampleNestedModel nested ) {

			mNested = nested;
		}
	}

	public static class BoundWidgetBuilderExampleNestedModel {

		private String	mTitle;

		private String	mFirstname;


		public String getTitle() {

			return mTitle;
		}

		public void setTitle( String title ) {

			mTitle = title;
		}

		@UiComesAfter( "title" )
		public String getFirstname() {

			return mFirstname;
		}

		public void setFirstname( String firstname ) {

			mFirstname = firstname;
		}

		@Override
		public String toString() {

			return mFirstname;
		}
	}
}
