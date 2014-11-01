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

package org.metawidget.inspectionresultprocessor.json.schema;

import junit.framework.TestCase;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessor;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonSchemaTypeMappingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJsonSchemaTypeMappingProcessor() {

		// Object, number, string, boolean and date

		Element element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" type=\"number\"/><property name=\"bar\" type=\"string\"/><property name=\"baz\" type=\"boolean\"/><property name=\"abc\" type=\"date\"/></entity></inspection-result>" ).getDocumentElement();
		element = new TypeMappingInspectionResultProcessor<Object>( new JsonSchemaTypeMappingProcessorConfig() ).processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"type\":\"int\"},\"bar\":{\"type\":\"java.lang.String\"},\"baz\":{\"type\":\"boolean\"},\"abc\":{\"type\":\"java.util.Date\"}}}", XmlUtils.inspectionResultToJsonSchema( element ) );

		// Color, array and not removing unknown types

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" type=\"color\"/><property name=\"bar\" type=\"array\"/><property name=\"baz\" type=\"unknown\"/></entity></inspection-result>" ).getDocumentElement();
		element = new TypeMappingInspectionResultProcessor<Object>( new JsonSchemaTypeMappingProcessorConfig() ).processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"type\":\"java.awt.Color\"},\"bar\":{\"type\":\"java.util.List\"},\"baz\":{\"type\":\"unknown\"}}}", XmlUtils.inspectionResultToJsonSchema( element ) );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JsonSchemaTypeMappingProcessorConfig.class, new JsonSchemaTypeMappingProcessorConfig() {
			// Subclass
		} );
	}
}
