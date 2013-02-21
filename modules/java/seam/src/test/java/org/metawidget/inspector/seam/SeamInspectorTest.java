// Metawidget (licensed under LGPL)
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

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.MetawidgetTestUtils;
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
