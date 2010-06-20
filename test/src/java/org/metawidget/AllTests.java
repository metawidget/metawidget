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

package org.metawidget;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.android.AndroidMetawidgetTests;
import org.metawidget.config.ConfigReaderTest;
import org.metawidget.config.XmlSchemaGeneratorTaskTest;
import org.metawidget.example.ExampleTests;
import org.metawidget.faces.FacesMetawidgetTests;
import org.metawidget.iface.MetawidgetExceptionTest;
import org.metawidget.inspectionresultprocessor.InspectionResultProcessorTests;
import org.metawidget.inspector.InspectorTests;
import org.metawidget.jsp.JspMetawidgetTests;
import org.metawidget.layout.LayoutTests;
import org.metawidget.pipeline.PipelineTests;
import org.metawidget.swing.SwingMetawidgetTests;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.InspectorUtilsTest;
import org.metawidget.util.UtilTests;
import org.metawidget.widgetbuilder.WidgetBuilderTests;
import org.metawidget.widgetprocessor.WidgetProcessorTests;

/**
 * @author Richard Kennard
 */

public class AllTests
	extends TestCase
{
	//
	// Public statics
	//

	public static Test suite()
	{
		// Note: if this fails saying 'java.lang.RuntimeException: Stub!' or such, it's
		// probably because it is picking up the version of JUnit inside android.jar.
		//
		// To fix, place the junit-4.5.jar first on your CLASSPATH

		TestSuite suite = new TestSuite( "Metawidget Tests" );
		suite.addTest( AndroidMetawidgetTests.suite() );
		suite.addTestSuite( ConfigReaderTest.class );
		suite.addTest( ExampleTests.suite() );
		suite.addTest( FacesMetawidgetTests.suite() );
		suite.addTest( InspectorTests.suite() );
		suite.addTestSuite( InspectorUtilsTest.class );
		suite.addTest( InspectionResultProcessorTests.suite() );
		suite.addTest( JspMetawidgetTests.suite() );
		suite.addTest( LayoutTests.suite() );
		suite.addTestSuite( MetawidgetExceptionTest.class );
		suite.addTest( PipelineTests.suite() );
		suite.addTest( SwingMetawidgetTests.suite() );
		suite.addTest( SwtMetawidgetTests.suite() );
		suite.addTest( UtilTests.suite() );
		suite.addTest( WidgetBuilderTests.suite() );
		suite.addTest( WidgetProcessorTests.suite() );
		suite.addTestSuite( XmlSchemaGeneratorTaskTest.class );

		return suite;
	}
}