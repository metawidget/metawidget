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

package org.metawidget.inspectionresultprocessor.xsd;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class XmlSchemaToJavaTypeMappingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testMapping()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" type=\"string\"/>";
		inputXml += "<property name=\"bar\" type=\"xs:string\"/>";
		inputXml += "<property name=\"baz\" type=\"integer\"/>";
		inputXml += "<property name=\"abc\" type=\"Abc\"/>";
		inputXml += "<property name=\"def\" type=\"xs:Def\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		XmlSchemaToJavaTypeMappingProcessor<Object> processor = new XmlSchemaToJavaTypeMappingProcessor<Object>();
		String outputXml = processor.processInspectionResult( inputXml, null, null, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"Foo\">";
		validateXml += "<property name=\"foo\" type=\"java.lang.String\"/>";
		validateXml += "<property name=\"bar\" type=\"java.lang.String\"/>";
		validateXml += "<property name=\"baz\" type=\"int\"/>";
		validateXml += "<property name=\"abc\" type=\"Abc\"/>";
		validateXml += "<property name=\"def\" type=\"Def\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}
}
