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

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.validator.jgoodies.JGoodiesValidator;

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
		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setValidatorClass( JGoodiesValidator.class );
		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setToInspect( foo );

		// TODO: implement me!
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

		private long						mBar;

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
