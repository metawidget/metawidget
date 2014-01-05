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

package org.metawidget.jsp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.metawidget.util.IOUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TldTest
	extends TestCase {

	//
	// Public methods
	//

	public void testTld()
		throws Exception {

		InputStream in = ClassLoader.getSystemResourceAsStream( "META-INF/metawidget-html.tld" );
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.streamBetween( in, out );
		Document document = XmlUtils.documentFromString( out.toString() );

		Element root = document.getDocumentElement();
		assertEquals( root.getNodeName(), "taglib" );

		// org.metawidget.jsp.tagext.html.HtmlMetawidgetTag

		Element tag = XmlUtils.getChildNamed( root, "tag" );
		Element tagclass = XmlUtils.getChildNamed( tag, "tagclass" );
		assertEquals( tagclass.getTextContent(), "org.metawidget.jsp.tagext.html.HtmlMetawidgetTag" );

		Element bodycontent = XmlUtils.getSiblingNamed( tagclass, "bodycontent" );
		assertEquals( bodycontent.getTextContent(), "JSP" );

		// org.metawidget.jsp.tagext.FacetTag

		tag = XmlUtils.getSiblingNamed( tag, "tag" );
		tagclass = XmlUtils.getChildNamed( tag, "tagclass" );
		assertEquals( tagclass.getTextContent(), "org.metawidget.jsp.tagext.FacetTag" );

		bodycontent = XmlUtils.getSiblingNamed( tagclass, "bodycontent" );
		assertEquals( bodycontent.getTextContent(), "JSP" );

		// org.metawidget.jsp.tagext.html.HtmlStubTag

		tag = XmlUtils.getSiblingNamed( tag, "tag" );
		tagclass = XmlUtils.getChildNamed( tag, "tagclass" );
		assertEquals( tagclass.getTextContent(), "org.metawidget.jsp.tagext.html.HtmlStubTag" );

		bodycontent = XmlUtils.getSiblingNamed( tagclass, "bodycontent" );
		assertEquals( bodycontent.getTextContent(), "JSP" );
	}
}