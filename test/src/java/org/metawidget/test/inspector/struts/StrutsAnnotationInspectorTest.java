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

package org.metawidget.test.inspector.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.struts.StrutsInspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.inspector.struts.StrutsAnnotationInspector;
import org.metawidget.inspector.struts.UiStrutsLookup;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class StrutsAnnotationInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testInspection()
	{
		StrutsAnnotationInspector inspector = new StrutsAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object1" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "bar1".equals( property.getAttribute( STRUTS_LOOKUP_NAME ) ) );
		assertTrue( "baz1".equals( property.getAttribute( STRUTS_LOOKUP_PROPERTY ) ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object2" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "bar2".equals( property.getAttribute( STRUTS_LOOKUP_NAME ) ) );
		assertTrue( "baz2".equals( property.getAttribute( STRUTS_LOOKUP_PROPERTY ) ) );
		assertTrue( "abc2".equals( property.getAttribute( STRUTS_LOOKUP_LABEL_NAME ) ) );
		assertTrue( "def2".equals( property.getAttribute( STRUTS_LOOKUP_LABEL_PROPERTY ) ) );

		assertTrue( entity.getChildNodes().getLength() == 2 );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@UiStrutsLookup( name = "bar1", property = "baz1" )
		public Object	object1;

		@UiStrutsLookup( name = "bar2", property = "baz2", labelName = "abc2", labelProperty = "def2" )
		public Object	object2;
	}
}
