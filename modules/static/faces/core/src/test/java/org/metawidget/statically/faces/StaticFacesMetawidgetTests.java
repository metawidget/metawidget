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

package org.metawidget.statically.faces;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.Assert;

import org.metawidget.statically.StaticUtils.IndentedWriter;
import org.metawidget.statically.StaticWidget;

/**
 * @author Richard Kennard
 */

public class StaticFacesMetawidgetTests {

	//
	// Public statics
	//

	public static void assertWidgetEquals( StaticWidget widget, String equals )
		throws IOException {

		StringWriter writer = new StringWriter();
		widget.write( new IndentedWriter( writer, 0 ) );
		Assert.assertEquals( equals, writer.toString() );
	}

	//
	// Private constructor
	//

	private StaticFacesMetawidgetTests() {

		// Can never be called
	}
}
