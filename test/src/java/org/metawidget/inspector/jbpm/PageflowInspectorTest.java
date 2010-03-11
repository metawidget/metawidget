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

package org.metawidget.inspector.jbpm;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class PageflowInspectorTest
	extends TestCase
{
	//
	// Private members
	//

	private Inspector	mInspector;

	//
	// Public methods
	//

	@Override
	public void setUp()
	{
		BaseXmlInspectorConfig config = new BaseXmlInspectorConfig();
		ConfigReader reader = new ConfigReader();
		config.setInputStreams( reader.openResource( "org/metawidget/inspector/jbpm/test-pageflow1.jpdl.xml" ), reader.openResource( "org/metawidget/inspector/jbpm/test-pageflow2.jpdl.xml" ));
		mInspector = new PageflowInspector( config );
	}

	public void testProperties()
	{
		String xml = mInspector.inspect( null, "newuser.contact" );
		Document document = XmlUtils.documentFromString( xml );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "newuser.contact", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( ACTION.equals( property.getNodeName() ) );
		assertTrue( "prev".equals( property.getAttribute( NAME ) ) );

		property = (Element) property.getNextSibling();
		assertTrue( ACTION.equals( property.getNodeName() ) );
		assertTrue( "next".equals( property.getAttribute( NAME ) ) );

		assertTrue( property.getNextSibling() == null );
	}
}
