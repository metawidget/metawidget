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

package org.metawidget.inspector.jbpm;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PageflowInspectorTest
	extends TestCase {

	//
	// Private members
	//

	private Inspector	mInspector;

	//
	// Public methods
	//

	@Override
	public void setUp() {

		BaseXmlInspectorConfig config = new BaseXmlInspectorConfig();
		config.setInputStreams( new SimpleResourceResolver().openResource( "org/metawidget/inspector/jbpm/test-pageflow1.jpdl.xml" ), new SimpleResourceResolver().openResource( "org/metawidget/inspector/jbpm/test-pageflow2.jpdl.xml" ) );
		mInspector = new PageflowInspector( config );
	}

	public void testProperties() {

		String xml = mInspector.inspect( null, "newuser.contact" );
		Document document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "newuser.contact", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( ACTION, property.getNodeName() );
		assertEquals( "prev", property.getAttribute( NAME ) );

		property = (Element) property.getNextSibling();
		assertEquals( ACTION, property.getNodeName() );
		assertEquals( "next", property.getAttribute( NAME ) );

		assertEquals( property.getNextSibling(), null );
	}
}
