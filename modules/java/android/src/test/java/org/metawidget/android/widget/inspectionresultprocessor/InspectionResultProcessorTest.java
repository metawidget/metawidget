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

package org.metawidget.android.widget.inspectionresultprocessor;

import junit.framework.TestCase;

import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class InspectionResultProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testXmlUtils()
		throws Exception {

		String xml = "<foo><bar>Test 1</bar><baz>Test 2</baz></foo>";
		Document document = XmlUtils.documentFromString( xml );

		// Android has a null localName sometimes

		Element root = document.getDocumentElement();
		Element child = XmlUtils.getFirstChildElement( root );
		assertEquals( "bar", child.getLocalName() );
		MetawidgetTestUtils.setPrivateField( child, "localName", null );
		assertEquals( null, child.getLocalName() );
		child = XmlUtils.getNextSiblingElement( child );
		assertEquals( "baz", child.getLocalName() );
		MetawidgetTestUtils.setPrivateField( child, "localName", null );
		assertEquals( null, child.getLocalName() );

		// Should fall back to nodeName in this case

		Element bar = XmlUtils.getChildNamed( root, "bar" );
		assertTrue( bar != null );
		Element baz = XmlUtils.getSiblingNamed( bar, "baz" );
		assertTrue( baz != null );
	}
}
