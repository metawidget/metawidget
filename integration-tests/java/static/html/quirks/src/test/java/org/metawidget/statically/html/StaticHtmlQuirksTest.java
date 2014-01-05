// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.statically.html;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.metawidget.integrationtest.statically.html.FreemarkerQuirks;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

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
				"\t\t\t\t<th>Retired yet?:</th>\r\n" +
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
				"\t\t\t\t\t<th>Retired yet?:</th>\r\n" +
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
