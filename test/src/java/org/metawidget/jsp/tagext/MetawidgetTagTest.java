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

package org.metawidget.jsp.tagext;

import junit.framework.TestCase;

import org.metawidget.iface.MetawidgetException;

/**
 * MetawidgetTag test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class MetawidgetTagTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testMissingDefaultConfig()
	{
		// TODO: MockPageContext

		//MetawidgetTag metawidget = new HtmlMetawidgetTag();
		//assertTrue( null == metawidget.getPageContext().getServletContext().getAttribute( "metawidget-config-reader" ));

		// Should not error (just log)

		//metawidget.configure();

		// Should have done something

		//assertTrue( metawidget.getPageContext().getServletContext().getAttribute( "metawidget-config-reader" ) instanceof ConfigReader );

		// Should error

		try
		{
			//metawidget.setConfig( "does-not-exist.xml" );
			//metawidget.configure();
			//assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			//assertTrue( "java.io.FileNotFoundException: Unable to locate does-not-exist.xml on CLASSPATH".equals( e.getMessage()));
		}
	}
}
