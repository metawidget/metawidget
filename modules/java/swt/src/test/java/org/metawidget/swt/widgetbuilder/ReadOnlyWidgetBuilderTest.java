// Metawidget (licensed under LGPL)
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

package org.metawidget.swt.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testReadOnlyDontExpand() {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		ReadOnlyWidgetBuilder widgetBuilder = new ReadOnlyWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, Foo.class.getName() );
		attributes.put( READ_ONLY, TRUE );
		attributes.put( DONT_EXPAND, TRUE );
		Control widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertTrue( widget instanceof Label );

		attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, Foo.class.getName() );
		attributes.put( READ_ONLY, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ));
	}

	//
	// Inner class
	//

	public static class Foo {

		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}

		public String getBaz() {

			return null;
		}

		public void setBaz( @SuppressWarnings( "unused" ) String baz ) {

			// Do nothing
		}
	}

	public static class NestedFoo {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		public Foo getNestedFoo() {

			return null;
		}

		public void setNestedFoo( @SuppressWarnings( "unused" ) Foo nestedFoo ) {

			// Do nothing
		}
	}
}
