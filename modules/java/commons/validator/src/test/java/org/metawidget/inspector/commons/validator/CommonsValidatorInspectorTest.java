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

package org.metawidget.inspector.commons.validator;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CommonsValidatorInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		CommonsValidatorInspector inspector = new CommonsValidatorInspector( new CommonsValidatorInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/commons/validator/validation.xml" ) ) );

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "testForm1" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "testForm1", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( "1", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( "99", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( "42", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 4 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertEquals( "5", property.getAttribute( MINIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "abc", property.getAttribute( NAME ) );
		assertEquals( "0.5", property.getAttribute( MINIMUM_VALUE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "def", property.getAttribute( NAME ) );
		assertEquals( "0.99", property.getAttribute( MAXIMUM_VALUE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 5 );
	}

	public void testBadInput() {

		CommonsValidatorInspector inspector = new CommonsValidatorInspector( new CommonsValidatorInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/commons/validator/validation.xml" ) ) );

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

		CommonsValidatorInspector inspector = new CommonsValidatorInspector( new CommonsValidatorInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/commons/validator/validation.xml" ) ) );

		assertTrue( null != inspector.inspect( null, "testForm1" ) );
		assertEquals( null, inspector.inspect( null, "testForm1/foo" ) );
		assertEquals( null, inspector.inspect( null, "testForm1/foo/bar" ) );
	}
}
