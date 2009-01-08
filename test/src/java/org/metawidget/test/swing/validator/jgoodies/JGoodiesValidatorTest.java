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

package org.metawidget.test.swing.validator.jgoodies;

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
import org.metawidget.swing.validator.jgoodies.JGoodiesValidator;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Richard Kennard
 */

public class JGoodiesValidatorTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public JGoodiesValidatorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testValidator()
		throws Exception
	{
		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setValidatorClass( JGoodiesValidator.class );
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() ) ) );
		metawidget.setToInspect( new Foo() );

		// Initial validation

		JTextField textField = (JTextField) metawidget.getComponent( 1 );
		assertTrue( null == ValidationComponentUtils.getMessageKeys( textField ));
		assertTrue( ValidationComponentUtils.isMandatory( textField ) );
		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField.getBorder() ) );
		assertTrue( ValidationComponentUtils.getMandatoryBackground().equals( textField.getBackground() ) );

		// Validation after a keypress

		textField.setText( "Not empty" );
		textField.getKeyListeners()[0].keyReleased( null );

		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField.getBorder() ) );
		assertTrue( !ValidationComponentUtils.getMandatoryBackground().equals( textField.getBackground() ) );

		// Validation after deleting contents

		textField.setText( "" );
		textField.getKeyListeners()[0].keyReleased( null );

		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField.getBorder() ) );
		assertTrue( ValidationComponentUtils.getMandatoryBackground().equals( textField.getBackground() ) );

		// Custom validator

		metawidget.setValidatorClass( MyJGoodiesValidator.class );
		textField = (JTextField) metawidget.getComponent( 1 );
		assertTrue( "bar".equals( ValidationComponentUtils.getMessageKeys( textField )[0] ));

		textField.setText( "error" );
		textField.getKeyListeners()[0].keyReleased( null );
		assertTrue( ValidationComponentUtils.getErrorBackground().equals( textField.getBackground() ));

		textField.setText( "warning" );
		textField.getKeyListeners()[0].keyReleased( null );
		assertTrue( ValidationComponentUtils.getWarningBackground().equals( textField.getBackground() ));

		textField.setText( "all good" );
		textField.getKeyListeners()[0].keyReleased( null );
		assertTrue( !ValidationComponentUtils.getErrorBackground().equals( textField.getBackground() ));
		assertTrue( !ValidationComponentUtils.getWarningBackground().equals( textField.getBackground() ));
		assertTrue( !ValidationComponentUtils.getMandatoryBackground().equals( textField.getBackground() ) );
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
	}

	protected static class MyJGoodiesValidator
		extends JGoodiesValidator
	{
		//
		// Constructor
		//

		public MyJGoodiesValidator( SwingMetawidget metawidget )
		{
			super( metawidget );
		}

		//
		// Protected methods
		//

		@Override
		protected Validator<?> getValidator( final JComponent component, final Map<String, String> attributes, String path )
		{
			return new Validator<String>()
			{
				@Override
				public ValidationResult validate( String validationTarget )
				{
					if ( "error".equals( validationTarget ))
					{
						ValidationMessage message = new SimpleValidationMessage( "MyJGoodiesValidator error", Severity.ERROR, attributes.get( NAME ));
						ValidationResult validationResult = new ValidationResult();
						validationResult.add( message );

						return validationResult;
					}

					if ( "warning".equals( validationTarget ))
					{
						ValidationMessage message = new SimpleValidationMessage( "MyJGoodiesValidator warning", Severity.WARNING, attributes.get( NAME ));
						ValidationResult validationResult = new ValidationResult();
						validationResult.add( message );

						return validationResult;
					}

					return null;
				}
			};
		}
	}
}
