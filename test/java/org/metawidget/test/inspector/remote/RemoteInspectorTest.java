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

package org.metawidget.test.inspector.remote;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class RemoteInspectorTest
	extends TestCase
{
	//
	//
	// Public methods
	//
	//

	public void testInspection()
		throws Exception
	{
		// Inspect back-end

		String toInspect = "<?xml version=\"1.0\"?>";
		toInspect += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\">";
		toInspect += "<entity type=\"org.metawidget.example.shared.addressbook.model.Communication\">";
		toInspect += "<property name=\"id\" hidden=\"true\"/>";
		toInspect += "<property name=\"type\" lookup=\"Phone, Fax\"/>";
		toInspect += "</entity></inspection-result>";

		XmlInspectorConfig configXml = new XmlInspectorConfig();
		configXml.setInputStream( new ByteArrayInputStream( toInspect.getBytes() ) );

		XmlInspector inspectorXml = new XmlInspector( configXml );
		Document document = XmlUtils.documentFromString( inspectorXml.inspect( null, Communication.class.getName() ));

		// Pass to front end

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream( bytes );
		out.writeObject( document );
		out.close();

		ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream( bytes.toByteArray() ));
		document = (Document) in.readObject();

		// Inspect front-end

		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new PropertyTypeInspector() );

		CompositeInspector inspectorMeta = new CompositeInspector( config );

		// Inspect

		document = XmlUtils.documentFromString( inspectorMeta.inspect( document, new Communication(), Communication.class.getName() ));

		// Test

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ));

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ));
		assertTrue( Communication.class.getName().equals( entity.getAttribute( TYPE ) ));
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "id".equals( property.getAttribute( NAME ) ));
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ));

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "type".equals( property.getAttribute( NAME ) ));
		assertTrue( String.class.getName().equals( property.getAttribute( TYPE ) ));
		assertTrue( "Phone, Fax".equals( property.getAttribute( LOOKUP ) ));

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "value".equals( property.getAttribute( NAME ) ));
		assertTrue( String.class.getName().equals( property.getAttribute( TYPE ) ));
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public RemoteInspectorTest( String name )
	{
		super( name );
	}
}
