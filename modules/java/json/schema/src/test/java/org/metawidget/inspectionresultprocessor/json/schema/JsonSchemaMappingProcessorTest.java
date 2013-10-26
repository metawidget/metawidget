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
		assertEquals( "{\"maxLength\":3,\"maximum\":1,\"minLength\":2,\"minimum\":0,\"title\":\"Foo\"}", XmlUtils.elementToJsonSchema( element ) );

		// Test child mappings

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" minimum-value=\"0\" maximum-value=\"1\" minimum-length=\"2\" maximum-length=\"3\" label=\"Foo\"/></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"maxLength\":3,\"maximum\":1,\"minLength\":2,\"minimum\":0,\"title\":\"Foo\"}}}", XmlUtils.elementToJsonSchema( element ) );

		// Test nested child mappings

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" minimum-value=\"0\" label=\"Foo\"><property name=\"bar\" maximum-value=\"1\" minimum-length=\"2\" maximum-length=\"3\"/></property></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"minimum\":0,\"title\":\"Foo\",\"properties\":{\"bar\":{\"maxLength\":3,\"maximum\":1,\"minLength\":2}}}}}", XmlUtils.elementToJsonSchema( element ) );

		// Test removing hidden

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"foo\" minimum-value=\"0\" hidden=\"false\" comes-after=\"ignore\" parameterized-type=\"ignore\"/><property name=\"bar\" hidden=\"true\"/></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>().processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"foo\":{\"minimum\":0}}}", XmlUtils.elementToJsonSchema( element ) );
	}

	public void testConfiguredProcessor() {

		// Test removeAttributes on entity

		Element element = XmlUtils.documentFromString( "<inspection-result><entity foo=\"Foo\" bar=\"Bar\" baz=\"Baz\" abc=\"Abc\"/></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>( new JsonSchemaMappingProcessorConfig().setRemoveAttributes( "bar", "baz" ) ).processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"abc\":\"Abc\",\"foo\":\"Foo\"}", XmlUtils.elementToJsonSchema( element ) );

		// Test removeAttributes on property

		element = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"name\" foo=\"Foo\" bar=\"Bar\" baz=\"Baz\" abc=\"Abc\"/></entity></inspection-result>" ).getDocumentElement();
		element = new JsonSchemaMappingProcessor<Object>( new JsonSchemaMappingProcessorConfig().setRemoveAttributes( "bar", "baz" ) ).processInspectionResultAsDom( element, null, null, null );
		assertEquals( "{\"properties\":{\"name\":{\"abc\":\"Abc\",\"foo\":\"Foo\"}}}", XmlUtils.elementToJsonSchema( element ) );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JsonSchemaMappingProcessorConfig.class, new JsonSchemaMappingProcessorConfig() {
			// Subclass
		} );
	}
}
