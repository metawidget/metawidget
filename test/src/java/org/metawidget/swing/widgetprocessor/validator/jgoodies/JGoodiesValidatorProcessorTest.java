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

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Richard Kennard
 */

public class JGoodiesValidatorProcessorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testValidator()
		throws Exception
	{
		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() ) ) );
		metawidget.setToInspect( new Foo() );
		metawidget.addWidgetProcessor( new JGoodiesValidatorProcessor() );

		// Initial validation

		JTextField textField1 = (JTextField) metawidget.getComponent( 1 );
		assertTrue( null == ValidationComponentUtils.getMessageKeys( textField1 ) );
		assertTrue( ValidationComponentUtils.isMandatory( textField1 ) );
		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField1.getBorder() ) );
		assertTrue( ValidationComponentUtils.getMandatoryBackground().equals( textField1.getBackground() ) );

		JTextField textField2 = (JTextField) metawidget.getComponent( 3 );
		assertTrue( null == ValidationComponentUtils.getMessageKeys( textField2 ) );
		assertTrue( !ValidationComponentUtils.isMandatory( textField2 ) );

		// Validation after a keypress

		textField1.setText( "Not empty" );
		textField1.getKeyListeners()[0].keyReleased( null );

		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField1.getBorder() ) );
		assertTrue( !ValidationComponentUtils.getMandatoryBackground().equals( textField1.getBackground() ) );

		// Validation after deleting contents

		textField1.setText( "" );
		textField1.getKeyListeners()[0].keyReleased( null );

		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField1.getBorder() ) );
		assertTrue( ValidationComponentUtils.getMandatoryBackground().equals( textField1.getBackground() ) );

		// Custom validator

		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() ) ) );
		metawidget.setToInspect( new Foo() );
		metawidget.setWidgetProcessors( null );
		metawidget.addWidgetProcessor( new JGoodiesValidatorProcessor()
		{
			@Override
			protected Validator<?> getValidator( final JComponent component, final Map<String, String> attributes, String path )
			{
				return new Validator<String>()
				{
					@Override
					public ValidationResult validate( String validationTarget )
					{
						if ( "error".equals( validationTarget ) )
						{
							ValidationMessage message = new SimpleValidationMessage( "MyJGoodiesValidator error", Severity.ERROR, attributes.get( NAME ) );
							ValidationResult validationResult = new ValidationResult();
							validationResult.add( message );

							return validationResult;
						}

						if ( "warning".equals( validationTarget ) )
						{
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
		assertTrue( "bar".equals( ValidationComponentUtils.getMessageKeys( textField1 )[0] ) );

		textField1.setText( "error" );
		textField1.getKeyListeners()[0].keyReleased( null );
		assertTrue( ValidationComponentUtils.getErrorBackground().equals( textField1.getBackground() ) );

		textField1.setText( "warning" );
		textField1.getKeyListeners()[0].keyReleased( null );
		assertTrue( ValidationComponentUtils.getWarningBackground().equals( textField1.getBackground() ) );

		textField1.setText( "all good" );
		textField1.getKeyListeners()[0].keyReleased( null );
		assertTrue( !ValidationComponentUtils.getErrorBackground().equals( textField1.getBackground() ) );
		assertTrue( !ValidationComponentUtils.getWarningBackground().equals( textField1.getBackground() ) );
		assertTrue( !ValidationComponentUtils.getMandatoryBackground().equals( textField1.getBackground() ) );
	}

	//
	// Inner class
	//

	protected static class Foo
	{
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
		public String getBar()
		{
			return mBar;
		}

		public void setBar( String bar )
		{
			mBar = bar;
		}

		public String getNotValidated()
		{
			return null;
		}

		public void setNotValidated( String notValidated )
		{
			// Do nothing
		}
	}
}
