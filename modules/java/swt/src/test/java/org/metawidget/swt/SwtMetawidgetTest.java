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
