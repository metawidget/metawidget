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

package org.metawidget.inspector.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.struts.StrutsInspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class StrutsAnnotationInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		StrutsAnnotationInspector inspector = new StrutsAnnotationInspector();
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
		assertEquals( "bar1", property.getAttribute( STRUTS_LOOKUP_NAME ) );
		assertEquals( "baz1", property.getAttribute( STRUTS_LOOKUP_PROPERTY ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object2" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar2", property.getAttribute( STRUTS_LOOKUP_NAME ) );
		assertEquals( "baz2", property.getAttribute( STRUTS_LOOKUP_PROPERTY ) );
		assertEquals( "abc2", property.getAttribute( STRUTS_LOOKUP_LABEL_NAME ) );
		assertEquals( "def2", property.getAttribute( STRUTS_LOOKUP_LABEL_PROPERTY ) );

		assertEquals( entity.getChildNodes().getLength(), 2 );
	}

	//
	// Inner class
	//

	public static class Foo {

		@UiStrutsLookup( name = "bar1", property = "baz1" )
		public Object getObject1() {

			return null;
		}

		public void setObject1( @SuppressWarnings( "unused" ) Object object1 ) {

			// Do nothing
		}

		@UiStrutsLookup( name = "bar2", property = "baz2", labelName = "abc2", labelProperty = "def2" )
		public Object getObject2() {

			return null;
		}

		public void setObject2( @SuppressWarnings( "unused" ) Object object2 ) {

			// Do nothing
		}
	}
}
