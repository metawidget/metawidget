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

package org.metawidget.inspector.seam;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.metawidget.config.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.TestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class SeamInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testSeamInspector() {

		SeamInspectorConfig config = new SeamInspectorConfig();
		config.setResourceResolver( new ResourceResolver() {

			@Override
			public InputStream openResource( String resource ) {

				try {
					if ( "components.xml".equals( resource ) ) {
						return ClassUtils.openResource( "org/metawidget/inspector/seam/test-components.xml" );
					}

					return ClassUtils.openResource( resource );
				} catch ( Exception e ) {
					throw InspectorException.newException( e );
				}
			}
		} );

		SeamInspector inspector = new SeamInspector( config );
		assertTrue( null == inspector.inspect( null, "newuser1.contact" ) );

		String xml = inspector.inspect( null, "newuser.contact" );
		Document document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
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
		assertTrue( null == inspector.inspect( null, "newuser.contact" ) );
	}

	public void testMissingFile() {

		try {
			new SeamInspector();
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "java.io.FileNotFoundException: Unable to locate components.xml on CLASSPATH", e.getMessage() );
		}
	}

	public void testConfig() {

		TestUtils.testEqualsAndHashcode( SeamInspectorConfig.class, new SeamInspectorConfig() {
			// Subclass
		}, "componentsInputStream" );
	}
}
