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

package org.metawidget.swing.widgetprocessor.validator.jgoodies;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JGoodiesValidatorProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testValidator()
		throws Exception {

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() ) ) );
		metawidget.setToInspect( new Foo() );
		metawidget.addWidgetProcessor( new JGoodiesValidatorProcessor() );

		// Initial validation

		JTextField textField1 = (JTextField) metawidget.getComponent( 1 );
		assertEquals( null, ValidationComponentUtils.getMessageKeys( textField1 ) );
		assertTrue( ValidationComponentUtils.isMandatory( textField1 ) );
		assertEquals( ValidationComponentUtils.getMandatoryBorder(), textField1.getBorder() );
		assertEquals( ValidationComponentUtils.getMandatoryBackground(), textField1.getBackground() );

		JTextField textField2 = (JTextField) metawidget.getComponent( 3 );
		assertEquals( null, ValidationComponentUtils.getMessageKeys( textField2 ) );
		assertFalse( ValidationComponentUtils.isMandatory( textField2 ) );

		// Validation after a keypress

		textField1.setText( "Not empty" );
		textField1.getKeyListeners()[0].keyReleased( null );

		assertEquals( ValidationComponentUtils.getMandatoryBorder(), textField1.getBorder() );
		assertFalse( ValidationComponentUtils.getMandatoryBackground().equals( textField1.getBackground() ) );

		// Validation after deleting contents

		textField1.setText( "" );
		textField1.getKeyListeners()[0].keyReleased( null );

		assertEquals( ValidationComponentUtils.getMandatoryBorder(), textField1.getBorder() );
		assertEquals( ValidationComponentUtils.getMandatoryBackground(), textField1.getBackground() );

		// Custom validator

		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() ) ) );
		metawidget.setToInspect( new Foo() );
		metawidget.setWidgetProcessors( (WidgetProcessor<JComponent, SwingMetawidget>[]) null );
		metawidget.addWidgetProcessor( new JGoodiesValidatorProcessor() {

			@Override
			protected Validator<?> getValidator( final JComponent component, final Map<String, String> attributes, String path, SwingMetawidget theMetawidget ) {

				return new Validator<String>() {

					public ValidationResult validate( String validationTarget ) {

						if ( "error".equals( validationTarget ) ) {
							ValidationMessage message = new SimpleValidationMessage( "MyJGoodiesValidator error", Severity.ERROR, attributes.get( NAME ) );
							ValidationResult validationResult = new ValidationResult();
							validationResult.add( message );

							return validationResult;
						}

						if ( "warning".equals( validationTarget ) ) {
							ValidationMessage message = new SimpleValidationMessage( "MyJGoodiesValidator warning", Severity.WARNING, attributes.get( NAME ) );
							ValidationResult validationResult = new ValidationResult();
							validationResult.add( message );

							return validationResult;
						}

						return null;
					}
				};
			}
		} );
		textField1 = (JTextField) metawidget.getComponent( 1 );
		assertEquals( "bar", ValidationComponentUtils.getMessageKeys( textField1 )[0] );

		textField1.setText( "error" );
		textField1.getKeyListeners()[0].keyReleased( null );
		assertEquals( ValidationComponentUtils.getErrorBackground(), textField1.getBackground() );

		textField1.setText( "warning" );
		textField1.getKeyListeners()[0].keyReleased( null );
		assertEquals( ValidationComponentUtils.getWarningBackground(), textField1.getBackground() );

		textField1.setText( "all good" );
		textField1.getKeyListeners()[0].keyReleased( null );
		assertFalse( ValidationComponentUtils.getErrorBackground().equals( textField1.getBackground() ) );
		assertFalse( ValidationComponentUtils.getWarningBackground().equals( textField1.getBackground() ) );
		assertFalse( ValidationComponentUtils.getMandatoryBackground().equals( textField1.getBackground() ) );
	}

	//
	// Inner class
	//

	protected static class Foo {

		//
		//
		// Private members
		//
		//

		private String	mBar;

		//
		//
		// Public methods
		//
		//

		@UiRequired
		public String getBar() {

			return mBar;
		}

		public void setBar( String bar ) {

			mBar = bar;
		}

		public String getNotValidated() {

			return null;
		}

		/**
		 * @param notValidated
		 */

		public void setNotValidated( String notValidated ) {

			// Do nothing
		}
	}
}
