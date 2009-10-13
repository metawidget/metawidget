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

package org.metawidget.inspector.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.iface.ResourceResolver;

/**
 * @author Richard Kennard
 */

public class BaseXmlInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		BaseXmlInspectorConfig config1 = new BaseXmlInspectorConfig();
		BaseXmlInspectorConfig config2 = new BaseXmlInspectorConfig();

		assertTrue( !config1.equals( "foo" ));
		assertTrue( config1.equals( config2 ));
		assertTrue( config1.hashCode() == config2.hashCode() );

		// resourceResolver

		ResourceResolver resourceResolver = new ConfigReader();
		config1.setResourceResolver( resourceResolver );
		assertTrue( resourceResolver == config1.getResourceResolver() );
		assertTrue( !config1.equals( config2 ));
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setResourceResolver( resourceResolver );
		assertTrue( config1.equals( config2 ));
		assertTrue( config1.hashCode() == config2.hashCode() );

		// inputStream

		InputStream inputStream1 = new ByteArrayInputStream( "buffer".getBytes() );
		config1.setInputStream( inputStream1 );
		assertTrue( !config1.equals( config2 ));
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setInputStream( inputStream1 );
		assertTrue( config1.equals( config2 ));
		assertTrue( config1.hashCode() == config2.hashCode() );

		// inputStreams

		InputStream inputStream2 = new ByteArrayInputStream( "buffer".getBytes() );
		config1.setInputStreams( inputStream1, inputStream2 );
		assertTrue( !config1.equals( config2 ));
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setInputStreams( inputStream1, inputStream2 );
		assertTrue( config1.equals( config2 ));
		assertTrue( config1.hashCode() == config2.hashCode() );
	}
}
