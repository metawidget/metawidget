// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.swt;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwtMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	/**
	 * Tests <code>buildWidgets</code> uses <code>configureOnce</code>.
	 */

	public void testConfigureOnce() {

		List<String> configured = new ArrayList<String>();
		SwtMetawidget metawidget = new ConfiguredSwtMetawidget( configured, new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		metawidget.setToInspect( new Foo() );
		metawidget.getChildren();

		metawidget.setToInspect( new Foo() );
		metawidget.getChildren();
		assertEquals( 2, configured.size() );
	}

	//
	// Inner class
	//

	protected static class ConfiguredSwtMetawidget
		extends SwtMetawidget {

		//
		// Private members
		//

		private List<String>	mConfigured;

		//
		// Constructor
		//

		public ConfiguredSwtMetawidget( List<String> configured, Composite parent, int style ) {

			this( parent, style );
			mConfigured = configured;
		}

		public ConfiguredSwtMetawidget( Composite parent, int style ) {

			super( parent, style );
		}

		//
		// Protected methods
		//

		@Override
		protected String getDefaultConfiguration() {

			if ( mConfigured != null ) {
				mConfigured.add( "called" );
			}
			return super.getDefaultConfiguration();
		}
	}

	public static class Foo {

		//
		// Private members
		//

		private String	mName;

		private Foo		mFoo;

		//
		// Public methods
		//

		public String getName() {

			return mName;
		}

		public void setName( String name ) {

			mName = name;
		}

		public Foo getFoo() {

			return mFoo;
		}

		public void setFoo( Foo foo ) {

			mFoo = foo;
		}
	}
}
