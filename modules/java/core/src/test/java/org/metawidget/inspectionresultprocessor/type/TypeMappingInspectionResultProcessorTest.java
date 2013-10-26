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

package org.metawidget.inspectionresultprocessor.type;

import junit.framework.TestCase;

import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TypeMappingInspectionResultProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testNoComesAfter()
		throws Exception {

		// Set up

		String inputXml = "<?xml version=\"1.0\"?>";
		inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		inputXml += "<entity type=\"Foo\">";
		inputXml += "<property name=\"foo\" type=\"fooType\"/>";
		inputXml += "<property name=\"bar\" type=\"barType\"/>";
		inputXml += "<property name=\"baz\" type=\"bazType\"/>";
		inputXml += "</entity></inspection-result>";

		// Run processor

		TypeMappingInspectionResultProcessorConfig config = new TypeMappingInspectionResultProcessorConfig();
		config.setTypeMapping( "Foo", "FooMapped" );
		config.setTypeMapping( "fooType", "fooTypeMapped" );
		config.setTypeMapping( "bazType", "bazTypeMapped" );
		TypeMappingInspectionResultProcessor<Object> processor = new TypeMappingInspectionResultProcessor<Object>( config );
		String outputXml = processor.processInspectionResult( inputXml, null, null, null );

		// Test result

		String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
		validateXml += "<entity type=\"FooMapped\">";
		validateXml += "<property name=\"foo\" type=\"fooTypeMapped\"/>";
		validateXml += "<property name=\"bar\" type=\"barType\"/>";
		validateXml += "<property name=\"baz\" type=\"bazTypeMapped\"/>";
		validateXml += "</entity>";
		validateXml += "</inspection-result>";

		assertEquals( validateXml, outputXml );
	}

	public void testNested()
			throws Exception {

			// Set up

			String inputXml = "<?xml version=\"1.0\"?>";
			inputXml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
			inputXml += "<entity type=\"Foo\">";
			inputXml += "<property name=\"foo\" type=\"fooType\"/>";
			inputXml += "<property name=\"bar\" type=\"barType\">";
			inputXml += "<property name=\"nestedBar1\" type=\"barType\"/>";
			inputXml += "<property name=\"nestedBar2\" type=\"barType\"/>";
			inputXml += "</property>";
			inputXml += "<property name=\"baz\" type=\"bazType\"/>";
			inputXml += "</entity></inspection-result>";

			// Run processor

			TypeMappingInspectionResultProcessorConfig config = new TypeMappingInspectionResultProcessorConfig();
			config.setTypeMapping( "Foo", "FooMapped" );
			config.setTypeMapping( "fooType", "fooTypeMapped" );
			config.setTypeMapping( "barType", "barTypeMapped" );
			config.setTypeMapping( "bazType", "bazTypeMapped" );
			TypeMappingInspectionResultProcessor<Object> processor = new TypeMappingInspectionResultProcessor<Object>( config );
			String outputXml = processor.processInspectionResult( inputXml, null, null, null );

			// Test result

			String validateXml = "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\">";
			validateXml += "<entity type=\"FooMapped\">";
			validateXml += "<property name=\"foo\" type=\"fooTypeMapped\"/>";
			validateXml += "<property name=\"bar\" type=\"barTypeMapped\">";
			validateXml += "<property name=\"nestedBar1\" type=\"barTypeMapped\"/>";
			validateXml += "<property name=\"nestedBar2\" type=\"barTypeMapped\"/>";
			validateXml += "</property>";
			validateXml += "<property name=\"baz\" type=\"bazTypeMapped\"/>";
			validateXml += "</entity>";
			validateXml += "</inspection-result>";

			assertEquals( validateXml, outputXml );
		}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( TypeMappingInspectionResultProcessorConfig.class, new TypeMappingInspectionResultProcessorConfig() {
			// Subclass
		} );
	}
}
