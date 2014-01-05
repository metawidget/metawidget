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
