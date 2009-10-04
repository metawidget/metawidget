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

import java.lang.reflect.Field;

import javax.servlet.jsp.tagext.TagSupport;

import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.util.LogUtils;

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
		throws Exception
	{
		MockPageContext pageContext = new MockPageContext();
		Field field = TagSupport.class.getDeclaredField( "pageContext" );
		field.setAccessible( true );

		MetawidgetTag metawidget = new HtmlMetawidgetTag();
		field.set( metawidget, pageContext );

		assertTrue( null == pageContext.getServletContext().getAttribute( "metawidget-config-reader" ));

		// Should not error (just log)

		metawidget.configure();
		assertTrue( "Could not locate metawidget.xml. This file is optional, but if you HAVE created one then Metawidget isn't finding it!".equals( LogUtils.LAST_INFO_MESSAGE ));

		// Should have done something

		assertTrue( metawidget.getPageContext().getServletContext().getAttribute( "metawidget-config-reader" ) instanceof ConfigReader );

		// Should error

		try
		{
			metawidget.setConfig( "does-not-exist.xml" );
			metawidget.configure();
			assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "java.io.FileNotFoundException: Unable to locate does-not-exist.xml on CLASSPATH".equals( e.getMessage()));
		}
	}
}
