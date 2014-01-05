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

package org.metawidget.integrationtest.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.util.CollectionUtils;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CastorInspectorExampleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspectorExample()
		throws Exception {

		// Set up

		String inputXml = "<!DOCTYPE mapping PUBLIC \"-//EXOLAB/Castor Mapping DTD Version 1.0//EN\" \"http://castor.org/mapping.dtd\">";
		inputXml += "<mapping>";
		inputXml += "<class name=\"com.myapp.Foo\">";
		inputXml += "<field name=\"foo\" type=\"java.lang.String\"/>";
		inputXml += "<field name=\"bar\" type=\"int\"/>";
		inputXml += "<field name=\"baz\" type=\"boolean\"/>";
		inputXml += "<ignore name=\"ignore\" type=\"ignore\"/>";
		inputXml += "</class>";
		inputXml += "</mapping>";

		// Run processor

		InputStream stream = new ByteArrayInputStream( inputXml.getBytes() );
		Inspector inspector = new CastorInspector( new BaseXmlInspectorConfig().setInputStream( stream ) );
		String outputXml = inspector.inspect( null, "com.myapp.Foo" );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\">";
		validateXml += "<entity type=\"com.myapp.Foo\">";
		validateXml += "<property name=\"foo\" type=\"java.lang.String\"/>";
		validateXml += "<property name=\"bar\" type=\"int\"/>";
		validateXml += "<property name=\"baz\" type=\"boolean\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	//
	// Inner classes
	//

	static class CastorInspector
		extends BaseXmlInspector {

		public CastorInspector( BaseXmlInspectorConfig config ) {

			super( config );
		}

		@Override
		protected Map<String, String> inspectProperty( Element toInspect ) {

			if ( !"field".equals( toInspect.getNodeName() ) ) {
				return null;
			}

			Map<String, String> attributes = CollectionUtils.newHashMap();
			attributes.put( NAME, toInspect.getAttribute( getNameAttribute() ) );
			attributes.put( TYPE, toInspect.getAttribute( getTypeAttribute() ) );
			return attributes;
		}

		@Override
		protected String getTopLevelTypeAttribute() {

			return NAME;
		}
	}
}
