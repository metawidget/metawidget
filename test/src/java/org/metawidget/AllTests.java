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

import org.metawidget.config.ConfigReaderTest;
import org.metawidget.config.XmlSchemaGeneratorTaskTest;
import org.metawidget.example.ExampleTests;
import org.metawidget.faces.FacesMetawidgetTests;
import org.metawidget.gwt.client.ui.layout.FlexTableLayoutTest;
import org.metawidget.iface.MetawidgetExceptionTest;
import org.metawidget.inspector.InspectorTests;
import org.metawidget.jsp.JspMetawidgetTests;
import org.metawidget.layout.iface.LayoutExceptionTest;
import org.metawidget.layout.impl.LayoutUtilsTest;
import org.metawidget.mixin.MixinTests;
import org.metawidget.swing.SwingMetawidgetTests;
import org.metawidget.util.UtilTests;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderTest;
import org.metawidget.widgetbuilder.iface.WidgetBuilderExceptionTest;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilderTest;
import org.metawidget.widgetprocessor.iface.WidgetProcessorExceptionTest;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessorTest;

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
		suite.addTestSuite( BaseWidgetBuilderTest.class );
		suite.addTestSuite( BaseWidgetProcessorTest.class );
		suite.addTestSuite( CompositeWidgetBuilderTest.class );
		suite.addTestSuite( ConfigReaderTest.class );
		suite.addTest( ExampleTests.suite() );
		suite.addTest( FacesMetawidgetTests.suite() );
		suite.addTestSuite( FlexTableLayoutTest.class );
		suite.addTest( InspectorTests.suite() );
		suite.addTest( JspMetawidgetTests.suite() );
		suite.addTestSuite( LayoutExceptionTest.class );
		suite.addTestSuite( LayoutUtilsTest.class );
		suite.addTestSuite( MetawidgetExceptionTest.class );
		suite.addTest( MixinTests.suite() );
		suite.addTest( SwingMetawidgetTests.suite() );
		suite.addTest( UtilTests.suite() );
		suite.addTestSuite( WidgetBuilderExceptionTest.class );
		suite.addTestSuite( WidgetProcessorExceptionTest.class );
		suite.addTestSuite( XmlSchemaGeneratorTaskTest.class );

		return suite;
	}
}