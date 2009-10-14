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

package org.metawidget.android;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.android.widget.AndroidConfigReaderTest;
import org.metawidget.android.widget.AndroidMetawidgetTest;
import org.metawidget.android.widget.FacetTest;
import org.metawidget.android.widget.StubTest;
import org.metawidget.android.widget.layout.LinearLayoutTest;
import org.metawidget.android.widget.widgetbuilder.AndroidWidgetBuilderTest;

/**
 * @author Richard Kennard
 */

public class AndroidMetawidgetTests
	extends TestCase
{
	//
	// Public statics
	//

	public static Test suite()
	{
		TestSuite suite = new TestSuite( "Android Metawidget Tests" );

		suite.addTestSuite( AndroidConfigReaderTest.class );
		suite.addTestSuite( AndroidMetawidgetTest.class );
		suite.addTestSuite( AndroidWidgetBuilderTest.class );
		suite.addTestSuite( FacetTest.class );
		suite.addTestSuite( LinearLayoutTest.class );
		suite.addTestSuite( StubTest.class );

		return suite;
	}
}
