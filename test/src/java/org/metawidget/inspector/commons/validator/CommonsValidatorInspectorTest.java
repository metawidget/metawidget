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

package org.metawidget.inspector.commons.validator;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class CommonsValidatorInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		CommonsValidatorInspectorConfig config = new CommonsValidatorInspectorConfig();
		config.setInputStream( new ConfigReader().openResource( "org/metawidget/inspector/commons/validator/validation.xml" ) );
		CommonsValidatorInspector inspector = new CommonsValidatorInspector( config );

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "testForm1" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "testForm1", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( "1", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "99", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( "42", property.getAttribute( MAXIMUM_LENGTH ) );
		assertTrue( property.getAttributes().getLength() == 4 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertEquals( "5", property.getAttribute( MINIMUM_LENGTH ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "abc", property.getAttribute( NAME ) );
		assertEquals( "0.5", property.getAttribute( MINIMUM_VALUE ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "def", property.getAttribute( NAME ) );
		assertEquals( "0.99", property.getAttribute( MAXIMUM_VALUE ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		assertTrue( entity.getChildNodes().getLength() == 5 );
	}

	public void testBadInput() {

		CommonsValidatorInspectorConfig config = new CommonsValidatorInspectorConfig();
		config.setInputStream( new ConfigReader().openResource( "org/metawidget/inspector/commons/validator/validation.xml" ) );
		CommonsValidatorInspector inspector = new CommonsValidatorInspector( config );

		try {
			inspector.inspect( null, "testForm2" );
		} catch ( InspectorException e ) {
			assertEquals( "Property 'foo' depends on floatRange but has no var-name of min or max", e.getMessage() );
		}

		try {
			inspector.inspect( null, "testForm3" );
		} catch ( InspectorException e ) {
			assertEquals( "Property 'foo' depends on minlength but has no var-name of minlength", e.getMessage() );
		}

		try {
			inspector.inspect( null, "testForm4" );
		} catch ( InspectorException e ) {
			assertEquals( "Property 'foo' depends on maxlength but has no var-name of maxlength", e.getMessage() );
		}

		try {
			inspector.inspect( null, "testForm5" );
		} catch ( InspectorException e ) {
			assertEquals( "Variable named 'min' has no var-value", e.getMessage() );
		}
	}

	public void testTraversal() {

		CommonsValidatorInspectorConfig config = new CommonsValidatorInspectorConfig();
		config.setInputStream( new ConfigReader().openResource( "org/metawidget/inspector/commons/validator/validation.xml" ) );
		CommonsValidatorInspector inspector = new CommonsValidatorInspector( config );

		assertTrue( null != inspector.inspect( null, "testForm1" ) );
		assertTrue( null == inspector.inspect( null, "testForm1/foo" ) );
		assertTrue( null == inspector.inspect( null, "testForm1/foo/bar" ) );
	}
}
