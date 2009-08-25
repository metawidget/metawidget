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

package org.metawidget.swing.widgetprocessor.validator.inputverifier;

import java.util.Map;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JSpinner;

import junit.framework.TestCase;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class InputVerifierProcessorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testValidator()
		throws Exception
	{
		// Model

		Foo foo1 = new Foo();
		foo1.setBar( 42 );
		Foo foo2 = new Foo();
		foo1.setNestedFoo( foo2 );

		SwingMetawidget metawidget = new SwingMetawidget();

		// Inspect

		metawidget.setInspector( new PropertyTypeInspector() );
		metawidget.setToInspect( foo1 );

		JSpinner spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( null == spinner.getInputVerifier() );

		// Setup

		metawidget.addWidgetProcessor( new InputVerifierProcessor()
		{
			//
			// Public methods
			//

			@Override
			public void onStartBuild( SwingMetawidget theMetawidget )
			{
				theMetawidget.putClientProperty( "onStartBuild", Boolean.TRUE );
			}

			//
			// Protected methods
			//

			@Override
			protected InputVerifier getInputVerifier( JComponent component, Map<String, String> attributes, String path )
			{
				if ( component instanceof SwingMetawidget )
					return null;

				return new InputVerifier()
				{
					@Override
					public boolean verify( JComponent input )
					{
						return false;
					}
				};
			}
		} );

		// Validate

		assertTrue( null == metawidget.getClientProperty( "onStartBuild" ) );
		spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( !spinner.getInputVerifier().verify( spinner ) );
		assertTrue( Boolean.TRUE.equals( metawidget.getClientProperty( "onStartBuild" ) ) );

		SwingMetawidget nestedMetawidget = (SwingMetawidget) metawidget.getComponent( 3 );
		assertTrue( null == nestedMetawidget.getInputVerifier() );

		JSpinner nestedSpinner = (JSpinner) nestedMetawidget.getComponent( 1 );
		assertTrue( !nestedSpinner.getInputVerifier().verify( spinner ) );
		assertTrue( Boolean.TRUE.equals( nestedMetawidget.getClientProperty( "onStartBuild" ) ) );
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

		private long	mBar;

		private Foo		mNestedFoo;

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

		public Foo getNestedFoo()
		{
			return mNestedFoo;
		}

		public void setNestedFoo( Foo nestedFoo )
		{
			mNestedFoo = nestedFoo;
		}
	}
}
