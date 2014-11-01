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

import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonSchemaMappingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testJsonSchemaMappingProcessor() {

		// Test mappings

		Element element = XmlUtils.documentFromString( "<inspection-result><entity minimum-value=\"0\" maximum-value=\"1\" minimum-length=\"2\" maximum-length=\"3\" label=\"Foo\"/></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"maxLength\":3,\"maximum\":1,\"minLength\":2,\"minimum\":0,\"title\":\"Foo\"}", XmlUtils.inspectionResultToJsonSchema( element ) );

		// Test child mappings

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" minimum-value=\"0\" maximum-value=\"1\" minimum-length=\"2\" maximum-length=\"3\" label=\"Foo\"/></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"maxLength\":3,\"maximum\":1,\"minLength\":2,\"minimum\":0,\"propertyOrder\":0,\"title\":\"Foo\"}}}", XmlUtils.inspectionResultToJsonSchema( element ) );

		// Test nested child mappings

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" minimum-value=\"0\" label=\"Foo\"><property name=\"bar\" maximum-value=\"1\" minimum-length=\"2\" maximum-length=\"3\"/></property></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"minimum\":0,\"propertyOrder\":0,\"title\":\"Foo\",\"properties\":{\"bar\":{\"maxLength\":3,\"maximum\":1,\"minLength\":2,\"propertyOrder\":0}}}}}", XmlUtils.inspectionResultToJsonSchema( element ) );

		// Test removing hidden

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" minimum-value=\"0\" hidden=\"false\" comes-after=\"ignore\" parameterized-type=\"ignore\" actual-class=\"ignore\"/><property name=\"bar\" hidden=\"true\"/></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"hidden\":false,\"minimum\":0,\"propertyOrder\":0}}}", XmlUtils.inspectionResultToJsonSchema( element ) );

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" minimum-value=\"0\" hidden=\"true\" comes-after=\"ignore\" parameterized-type=\"ignore\" actual-type=\"ignore\"/><property name=\"bar\" hidden=\"true\"/></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{}", XmlUtils.inspectionResultToJsonSchema( element ) );
	}

	public void testConfiguredProcessor() {

		// Test removeAttributes on entity

		Element element = XmlUtils.documentFromString( "<inspection-result><entity foo=\"Foo\" bar=\"Bar\" baz=\"Baz\" abc=\"Abc\"/></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>( new JsonSchemaMappingProcessorConfig().setRemoveAttributes( "bar", "baz" ) ).processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"abc\":\"Abc\",\"foo\":\"Foo\"}", XmlUtils.inspectionResultToJsonSchema( element ) );

		// Test removeAttributes on property

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"name\" foo=\"Foo\" bar=\"Bar\" baz=\"Baz\" abc=\"Abc\"/></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>( new JsonSchemaMappingProcessorConfig().setRemoveAttributes( "bar", "baz" ) ).processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"name\":{\"abc\":\"Abc\",\"foo\":\"Foo\",\"propertyOrder\":0}}}", XmlUtils.inspectionResultToJsonSchema( element ) );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JsonSchemaMappingProcessorConfig.class, new JsonSchemaMappingProcessorConfig() {
			// Subclass
		} );
	}
}
