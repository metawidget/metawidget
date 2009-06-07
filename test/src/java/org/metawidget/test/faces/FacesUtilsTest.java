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

package org.metawidget.test.faces;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;

import junit.framework.TestCase;

import org.metawidget.faces.FacesUtils;

/**
 * @author Richard Kennard
 */

public class FacesUtilsTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testCopyAttributes()
		throws Exception
	{
		UIComponent component1 = new HtmlInputText();
		UIComponent component2 = new HtmlInputText();

		component1.getAttributes().put( "id", "foo" );
		component1.getAttributes().put( "bar", "baz" );

		FacesUtils.copyAttributes( component1, component2 );

		assertTrue( "baz".equals( component2.getAttributes().get( "bar" )));
		assertTrue( !component2.getAttributes().containsKey( "id" ));
	}
}
