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

package org.metawidget.inspector.jaxb;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JaxbInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		JaxbInspector inspector = new JaxbInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "transientProperty" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "required" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( FALSE, property.getAttribute( HIDDEN ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		assertEquals( entity.getChildNodes().getLength(), 2 );
	}

	//
	// Inner class
	//

	public static class Foo {

		@XmlTransient
		public String	transientProperty;

		@XmlElement( required = true )
		public String	required;
	}
}
