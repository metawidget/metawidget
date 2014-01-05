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

package org.metawidget.inspector.seam;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SeamInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testSeamInspector() {

		SeamInspectorConfig config = new SeamInspectorConfig();
		config.setResourceResolver( new ResourceResolver() {

			public InputStream openResource( String resource ) {

				try {
					if ( "components.xml".equals( resource ) ) {
						return new SimpleResourceResolver().openResource( "org/metawidget/inspector/seam/test-components.xml" );
					}

					return new SimpleResourceResolver().openResource( resource );
				} catch ( Exception e ) {
					throw InspectorException.newException( e );
				}
			}
		} );

		SeamInspector inspector = new SeamInspector( config );
		assertEquals( null, inspector.inspect( null, "newuser1.contact" ) );

		String xml = inspector.inspect( null, "newuser.contact" );
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

	public void testNoPageflow() {

		SeamInspectorConfig config = new SeamInspectorConfig();
		config.setComponentsInputStream( new ByteArrayInputStream( "<foo></foo>".getBytes() ) );

		SeamInspector inspector = new SeamInspector( config );
		assertEquals( null, inspector.inspect( null, "newuser.contact" ) );
	}

	@SuppressWarnings( "unused" )
	public void testMissingFile() {

		try {
			new SeamInspector();
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "java.io.FileNotFoundException: Unable to locate components.xml on CLASSPATH", e.getMessage() );
		}
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( SeamInspectorConfig.class, new SeamInspectorConfig() {
			// Subclass
		}, "componentsInputStream" );
	}
}
