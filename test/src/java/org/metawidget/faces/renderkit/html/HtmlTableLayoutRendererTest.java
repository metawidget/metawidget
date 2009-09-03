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

package org.metawidget.faces.renderkit.html;

import junit.framework.TestCase;

import org.metawidget.faces.component.html.HtmlMetawidget;

/**
 * @author Richard Kennard
 */

public class HtmlTableLayoutRendererTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testSectionCleared()
		throws Exception
	{
		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.getAttributes().put( "columns", 1 );
		metawidget.getAttributes().put( "currentSection", "Foo" );

		HtmlTableLayoutRenderer renderer = new HtmlTableLayoutRenderer();
		renderer.encodeChildren( null, metawidget );

		assertTrue( "".equals( metawidget.getAttributes().get( "currentSection" )));
	}
}
