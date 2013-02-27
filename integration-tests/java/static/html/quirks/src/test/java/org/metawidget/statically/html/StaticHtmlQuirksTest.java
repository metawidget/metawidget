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

package org.metawidget.statically.html;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.metawidget.integrationtest.statically.html.FreemarkerQuirks;

public class StaticHtmlQuirksTest
	extends TestCase {

	//
	// Public methods
	//

	public void testFreemarkerQuirks() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setConfig( "org/metawidget/integrationtest/statically/html/quirks/metawidget-freemarker.xml" );
		metawidget.setPath( FreemarkerQuirks.class.getName() );

		String result = "<form>\r\n" +
				"\t<table>\r\n" +
				"\t\t<tbody>\r\n" +
				"\t\t\t<tr>\r\n" +
				"\t\t\t\t<th>Name:</th>\r\n" +
				"\t\t\t\t<td><input id=\"name\" name=\"name\" type=\"text\"/></td>\r\n" +
				"\t\t\t\t<td></td>\r\n" +
				"\t\t\t</tr><tr>\r\n" +
				"\t\t\t\t<th>Age:</th>\r\n" +
				"\t\t\t\t<td><input id=\"age\" name=\"age\" type=\"number\"/></td>\r\n" +
				"\t\t\t\t<td></td>\r\n" +
				"\t\t\t</tr><tr>\r\n" +
				"\t\t\t\t<th>Retired:</th>\r\n" +
				"\t\t\t\t<td><input id=\"retired\" name=\"retired\" type=\"checkbox\"/></td>\r\n" +
				"\t\t\t\t<td></td>\r\n" +
				"\t\t\t</tr>\r\n" +
				"\t\t</tbody>\r\n" +
				"\t</table>\r\n" +
				"</form>";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );

		result = "\t<form>\r\n" +
				"\t\t<table>\r\n" +
				"\t\t\t<tbody>\r\n" +
				"\t\t\t\t<tr>\r\n" +
				"\t\t\t\t\t<th>Name:</th>\r\n" +
				"\t\t\t\t\t<td><input id=\"name\" name=\"name\" type=\"text\"/></td>\r\n" +
				"\t\t\t\t\t<td></td>\r\n" +
				"\t\t\t\t</tr><tr>\r\n" +
				"\t\t\t\t\t<th>Age:</th>\r\n" +
				"\t\t\t\t\t<td><input id=\"age\" name=\"age\" type=\"number\"/></td>\r\n" +
				"\t\t\t\t\t<td></td>\r\n" +
				"\t\t\t\t</tr><tr>\r\n" +
				"\t\t\t\t\t<th>Retired:</th>\r\n" +
				"\t\t\t\t\t<td><input id=\"retired\" name=\"retired\" type=\"checkbox\"/></td>\r\n" +
				"\t\t\t\t\t<td></td>\r\n" +
				"\t\t\t\t</tr>\r\n" +
				"\t\t\t</tbody>\r\n" +
				"\t\t</table>\r\n" +
				"\t</form>";

		writer = new StringWriter();
		metawidget.write( writer, 1 );
		assertEquals( result, writer.toString() );
	}
}
