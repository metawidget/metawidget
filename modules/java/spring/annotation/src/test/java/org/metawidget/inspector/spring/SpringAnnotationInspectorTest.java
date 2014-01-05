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

package org.metawidget.inspector.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringAnnotationInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		SpringAnnotationInspector inspector = new SpringAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( SPRING_LOOKUP ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object2" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( SPRING_LOOKUP ) );
		assertEquals( "abc", property.getAttribute( SPRING_LOOKUP_ITEM_VALUE ) );
		assertEquals( "def", property.getAttribute( SPRING_LOOKUP_ITEM_LABEL ) );

		assertEquals( entity.getChildNodes().getLength(), 2 );
	}

	//
	// Inner class
	//

	public static class Foo {

		@UiSpringLookup( "bar" )
		public Object getObject1() {

			return null;
		}

		public void setObject1( @SuppressWarnings( "unused" ) Object object1 ) {

			// Do nothing
		}

		@UiSpringLookup( value = "baz", itemValue = "abc", itemLabel = "def" )
		public Object getObject2() {

			return null;
		}

		public void setObject2( @SuppressWarnings( "unused" ) Object object2 ) {

			// Do nothing
		}
	}
}
