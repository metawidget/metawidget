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

package org.metawidget.inspector.remote;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RemoteInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection()
		throws Exception {

		// Inspect back-end

		String toInspect = "<?xml version=\"1.0\"?>";
		toInspect += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		toInspect += "<entity type=\"" + Foo.class.getName() + "\">";
		toInspect += "<property name=\"id\" hidden=\"true\"/>";
		toInspect += "<property name=\"type\" lookup=\"Phone, Fax\"/>";
		toInspect += "</entity></inspection-result>";

		XmlInspector inspectorXml = new XmlInspector( new XmlInspectorConfig().setInputStream( new ByteArrayInputStream( toInspect.getBytes() ) ) );
		String backEnd = inspectorXml.inspect( null, Foo.class.getName() );

		// Inspect front-end

		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new PropertyTypeInspector() );

		CompositeInspector inspectorMeta = new CompositeInspector( config );

		// Inspect

		Document document = XmlUtils.documentFromString( inspectorMeta.inspect( backEnd, new Foo(), Foo.class.getName() ) );

		// Test

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "id", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "type", property.getAttribute( NAME ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( "Phone, Fax", property.getAttribute( LOOKUP ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "value", property.getAttribute( NAME ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );

		assertEquals( 3, entity.getChildNodes().getLength() );
	}

	//
	// Inner class
	//

	static class Foo {

		private String	mId;

		private String	mType;

		private String	mValue;

		public String getId() {

			return mId;
		}

		public void setId( String id ) {

			mId = id;
		}

		public String getType() {

			return mType;
		}

		public void setType( String type ) {

			mType = type;
		}

		public String getValue() {

			return mValue;
		}

		public void setValue( String value ) {

			mValue = value;
		}
	}
}
