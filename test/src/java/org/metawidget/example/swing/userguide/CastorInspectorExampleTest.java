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

package org.metawidget.example.swing.userguide;

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
 * @author Richard Kennard
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

			if ( toInspect.hasAttribute( getNameAttribute() ) ) {
				attributes.put( NAME, toInspect.getAttribute( getNameAttribute() ) );
			}

			if ( toInspect.hasAttribute( getTypeAttribute() ) ) {
				attributes.put( TYPE, toInspect.getAttribute( getTypeAttribute() ) );
			}

			return attributes;
		}

		@Override
		protected String getTopLevelTypeAttribute() {

			return NAME;
		}
	}
}
