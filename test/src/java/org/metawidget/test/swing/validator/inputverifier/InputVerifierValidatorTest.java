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

package org.metawidget.test.swing.validator.inputverifier;

import java.awt.Component;
import java.util.Map;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JSpinner;

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.validator.inputverifier.InputVerifierValidator;

/**
 * @author Richard Kennard
 */

public class InputVerifierValidatorTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public InputVerifierValidatorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testValidator()
		throws Exception
	{
		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setToInspect( foo );

		JSpinner spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( null == spinner.getInputVerifier() );

		// Validate

		metawidget.setValidatorClass( MyInputVerifierValidator.class );
		spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( !spinner.getInputVerifier().verify( spinner ) );
	}

	//
	// Inner class
	//

	protected static class MyInputVerifierValidator
		extends InputVerifierValidator
	{
		//
		// Constructor
		//

		public MyInputVerifierValidator( SwingMetawidget metawidget )
		{
			super( metawidget );
		}

		//
		// Public methods
		//

		@Override
		protected InputVerifier getInputVerifier( Component component, Map<String, String> attributes, String path )
		{
			return new InputVerifier()
			{
				@Override
				public boolean verify( JComponent input )
				{
					return false;
				}
			};
		}
	}

	protected static class Foo
	{
		//
		//
		// Private members
		//
		//

		private long	mBar;

		//
		//
		// Public methods
		//
		//

		public long getBar()
		{
			return mBar;
		}

		public void setBar( long bar )
		{
			mBar = bar;
		}
	}
}
