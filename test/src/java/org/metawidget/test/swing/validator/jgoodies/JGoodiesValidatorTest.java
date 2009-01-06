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

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.validator.jgoodies.JGoodiesValidator;

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
		assertTrue( ValidationComponentUtils.isMandatory( textField ));
		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField.getBorder() ));
		assertTrue( ValidationComponentUtils.getMandatoryBackground().equals( textField.getBackground() ));

		// Validation after a keypress

		textField.setText( "Not empty" );
		textField.getKeyListeners()[0].keyReleased( null );

		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField.getBorder() ));
		assertTrue( !ValidationComponentUtils.getMandatoryBackground().equals( textField.getBackground() ));

		// Validation after deleting contents

		textField.setText( "" );
		textField.getKeyListeners()[0].keyReleased( null );

		assertTrue( ValidationComponentUtils.getMandatoryBorder().equals( textField.getBorder() ));
		assertTrue( ValidationComponentUtils.getMandatoryBackground().equals( textField.getBackground() ));
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

		private String						mBar;

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
}
