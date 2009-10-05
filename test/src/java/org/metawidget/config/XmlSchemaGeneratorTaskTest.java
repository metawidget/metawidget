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

package org.metawidget.config;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class XmlSchemaGeneratorTaskTest
	extends TestCase
{
	//
	// Private members
	//

	private XmlSchemaGeneratorTask mXmlSchemaGeneratorTask;

	//
	// Public methods
	//

	public void testSchemaGenerator()
		throws Exception
	{
		// No config

		String schema = mXmlSchemaGeneratorTask.generateClassBlock( "org.metawidget.swing.widgetbuilder", "SwingWidgetBuilder" );

		assertTrue( schema.contains( "\r\n\t<!-- SwingWidgetBuilder -->\r\n" ));
		assertTrue( schema.contains( "\t<xs:element name=\"swingWidgetBuilder\">\r\n" ));
		assertTrue( schema.contains( "\t\t<xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t<xs:attribute name=\"config\" type=\"xs:string\" use=\"prohibited\"/>\r\n" ));
		assertTrue( schema.contains( "\t\t</xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t</xs:element>\r\n" ));

		// Optional config

		schema = mXmlSchemaGeneratorTask.generateClassBlock( "org.metawidget.inspector.java5", "Java5Inspector" );

		assertTrue( schema.contains( "\r\n\t<!-- Java5Inspector -->\r\n" ));
		assertTrue( schema.contains( "\t<xs:element name=\"java5Inspector\">\r\n" ));
		assertTrue( schema.contains( "\t\t<xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t<xs:all>\r\n" ));

		assertTrue( schema.contains( "\t\t\t\t<xs:element name=\"actionStyle\" minOccurs=\"0\">\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t<xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t<xs:sequence>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t<xs:any processContents=\"lax\"/>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t</xs:sequence>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t</xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t</xs:element>\r\n" ));

		assertTrue( schema.contains( "\t\t\t\t<xs:element name=\"propertyStyle\" minOccurs=\"0\">\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t<xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t<xs:sequence>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t<xs:any processContents=\"lax\"/>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t</xs:sequence>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t\t</xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t\t</xs:element>\r\n" ));

		assertTrue( schema.contains( "\t\t\t</xs:all>\r\n" ));
		assertTrue( schema.contains( "\t\t\t<xs:attribute name=\"config\" type=\"xs:string\"/>\r\n" ));
		assertTrue( schema.contains( "\t\t</xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t</xs:element>\r\n" ));

		// Required config

		schema = mXmlSchemaGeneratorTask.generateClassBlock( "org.metawidget.inspector.xml", "XmlInspector" );

		assertTrue( schema.contains( "\r\n\t<!-- XmlInspector -->\r\n" ));
		assertTrue( schema.contains( "\t<xs:element name=\"xmlInspector\">\r\n" ));
		assertTrue( schema.contains( "\t\t<xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t<xs:all>\r\n" ));

		assertTrue( schema.contains( "\t\t\t\t<xs:element name=\"inputStream\" minOccurs=\"0\">" ));
		assertTrue( schema.contains( "\t\t\t\t\t<xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t<xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t<xs:choice>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t<xs:element name=\"file\" type=\"xs:string\"/>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t<xs:element name=\"resource\" type=\"xs:string\"/>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t<xs:element name=\"url\" type=\"xs:anyURI\"/>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t</xs:choice>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t</xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t</xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t</xs:element>" ));

		assertTrue( schema.contains( "\t\t\t\t<xs:element name=\"inputStreams\" minOccurs=\"0\">" ));
		assertTrue( schema.contains( "\t\t\t\t\t<xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t<xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t<xs:element name=\"array\">" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t<xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t\t<xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t\t\t<xs:any maxOccurs=\"unbounded\" processContents=\"lax\"/>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t\t</xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t\t</xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t</xs:element>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t</xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t</xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t</xs:element>" ));

		assertTrue( schema.contains( "\t\t\t</xs:all>\r\n" ));
		assertTrue( schema.contains( "\t\t\t<xs:attribute name=\"config\" type=\"xs:string\" use=\"required\"/>\r\n" ));
		assertTrue( schema.contains( "\t\t</xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t</xs:element>\r\n" ));

		// maxOccurs="unbounded"

		schema = mXmlSchemaGeneratorTask.generateClassBlock( "org.metawidget.faces.component.html", "HtmlMetawidget" );

		assertTrue( schema.contains( "\r\n\t<!-- HtmlMetawidget -->\r\n" ));
		assertTrue( schema.contains( "\t<xs:element name=\"htmlMetawidget\">\r\n" ));
		assertTrue( schema.contains( "\t\t<xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t\t\t<xs:sequence>\r\n" ));

		assertTrue( schema.contains( "\t\t\t\t<xs:element name=\"parameter\" minOccurs=\"0\" maxOccurs=\"unbounded\">" ));
		assertTrue( schema.contains( "\t\t\t\t\t<xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t<xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t<xs:element name=\"string\" type=\"xs:string\"/>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t\t<xs:any processContents=\"lax\"/>" ));
		assertTrue( schema.contains( "\t\t\t\t\t\t</xs:sequence>" ));
		assertTrue( schema.contains( "\t\t\t\t\t</xs:complexType>" ));
		assertTrue( schema.contains( "\t\t\t\t</xs:element>" ));

		assertTrue( schema.contains( "\t\t\t</xs:sequence>\r\n" ));
		assertTrue( schema.contains( "\t\t</xs:complexType>\r\n" ));
		assertTrue( schema.contains( "\t</xs:element>\r\n" ));
	}

	//
	// Protected methods
	//

	@Override
	public void setUp()
	{
		mXmlSchemaGeneratorTask = new XmlSchemaGeneratorTask();
	}
}